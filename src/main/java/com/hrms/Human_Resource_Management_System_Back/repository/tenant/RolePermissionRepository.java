package com.hrms.Human_Resource_Management_System_Back.repository.tenant;

import com.hrms.Human_Resource_Management_System_Back.model.dto.UserRolePermissionDto;
import com.hrms.Human_Resource_Management_System_Back.model.tenant.RolePermission;
import com.hrms.Human_Resource_Management_System_Back.repository.BaseRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository interface for managing {@link RolePermission} entities.
 * <p>
 * This repository extends {@link BaseRepository} and provides methods for accessing and modifying role permission data.
 * It includes custom queries for retrieving scoped permissions and deleting permissions associated with specific roles.
 * </p>
 */
@Repository
public interface RolePermissionRepository extends BaseRepository<RolePermission, Integer> {

    /**
     * Retrieves the scoped permissions for a user tenant based on the user tenant ID.
     * <p>
     * This custom query fetches the permissions associated with the roles granted to a user tenant. It includes the
     * permission name, verb, resource, and the associated target role (if any).
     * </p>
     *
     * @param userTenantId the ID of the user tenant to fetch permissions for
     * @return a list of {@link UserRolePermissionDto} containing the scoped permissions for the user tenant
     */
    @Query("""
    SELECT DISTINCT new com.hrms.Human_Resource_Management_System_Back.model.dto.UserRolePermissionDto(
        p.name, p.verb, p.resource,
        CASE WHEN rp.targetRoleId IS NULL THEN null ELSE rp.targetRoleId.roleName END
    )
    FROM UserTenant ut
    JOIN UserRole ur ON ut.userTenantId = ur.userTenant.userTenantId
    JOIN RolePermission rp ON ur.role.roleId = rp.role.roleId
    JOIN TenantPermission p ON rp.tenantPermission.tenantPermissionId = p.tenantPermissionId
    LEFT JOIN Role r ON r.roleId = rp.targetRoleId.roleId
    WHERE ut.user.userId = :userTenantId
""")
    List<UserRolePermissionDto> findScopedPermissionsByUserTenantId(@Param("userTenantId") Integer userTenantId);

    /**
     * Retrieves a list of {@link RolePermission} entities associated with a specific role ID.
     *
     * @param roleId the ID of the role to retrieve permissions for
     * @return a list of {@link RolePermission} associated with the specified role ID
     */
    List<RolePermission> findAllByRole_RoleId(Integer roleId);

    /**
     * Deletes all {@link RolePermission} entities associated with a specific role ID.
     * <p>
     * This method removes all role permissions for the given role ID, ensuring that the specified role no longer has any
     * associated permissions.
     * </p>
     *
     * @param roleId the ID of the role whose permissions are to be deleted
     */
    @Modifying
    @Query("DELETE FROM RolePermission rp WHERE rp.role.roleId = :roleId")
    void deleteByRoleId(@Param("roleId") Integer roleId);

    boolean existsByRole_RoleIdAndTenantPermission_TenantPermissionId(Integer roleId, Integer tenantPermissionId);
}