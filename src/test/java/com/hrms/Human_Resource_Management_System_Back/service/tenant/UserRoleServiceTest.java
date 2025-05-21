package com.hrms.Human_Resource_Management_System_Back.service.tenant;

import com.hrms.Human_Resource_Management_System_Back.model.tenant.Role;
import com.hrms.Human_Resource_Management_System_Back.model.tenant.UserRole;
import com.hrms.Human_Resource_Management_System_Back.model.tenant.UserTenant;
import com.hrms.Human_Resource_Management_System_Back.repository.tenant.RoleRepository;
import com.hrms.Human_Resource_Management_System_Back.repository.tenant.UserRoleRepository;
import com.hrms.Human_Resource_Management_System_Back.repository.tenant.UserTenantRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserRoleServiceTest {

    @Mock
    private UserRoleRepository userRoleRepository;

    @Mock
    private UserTenantRepository userTenantRepository;

    @Mock
    private RoleRepository roleRepository;

    @InjectMocks
    private UserRoleService userRoleService;

    @Captor
    private ArgumentCaptor<List<UserRole>> userRolesCaptor;

    private UserTenant userTenant;
    private Role role1;
    private Role role2;
    private UserRole userRole1;
    private UserRole userRole2;

    @BeforeEach
    void setUp() {
        userTenant = new UserTenant();
        userTenant.setUserTenantId(1);
        userTenant.setFirstName("Test");
        userTenant.setLastName("User");

        role1 = new Role();
        role1.setRoleId(1);
        role1.setRoleName("ADMIN");

        role2 = new Role();
        role2.setRoleId(2);
        role2.setRoleName("USER");

        userRole1 = new UserRole();
        userRole1.setUserRoleId(1);
        userRole1.setUserTenant(userTenant);
        userRole1.setRole(role1);

        userRole2 = new UserRole();
        userRole2.setUserRoleId(2);
        userRole2.setUserTenant(userTenant);
        userRole2.setRole(role2);
    }

    @Test
    void getUserRoles_shouldReturnUserRolesByUserId() {
        // Arrange
        List<UserRole> expectedRoles = Arrays.asList(userRole1, userRole2);
        when(userRoleRepository.findByUserId(1)).thenReturn(expectedRoles);

        // Act
        List<UserRole> result = userRoleService.getUserRoles(1);

        // Assert
        assertEquals(2, result.size());
        assertEquals(expectedRoles, result);
        verify(userRoleRepository).findByUserId(1);
    }

    @Test
    void findByUserTenantId_shouldReturnUserRolesByUserTenantId() {
        // Arrange
        List<UserRole> expectedRoles = Arrays.asList(userRole1, userRole2);
        when(userRoleRepository.findAllByUserTenant_UserTenantId(1)).thenReturn(expectedRoles);

        // Act
        List<UserRole> result = userRoleService.findByUserTenantId(1);

        // Assert
        assertEquals(2, result.size());
        assertEquals(expectedRoles, result);
        verify(userRoleRepository).findAllByUserTenant_UserTenantId(1);
    }

    @Test
    void replaceRoles_shouldDeleteExistingRolesAndCreateNewOnes() {
        // Arrange
        List<Integer> roleIds = Arrays.asList(1, 2);

        when(userTenantRepository.getReferenceById(1)).thenReturn(userTenant);
        when(roleRepository.getReferenceById(1)).thenReturn(role1);
        when(roleRepository.getReferenceById(2)).thenReturn(role2);
        doNothing().when(userRoleRepository).deleteByUserTenantId(anyInt());
        when(userRoleRepository.saveAll(anyList())).thenReturn(Arrays.asList(userRole1, userRole2));

        // Act
        userRoleService.replaceRoles(1, roleIds);

        // Assert
        verify(userRoleRepository).deleteByUserTenantId(1);
        verify(userTenantRepository).getReferenceById(1);
        verify(roleRepository, times(2)).getReferenceById(anyInt());
        verify(userRoleRepository).saveAll(userRolesCaptor.capture());

        List<UserRole> capturedRoles = userRolesCaptor.getValue();
        assertEquals(2, capturedRoles.size());

        // Check that roles were correctly assigned
        assertEquals(userTenant, capturedRoles.get(0).getUserTenant());
        assertEquals(role1, capturedRoles.get(0).getRole());
        assertEquals(userTenant, capturedRoles.get(1).getUserTenant());
        assertEquals(role2, capturedRoles.get(1).getRole());
    }

    @Test
    void replaceRoles_shouldHandleEmptyRolesList() {
        // Arrange
        List<Integer> roleIds = List.of();

        when(userTenantRepository.getReferenceById(1)).thenReturn(userTenant);
        doNothing().when(userRoleRepository).deleteByUserTenantId(anyInt());
        when(userRoleRepository.saveAll(anyList())).thenReturn(List.of());

        // Act
        userRoleService.replaceRoles(1, roleIds);

        // Assert
        verify(userRoleRepository).deleteByUserTenantId(1);
        verify(userTenantRepository).getReferenceById(1);
        verify(roleRepository, never()).getReferenceById(anyInt());
        verify(userRoleRepository).saveAll(userRolesCaptor.capture());

        List<UserRole> capturedRoles = userRolesCaptor.getValue();
        assertEquals(0, capturedRoles.size());
    }
}
