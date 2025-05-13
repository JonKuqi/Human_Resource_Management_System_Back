package com.hrms.Human_Resource_Management_System_Back.service.tenant;

import com.hrms.Human_Resource_Management_System_Back.model.tenant.Role;
import com.hrms.Human_Resource_Management_System_Back.repository.tenant.RoleRepository;
import com.hrms.Human_Resource_Management_System_Back.service.BaseService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

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
}
