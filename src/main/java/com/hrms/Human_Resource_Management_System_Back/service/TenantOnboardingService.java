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


/**
 * Service for handling tenant onboarding, including tenant registration, schema creation, user creation,
 * and tenant email verification.
 * <p>
 * This service is responsible for managing the entire tenant onboarding process, including registering a tenant,
 * sending verification emails, creating tenant-specific schemas, and setting up the first tenant user (owner).
 * </p>
 */
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

    /**
     * Frontend URL used in the verification email.
     */
    @Value("${app.frontend.url}")
    private String frontEndUrl;

    /**
     * Step 1: Registers the tenant and sends a verification email.
     * <p>
     * This method handles tenant registration, including saving the tenant information, generating a unique schema name,
     * creating a short-lived JWT token, and sending a verification email to the provided email address.
     * </p>
     *
     * @param rq the tenant registration request containing the necessary information
     */
    @Transactional
    public void registerTenant(TenantRegistrationRequest rq) {
        System.out.println("IN Tenant Service");

        // Persist the address for the tenant
        Address addr = addressRepo.save(rq.getAddress().toEntity());

        // Generate a unique schema name for the tenant
        String schemaName = "tenant_" + UUID.randomUUID().toString().replace("-", "").substring(0, 12);

        // Save the tenant information
        Tenant t = Tenant.builder()
                .name(rq.getName())
                .contactEmail(rq.getContactEmail())
                .address(addr)
                .schemaName(schemaName)
                .createdAt(LocalDateTime.now())
                .build();
        tenantRepo.save(t);

        // Generate a short-lived JWT token for tenant verification
        String token = jwtService.generateToken(
                Map.of("tenantId", t.getTenantId(), "type", REG_TYPE),
                Duration.ofHours(24)
        );
        System.out.println(" \uD83D\uDD10 The jwt token is " + token);

        // Send verification email with the token
        String text = "Click to verify: " + frontEndUrl + "/tenant/onboarding?token=" + token;
        EmailSenderService.sendVerificationEmail(
                t.getContactEmail(), "Please verify your company creation.", text
        );

        // Reset the schema to public after sending email
        jdbc.execute("SET search_path TO public");
    }

    /**
     * Step 2: Creates the owner (first user) for the tenant after email verification.
     * <p>
     * This method validates the tenant's registration token, creates a new schema for the tenant if it doesn't exist,
     * sets the schema search path, runs SQL migrations for the tenant schema, and creates the first user (owner) for the tenant.
     * </p>
     *
     * @param rq the request containing the verification token and user details
     * @return an {@link AuthenticationResponse} containing the generated JWT token for the logged-in user
     * @throws ResponseStatusException if the token is invalid or the tenant is not found
     */
    @Transactional
    public AuthenticationResponse createOwnerAfterVerification(OwnerCreationRequest rq) {
        System.out.println("📥 Token received: " + rq.getToken());

        // 1. Validate the JWT token
        Claims c = jwtService.parseToken(rq.getToken());
        if (!REG_TYPE.equals(c.get("type"))) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid token");
        }

        Integer tenantId = (Integer) c.get("tenantId");
        Tenant t = tenantRepo.findById(tenantId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Tenant not found"));

        // 2. Create the tenant schema if it doesn't exist
        String schema = t.getSchemaName();
        jdbc.execute("CREATE SCHEMA IF NOT EXISTS \"" + schema + "\"");

        // 3. Set the search path to the tenant schema
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

        // 5. Set the search path back to the public schema
        jdbc.execute("SET search_path TO public");

        // 6. Save the user (owner) in the public schema
        String hash = passwordEncoder.encode(rq.getPassword());
        User user = User.builder()
                .username(rq.getUsername())
                .email(rq.getEmail())
                .passwordHash(hash)
                .tenantId(t.getTenantId())
                .role("TENANT_USER")
                .build();
        userRepo.save(user);

        // 7. Save the tenant address in the tenant schema
        Address addr = addressRepo.save(rq.getAddress().toEntity());
        jdbc.execute("SET search_path TO \"" + schema + "\"");

        // 8. Save UserTenant (tenant schema)
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

        // 9. Save the "OWNER" role for the user
        Role role = Role.builder()
                .roleName("OWNER")
                .description("CEO created during tenant onboarding.")
                .build();
        roleRepo.save(role);

        // 10. Link the user to the "OWNER" role
        UserRole ur = UserRole.builder()
                .userTenant(ut)
                .role(role)
                .build();
        userRoleRepo.save(ur);

        // 11. Generate and return the JWT token for the user
        CustomUserDetails userDetails = new CustomUserDetails(ut.getUser().getUserId(), ut, t.getSchemaName());
        Map<String, Object> claims = Map.of(
                "tenant", userDetails.getTenant(),
                "role", userDetails.getRole()
        );

        String authToken = jwtService.generateToken(claims, userDetails.getUsername(), Duration.ofHours(12));
        return AuthenticationResponse.builder()
                .token(authToken)
                .build();
    }
}