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
import org.springframework.transaction.annotation.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Map;

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

    @Override
    protected UserTenantRepository getRepository() {
        return repo;
    }

    @Transactional
    public AuthenticationResponse register(RegisterTenantUserRequest rq) {
        String schema = TenantCtx.getTenant();

        /* 1. find tenant (public schema) */
        Tenant tenant = tenantRepository.findBySchemaName(schema)
                .orElseThrow(() -> new IllegalArgumentException("Tenant not found"));

        /* 2. create base User (public schema) */
        User user = User.builder()
                .email(rq.getEmail())
                .username(rq.getUsername())
                .passwordHash(passwordEncoder.encode(rq.getPassword()))
                .role("TENANT_USER")
                .tenantId(tenant.getTenantId())
                .build();
        User updatedUser = userRepository.save(user);
        //userRepository.flush();

        /* 3. persist Address in tenant schema */
        Address addr = addressRepository.save(rq.getAddress().toEntity());

        /* 4. switch search_path to tenant schema for UserTenant row */
        // The SchemaRoutingFilter + Hibernate multi‑tenancy will do
        // this automatically once TenantContext is set. Here we just
        // build the entity and save.
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
                .profilePhoto(new byte[0])   // empty blobs
                .build();
        tenantRepo.save(ut);

//        Optional<User> us = userRepository.findByEmail(user.getEmail());
//        User u = us.orElseThrow(() -> new RuntimeException("User not found"));

       // System.out.println("______INSIDE USER TENANT____");
        //System.out.println(updatedUser.getUserId());

        /* 5. JWT */
        Map<String,Object> claims = Map.of(
                "user_id", updatedUser.getUserId(),
                "tenant", tenant.getSchemaName(),   // e.g. "tenant_abc"
                "role", "TENANT_USER"
        );
        String jwt = jwtService.generateToken(
                claims,
                user.getUsername(),
                Duration.ofHours(12)
        );

        return AuthenticationResponse.builder()
                .token(jwt)
                .build();
    }

    private void validateMaxUsersLimit(Tenant tenant) {
        TenantSubscription tenantSubscription = tenantSubscriptionRepository.findByTenant(tenant)
                .orElseThrow(() -> new RuntimeException("Subscription not found for tenant"));

        Subscription subscription = tenantSubscription.getSubscription();

        Integer maxUsers = subscription.getMaxUsers();
        if (maxUsers == null) return;

        int currentUsers =Integer.TYPE.cast(repo.count());

        if (currentUsers >= maxUsers) {
            throw new RuntimeException("Maximum user limit reached for your subscription plan (" + maxUsers + " users allowed).");
        }
    }
}