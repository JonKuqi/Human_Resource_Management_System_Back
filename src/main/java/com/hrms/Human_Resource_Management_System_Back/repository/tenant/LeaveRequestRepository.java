package com.hrms.Human_Resource_Management_System_Back.repository.tenant;

import com.hrms.Human_Resource_Management_System_Back.model.tenant.LeaveRequest;
import com.hrms.Human_Resource_Management_System_Back.repository.BaseUserSpecificRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LeaveRequestRepository
        extends BaseUserSpecificRepository<LeaveRequest, Integer> {

    @Query("""
       SELECT lr
       FROM LeaveRequest lr
       JOIN UserRole       ur ON ur.userTenant.userTenantId = lr.userTenant.userTenantId
       JOIN RolePermission rp ON ur.role.roleId            = rp.role.roleId
       WHERE rp.targetRoleId.roleName IN :roles
       GROUP BY lr
       HAVING COUNT(DISTINCT rp.targetRoleId.roleName) = :#{#roles.size()}
    """)
    List<LeaveRequest> findAllRole(@Param("roles") List<String> roles);

    @Query("""
       SELECT lr
       FROM LeaveRequest lr
       JOIN UserRole       ur ON ur.userTenant.userTenantId = lr.userTenant.userTenantId
       JOIN RolePermission rp ON ur.role.roleId            = rp.role.roleId
       WHERE lr.leaveRequestId = :id
         AND rp.targetRoleId.roleName IN :roles
       GROUP BY lr
       HAVING COUNT(DISTINCT rp.targetRoleId.roleName) = :#{#roles.size()}
    """)
    Optional<LeaveRequest> findByIdRole(@Param("id") Integer id,
                                        @Param("roles") List<String> roles);

    @Override
    @Modifying
    @Transactional
    @Query("""
       DELETE FROM LeaveRequest lr
       WHERE lr.leaveRequestId = :id AND EXISTS (
           SELECT 1
           FROM UserRole       ur
           JOIN RolePermission rp ON ur.role.roleId = rp.role.roleId
           WHERE ur.userTenant.userTenantId = lr.userTenant.userTenantId
             AND rp.targetRoleId.roleName IN :roles
           GROUP BY ur.userTenant.userTenantId
           HAVING COUNT(DISTINCT rp.targetRoleId.roleName) = :#{#roles.size()}
       )
    """)
    void deleteByIdRole(@Param("id") Integer id,
                        @Param("roles") List<String> roles);
}