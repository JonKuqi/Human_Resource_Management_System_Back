package com.hrms.Human_Resource_Management_System_Back.service;

import com.hrms.Human_Resource_Management_System_Back.model.Tenant;
import com.hrms.Human_Resource_Management_System_Back.model.User;
import com.hrms.Human_Resource_Management_System_Back.repository.TenantRepository;
import com.hrms.Human_Resource_Management_System_Back.repository.UserGeneralRepository;
import com.hrms.Human_Resource_Management_System_Back.repository.UserRepository;
import com.hrms.Human_Resource_Management_System_Back.repository.tenant.UserTenantRepository;
import com.hrms.Human_Resource_Management_System_Back.security.CustomUserDetails;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


/**
 * Custom implementation of {@link UserDetailsService} for loading user details based on email.
 * <p>
 * This service class handles the process of authenticating users, whether they are public/general users
 * or tenant-specific users. It retrieves user details from different repositories and determines the appropriate schema
 * based on the user's role and tenant association.
 * </p>
 */
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    /**
     * Repository for accessing public/general users.
     */
    private final UserRepository userRepository;

    /**
     * Repository for accessing user details for general users.
     */
    private final UserGeneralRepository userGeneralRepository;

    /**
     * Repository for accessing user details for tenant-specific users.
     */
    private final UserTenantRepository userTenantRepository;

    /**
     * Repository for accessing tenant details, specifically for fetching the schema name.
     */
    private final TenantRepository tenantRepository;

    /**
     * Loads the user details by email.
     * <p>
     * This method first attempts to find a user in the public/general user repository by email.
     * If the user is a tenant user, it will attempt to find the associated tenant and extract the schema name for routing.
     * The schema is assigned to tenant users, while general/system users fall back to the "public" schema.
     * </p>
     *
     * @param email the email of the user to load
     * @return a {@link UserDetails} object containing the user's details
     * @throws UsernameNotFoundException if no user with the provided email is found
     */
    @Override
    @Transactional
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        // Try finding public/general users by email
        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new UsernameNotFoundException("User not found"));

        // Determine tenant schema if the user is a tenant user
        String schema = null;
        if ("TENANT_USER".equals(user.getRole())) {
            schema = tenantRepository.findById(user.getTenantId())
                    .map(Tenant::getSchemaName)  // Extract tenant's schema name
                    .orElseThrow(() ->
                            new IllegalStateException("Tenant row missing"));
        }

        // Fallback for public/general/system users
        if (!"TENANT_USER".equals(user.getRole())) {
            schema = "public";  // Use public schema for general/system users
        }

        // Return user details with schema, may be null for system admins
        return new CustomUserDetails(
                user.getUserId(),
                user.getEmail(),
                user.getPasswordHash(),
                user.getRole(),
                schema        // Schema may be null for system admins
        );
    }
}