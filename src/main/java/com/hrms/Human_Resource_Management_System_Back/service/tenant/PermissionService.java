package com.hrms.Human_Resource_Management_System_Back.service.tenant;

import com.hrms.Human_Resource_Management_System_Back.model.tenant.Permission;
import com.hrms.Human_Resource_Management_System_Back.repository.tenant.PermissionRepository;
import com.hrms.Human_Resource_Management_System_Back.service.BaseService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class PermissionService extends BaseService<Permission, Integer> {
    private final PermissionRepository repo;

    @Override
    protected PermissionRepository getRepository() {
        return repo;
    }
}
