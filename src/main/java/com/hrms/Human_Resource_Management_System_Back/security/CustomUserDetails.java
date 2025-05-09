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
 * Unified UserDetails implementation to handle UserGeneral (public users)
 * and UserTenant (tenant schema users).
 */
@Getter
public class CustomUserDetails implements UserDetails {

    private final Integer userId;
    private final String username;
    private final String password;
    private final String role;
    private final String tenant;

    public CustomUserDetails(Integer userId, UserGeneral userGeneral) {
        this.userId = userId;
        this.username = userGeneral.getUser().getEmail();
        this.password = userGeneral.getUser().getPasswordHash();
        this.role = userGeneral.getUser().getRole();
        this.tenant = "public";
    }

    public CustomUserDetails(Integer userId,
                             String email,
                             String passwordHash,
                             String role,
                             String tenantSchema) {
        this.userId = userId;
        this.username = email;       // or userId.toString() if you prefer
        this.password = passwordHash;
        this.role     = role;
        this.tenant   = tenantSchema;
    }

    public CustomUserDetails(Integer userId, UserTenant userTenant, String tenantSchema) {
        this.userId = userId;
        this.username = userTenant.getUser().getEmail();
        this.password = userTenant.getUser().getPasswordHash();
        this.role = userTenant.getUser().getRole();
        this.tenant = tenantSchema;
    }
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority(role));
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }


}
