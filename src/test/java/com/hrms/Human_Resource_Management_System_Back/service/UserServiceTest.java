package com.hrms.Human_Resource_Management_System_Back.service;

import com.hrms.Human_Resource_Management_System_Back.exception.EmailNotVerifiedException;
import com.hrms.Human_Resource_Management_System_Back.model.Tenant;
import com.hrms.Human_Resource_Management_System_Back.model.User;
import com.hrms.Human_Resource_Management_System_Back.model.UserGeneral;
import com.hrms.Human_Resource_Management_System_Back.model.dto.AuthenticationRequest;
import com.hrms.Human_Resource_Management_System_Back.model.dto.AuthenticationResponse;
import com.hrms.Human_Resource_Management_System_Back.model.dto.ChangePasswordRequest;
import com.hrms.Human_Resource_Management_System_Back.model.tenant.UserTenant;
import com.hrms.Human_Resource_Management_System_Back.repository.TenantRepository;
import com.hrms.Human_Resource_Management_System_Back.repository.UserGeneralRepository;
import com.hrms.Human_Resource_Management_System_Back.repository.UserRepository;
import com.hrms.Human_Resource_Management_System_Back.repository.tenant.UserTenantRepository;
import com.hrms.Human_Resource_Management_System_Back.security.CustomUserDetails;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.Duration;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(org.mockito.junit.jupiter.MockitoExtension.class)
class UserServiceTest {

    /* ---------- collaborators ---------- */

    @Mock JwtService jwtService;
    @Mock AuthenticationManager authenticationManager;
    @Mock UserRepository userRepository;
    @Mock CustomUserDetailsService userDetailsService;
    @Mock UserGeneralRepository userGeneralRepository;
    @Mock UserTenantRepository userTenantRepository;
    @Mock TenantRepository tenantRepository;
    @Mock JdbcTemplate jdbc;
    @Mock PasswordEncoder encoder;

    @InjectMocks UserService service;   // << system-under-test

    /* ---------- fixtures ---------- */

    User userGeneral;
    UserGeneral ug;

    User userTenant;
    UserTenant ut;
    Tenant tenant;

    @BeforeEach
    void init() {
        /* ---- general user (public schema) ---- */
        userGeneral = new User();
        userGeneral.setUserId(11);
        userGeneral.setEmail("john@doe.com");
        userGeneral.setPasswordHash("hash");
        userGeneral.setRole("GENERAL_USER");

        ug = new UserGeneral();
        ug.setUser(userGeneral);
        ug.setUserGeneralId(22);

        /* ---- tenant user ---- */
        userTenant = new User();
        userTenant.setUserId(33);
        userTenant.setEmail("tenant@acme.com");
        userTenant.setPasswordHash("hashT");
        userTenant.setRole("TENANT_USER");
        userTenant.setTenantId(77);

        ut = new UserTenant();
        ut.setUser(userTenant);
        ut.setUserTenantId(44);

        tenant = new Tenant();
        tenant.setTenantId(77);
        tenant.setSchemaName("tenant_schema");
    }

    /* ====================================================================== */
    /*  AUTHENTICATE – GENERAL USER (email already verified)                  */
    /* ====================================================================== */
    @Test
    void authenticate_general_verified_returnsJwt() {

        ug.setVerified(true);                               // ✅ verified

        CustomUserDetails cud =
                new CustomUserDetails(userGeneral.getUserId(),
                        "john@doe.com", "hash", "GENERAL_USER", "public");

        Authentication springAuth =
                new UsernamePasswordAuthenticationToken(cud, null, cud.getAuthorities());

        when(authenticationManager.authenticate(any(Authentication.class)))
                .thenReturn(springAuth);

        when(userGeneralRepository.findByUser_Email("john@doe.com"))
                .thenReturn(Optional.of(ug));

        when(jwtService.generateToken(anyMap(), eq("john@doe.com"), any(Duration.class)))
                .thenReturn("jwtGeneral");

        AuthenticationRequest rq = new AuthenticationRequest();
        rq.setEmail("john@doe.com");
        rq.setPassword("plain");

        AuthenticationResponse resp = service.authenticate(rq);

        assertEquals("jwtGeneral", resp.getToken());
        verify(jwtService).generateToken(anyMap(), eq("john@doe.com"), any());
    }

    /* ====================================================================== */
    /*  AUTHENTICATE – GENERAL USER (email NOT verified)                      */
    /* ====================================================================== */
    @Test
    void authenticate_general_notVerified_throws() {

        ug.setVerified(false);                              // ❌ not verified

        CustomUserDetails cud =
                new CustomUserDetails(userGeneral.getUserId(),
                        "john@doe.com", "hash", "GENERAL_USER", "public");

        Authentication springAuth =
                new UsernamePasswordAuthenticationToken(cud, null, cud.getAuthorities());

        when(authenticationManager.authenticate(any(Authentication.class)))
                .thenReturn(springAuth);

        when(userGeneralRepository.findByUser_Email("john@doe.com"))
                .thenReturn(Optional.of(ug));

        AuthenticationRequest rq = new AuthenticationRequest();
        rq.setEmail("john@doe.com");
        rq.setPassword("plain");

        assertThrows(EmailNotVerifiedException.class, () -> service.authenticate(rq));
        verify(jwtService, never()).generateToken(anyMap(), anyString(), any());
    }

    /* ====================================================================== */
    /*  AUTHENTICATE – TENANT USER                                            */
    /* ====================================================================== */
    @Test
    void authenticate_tenantUser_returnsJwt() {

        CustomUserDetails cud =
                new CustomUserDetails(userTenant.getUserId(),
                        "tenant@acme.com", "hashT", "TENANT_USER", "tenant_schema");

        Authentication springAuth =
                new UsernamePasswordAuthenticationToken(cud, null, cud.getAuthorities());

        when(authenticationManager.authenticate(any(Authentication.class)))
                .thenReturn(springAuth);

        when(userRepository.getReferenceById(33)).thenReturn(userTenant);
        when(tenantRepository.getReferenceById(77)).thenReturn(tenant);

        doNothing().when(jdbc).execute(anyString());       // swallow schema switch

        when(userTenantRepository.findByUser_Email("tenant@acme.com"))
                .thenReturn(Optional.of(ut));

        when(jwtService.generateToken(anyMap(), eq("tenant@acme.com"), any(Duration.class)))
                .thenReturn("jwtTenant");

        AuthenticationRequest rq = new AuthenticationRequest();
        rq.setEmail("tenant@acme.com");
        rq.setPassword("plain");

        AuthenticationResponse resp = service.authenticate(rq);

        assertEquals("jwtTenant", resp.getToken());
        verify(jdbc).execute(startsWith("SET search_path"));
    }

    /* ====================================================================== */
    /*  CHANGE-PASSWORD                                                       */
    /* ====================================================================== */
    @Test
    void changePassword_happyPath_updatesHash() {
        // put a fake authentication into the security context
        String email = "john@doe.com";
        Authentication auth =
                new UsernamePasswordAuthenticationToken(email, null, java.util.List.of());

        SecurityContextHolder.clearContext();
        SecurityContextHolder.getContext().setAuthentication(auth);

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(userGeneral));
        when(encoder.matches("old", "hash")).thenReturn(true);
        when(encoder.encode("newPwd")).thenReturn("newHash");

        ChangePasswordRequest dto = new ChangePasswordRequest();
        dto.setCurrentPassword("old");
        dto.setNewPassword("newPwd");

        service.changePassword(dto);

        verify(userRepository).save(argThat(u -> "newHash".equals(u.getPasswordHash())));
    }

    /* ====================================================================== */
    /*  CHANGE-PASSWORD – wrong curr pwd                                      */
    /* ====================================================================== */
    @Test
    void changePassword_wrongCurrent_throws() {
        String email = "john@doe.com";
        Authentication auth =
                new UsernamePasswordAuthenticationToken(email, null, java.util.List.of());
        SecurityContextHolder.getContext().setAuthentication(auth);

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(userGeneral));
        when(encoder.matches("bad", "hash")).thenReturn(false);

        ChangePasswordRequest dto = new ChangePasswordRequest();
        dto.setCurrentPassword("bad");
        dto.setNewPassword("whatever");

        assertThrows(IllegalArgumentException.class, () -> service.changePassword(dto));
    }
}
