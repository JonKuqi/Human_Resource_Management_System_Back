package com.hrms.Human_Resource_Management_System_Back.service;

import com.hrms.Human_Resource_Management_System_Back.model.User;
import com.hrms.Human_Resource_Management_System_Back.model.UserGeneral;
import com.hrms.Human_Resource_Management_System_Back.model.VerificationCode;
import com.hrms.Human_Resource_Management_System_Back.model.dto.AuthenticationResponse;
import com.hrms.Human_Resource_Management_System_Back.model.dto.RegisterGeneralRequest;
import com.hrms.Human_Resource_Management_System_Back.model.dto.VerifyRequest;
import com.hrms.Human_Resource_Management_System_Back.repository.UserGeneralRepository;
import com.hrms.Human_Resource_Management_System_Back.repository.UserRepository;
import com.hrms.Human_Resource_Management_System_Back.repository.VerificationCodeRepository;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserGeneralServiceTest {

    // ── repositories ───────────────────────────────────────────────
    @Mock UserGeneralRepository      userGeneralRepository;
    @Mock UserRepository             userRepository;
    @Mock VerificationCodeRepository verificationCodeRepository;

    // ── infrastructure ─────────────────────────────────────────────
    @Mock JwtService      jwtService;
    @Mock PasswordEncoder passwordEncoder;
    @Mock JavaMailSender  mailSender;

    @InjectMocks
    private UserGeneralService userGeneralService;

    /* mock static helper just once for the whole class */
    private static MockedStatic<EmailSenderService> emailSenderMock;

    @BeforeAll
    static void initStatic() {
        emailSenderMock = mockStatic(EmailSenderService.class);
    }

    @AfterAll
    static void closeStatic() {
        emailSenderMock.close();
    }

    @BeforeEach
    void resetStatic() {
        emailSenderMock.reset();                      // clean previous stubbing
        emailSenderMock.when(() ->
                        EmailSenderService.sendVerificationEmail(anyString(), anyString(), anyString()))
                .thenAnswer(inv -> null);      // swallow every call
    }

    /* ------------------------------------------------------------------ */
    @Test
    void register_shouldSaveUser_and_sendVerificationEmail() {
        // Arrange -------------------------------------------------------
        RegisterGeneralRequest rq = new RegisterGeneralRequest();
        rq.setEmail("john@doe.com");
        rq.setUsername("jdoe");
        rq.setPassword("secret");
        rq.setFirstName("John");
        rq.setLastName("Doe");
        rq.setPhone("123");
        rq.setGender("M");
        rq.setBirthDate(LocalDate.parse("1990-01-01"));

        /* when(service saves) → give back *same* instance with id filled */
        when(userRepository.save(any(User.class)))
                .thenAnswer(inv -> { User u = inv.getArgument(0); u.setUserId(11); return u; });

        when(userGeneralRepository.save(any(UserGeneral.class)))
                .thenAnswer(inv -> { UserGeneral ug = inv.getArgument(0); ug.setUserGeneralId(22); return ug; });

        when(passwordEncoder.encode("secret")).thenReturn("hash");
        when(jwtService.generateToken(anyMap(), anyString(), any()))
                .thenReturn("jwt-token")          // <- 1st call  (login token)
                .thenReturn("verification-jwt");  // <- 2nd call (email link)

        // Act -----------------------------------------------------------
        AuthenticationResponse resp = userGeneralService.register(rq);

        // Assert --------------------------------------------------------
        assertEquals("jwt-token", resp.getToken());
        verify(userRepository).save(any(User.class));
        verify(userGeneralRepository).save(any(UserGeneral.class));
        verify(verificationCodeRepository).save(any(VerificationCode.class));
        emailSenderMock.verify(() ->
                EmailSenderService.sendVerificationEmail(eq("john@doe.com"), anyString(), anyString()));
    }

    /* ------------------------------------------------------------------ */
    @Test
    void verifyEmail_should_mark_code_used_and_activate_account() {
        // Arrange -------------------------------------------------------
        VerifyRequest rq = new VerifyRequest();
        rq.setEmail("john@doe.com");
        rq.setCode("123456");

        VerificationCode vc = new VerificationCode();
        vc.setCodeHash("hash");
        vc.setExpiresAt(LocalDateTime.now().plusMinutes(5));
        vc.setIsUsed(false);

        when(verificationCodeRepository.findLatestByUserEmail("john@doe.com"))
                .thenReturn(Optional.of(vc));

        when(passwordEncoder.matches("123456", "hash")).thenReturn(true);

        UserGeneral ug = new UserGeneral();
        when(userGeneralRepository.findByUser_Email("john@doe.com"))
                .thenReturn(Optional.of(ug));

        // Act -----------------------------------------------------------
        var resp = userGeneralService.verifyEmail(rq);

        // Assert --------------------------------------------------------
        assertNotNull(resp);
        assertTrue(vc.getIsUsed());
        verify(verificationCodeRepository).save(vc);
        verify(userGeneralRepository).save(ug);
    }

    /* ------------------------------------------------------------------ */
    @Test
    void resendVerificationCode_should_persist_new_code() {
        // Arrange -------------------------------------------------------
        String email = "john@doe.com";

        User u = new User();  u.setEmail(email);
        UserGeneral ug = new UserGeneral(); ug.setUser(u); ug.setFirstName("John");

        when(userGeneralRepository.findByUser_Email(email)).thenReturn(Optional.of(ug));
        when(passwordEncoder.encode(anyString())).thenReturn("new-hash");

        // Act -----------------------------------------------------------
        var resp = userGeneralService.resendVerificationCode(email);

        // Assert --------------------------------------------------------
        assertNotNull(resp);
        verify(verificationCodeRepository).save(any(VerificationCode.class));
    }
}
