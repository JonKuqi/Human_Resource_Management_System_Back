package com.hrms.Human_Resource_Management_System_Back.controller;

import com.hrms.Human_Resource_Management_System_Back.model.UserGeneral;
import com.hrms.Human_Resource_Management_System_Back.model.dto.AuthenticationResponse;
import com.hrms.Human_Resource_Management_System_Back.model.dto.RegisterGeneralRequest;
import com.hrms.Human_Resource_Management_System_Back.model.dto.VerifyRequest;
import com.hrms.Human_Resource_Management_System_Back.service.BaseService;
import com.hrms.Human_Resource_Management_System_Back.service.JwtService;
import com.hrms.Human_Resource_Management_System_Back.service.UserGeneralService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;


/**
 * Controller for handling user general operations such as registration, email verification, and resending verification codes.
 * <p>
 * This controller provides endpoints for user general functions including registration, email verification,
 * and managing verification code resend requests.
 * </p>
 */
@RestController
@RequestMapping("/api/v1/public/user-general")
@AllArgsConstructor
public class UserGeneralController extends BaseController<UserGeneral, Integer> {

    /**
     * The service responsible for handling user general business logic.
     */
    private final UserGeneralService userGeneralService;

    /**
     * The service responsible for handling JWT authentication and token generation.
     */
    private final JwtService jwtService;

    /**
     * Overrides {@link BaseController#getService()} to return the user general service.
     *
     * @return the user general service
     */
    @Override
    protected BaseService<UserGeneral, Integer> getService() {
        return userGeneralService;
    }

    /**
     * Registers a new user in the system.
     * <p>
     * This method handles the registration of a new user based on the provided request, including user details.
     * After successful registration, an authentication response containing necessary tokens is returned.
     * </p>
     *
     * @param request the registration request containing user details
     * @return a {@link ResponseEntity} containing the authentication response
     */
    @Operation(
            summary = "Register a general user",
            description = "Registers a new user and returns an authentication token upon success."
    )
    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(@RequestBody RegisterGeneralRequest request) {
        return ResponseEntity.ok(userGeneralService.register(request));
    }

    /**
     * Verifies a user's email address.
     * <p>
     * This method validates the user's email verification request, typically through a verification code sent to the user's email.
     * </p>
     *
     * @param request the request containing the verification code and user email
     * @return a {@link ResponseEntity} containing the result of the email verification process
     */
    @Operation(
            summary = "Verify user email",
            description = "Verifies the email of a registered user using a verification code."
    )
    @PostMapping("/verify")
    public ResponseEntity<?> verifyEmail(@RequestBody VerifyRequest request) {
        return userGeneralService.verifyEmail(request);
    }

    /**
     * Resends the email verification code to the user.
     * <p>
     * This method takes the user's email, validates it, and sends a new verification code to the specified email address.
     * If the email is missing or invalid, a bad request response is returned.
     * </p>
     *
     * @param body a map containing the user's email address
     * @return a {@link ResponseEntity} indicating the result of the resend process
     */
    @Operation(
            summary = "Resend email verification code",
            description = "Sends a new verification code to the specified email address."
    )
    @PostMapping("/resend")
    public ResponseEntity<?> resendVerificationCode(@RequestBody Map<String, String> body) {
        String email = body.get("email");
        if (email == null || email.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("error", "Email is required"));
        }
        return userGeneralService.resendVerificationCode(email);
    }
}