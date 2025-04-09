package com.hrms.Human_Resource_Management_System_Back.service.tenant;

import com.hrms.Human_Resource_Management_System_Back.model.tenant.UserRole;
import com.hrms.Human_Resource_Management_System_Back.repository.tenant.UserRoleRepository;
import com.hrms.Human_Resource_Management_System_Back.service.BaseService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserRoleService extends BaseService<UserRole, Integer> {
    private final UserRoleRepository repo;

    @Override
    protected UserRoleRepository getRepository() {
        return repo;
    }
}
