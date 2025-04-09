package com.hrms.Human_Resource_Management_System_Back.service.tenant;

import com.hrms.Human_Resource_Management_System_Back.model.tenant.RolePermission;
import com.hrms.Human_Resource_Management_System_Back.repository.tenant.RolePermissionRepository;
import com.hrms.Human_Resource_Management_System_Back.service.BaseService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class RolePermissionService extends BaseService<RolePermission, Integer> {
    private final RolePermissionRepository repo;

    @Override
    protected RolePermissionRepository getRepository() {
        return repo;
    }
}
