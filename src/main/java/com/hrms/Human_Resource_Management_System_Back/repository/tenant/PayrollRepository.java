package com.hrms.Human_Resource_Management_System_Back.repository.tenant;

import com.hrms.Human_Resource_Management_System_Back.model.tenant.Payroll;
import com.hrms.Human_Resource_Management_System_Back.repository.BaseUserSpecificRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PayrollRepository
        extends BaseUserSpecificRepository<Payroll, Integer> {

    @Query("""
       SELECT p
       FROM Payroll p
       JOIN UserRole       ur ON ur.userTenant.userTenantId = p.userTenant.userTenantId
       JOIN RolePermission rp ON ur.role.roleId            = rp.role.roleId
       WHERE rp.targetRoleId.roleName IN :roles
       GROUP BY p
       HAVING COUNT(DISTINCT rp.targetRoleId.roleName) = :#{#roles.size()}
    """)
    List<Payroll> findAllRole(@Param("roles") List<String> roles);

    @Query("""
       SELECT p
       FROM Payroll p
       JOIN UserRole       ur ON ur.userTenant.userTenantId = p.userTenant.userTenantId
       JOIN RolePermission rp ON ur.role.roleId            = rp.role.roleId
       WHERE p.payrollId = :id
         AND rp.targetRoleId.roleName IN :roles
       GROUP BY p
       HAVING COUNT(DISTINCT rp.targetRoleId.roleName) = :#{#roles.size()}
    """)
    Optional<Payroll> findByIdRole(@Param("id") Integer id,
                                   @Param("roles") List<String> roles);

    @Override
    @Modifying
    @Transactional
    @Query("""
       DELETE FROM Payroll p
       WHERE p.payrollId = :id AND EXISTS (
           SELECT 1
           FROM UserRole       ur
           JOIN RolePermission rp ON ur.role.roleId = rp.role.roleId
           WHERE ur.userTenant.userTenantId = p.userTenant.userTenantId
             AND rp.targetRoleId.roleName IN :roles
           GROUP BY ur.userTenant.userTenantId
           HAVING COUNT(DISTINCT rp.targetRoleId.roleName) = :#{#roles.size()}
       )
    """)
    void deleteByIdRole(@Param("id") Integer id,
                        @Param("roles") List<String> roles);
}