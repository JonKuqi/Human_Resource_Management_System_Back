package com.hrms.Human_Resource_Management_System_Back.repository.tenant;

import com.hrms.Human_Resource_Management_System_Back.model.tenant.PerformanceEvaluation;
import com.hrms.Human_Resource_Management_System_Back.repository.BaseUserSpecificRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PerformanceEvalRepository
        extends BaseUserSpecificRepository<PerformanceEvaluation, Integer> {

    /* We join on **toUserTenant** (the “target” of the evaluation). */
    @Query("""
       SELECT pe
       FROM PerformanceEvaluation pe
       JOIN UserRole       ur ON ur.userTenant.userTenantId = pe.toUserTenant.userTenantId
       JOIN RolePermission rp ON ur.role.roleId            = rp.role.roleId
       WHERE rp.targetRoleId.roleName IN :roles
       GROUP BY pe
       HAVING COUNT(DISTINCT rp.targetRoleId.roleName) = :#{#roles.size()}
    """)
    List<PerformanceEvaluation> findAllRole(@Param("roles") List<String> roles);

    @Query("""
       SELECT pe
       FROM PerformanceEvaluation pe
       JOIN UserRole       ur ON ur.userTenant.userTenantId = pe.toUserTenant.userTenantId
       JOIN RolePermission rp ON ur.role.roleId            = rp.role.roleId
       WHERE pe.performanceEvaluationId = :id
         AND rp.targetRoleId.roleName IN :roles
       GROUP BY pe
       HAVING COUNT(DISTINCT rp.targetRoleId.roleName) = :#{#roles.size()}
    """)
    Optional<PerformanceEvaluation> findByIdRole(@Param("id") Integer id,
                                                 @Param("roles") List<String> roles);

    @Override
    @Modifying
    @Transactional
    @Query("""
       DELETE FROM PerformanceEvaluation pe
       WHERE pe.performanceEvaluationId = :id AND EXISTS (
           SELECT 1
           FROM UserRole       ur
           JOIN RolePermission rp ON ur.role.roleId = rp.role.roleId
           WHERE ur.userTenant.userTenantId = pe.toUserTenant.userTenantId
             AND rp.targetRoleId.roleName IN :roles
           GROUP BY ur.userTenant.userTenantId
           HAVING COUNT(DISTINCT rp.targetRoleId.roleName) = :#{#roles.size()}
       )
    """)
    void deleteByIdRole(@Param("id") Integer id,
                        @Param("roles") List<String> roles);
}