package com.hrms.Human_Resource_Management_System_Back.repository.tenant;

import com.hrms.Human_Resource_Management_System_Back.model.tenant.Role;
import com.hrms.Human_Resource_Management_System_Back.repository.BaseRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for managing {@link Role} entities.
 * <p>
 * This repository extends {@link BaseRepository} and provides CRUD operations for {@link Role} entities.
 * </p>
 */
@Repository
public interface RoleRepository extends BaseRepository<Role, Integer> {
    Object findByRoleName(String owner);
}