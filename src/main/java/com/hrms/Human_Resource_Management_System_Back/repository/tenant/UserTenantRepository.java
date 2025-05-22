package com.hrms.Human_Resource_Management_System_Back.repository.tenant;

import com.hrms.Human_Resource_Management_System_Back.model.tenant.UserTenant;
import com.hrms.Human_Resource_Management_System_Back.repository.BaseUserSpecificRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for managing {@link UserTenant} entities.
 * <p>
 * This repository extends {@link BaseUserSpecificRepository} and provides methods for querying and modifying user-tenant data,
 * including role-based access control (RBAC).
 * </p>
 */
@Repository
public interface UserTenantRepository extends BaseUserSpecificRepository<UserTenant, Integer> {

    /**
     * Retrieves a {@link UserTenant} entity by its user's email.
     *
     * @param email the email of the user
     * @return an {@link Optional} containing the {@link UserTenant} if found
     */
    Optional<UserTenant> findByUser_Email(String email);

    /**
     * Retrieves all {@link UserTenant} entities that have all of the specified roles.
     *
     * @param roles the list of roles to check
     * @return a list of {@link UserTenant} entities that have all the specified roles
     */
    @Query("""
       SELECT ut
       FROM UserTenant ut
       JOIN UserRole        ur ON ur.userTenant.userTenantId = ut.userTenantId
       JOIN RolePermission  rp ON ur.role.roleId            = rp.role.roleId
       WHERE rp.targetRoleId.roleName IN :roles
       GROUP BY ut
       HAVING COUNT(DISTINCT rp.targetRoleId.roleName) = :#{#roles.size()}
    """)
    List<UserTenant> findAllRole(@Param("roles") List<String> roles);

    /**
     * Retrieves a {@link UserTenant} entity by its ID if the user has all the specified roles.
     *
     * @param id    the ID of the user tenant
     * @param roles the list of roles to check
     * @return an {@link Optional} containing the {@link UserTenant} if found
     */
    @Query("""
       SELECT ut
       FROM UserTenant ut
       JOIN UserRole        ur ON ur.userTenant.userTenantId = ut.userTenantId
       JOIN RolePermission  rp ON ur.role.roleId            = rp.role.roleId
       WHERE ut.userTenantId = :id
         AND rp.targetRoleId.roleName IN :roles
       GROUP BY ut
       HAVING COUNT(DISTINCT rp.targetRoleId.roleName) = :#{#roles.size()}
    """)
    Optional<UserTenant> findByIdRole(@Param("id") Integer id,
                                      @Param("roles") List<String> roles);

    /**
     * Deletes a {@link UserTenant} entity by its ID if the user has all the specified roles.
     *
     * @param id    the ID of the user tenant
     * @param roles the list of roles to check
     */
    @Override
    @Modifying
    @Transactional
    @Query("""
       DELETE FROM UserTenant ut
       WHERE ut.userTenantId = :id AND EXISTS (
           SELECT 1
           FROM UserRole       ur
           JOIN RolePermission rp ON ur.role.roleId = rp.role.roleId
           WHERE ur.userTenant.userTenantId = ut.userTenantId
             AND rp.targetRoleId.roleName IN :roles
           GROUP BY ur.userTenant.userTenantId
           HAVING COUNT(DISTINCT rp.targetRoleId.roleName) = :#{#roles.size()}
       )
    """)
    void deleteByIdRole(@Param("id") Integer id,
                        @Param("roles") List<String> roles);
}

