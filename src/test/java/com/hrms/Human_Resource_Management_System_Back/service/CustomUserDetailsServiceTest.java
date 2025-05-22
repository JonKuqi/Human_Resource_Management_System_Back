package com.hrms.Human_Resource_Management_System_Back.service;

import com.hrms.Human_Resource_Management_System_Back.model.Tenant;
import com.hrms.Human_Resource_Management_System_Back.model.User;
import com.hrms.Human_Resource_Management_System_Back.repository.TenantRepository;
import com.hrms.Human_Resource_Management_System_Back.repository.UserRepository;
import com.hrms.Human_Resource_Management_System_Back.security.CustomUserDetails;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@Transactional // Ensures database changes are rolled back after each test
class CustomUserDetailsServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private TenantRepository tenantRepository;

    @InjectMocks
    private CustomUserDetailsService customUserDetailsService;

    private User user;
    private Tenant tenant;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        user = new User();
        user.setUserId(1);
        user.setEmail("test@example.com");
        user.setPasswordHash("hashedPassword");
        user.setRole("TENANT_USER");
        user.setTenantId(1);

        tenant = new Tenant();
        tenant.setTenantId(1);
        tenant.setSchemaName("test_schema");
    }

    @Test
    void loadUserByUsername_shouldReturnUserDetailsForTenantUser() {
        // Arrange
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(user));
        when(tenantRepository.findById(1)).thenReturn(Optional.of(tenant));

        // Act
        CustomUserDetails userDetails = (CustomUserDetails) customUserDetailsService.loadUserByUsername("test@example.com");

        // Assert
        assertNotNull(userDetails);
        assertEquals("test@example.com", userDetails.getUsername());
        assertEquals("hashedPassword", userDetails.getPassword());
        assertEquals("TENANT_USER", userDetails.getRole());

    }

    @Test
    void loadUserByUsername_shouldThrowExceptionWhenUserNotFound() {
        // Arrange
        when(userRepository.findByEmail("nonexistent@example.com")).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(UsernameNotFoundException.class, () -> customUserDetailsService.loadUserByUsername("nonexistent@example.com"));
    }

    @Test
    void loadUserByUsername_shouldThrowExceptionWhenTenantNotFound() {
        // Arrange
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(user));
        when(tenantRepository.findById(1)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(IllegalStateException.class, () -> customUserDetailsService.loadUserByUsername("test@example.com"));
    }

    @Test
    void loadUserByUsername_shouldReturnUserDetailsForPublicUser() {
        // Arrange
        user.setRole("PUBLIC_USER");
        when(userRepository.findByEmail("public@example.com")).thenReturn(Optional.of(user));

        // Act
        CustomUserDetails userDetails = (CustomUserDetails) customUserDetailsService.loadUserByUsername("public@example.com");

        // Assert
        assertNotNull(userDetails);
        assertEquals("test@example.com", userDetails.getUsername());
        assertEquals("hashedPassword", userDetails.getPassword());
        assertEquals("PUBLIC_USER", userDetails.getRole());

    }
}
