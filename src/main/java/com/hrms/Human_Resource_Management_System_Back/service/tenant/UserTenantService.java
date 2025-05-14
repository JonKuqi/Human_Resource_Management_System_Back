package com.hrms.Human_Resource_Management_System_Back.service.tenant;

import com.hrms.Human_Resource_Management_System_Back.middleware.TenantCtx;
import com.hrms.Human_Resource_Management_System_Back.model.*;
import com.hrms.Human_Resource_Management_System_Back.model.dto.AuthenticationResponse;
import com.hrms.Human_Resource_Management_System_Back.model.dto.RegisterTenantUserRequest;
import com.hrms.Human_Resource_Management_System_Back.model.tenant.UserTenant;
import com.hrms.Human_Resource_Management_System_Back.repository.AddressRepository;
import com.hrms.Human_Resource_Management_System_Back.repository.TenantRepository;
import com.hrms.Human_Resource_Management_System_Back.repository.TenantSubscriptionRepository;
import com.hrms.Human_Resource_Management_System_Back.repository.UserRepository;
import com.hrms.Human_Resource_Management_System_Back.repository.tenant.UserTenantRepository;
import com.hrms.Human_Resource_Management_System_Back.service.BaseUserSpecificService;
import com.hrms.Human_Resource_Management_System_Back.service.JwtService;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Map;

/**
 * Service class for handling user-tenant operations such as user registration, profile photo updates,
 * and validating user limits based on tenant subscriptions.
 * <p>
 * This service extends {@link BaseUserSpecificService} and provides methods for managing user-tenant relationships,
 * including user registration, profile photo updates, and subscription-based user limits.
 * </p>
 */
@Service
@AllArgsConstructor
public class UserTenantService extends BaseUserSpecificService<UserTenant, Integer> {

    private final UserTenantRepository repo;
    private final UserTenantRepository tenantRepo;
    private final TenantRepository tenantRepository;
    private final AddressRepository addressRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final TenantSubscriptionRepository tenantSubscriptionRepository;

    /**
     * Returns the user-tenant repository.
     * <p>
     * This method overrides the {@link BaseUserSpecificService#getRepository()} method to return the specific repository
     * for user-tenant entities.
     * </p>
     *
     * @return the user-tenant repository
     */
    @Override
    protected UserTenantRepository getRepository() {
        return repo;
    }

    /**
     * Registers a new tenant user, including creating the user, user-tenant relationship, and generating a JWT token.
     * <p>
     * This method creates a new user in the public schema, persists the user address in the tenant schema, and saves
     * the user-tenant relationship. It then validates the user limit based on the tenant's subscription and generates
     * a JWT token for the authenticated user.
     * </p>
     *
     * @param rq the registration request containing user and address information
     * @return an {@link AuthenticationResponse} containing the generated JWT token
     */
    @Transactional
    public AuthenticationResponse register(RegisterTenantUserRequest rq) {
        String schema = TenantCtx.getTenant();

        // 1. Find tenant based on the schema (public schema)
        Tenant tenant = tenantRepository.findBySchemaName(schema)
                .orElseThrow(() -> new IllegalArgumentException("Tenant not found"));

        // 2. Create base User (public schema)
        User user = User.builder()
                .email(rq.getEmail())
                .username(rq.getUsername())
                .passwordHash(passwordEncoder.encode(rq.getPassword()))
                .role("TENANT_USER")
                .tenantId(tenant.getTenantId())
                .build();
        User updatedUser = userRepository.save(user);

        // 3. Persist Address in tenant schema
        Address addr = addressRepository.save(rq.getAddress().toEntity());

        // 4. Validate max user limit based on tenant subscription and create UserTenant row
        validateMaxUsersLimit(tenant);
        UserTenant ut = UserTenant.builder()
                .user(user)
                .tenant(tenant)
                .firstName(rq.getFirstName())
                .lastName(rq.getLastName())
                .phone(rq.getPhone())
                .gender(rq.getGender())
                .address(addr)
                .createdAt(LocalDateTime.now())
                .profilePhoto(new byte[0])   // empty blobs for profile photo
                .build();
        tenantRepo.save(ut);

        // 5. Generate JWT token with user and tenant claims
        Map<String, Object> claims = Map.of(
                "user_id", updatedUser.getUserId(),
                "tenant", tenant.getSchemaName(),   // e.g. "tenant_abc"
                "role", "TENANT_USER"
        );
        String jwt = jwtService.generateToken(claims, user.getUsername(), Duration.ofHours(12));

        return AuthenticationResponse.builder()
                .token(jwt)
                .build();
    }

    /**
     * Validates the maximum number of users allowed for a tenant based on their subscription plan.
     * <p>
     * This method checks the subscription plan associated with the tenant to ensure that the number of users
     * does not exceed the maximum allowed users. If the limit is reached, a {@link RuntimeException} is thrown.
     * </p>
     *
     * @param tenant the tenant to validate the user limit for
     * @throws RuntimeException if the user limit is exceeded
     */
    private void validateMaxUsersLimit(Tenant tenant) {
        TenantSubscription tenantSubscription = tenantSubscriptionRepository.findByTenant(tenant)
                .orElseThrow(() -> new RuntimeException("Subscription not found for tenant"));

        Subscription subscription = tenantSubscription.getSubscription();

        Integer maxUsers = subscription.getMaxUsers();
        if (maxUsers == null) return;  // No limit

        int currentUsers = Integer.TYPE.cast(repo.count());

        if (currentUsers >= maxUsers) {
            throw new RuntimeException("Maximum user limit reached for your subscription plan (" + maxUsers + " users allowed).");
        }
    }

    /**
     * Updates the profile photo for a user tenant.
     * <p>
     * This method allows the user tenant to upload and update their profile photo.
     * It stores the uploaded photo as a byte array in the user tenant record.
     * </p>
     *
     * @param id   the ID of the user tenant whose profile photo is to be updated
     * @param file the file containing the new profile photo
     * @throws IOException if there is an error reading the file
     */
    @Transactional
    public void updateProfilePhoto(Integer id, MultipartFile file) throws IOException {
        var entity = repo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User-Tenant not found"));

        entity.setProfilePhoto(file.getBytes());  // Set the profile photo as a byte array
        repo.save(entity);  // Save the updated user tenant entity
    }
}