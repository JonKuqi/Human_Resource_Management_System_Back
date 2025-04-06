package com.hrms.Human_Resource_Management_System_Back.service;

import com.hrms.Human_Resource_Management_System_Back.model.Tenant;
import com.hrms.Human_Resource_Management_System_Back.repository.TenantRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class TenantService extends BaseService<Tenant, Integer> {

    private final TenantRepository tenantRepository;

    @Override
    protected TenantRepository getRepository() {
        return tenantRepository;
    }
}
