package com.hrms.Human_Resource_Management_System_Back.service;


import com.hrms.Human_Resource_Management_System_Back.model.Address;
import com.hrms.Human_Resource_Management_System_Back.model.Tenant;
import com.hrms.Human_Resource_Management_System_Back.model.User;
import com.hrms.Human_Resource_Management_System_Back.model.dto.AuthenticationResponse;
import com.hrms.Human_Resource_Management_System_Back.model.dto.OwnerCreationRequest;
import com.hrms.Human_Resource_Management_System_Back.model.dto.TenantRegistrationRequest;
import com.hrms.Human_Resource_Management_System_Back.model.tenant.Role;
import com.hrms.Human_Resource_Management_System_Back.model.tenant.UserRole;
import com.hrms.Human_Resource_Management_System_Back.model.tenant.UserTenant;
import com.hrms.Human_Resource_Management_System_Back.repository.AddressRepository;
import com.hrms.Human_Resource_Management_System_Back.repository.TenantRepository;
import com.hrms.Human_Resource_Management_System_Back.repository.UserRepository;
import com.hrms.Human_Resource_Management_System_Back.repository.tenant.RoleRepository;
import com.hrms.Human_Resource_Management_System_Back.repository.tenant.UserRoleRepository;
import com.hrms.Human_Resource_Management_System_Back.repository.tenant.UserTenantRepository;
import com.hrms.Human_Resource_Management_System_Back.security.CustomUserDetails;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import javax.sql.DataSource;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class TenantOnboardingService {
    private final TenantRepository tenantRepo;
    private final AddressRepository addressRepo;
    private final JavaMailSender mailSender;
    private final JwtService jwtService;
    private final JdbcTemplate jdbc;
    private final DataSource dataSource;
    private final UserRepository userRepo;
    private final UserTenantRepository userTenantRepo;
    private final UserRoleRepository userRoleRepo;
    private final RoleRepository roleRepo;
    private final PasswordEncoder passwordEncoder;

    private static String REG_TYPE = "TENANT_REG";

    @Value("${app.frontend.url}")
    private String frontEndUrl;

    /** STEP 1: Register tenant & send email */

    @Transactional
    public void registerTenant(TenantRegistrationRequest rq) {
        System.out.println("IN Tenant Service");
            // persist address
        Address addr = addressRepo.save(rq.getAddress().toEntity());

            // generate schemaName
        String schemaName = "tenant_" +
                UUID.randomUUID().toString().replace("-", "").substring(0,12);

        // 3. save tenant (status could be a field you add)
        Tenant t = Tenant.builder()
                .name(rq.getName())
                .contactEmail(rq.getContactEmail())
                .address(addr)
                .schemaName(schemaName)
                .createdAt(LocalDateTime.now())
                .build();
        tenantRepo.save(t);

        // 4. create a short‑lived JWT carrying tenantId & type
        String token = jwtService.generateToken(
                Map.of("tenantId", t.getTenantId(), "type", REG_TYPE),
                Duration.ofHours(24)
        );
        System.out.println(" \uD83D\uDD10 The jwt token is" + token);

        // 5. send verification email
//        SimpleMailMessage msg = new SimpleMailMessage();
//        msg.setFrom("kucijon@gmail.com");
//        msg.setTo(t.getContactEmail());
//        msg.setSubject("Please verify your email");
//        msg.setText(
//                "Click to verify: https://your‑frontend.com/verify?token=" + token
//        );
//
//        mailSender.send(msg);


        String text = "Click to verify: "+frontEndUrl+"/tenant/onboarding?token=" + token;

        EmailSenderService.sendVerificationEmail(
                t.getContactEmail(),  "Please verify your company creation.", text
        );
        jdbc.execute("SET search_path TO public");
    }

    @Transactional
    public AuthenticationResponse createOwnerAfterVerification(OwnerCreationRequest rq) {

        System.out.println("📥 Token received: " + rq.getToken());

        // 1. Validate token
        Claims c = jwtService.parseToken(rq.getToken());
        if (!REG_TYPE.equals(c.get("type"))) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid token");
        }

        Integer tenantId = (Integer) c.get("tenantId");
        Tenant t = tenantRepo.findById(tenantId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Tenant not found"));

        // Mark as verified (if applicable)
        tenantRepo.save(t);

        String schema = t.getSchemaName();

        // 2. Create schema if it doesn't exist
        jdbc.execute("CREATE SCHEMA IF NOT EXISTS \"" + schema + "\"");

        // 3. Set search path BEFORE any schema-level operations
        jdbc.execute("SET search_path TO \"" + schema + "\"");

        // 4. Run schema SQL migration
        try {
            Resource resource = new ClassPathResource("Temporary_Migrations/V2__initial_tenant_schema.sql");
            String sql = new String(resource.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
            for (String stmt : sql.split(";")) {
                String trimmed = stmt.trim();
                if (!trimmed.isEmpty()) {
                    jdbc.execute(trimmed);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to load tenant schema SQL", e);
        }
        jdbc.execute("SET search_path TO \"" + "public" + "\"");
        // 5. Save user in PUBLIC schema

        String hash = passwordEncoder.encode(rq.getPassword());
        User user = User.builder()
                .username(rq.getUsername())
                .email(rq.getEmail())
                .passwordHash(hash)
                .tenantId(t.getTenantId())
                .role("TENANT_USER")
                .build();
        userRepo.save(user);

        // 6. Save tenant address in tenant schema
        Address addr = addressRepo.save(rq.getAddress().toEntity());

        jdbc.execute("SET search_path TO \"" + schema + "\"");

        // 7. Save UserTenant (tenant schema)
        UserTenant ut = UserTenant.builder()
                .user(user)
                .tenant(t)
                .firstName(rq.getFirstName())
                .lastName(rq.getLastName())
                .phone(rq.getPhone())
                .gender(rq.getGender())
                .address(addr)
                .createdAt(LocalDateTime.now())
                .build();
        userTenantRepo.save(ut);

        // 8. Save role
        Role role = Role.builder()
                .roleName("OWNER")
                .description("CEO created during tenant onboarding.")
                .build();
        roleRepo.save(role);

        // 9. Link user to role
        UserRole ur = UserRole.builder()
                .userTenant(ut)
                .role(role)
                .build();
        userRoleRepo.save(ur);

        CustomUserDetails userDetails = new CustomUserDetails(ut.getUser().getUserId(), ut, t.getSchemaName());

        Map<String, Object> claims = Map.of(
                "tenant", userDetails.getTenant(),
                "role", userDetails.getRole()
        );

        // 10. Generate auth token for login
        String authToken = jwtService.generateToken(claims, userDetails.getUsername(), Duration.ofHours(12));
        return AuthenticationResponse.builder()
                .token(authToken)
                .build();
    }


}
