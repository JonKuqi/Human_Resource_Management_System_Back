package com.hrms.Human_Resource_Management_System_Back.service;

import com.hrms.Human_Resource_Management_System_Back.model.User;
import com.hrms.Human_Resource_Management_System_Back.model.dto.AuthenticationRequest;
import com.hrms.Human_Resource_Management_System_Back.model.dto.AuthenticationResponse;
import com.hrms.Human_Resource_Management_System_Back.repository.BaseRepository;
import com.hrms.Human_Resource_Management_System_Back.repository.UserGeneralRepository;
import com.hrms.Human_Resource_Management_System_Back.repository.UserRepository;
import com.hrms.Human_Resource_Management_System_Back.security.CustomUserDetails;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Map;

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


    @Override
    protected BaseRepository<User, Integer> getRepository() {
        return userRepository;
    }

    /**
     * Registers a new user and generates a JWT token.
     * @param request the registration request containing user details
     * @return an {@link AuthenticationResponse} containing the generated JWT token
     */


    /**
     *     RETIRED THE METHOD. USE USER GENERAL SERVICE
     */

//    @Transactional
//    public AuthenticationResponse register(RegisterRequest request) {
//        String salt = PasswordHasher.generateSalt();
//        String role = String.valueOf(RoleUser.GENERAL_USER);
//
//        // 1. Create and save the base user
//        var user = User.builder()
//                .email(request.getEmail())
//                .username(request.getUsername())
//                .passwordHash(PasswordHasher.generateSaltedHash(request.getPassword(), salt))
//                .role(role)
//                .tenantId(null)
//                .build();
//
//        userRepository.save(user);
//        //System.out.println("HEREEEEEEEEEE");
//
//        // 2. Create dummy UserGeneral (not saved, just for wrapping)
//        var userGeneral = UserGeneral.builder()
//                .user(user)
//                .firstName("Test")
//                .lastName("User")
//                .phone("123456789")
//                .gender("Other")
//                .build();
//        userGeneralRepository.save(userGeneral);
//
//        // 3. Wrap in CustomUserDetails
//        var userDetails = new CustomUserDetails(userGeneral);
//
//        // 4. Add tenant + role to JWT claims
//        Map<String, Object> claims = new HashMap<>();
//        claims.put("tenant", userDetails.getTenant());
//        claims.put("role", userDetails.getRole());
//
//        String jwtToken = jwtService.generateToken(claims, userDetails.getUsername(), Duration.ofHours(2));
//
//        return AuthenticationResponse.builder()
//                .token(jwtToken)
//                .build();
//    }

    /**
     * Authenticates a user and generates a JWT token.
     * @param request the authentication request containing user credentials
     * @return an {@link AuthenticationResponse} containing the generated JWT token
     */
    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );
        CustomUserDetails userDetails = (CustomUserDetails) auth.getPrincipal();

        // 3. Create JWT with tenant and role claims
        Map<String, Object> claims = Map.of(
                "user_id", userDetails.getUserId(),
                "tenant", userDetails.getTenant(),
                "role",   userDetails.getRole()
        );
        String jwtToken = jwtService.generateToken(claims, userDetails.getUsername(), Duration.ofHours(12));

        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

}
