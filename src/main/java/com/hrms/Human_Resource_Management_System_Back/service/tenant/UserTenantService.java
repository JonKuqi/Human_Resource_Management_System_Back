package com.hrms.Human_Resource_Management_System_Back.service.tenant;

import com.hrms.Human_Resource_Management_System_Back.middleware.TenantCtx;
import com.hrms.Human_Resource_Management_System_Back.model.*;
import com.hrms.Human_Resource_Management_System_Back.model.dto.AuthenticationResponse;
import com.hrms.Human_Resource_Management_System_Back.model.dto.RegisterTenantUserRequest;
import com.hrms.Human_Resource_Management_System_Back.model.tenant.Contract;
import com.hrms.Human_Resource_Management_System_Back.model.tenant.Department;
import com.hrms.Human_Resource_Management_System_Back.model.tenant.Position;
import com.hrms.Human_Resource_Management_System_Back.model.tenant.UserTenant;
import com.hrms.Human_Resource_Management_System_Back.repository.AddressRepository;
import com.hrms.Human_Resource_Management_System_Back.repository.TenantRepository;
import com.hrms.Human_Resource_Management_System_Back.repository.TenantSubscriptionRepository;
import com.hrms.Human_Resource_Management_System_Back.repository.UserRepository;
import com.hrms.Human_Resource_Management_System_Back.repository.tenant.ContractRepository;
import com.hrms.Human_Resource_Management_System_Back.repository.tenant.DepartmentRepository;
import com.hrms.Human_Resource_Management_System_Back.repository.tenant.PositionRepository;
import com.hrms.Human_Resource_Management_System_Back.repository.tenant.UserTenantRepository;
import com.hrms.Human_Resource_Management_System_Back.service.BaseUserSpecificService;
import com.hrms.Human_Resource_Management_System_Back.service.EmailSenderService;
import com.hrms.Human_Resource_Management_System_Back.service.JwtService;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.SecureRandom;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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
    private final DepartmentRepository departmentRepo;
    private final PositionRepository positionRepo;
    private final ContractRepository contractRepo;

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
     * the user-tenant relationship. It then  generates a JWT token for the authenticated user.
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
        List<TenantSubscription> subscriptions = tenantSubscriptionRepository.findAllByTenantOrderByCreatedAtDesc(tenant);

        if (subscriptions.isEmpty()) {
            throw new RuntimeException("Subscription not found for tenant");
        }

        TenantSubscription latest = subscriptions.get(0);

        Subscription subscription = latest.getSubscription();
        Integer maxUsers = subscription.getMaxUsers();

        if (maxUsers == null) return;

        int currentUsers = (int) repo.count();

        if (currentUsers >= maxUsers) {
            throw new RuntimeException(
                    "Maximum user limit reached for your current plan '" + subscription.getPlanName() +
                            "' (" + maxUsers + " users allowed). Please upgrade to a higher plan."
            );
        }
    }



    /**
     * Creates a new employee along with related data by User Tenant Owner.
     * <p>
     * Validates the current tenant and checks subscription user limits.
     * Creates a new {@link User} in the public schema with a temporary password.
     * Stores the employee's address in the public schema as {@link Address}.
     * Creates a new {@link UserTenant} entry associated with the tenant and user.
     * Optionally handles {@link Department} and {@link Position} creation if values are provided.
     * If position is created or found, it also creates a {@link Contract} with the provided details.
     * Sends a verification email with the generated temporary password to the employee.
     * </p>
     *
     * @param entity the request containing employee registration data
     * @throws RuntimeException if user limit is exceeded or tenant is invalid
     */
    @Override
    @Transactional
    public UserTenant save(UserTenant entity) {
        String schema = TenantCtx.getTenant();
        Tenant tenant = tenantRepository.findBySchemaName(schema)
                .orElseThrow(() -> new IllegalArgumentException("Tenant not found"));

        validateMaxUsersLimit(tenant);

        String rawPassword = generateRandomPassword(10);

        User user = User.builder()
                .email(entity.getUser().getEmail())
                .username(entity.getUser().getEmail())
                .passwordHash(passwordEncoder.encode(rawPassword))
                .role("TENANT_USER")
                .tenantId(tenant.getTenantId())
                .build();
        userRepository.save(user);

        entity.setUser(user);
        entity.setTenant(tenant);
        entity.setCreatedAt(LocalDateTime.now());
        entity.setProfilePhoto(new byte[0]);

        UserTenant savedUserTenant = repo.save(entity);

        EmailSenderService.sendVerificationEmail(
                user.getEmail(),
                "Welcome to NexHR",
                "Dear " + entity.getFirstName() + ",\n\n" +
                        "Your temporary password is:\n\n" + rawPassword +
                        "\n\nPlease log in and change it as soon as possible.\n\nRegards,\nNexHR Team"
        );

        return savedUserTenant;
    }


    /**
     * Generates a random password string with a given length.
     *
     * @param length the desired length of the password
     * @return the generated password string
     */
    private String generateRandomPassword(int length) {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789@#$%";
        SecureRandom rnd = new SecureRandom();
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append(chars.charAt(rnd.nextInt(chars.length())));
        }
        return sb.toString();
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

    public boolean updateBasicInfo(UserTenantUpdateDTO dto) {
        Optional<UserTenant> optionalUserTenant = repo.findById(dto.getUserTenantId());
        Optional<Address> optionalAddress = addressRepository.findById(dto.getAddressId());

        if (optionalUserTenant.isEmpty() || optionalAddress.isEmpty()) {
            return false;
        }

        UserTenant ut = optionalUserTenant.get();
        ut.setFirstName(dto.getFirstName());
        ut.setLastName(dto.getLastName());
        ut.setPhone(dto.getPhone());
        ut.setGender(dto.getGender());
        ut.setAddress(optionalAddress.get());

        repo.save(ut);
        return true;
    }
}