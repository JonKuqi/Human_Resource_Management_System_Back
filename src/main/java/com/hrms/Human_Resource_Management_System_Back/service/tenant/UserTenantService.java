package com.hrms.Human_Resource_Management_System_Back.service.tenant;

import com.hrms.Human_Resource_Management_System_Back.model.tenant.UserTenant;
import com.hrms.Human_Resource_Management_System_Back.repository.tenant.UserTenantRepository;
import com.hrms.Human_Resource_Management_System_Back.service.BaseService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserTenantService extends BaseService<UserTenant, Integer> {
    private final UserTenantRepository repo;

    @Override
    protected UserTenantRepository getRepository() {
        return repo;
    }
}
