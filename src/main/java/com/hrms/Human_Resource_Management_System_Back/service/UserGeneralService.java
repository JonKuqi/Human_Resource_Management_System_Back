package com.hrms.Human_Resource_Management_System_Back.service;

import com.hrms.Human_Resource_Management_System_Back.model.User;
import com.hrms.Human_Resource_Management_System_Back.model.UserGeneral;
import com.hrms.Human_Resource_Management_System_Back.model.dto.AuthenticationResponse;
import com.hrms.Human_Resource_Management_System_Back.model.dto.RegisterGeneralRequest;
import com.hrms.Human_Resource_Management_System_Back.model.types.RoleUser;
import com.hrms.Human_Resource_Management_System_Back.repository.UserGeneralRepository;
import com.hrms.Human_Resource_Management_System_Back.repository.UserRepository;
import com.hrms.Human_Resource_Management_System_Back.security.CustomUserDetails;
import org.springframework.transaction.annotation.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Map;

@Service
@AllArgsConstructor
public class UserGeneralService extends BaseService<UserGeneral, Integer> {

    private final UserGeneralRepository userGeneralRepository;
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    @Override
    protected UserGeneralRepository getRepository() {
        return userGeneralRepository;
    }

    @Transactional
    public AuthenticationResponse register(RegisterGeneralRequest request) {
        String salt = PasswordHasher.generateSalt();
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

        userGeneralRepository.save(userGeneral);

        // 3. Wrap and generate token
        CustomUserDetails userDetails = new CustomUserDetails(u.getUserId(), userGeneral);

        Map<String, Object> claims = Map.of(
                "tenant", userDetails.getTenant(),
                "role", userDetails.getRole()
        );

        String jwtToken = jwtService.generateToken(claims, userDetails.getUsername(), Duration.ofHours(2));

        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }
}
