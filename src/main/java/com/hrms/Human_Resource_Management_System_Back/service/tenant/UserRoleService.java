package com.hrms.Human_Resource_Management_System_Back.service.tenant;

import com.hrms.Human_Resource_Management_System_Back.model.tenant.UserRole;
import com.hrms.Human_Resource_Management_System_Back.model.tenant.UserTenant;
import com.hrms.Human_Resource_Management_System_Back.repository.tenant.RoleRepository;
import com.hrms.Human_Resource_Management_System_Back.repository.tenant.UserRoleRepository;
import com.hrms.Human_Resource_Management_System_Back.repository.tenant.UserTenantRepository;
import com.hrms.Human_Resource_Management_System_Back.service.BaseService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service class for handling business logic related to user roles.
 * <p>
 * This class extends {@link BaseService} and provides methods for managing user role entities,
 * including assigning roles to users, retrieving roles based on user or tenant, and replacing user roles.
 * </p>
 */
@Service
@AllArgsConstructor
public class UserRoleService extends BaseService<UserRole, Integer> {

    /**
     * The repository for performing CRUD operations on user role entities.
     */
    private final UserRoleRepository repo;

    /**
     * The repository for managing user-tenant associations.
     */
    private final UserTenantRepository tenantRepo;

    /**
     * The repository for managing role entities.
     */
    private final RoleRepository roleRepo;

    /**
     * Returns the user role repository.
     * <p>
     * This method overrides the {@link BaseService#getRepository()} method to return the specific repository
     * for user role entities.
     * </p>
     *
     * @return the user role repository
     */
    @Override
    protected UserRoleRepository getRepository() {
        return repo;
    }

    /**
     * Retrieves a list of user roles associated with a specific user ID.
     * <p>
     * This method fetches all roles that are assigned to the user with the specified ID.
     * </p>
     *
     * @param userId the ID of the user whose roles are to be fetched
     * @return a list of {@link UserRole} associated with the user ID
     */
    public List<UserRole> getUserRoles(Integer userId) {
        return repo.findByUserId(userId);
    }

    /**
     * Retrieves a list of user roles associated with a specific user tenant ID.
     * <p>
     * This method fetches all roles associated with the user tenant, allowing the roles to be filtered
     * based on the user's tenant ID.
     * </p>
     *
     * @param userTenantId the ID of the user tenant whose roles are to be fetched
     * @return a list of {@link UserRole} associated with the user tenant ID
     */
    @Cacheable(value = "user-roles", key = "#userTenantId")
    public List<UserRole> findByUserTenantId(Integer userTenantId) {
        return repo.findAllByUserTenant_UserTenantId(userTenantId);
    }

    /**
     * Replaces the roles associated with a specific user tenant ID.
     * <p>
     * This method first deletes the existing roles associated with the user tenant, and then assigns the new roles
     * specified by the provided list of role IDs.
     * </p>
     *
     * @param userTenantId the ID of the user tenant whose roles are to be replaced
     * @param roleIds a list of role IDs to assign to the user tenant
     */
    @Transactional
    @CacheEvict(value = "user-roles", key = "#userTenantId")
    public void replaceRoles(Integer userTenantId, List<Integer> roleIds) {
        // Delete existing roles for the user tenant
        repo.deleteByUserTenantId(userTenantId);

        // Get the user tenant from the repository
        UserTenant ut = tenantRepo.getReferenceById(userTenantId);

        // Create new user-role associations
        List<UserRole> fresh = roleIds.stream()
                .map(roleRepo::getReferenceById)  // Get the role by ID
                .map(role -> UserRole.builder()    // Map each role to a UserRole object
                        .userTenant(ut)
                        .role(role)
                        .build())
                .toList();

        // Save the new roles for the user tenant
        repo.saveAll(fresh);
    }
}