package com.hrms.Human_Resource_Management_System_Back.service;

import com.hrms.Human_Resource_Management_System_Back.config.JwtService;
import com.hrms.Human_Resource_Management_System_Back.model.dto.AuthenticationRequest;
import com.hrms.Human_Resource_Management_System_Back.model.dto.AuthenticationResponse;
import com.hrms.Human_Resource_Management_System_Back.model.dto.RegisterRequest;
import com.hrms.Human_Resource_Management_System_Back.model.Role;
import com.hrms.Human_Resource_Management_System_Back.model.User;
import com.hrms.Human_Resource_Management_System_Back.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * Service class for handling user authentication and registration.
 * <p>
 * This service provides methods for registering new users and authenticating existing ones.
 * </p>
 */
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    /**
     * Registers a new user and generates a JWT token.
     * @param request the registration request containing user details
     * @return an {@link AuthenticationResponse} containing the generated JWT token
     */
    public AuthenticationResponse register(RegisterRequest request) {
        String salt = PasswordHasher.generateSalt();

        var user = User.builder()
                .name(request.getFirstName())
                .email(request.getEmail())
                .username(request.getUsername())
                .salt(salt)
                .passwordHash(PasswordHasher.generateSaltedHash(request.getPassword(), salt))
                .role(Role.USER)
                .build();
        repository.save(user);
        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    /**
     * Authenticates a user and generates a JWT token.
     * @param request the authentication request containing user credentials
     * @return an {@link AuthenticationResponse} containing the generated JWT token
     */
    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );
        var user = repository.findByEmail(request.getEmail())
                .orElseThrow();
        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }
}
