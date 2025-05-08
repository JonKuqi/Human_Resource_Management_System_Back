package com.hrms.Human_Resource_Management_System_Back.repository.tenant;

import com.hrms.Human_Resource_Management_System_Back.model.dto.UserRolePermissionDto;
import com.hrms.Human_Resource_Management_System_Back.model.tenant.RolePermission;
import com.hrms.Human_Resource_Management_System_Back.repository.BaseRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RolePermissionRepository extends BaseRepository<RolePermission, Integer> {

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
}
