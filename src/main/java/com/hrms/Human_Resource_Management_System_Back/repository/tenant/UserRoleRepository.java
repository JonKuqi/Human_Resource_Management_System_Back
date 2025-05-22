package com.hrms.Human_Resource_Management_System_Back.repository.tenant;

import com.hrms.Human_Resource_Management_System_Back.model.tenant.UserRole;
import com.hrms.Human_Resource_Management_System_Back.repository.BaseRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository interface for managing {@link UserRole} entities.
 * <p>
 * This repository extends {@link BaseRepository} and provides methods for accessing and modifying user role data,
 * including retrieving roles by user ID, by user tenant ID, and deleting roles by user tenant ID.
 * </p>
 */
@Repository
public interface UserRoleRepository extends BaseRepository<UserRole, Integer> {

    /**
     * Retrieves a list of {@link UserRole} entities associated with a specific user ID.
     * <p>
     * This method uses a custom query to fetch the user roles for the user with the given user ID,
     * along with the associated role information.
     * </p>
     *
     * @param userId the ID of the user whose roles are to be retrieved
     * @return a list of {@link UserRole} associated with the specified user ID
     */
    @Query("""
  SELECT ur FROM UserRole ur
  JOIN FETCH ur.role
  WHERE ur.userTenant.user.userId = :userId
""")
    List<UserRole> findByUserId(@Param("userId") Integer userId);

    /**
     * Retrieves a list of {@link UserRole} entities associated with a specific user tenant ID.
     *
     * @param userTenantId the ID of the user tenant whose roles are to be retrieved
     * @return a list of {@link UserRole} associated with the specified user tenant ID
     */
    List<UserRole> findAllByUserTenant_UserTenantId(Integer userTenantId);

    /**
     * Deletes {@link UserRole} entities associated with a specific user tenant ID.
     * <p>
     * This method deletes all user roles linked to the user tenant with the given user tenant ID.
     * </p>
     *
     * @param userTenantId the ID of the user tenant whose roles are to be deleted
     */
    @Modifying
    @Query("DELETE FROM UserRole ur WHERE ur.userTenant.userTenantId = :userTenantId")
    void deleteByUserTenantId(@Param("userTenantId") Integer userTenantId);
}
