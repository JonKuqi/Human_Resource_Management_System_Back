package com.hrms.Human_Resource_Management_System_Back.service.tenant;

import com.hrms.Human_Resource_Management_System_Back.model.TenantPermission;
import com.hrms.Human_Resource_Management_System_Back.model.tenant.Role;
import com.hrms.Human_Resource_Management_System_Back.model.tenant.RolePermission;
import com.hrms.Human_Resource_Management_System_Back.repository.TenantPermissionRepository;
import com.hrms.Human_Resource_Management_System_Back.repository.tenant.RolePermissionRepository;
import com.hrms.Human_Resource_Management_System_Back.repository.tenant.RoleRepository;
import com.hrms.Human_Resource_Management_System_Back.service.BaseService;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Service class for handling business logic related to roles.
 * <p>
 * This class extends {@link BaseService} and provides methods for managing role entities,
 * including interacting with the underlying repository.
 * </p>
 */
@Service
@AllArgsConstructor
public class RoleService extends BaseService<Role, Integer> {

    /**
     * The repository for performing CRUD operations on role entities.
     */
    private final RoleRepository repo;
    private final RolePermissionRepository rolePermissionRepo;
    private final TenantPermissionRepository tenantPermissionRepo;

    private static final List<String> STARTER_PERMISSION_NAMES = List.of(
            "AUTHENTICATE_USER",
            "CHANGE_PASSWORD",
            "VIEW_USER",           // see own profile
            "UPDATE_USER",         // edit own profile
            "LIST_NOTIFICATIONS",  // bell icon
            "VIEW_NOTIFICATION"
    );

    /**
     * Returns the role repository.
     * <p>
     * This method overrides the {@link BaseService#getRepository()} method to return the specific repository
     * for role entities.
     * </p>
     *
     * @return the role repository
     */
    @Override
    protected RoleRepository getRepository() {
        return repo;
    }

    @Override
    @Cacheable("roles")
    public List<Role> findAll() {
        return super.findAll();
    }

    /**
     * Saves a role and, if it’s brand-new, attaches the starter permissions.
     */
    @Override
    @Transactional
    @CacheEvict(value = "roles", allEntries = true)
    public Role save(Role entity) {

        boolean isNew = (entity.getRoleId() == null);
        Role saved = super.save(entity);

        if (isNew) {
            // Fetch all matching TenantPermission rows in one round-trip
            List<TenantPermission> perms =
                    tenantPermissionRepo.findByNameIn(STARTER_PERMISSION_NAMES);

            perms.forEach(p -> {
                // Avoid duplicates if the list or DB changes later
                boolean alreadyLinked = rolePermissionRepo
                        .existsByRole_RoleIdAndTenantPermission_TenantPermissionId(
                                saved.getRoleId(), p.getTenantPermissionId());

                if (!alreadyLinked) {
                    RolePermission rp = new RolePermission();
                    rp.setRole(saved);
                    rp.setTenantPermission(p);
                    rolePermissionRepo.save(rp);
                }
            });
        }
        return saved;
    }

    @Override
    @CacheEvict(value = "roles", allEntries = true)
    public void deleteById(Integer id) {
        super.deleteById(id);
    }
}
