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
 * This service provides methods for registering new users and authenticating existing ones.
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


    @Override
    protected BaseRepository<User, Integer> getRepository() {
        return userRepository;
    }

    /**
     * Authenticates a user and generates a JWT token.
     * @param request the authentication request containing user credentials
     * @return an {@link AuthenticationResponse} containing the generated JWT token
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

//        System.out.println("USER DETAILS: " +userDetails);
//        System.out.println("    "+ userDetails.getUsername());
//        System.out.println("    "+userDetails.getUserId());
//        System.out.println("    "+userDetails.getTenant());
//        System.out.println("   "+userDetails.getRole());

        Map<String,Object> claims = new HashMap<>();

        if (userDetails.getRole().equals("GENERAL_USER")){
            Optional<UserGeneral> optionalUG = userGeneralRepository.findByUser_Email(userDetails.getUsername());

            UserGeneral ug = optionalUG.orElseThrow(() ->
                    new BadCredentialsException("UserGeneral not found for email: " + userDetails.getUsername())
            );

            if (!ug.isVerified()) {
                throw new BadCredentialsException("Email not verified.");
            }
            claims.put("user_general_id", ug.getUserGeneralId());

        }
        if(userDetails.getRole().equals("TENANT_USER")){
            User u = userRepository.getReferenceById(userDetails.getUserId());
            Tenant t = tenantRepository.getReferenceById(u.getTenantId());
            jdbc.execute("SET search_path TO "+ t.getSchemaName());
            Optional<UserTenant> userTenant = userTenantRepository.findByUser_Email(userDetails.getUsername());
            UserTenant ut = userTenant.orElseThrow(() ->
                    new BadCredentialsException("UserTenant not found for email: " + userDetails.getUsername())
            );
            claims.put("user_tenant_id", ut.getUserTenantId());

        }

        // 3. Create JWT with tenant and role claims

        claims.put("sub",     userDetails.getUsername());
        claims.put("user_id", userDetails.getUserId());
        claims.put("role",    userDetails.getRole());
        if (userDetails.getTenant() != null) {
            claims.put("tenant", userDetails.getTenant());
        }
        String jwtToken = jwtService.generateToken(claims, userDetails.getUsername(), Duration.ofHours(12));
        System.out.println(" JWT TOKEN IS: " + jwtToken);

        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    @Transactional
    public void changePassword(ChangePasswordRequest dto) {

        // 1) who is logged-in?
        String email = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();        // we saved email in JWT

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(email));

        // 2) check old password
        if (!encoder.matches(dto.getCurrentPassword(), user.getPasswordHash())) {
            throw new IllegalArgumentException("Current password is incorrect");
        }

        // 3) validate new password (length etc.) – very simple example
        if (dto.getNewPassword().length() < 6) {
            throw new IllegalArgumentException("Password must be at least 6 characters");
        }

        // 4) save new password
        user.setPasswordHash(encoder.encode(dto.getNewPassword()));
        userRepository.save(user);
    }

}
