package com.hrms.Human_Resource_Management_System_Back.service.tenant;

import com.hrms.Human_Resource_Management_System_Back.model.TenantPermission;
import com.hrms.Human_Resource_Management_System_Back.model.dto.RolePermissionReplaceRequest;
import com.hrms.Human_Resource_Management_System_Back.model.dto.UserRolePermissionDto;
import com.hrms.Human_Resource_Management_System_Back.model.tenant.Role;
import com.hrms.Human_Resource_Management_System_Back.model.tenant.RolePermission;
import com.hrms.Human_Resource_Management_System_Back.repository.TenantPermissionRepository;
import com.hrms.Human_Resource_Management_System_Back.repository.tenant.RolePermissionRepository;
import com.hrms.Human_Resource_Management_System_Back.repository.tenant.RoleRepository;
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
public class RolePermissionServiceTest {

    @Mock
    private RolePermissionRepository rolePermissionRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private TenantPermissionRepository tenantPermissionRepository;

    @InjectMocks
    private RolePermissionService rolePermissionService;

    @Captor
    private ArgumentCaptor<List<RolePermission>> rolePermissionsCaptor;

    private Role role;
    private Role targetRole;
    private TenantPermission permission1;
    private TenantPermission permission2;
    private RolePermission rolePermission1;
    private RolePermission rolePermission2;
    private UserRolePermissionDto permissionDto1;
    private UserRolePermissionDto permissionDto2;
    private UserRolePermissionDto permissionDto3;

    @BeforeEach
    void setUp() {
        role = new Role();
        role.setRoleId(1);
        role.setRoleName("ADMIN");

        targetRole = new Role();
        targetRole.setRoleId(2);
        targetRole.setRoleName("USER");

        permission1 = new TenantPermission();
        permission1.setTenantPermissionId(1);
        permission1.setResource("user");
        permission1.setVerb("read");

        permission2 = new TenantPermission();
        permission2.setTenantPermissionId(2);
        permission2.setResource("user");
        permission2.setVerb("write");

        rolePermission1 = new RolePermission();
        rolePermission1.setRolePermissionId(1);
        rolePermission1.setRole(role);
        rolePermission1.setTenantPermission(permission1);
        rolePermission1.setTargetRoleId(null);

        rolePermission2 = new RolePermission();
        rolePermission2.setRolePermissionId(2);
        rolePermission2.setRole(role);
        rolePermission2.setTenantPermission(permission2);
        rolePermission2.setTargetRoleId(targetRole);

        // Set up permission DTOs with same verb+resource but different targets
        permissionDto1 = new UserRolePermissionDto();
        permissionDto1.setVerb("read");
        permissionDto1.setResource("user");
        permissionDto1.setTarget_role(null); // global scope

        permissionDto2 = new UserRolePermissionDto();
        permissionDto2.setVerb("read");
        permissionDto2.setResource("user");
        permissionDto2.setTarget_role("USER"); // scoped to USER role

        permissionDto3 = new UserRolePermissionDto();
        permissionDto3.setVerb("write");
        permissionDto3.setResource("user");
        permissionDto3.setTarget_role("USER");
    }

    @Test
    void getUserRolePermissions_shouldFilterDuplicatePermissionsPreferringGlobalScope() {
        // Arrange
        List<UserRolePermissionDto> permissions = Arrays.asList(permissionDto2, permissionDto1, permissionDto3);
        when(rolePermissionRepository.findScopedPermissionsByUserTenantId(1)).thenReturn(permissions);

        // Act
        List<UserRolePermissionDto> result = rolePermissionService.getUserRolePermissions(1);

        // Assert
        assertEquals(2, result.size());

        // Check that the result contains the global scope permission rather than the targeted one
        boolean hasGlobalReadUser = false;
        boolean hasTargetedWriteUser = false;

        for (UserRolePermissionDto dto : result) {
            if (dto.getVerb().equals("read") && dto.getResource().equals("user") && dto.getTarget_role() == null) {
                hasGlobalReadUser = true;
            }
            if (dto.getVerb().equals("write") && dto.getResource().equals("user") && "USER".equals(dto.getTarget_role())) {
                hasTargetedWriteUser = true;
            }
        }

        assertTrue(hasGlobalReadUser, "Should contain global read:user permission");
        assertTrue(hasTargetedWriteUser, "Should contain targeted write:user permission");

        verify(rolePermissionRepository).findScopedPermissionsByUserTenantId(1);
    }

    @Test
    void findByRoleId_shouldReturnRolePermissionsByRoleId() {
        // Arrange
        List<RolePermission> expectedPermissions = Arrays.asList(rolePermission1, rolePermission2);
        when(rolePermissionRepository.findAllByRole_RoleId(1)).thenReturn(expectedPermissions);

        // Act
        List<RolePermission> result = rolePermissionService.findByRoleId(1);

        // Assert
        assertEquals(2, result.size());
        assertEquals(expectedPermissions, result);
        verify(rolePermissionRepository).findAllByRole_RoleId(1);
    }

    @Test
    void replacePermissions_shouldDeleteExistingPermissionsAndCreateNewOnes() {
        // Arrange
        RolePermissionReplaceRequest request = new RolePermissionReplaceRequest();
        request.setPermissionIds(Arrays.asList(1, 2));
        request.setTargetRoleIds(Arrays.asList(null, 2));

        when(roleRepository.getReferenceById(1)).thenReturn(role);
        when(roleRepository.getReferenceById(2)).thenReturn(targetRole);
        when(tenantPermissionRepository.getReferenceById(1)).thenReturn(permission1);
        when(tenantPermissionRepository.getReferenceById(2)).thenReturn(permission2);
        doNothing().when(rolePermissionRepository).deleteByRoleId(anyInt());
        when(rolePermissionRepository.saveAll(anyList())).thenReturn(Arrays.asList(rolePermission1, rolePermission2));

        // Act
        rolePermissionService.replacePermissions(1, request);

        // Assert
        verify(rolePermissionRepository).deleteByRoleId(1);
        verify(roleRepository).getReferenceById(1);
        verify(roleRepository).getReferenceById(2);
        verify(tenantPermissionRepository, times(2)).getReferenceById(anyInt());
        verify(rolePermissionRepository).saveAll(rolePermissionsCaptor.capture());

        List<RolePermission> capturedPermissions = rolePermissionsCaptor.getValue();
        assertEquals(2, capturedPermissions.size());

        // Check that permissions were correctly assigned
        assertEquals(role, capturedPermissions.get(0).getRole());
        assertEquals(permission1, capturedPermissions.get(0).getTenantPermission());
        assertNull(capturedPermissions.get(0).getTargetRoleId());

        assertEquals(role, capturedPermissions.get(1).getRole());
        assertEquals(permission2, capturedPermissions.get(1).getTenantPermission());
        assertEquals(targetRole, capturedPermissions.get(1).getTargetRoleId());
    }

    @Test
    void replacePermissions_shouldHandleNullTargetRoleIds() {
        // Arrange
        RolePermissionReplaceRequest request = new RolePermissionReplaceRequest();
        request.setPermissionIds(Arrays.asList(1, 2));
        request.setTargetRoleIds(null); // null target role ids

        when(roleRepository.getReferenceById(1)).thenReturn(role);
        when(tenantPermissionRepository.getReferenceById(1)).thenReturn(permission1);
        when(tenantPermissionRepository.getReferenceById(2)).thenReturn(permission2);
        doNothing().when(rolePermissionRepository).deleteByRoleId(anyInt());
        when(rolePermissionRepository.saveAll(anyList())).thenReturn(Arrays.asList(rolePermission1, rolePermission2));

        // Act
        rolePermissionService.replacePermissions(1, request);

        // Assert
        verify(rolePermissionRepository).deleteByRoleId(1);
        verify(roleRepository).getReferenceById(1);
        verify(roleRepository, never()).getReferenceById(2);
        verify(tenantPermissionRepository, times(2)).getReferenceById(anyInt());
        verify(rolePermissionRepository).saveAll(rolePermissionsCaptor.capture());

        List<RolePermission> capturedPermissions = rolePermissionsCaptor.getValue();
        assertEquals(2, capturedPermissions.size());

        // Check that permissions were correctly assigned with null target roles
        assertEquals(role, capturedPermissions.get(0).getRole());
        assertEquals(permission1, capturedPermissions.get(0).getTenantPermission());
        assertNull(capturedPermissions.get(0).getTargetRoleId());

        assertEquals(role, capturedPermissions.get(1).getRole());
        assertEquals(permission2, capturedPermissions.get(1).getTenantPermission());
        assertNull(capturedPermissions.get(1).getTargetRoleId());
    }
}
