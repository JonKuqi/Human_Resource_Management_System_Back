package com.hrms.Human_Resource_Management_System_Back.service;

import com.hrms.Human_Resource_Management_System_Back.model.TenantPermission;
import com.hrms.Human_Resource_Management_System_Back.repository.TenantPermissionRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class TenantPermissionService extends BaseService<TenantPermission, Integer> {
    private final TenantPermissionRepository repo;

    @Override
    protected TenantPermissionRepository getRepository() {
        return repo;
    }
}
