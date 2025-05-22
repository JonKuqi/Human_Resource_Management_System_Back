package com.hrms.Human_Resource_Management_System_Back.service;

import com.hrms.Human_Resource_Management_System_Back.model.User;
import com.hrms.Human_Resource_Management_System_Back.model.UserGeneral;
import com.hrms.Human_Resource_Management_System_Back.model.VerificationCode;
import com.hrms.Human_Resource_Management_System_Back.model.dto.AuthenticationResponse;
import com.hrms.Human_Resource_Management_System_Back.model.dto.RegisterGeneralRequest;
import com.hrms.Human_Resource_Management_System_Back.model.dto.VerifyRequest;
import com.hrms.Human_Resource_Management_System_Back.model.types.RoleUser;
import com.hrms.Human_Resource_Management_System_Back.repository.UserGeneralRepository;
import com.hrms.Human_Resource_Management_System_Back.repository.UserRepository;
import com.hrms.Human_Resource_Management_System_Back.repository.VerificationCodeRepository;
import com.hrms.Human_Resource_Management_System_Back.security.CustomUserDetails;
import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * Service class for handling user general operations such as registration, email verification,
 * and verification code management.
 * <p>
 * This service manages the logic for registering general users, verifying their emails, and resending
 * verification codes. It interacts with repositories for managing user data and handles sending verification
 * emails to users.
 * </p>
 */
@Service
@AllArgsConstructor
public class UserGeneralService extends BaseService<UserGeneral, Integer> {

    private final UserGeneralRepository userGeneralRepository;
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final VerificationCodeRepository verificationCodeRepository;
    @Autowired
    private JavaMailSender mailSender;

    /**
     * Returns the user general repository.
     * <p>
     * This method overrides the {@link BaseService#getRepository()} method to return the specific repository
     * for user general entities.
     * </p>
     *
     * @return the user general repository
     */
    @Override
    protected UserGeneralRepository getRepository() {
        return userGeneralRepository;
    }

    /**
     * Initializes the email sender service.
     * <p>
     * This method is called after the service is constructed to initialize the email sender with the configured mail sender.
     * </p>
     */
    @PostConstruct
    public void init() {
        EmailSenderService.initialize(mailSender);
    }

    /**
     * Registers a new user general account and sends a verification email.
     * <p>
     * This method handles user registration, including saving the user and user general information, generating
     * a verification code, and sending a verification email to the user.
     * </p>
     *
     * @param request the registration request containing user details
     * @return an authentication response containing the generated JWT token for the user
     */
    @Transactional
    public AuthenticationResponse register(RegisterGeneralRequest request) {
        String role = String.valueOf(RoleUser.GENERAL_USER);

        String encoded = passwordEncoder.encode(request.getPassword());
        // 1. Save user
        User user = User.builder()
                .email(request.getEmail())
                .username(request.getUsername())
                .passwordHash(encoded)
                .role(role)
                .tenantId(null)
                .build();

        User u = userRepository.save(user);

        // 2. Save UserGeneral using real request fields
        UserGeneral userGeneral = UserGeneral.builder()
                .user(user)
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .phone(request.getPhone())
                .gender(request.getGender())
                .birthDate(request.getBirthDate())
                .cvData(new byte[0])
                .portfolio(new byte[0])
                .profilePhoto(new byte[0])
                .build();

        UserGeneral ug = userGeneralRepository.save(userGeneral);

        // 3. Wrap and generate token
        CustomUserDetails userDetails = new CustomUserDetails(u.getUserId(), userGeneral);

        String rawCode = VerificationCodeService.generate6DigitCode(); // You write this util
        System.out.println("Your Raw Code is :" + rawCode);
        String codeHash = passwordEncoder.encode(rawCode); // or use a hash function if not re-checking

        VerificationCode code = VerificationCode.builder()
                .user(user)
                .codeHash(codeHash)
                .expiresAt(LocalDateTime.now().plusMinutes(15))
                .isUsed(false)
                .build();
        verificationCodeRepository.save(code);

        // Send email with verification code
        EmailSenderService.sendVerificationEmail(
                user.getEmail(),
                "Verify your email",
                "Your code is: " + rawCode + "\nClick: https://your-frontend.com/verify?email=" + user.getEmail() + "&code=" + rawCode
        );

        // Generate JWT token and return response
        Map<String, Object> claims = Map.of(
                "sub", userDetails.getUsername(),
                "tenant", userDetails.getTenant(),
                "role", userDetails.getRole(),
                "user_id", u.getUserId(),
                "user_general_id", ug.getUserGeneralId(),
                "email", u.getEmail()
        );
        String jwtToken = jwtService.generateToken(claims, userDetails.getUsername(), Duration.ofHours(12));
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .verified(false)
                .build();
    }

    /**
     * Verifies the user's email address using the provided verification code.
     * <p>
     * This method checks if the provided verification code is valid, ensures that it has not expired or been used already,
     * and then marks it as used. After successful verification, the user's email is marked as verified.
     * </p>
     *
     * @param request the request containing the email and verification code
     * @return a response indicating the result of the email verification
     */
    @Transactional
    public ResponseEntity<?> verifyEmail(VerifyRequest request) {
        VerificationCode code = verificationCodeRepository.findLatestByUserEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("No verification code found"));

        if (code.getIsUsed() || code.getExpiresAt().isBefore(LocalDateTime.now())) {
            return ResponseEntity.badRequest().body(Map.of("error", "Code expired or already used"));
        }

        if (!passwordEncoder.matches(request.getCode(), code.getCodeHash())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Invalid verification code"));
        }

        code.setIsUsed(true);
        verificationCodeRepository.save(code);

        UserGeneral userGeneral = userGeneralRepository.findByUser_Email(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        userGeneral.setVerified(true);
        userGeneralRepository.save(userGeneral);

        return ResponseEntity.ok(Map.of("message", "Email verified successfully"));
    }

    /**
     * Resends the verification code to the specified email address.
     * <p>
     * This method looks up the user by their email, invalidates any previous verification codes, generates a new code,
     * and sends it via email.
     * </p>
     *
     * @param email the email address to resend the verification code to
     * @return a response indicating the success of the resend operation
     */
    @Transactional
    public ResponseEntity<?> resendVerificationCode(String email) {
        // 1. Look up the user and userGeneral
        UserGeneral userGeneral = userGeneralRepository.findByUser_Email(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        User user = userGeneral.getUser();

        // 2. Mark all previous codes as used
        List<VerificationCode> previousCodes = verificationCodeRepository.findAllByUserEmail(email);
        for (VerificationCode vc : previousCodes) {
            vc.setIsUsed(true);
        }
        verificationCodeRepository.saveAll(previousCodes);

        // 3. Generate new verification code
        String rawCode = VerificationCodeService.generate6DigitCode();
        String codeHash = passwordEncoder.encode(rawCode);

        VerificationCode newCode = VerificationCode.builder()
                .user(user)
                .codeHash(codeHash)
                .expiresAt(LocalDateTime.now().plusMinutes(15))
                .isUsed(false)
                .build();
        verificationCodeRepository.save(newCode);

        // 4. Send email with new verification code
        EmailSenderService.sendVerificationEmail(
                user.getEmail(),
                "Verify your email",
                "Hello " + userGeneral.getFirstName() + ",\n\nYour new verification code is: " + rawCode +
                        "\n\nOr click: https://your-frontend.com/verify?email=" + user.getEmail() + "&code=" + rawCode
        );

        return ResponseEntity.ok(Map.of("message", "Verification code resent successfully"));
    }
}
