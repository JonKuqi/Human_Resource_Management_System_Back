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

/**
 * Repository interface for managing {@link Payroll} entities.
 * <p>
 * This repository extends {@link BaseUserSpecificRepository} and provides role-based access
 * control (RBAC) methods for querying and modifying payroll data.
 * </p>
 */
@Repository
public interface PayrollRepository extends BaseUserSpecificRepository<Payroll, Integer> {

    /**
     * Retrieves all payroll records for users who have **every** role specified in the provided list.
     *
     * @param roles the list of required role names
     * @return a list of {@link Payroll} entities matching the specified access control
     */
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

    /**
     * Retrieves a payroll record by its ID, only if the associated user has **every** role specified.
     *
     * @param id    the payroll ID
     * @param roles the list of required roles to authorize access
     * @return an {@link Optional} containing the {@link Payroll} if found and permitted
     */
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

    /**
     * Deletes a payroll record by its ID, only if the associated user has **every** role specified.
     *
     * @param id    the ID of the payroll to delete
     * @param roles the list of required roles for deletion authorization
     */
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
