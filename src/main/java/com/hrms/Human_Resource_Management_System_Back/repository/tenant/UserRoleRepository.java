package com.hrms.Human_Resource_Management_System_Back.repository.tenant;

import com.hrms.Human_Resource_Management_System_Back.model.tenant.UserRole;
import com.hrms.Human_Resource_Management_System_Back.repository.BaseRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRoleRepository extends BaseRepository<UserRole, Integer> {
}
