package com.hrms.Human_Resource_Management_System_Back.service;

import com.hrms.Human_Resource_Management_System_Back.model.Tenant;
import com.hrms.Human_Resource_Management_System_Back.model.User;
import com.hrms.Human_Resource_Management_System_Back.model.UserGeneral;
import com.hrms.Human_Resource_Management_System_Back.model.dto.AuthenticationRequest;
import com.hrms.Human_Resource_Management_System_Back.model.dto.AuthenticationResponse;
import com.hrms.Human_Resource_Management_System_Back.model.dto.ChangePasswordRequest;
import com.hrms.Human_Resource_Management_System_Back.model.tenant.UserTenant;
import com.hrms.Human_Resource_Management_System_Back.repository.BaseRepository;
import com.hrms.Human_Resource_Management_System_Back.repository.TenantRepository;
import com.hrms.Human_Resource_Management_System_Back.repository.UserGeneralRepository;
import com.hrms.Human_Resource_Management_System_Back.repository.UserRepository;
import com.hrms.Human_Resource_Management_System_Back.repository.tenant.UserTenantRepository;
import com.hrms.Human_Resource_Management_System_Back.security.CustomUserDetails;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Service class for handling user authentication and registration.
 * <p>
 * This service provides methods for registering new users, authenticating existing ones,
 * and handling user password changes.
 * </p>
 */
@Service
@AllArgsConstructor
public class UserService extends BaseService<User, Integer> {

    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final CustomUserDetailsService userDetailsService;
    private final UserGeneralRepository userGeneralRepository;
    private final UserTenantRepository userTenantRepository;
    private final TenantRepository tenantRepository;
    private final JdbcTemplate jdbc;
    private final PasswordEncoder encoder;

    /**
     * Returns the user repository.
     * <p>
     * This method overrides the {@link BaseService#getRepository()} method to return the specific repository
     * for user entities.
     * </p>
     *
     * @return the user repository
     */
    @Override
    protected BaseRepository<User, Integer> getRepository() {
        return userRepository;
    }

    /**
     * Authenticates a user and generates a JWT token.
     * <p>
     * This method authenticates the user by verifying the provided credentials. After successful authentication,
     * it creates a JWT token containing user and tenant-related claims. The token is returned in the response.
     * </p>
     *
     * @param request the authentication request containing user credentials (email and password)
     * @return an {@link AuthenticationResponse} containing the generated JWT token
     * @throws BadCredentialsException if the credentials are invalid or the user is not found
     */
    @Transactional
    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );
        CustomUserDetails userDetails = (CustomUserDetails) auth.getPrincipal();

        Map<String,Object> claims = new HashMap<>();

        // Handle GENERAL_USER case
        if (userDetails.getRole().equals("GENERAL_USER")) {
            Optional<UserGeneral> optionalUG = userGeneralRepository.findByUser_Email(userDetails.getUsername());

            UserGeneral ug = optionalUG.orElseThrow(() ->
                    new BadCredentialsException("UserGeneral not found for email: " + userDetails.getUsername())
            );

            if (!ug.isVerified()) {
                throw new BadCredentialsException("Email not verified.");
            }
            claims.put("user_general_id", ug.getUserGeneralId());
        }

        // Handle TENANT_USER case
        if (userDetails.getRole().equals("TENANT_USER")) {
            User u = userRepository.getReferenceById(userDetails.getUserId());
            Tenant t = tenantRepository.getReferenceById(u.getTenantId());
            jdbc.execute("SET search_path TO " + t.getSchemaName());
            Optional<UserTenant> userTenant = userTenantRepository.findByUser_Email(userDetails.getUsername());
            UserTenant ut = userTenant.orElseThrow(() ->
                    new BadCredentialsException("UserTenant not found for email: " + userDetails.getUsername())
            );
            claims.put("user_tenant_id", ut.getUserTenantId());
        }

        // Create JWT with tenant and role claims
        claims.put("sub", userDetails.getUsername());
        claims.put("user_id", userDetails.getUserId());
        claims.put("role", userDetails.getRole());
        if (userDetails.getTenant() != null) {
            claims.put("tenant", userDetails.getTenant());
        }

        String jwtToken = jwtService.generateToken(claims, userDetails.getUsername(), Duration.ofHours(12));
        System.out.println("JWT TOKEN IS: " + jwtToken);

        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    /**
     * Changes the user's password.
     * <p>
     * This method allows a user to change their password. It first verifies the current password, checks if the new password
     * meets the length requirement, and then updates the password in the repository.
     * </p>
     *
     * @param dto the request containing the current password and the new password
     * @throws IllegalArgumentException if the current password is incorrect or the new password does not meet the requirements
     */
    @Transactional
    public void changePassword(ChangePasswordRequest dto) {
        // 1) Get logged-in user email
        String email = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName(); // Email is stored in JWT

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(email));

        // 2) Check if current password is correct
        if (!encoder.matches(dto.getCurrentPassword(), user.getPasswordHash())) {
            throw new IllegalArgumentException("Current password is incorrect");
        }

        // 3) Validate new password
        if (dto.getNewPassword().length() < 6) {
            throw new IllegalArgumentException("Password must be at least 6 characters");
        }

        // 4) Save new password
        user.setPasswordHash(encoder.encode(dto.getNewPassword()));
        userRepository.save(user);
    }
}