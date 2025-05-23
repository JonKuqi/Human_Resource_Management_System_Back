package com.hrms.Human_Resource_Management_System_Back.controller;

import com.hrms.Human_Resource_Management_System_Back.model.User;
import com.hrms.Human_Resource_Management_System_Back.model.dto.AuthenticationRequest;
import com.hrms.Human_Resource_Management_System_Back.model.dto.AuthenticationResponse;
import com.hrms.Human_Resource_Management_System_Back.model.dto.ChangePasswordRequest;
import com.hrms.Human_Resource_Management_System_Back.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller for managing user-related operations such as authentication and password changes.
 * <p>
 * This controller handles user-specific endpoints, including authentication and password management.
 * </p>
 */
@RestController
@RequestMapping("api/v1/public/user")
@AllArgsConstructor
public class UserController extends BaseController<User, Integer> {

    /**
     * The service responsible for handling user business logic.
     */
    private final UserService userService;

    /**
     * Overrides {@link BaseController#getService()} to return the user service.
     *
     * @return the user service
     */
    @Override
    protected UserService getService() {
        return userService;
    }

    /**
     * Authenticates a user based on the provided authentication request.
     * <p>
     * This method takes a user's authentication request (which typically includes email and password),
     * and returns an authentication response, typically containing a JWT or session token.
     * </p>
     *
     * @param request the authentication request containing the user's credentials
     * @return a {@link ResponseEntity} containing the authentication response
     */
    @Operation(
            summary = "Authenticate user",
            description = "Authenticates a user and returns an authentication response with a JWT token."
    )
    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(
            @RequestBody AuthenticationRequest request
    ) {
        System.out.println("Here");
        return ResponseEntity.ok(userService.authenticate(request));
    }

    /**
     * Changes the password for a user.
     * <p>
     * This method validates the change password request and calls the user service to update the user's password.
     * It returns a {@link ResponseEntity} with a status of NO_CONTENT (204) upon successful completion.
     * </p>
     *
     * @param req the change password request containing the user's current and new password
     * @return a {@link ResponseEntity} with a 204 status on success
     */
    @Operation(
            summary = "Change user password",
            description = "Changes the password for the user. Requires the old password and new password."
    )
    @PostMapping("/change-password")
    public ResponseEntity<Void> changePassword(
            @Valid @RequestBody ChangePasswordRequest req) {

        userService.changePassword(req);
        return ResponseEntity.noContent().build();   // 204 on success
    }
}