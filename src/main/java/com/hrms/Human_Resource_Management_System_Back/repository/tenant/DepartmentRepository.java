package com.hrms.Human_Resource_Management_System_Back.repository.tenant;

import com.hrms.Human_Resource_Management_System_Back.model.tenant.Department;
import com.hrms.Human_Resource_Management_System_Back.repository.BaseRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository interface for managing {@link Department} entities.
 * <p>
 * Extends {@link BaseRepository} to provide basic CRUD operations, and defines
 * additional query methods specific to the {@link Department} entity.
 * </p>
 */
@Repository
public interface DepartmentRepository extends BaseRepository<Department, Integer> {
    /**
     * Retrieves a department by its name.
     *
     * @param name the name of the department
     * @return an {@link Optional} containing the matching {@link Department} if found, otherwise empty
     */
    Optional<Department> findByName(String name);

}
