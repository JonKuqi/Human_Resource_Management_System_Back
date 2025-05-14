package com.hrms.Human_Resource_Management_System_Back.security;

import com.hrms.Human_Resource_Management_System_Back.model.UserGeneral;
import com.hrms.Human_Resource_Management_System_Back.model.tenant.UserTenant;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

/**
 * Unified {@link UserDetails} implementation to handle UserGeneral (public users)
 * and UserTenant (tenant schema users).
 * <p>
 * This class implements {@link UserDetails} to provide a unified way of representing
 * both public and tenant users for authentication purposes. It contains user-related
 * information, including their role, username, password, and the associated tenant schema.
 * </p>
 */
@Getter
public class CustomUserDetails implements UserDetails {

    /**
     * The unique identifier for the user.
     */
    private final Integer userId;

    /**
     * The username for the user (typically email).
     */
    private final String username;

    /**
     * The hashed password for the user.
     */
    private final String password;

    /**
     * The role assigned to the user (e.g., "USER", "ADMIN").
     */
    private final String role;

    /**
     * The tenant schema for tenant users, or "public" for general users.
     */
    private final String tenant;

    /**
     * Constructor for creating a {@link CustomUserDetails} for a general (public) user.
     * <p>
     * This constructor is used to create {@link CustomUserDetails} for users in the public schema
     * (e.g., general users that are not associated with a specific tenant).
     * </p>
     *
     * @param userId       the user ID
     * @param userGeneral  the user general entity
     */
    public CustomUserDetails(Integer userId, UserGeneral userGeneral) {
        this.userId = userId;
        this.username = userGeneral.getUser().getEmail();
        this.password = userGeneral.getUser().getPasswordHash();
        this.role = userGeneral.getUser().getRole();
        this.tenant = "public";  // Public schema for general users
    }

    /**
     * Constructor for creating a {@link CustomUserDetails} for a user in a tenant.
     * <p>
     * This constructor is used for creating {@link CustomUserDetails} for users associated with a tenant schema.
     * </p>
     *
     * @param userId         the user ID
     * @param email          the email of the user
     * @param passwordHash   the hashed password of the user
     * @param role           the role of the user
     * @param tenantSchema   the schema for the tenant to which the user belongs
     */
    public CustomUserDetails(Integer userId, String email, String passwordHash, String role, String tenantSchema) {
        this.userId = userId;
        this.username = email;
        this.password = passwordHash;
        this.role = role;
        this.tenant = tenantSchema;
    }

    /**
     * Constructor for creating a {@link CustomUserDetails} for a tenant user.
     * <p>
     * This constructor is used to create {@link CustomUserDetails} for users associated with a specific tenant.
     * </p>
     *
     * @param userId       the user ID
     * @param userTenant   the user tenant entity
     * @param tenantSchema the tenant schema for the user
     */
    public CustomUserDetails(Integer userId, UserTenant userTenant, String tenantSchema) {
        this.userId = userId;
        this.username = userTenant.getUser().getEmail();
        this.password = userTenant.getUser().getPasswordHash();
        this.role = userTenant.getUser().getRole();
        this.tenant = tenantSchema;
    }

    /**
     * Returns the authorities granted to the user.
     * <p>
     * This method returns a collection of authorities (roles) granted to the user. In this case, it returns a single
     * {@link SimpleGrantedAuthority} representing the user's role.
     * </p>
     *
     * @return a collection of granted authorities
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority(role));
    }

    /**
     * Indicates whether the account has expired.
     * <p>
     * This method always returns {@code true}, meaning the account is not expired.
     * </p>
     *
     * @return {@code true}, indicating the account is not expired
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * Indicates whether the account is locked.
     * <p>
     * This method always returns {@code true}, meaning the account is not locked.
     * </p>
     *
     * @return {@code true}, indicating the account is not locked
     */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /**
     * Indicates whether the credentials (password) have expired.
     * <p>
     * This method always returns {@code true}, meaning the credentials are not expired.
     * </p>
     *
     * @return {@code true}, indicating the credentials are not expired
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * Indicates whether the account is enabled.
     * <p>
     * This method always returns {@code true}, meaning the account is enabled.
     * </p>
     *
     * @return {@code true}, indicating the account is enabled
     */
    @Override
    public boolean isEnabled() {
        return true;
    }
}
