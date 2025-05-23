package com.hrms.Human_Resource_Management_System_Back.repository.tenant;

import com.hrms.Human_Resource_Management_System_Back.model.tenant.LeaveRequest;
import com.hrms.Human_Resource_Management_System_Back.repository.BaseRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Repository interface for managing {@link LeaveRequest} entities.
 * <p>
 * This repository extends {@link BaseRepository} and provides methods to handle
 * leave request data, including role-based filtering and conflict detection.
 * </p>
 */
@Repository
public interface LeaveRequestRepository extends BaseRepository<LeaveRequest, Integer> {

    /**
     * Retrieves all leave requests for users who possess all specified roles.
     *
     * @param roles the list of role names required for access
     * @return a list of {@link LeaveRequest} entities permitted by role
     */
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

    /**
     * Retrieves a specific leave request by ID, accessible only if the user has all required roles.
     *
     * @param id    the leave request ID
     * @param roles the list of role names required for access
     * @return an {@link Optional} containing the {@link LeaveRequest} if authorized
     */
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

    /**
     * Deletes a leave request by ID, only if the associated user has all required roles.
     *
     * @param id    the leave request ID to delete
     * @param roles the list of required role names
     */
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

    /**
     * Checks whether there is an overlapping leave request for the same user and given date range.
     *
     * @param userId       the ID of the user
     * @param newStartDate the new leave start date
     * @param newEndDate   the new leave end date
     * @return true if an overlap exists; false otherwise
     */
    @Query("""
    SELECT CASE WHEN COUNT(lr) > 0 THEN true ELSE false END
    FROM LeaveRequest lr
    WHERE lr.userTenant.userTenantId = :userId
      AND lr.startDate <= :newEndDate
      AND lr.endDate >= :newStartDate
    """)
    boolean existsByUserTenantUserTenantIdAndDateOverlap(@Param("userId") Integer userId,
                                                         @Param("newStartDate") LocalDate newStartDate,
                                                         @Param("newEndDate") LocalDate newEndDate);
}
