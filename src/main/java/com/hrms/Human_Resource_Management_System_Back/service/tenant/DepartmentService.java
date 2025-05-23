package com.hrms.Human_Resource_Management_System_Back.service.tenant;

import com.hrms.Human_Resource_Management_System_Back.model.tenant.Department;
import com.hrms.Human_Resource_Management_System_Back.repository.tenant.DepartmentRepository;
import com.hrms.Human_Resource_Management_System_Back.service.BaseService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;


/**
 * Service class for handling business logic related to departments within a tenant schema.
 * <p>
 * This service provides methods to manage {@link Department} entities, including basic CRUD operations.
 * It extends {@link BaseService} to reuse generic service functionality.
 * </p>
 */
@Service
@AllArgsConstructor
public class DepartmentService extends BaseService<Department, Integer> {
    /**
     * The repository responsible for performing database operations on departments.
     */
    private final DepartmentRepository repo;
    /**
     * Returns the department repository.
     * <p>
     * This method overrides the {@link BaseService#getRepository()} method to return the specific repository
     * for department entities.
     * </p>
     *
     * @return the department repository
     */
    @Override
    protected DepartmentRepository getRepository() {
        return repo;
    }
}
