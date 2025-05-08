package com.hrms.Human_Resource_Management_System_Back.controller;


import com.hrms.Human_Resource_Management_System_Back.model.dto.AuthenticationRequest;
import com.hrms.Human_Resource_Management_System_Back.model.dto.AuthenticationResponse;
import com.hrms.Human_Resource_Management_System_Back.model.dto.RegisterRequest;
import com.hrms.Human_Resource_Management_System_Back.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * Controller for handling authentication-related requests.
 * <p>
 * This controller provides endpoints for user registration and authentication.
 * </p>
 */
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final UserService userService;

    /**
     * Handles user registration.
     *
     * @param request the {@link RegisterRequest} containing user details
     * @return a {@link ResponseEntity} containing the {@link AuthenticationResponse}
     */
//    @PostMapping("/register")
//    public ResponseEntity<AuthenticationResponse> register(
//            @RequestBody RegisterRequest request
//    ) {
//        return ResponseEntity.ok(userService.register(request));
//    }

    /**
     * Handles user authentication.
     * <p>
     * Expected request body:
     * <ul>
     *   <li>name</li>
     *   <li>email</li>
     *   <li>username</li>
     *   <li>password</li>
     * </ul>
     * </p>
     *
     * @param request the {@link AuthenticationRequest} containing login credentials
     * @return a {@link ResponseEntity} containing the {@link AuthenticationResponse}
     */

}
