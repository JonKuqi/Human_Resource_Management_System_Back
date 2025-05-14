package com.hrms.Human_Resource_Management_System_Back.service;

import com.hrms.Human_Resource_Management_System_Back.model.Tenant;
import com.hrms.Human_Resource_Management_System_Back.repository.TenantRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
/**
 * Service class for handling business logic related to tenants.
 * <p>
 * This class extends {@link BaseService} and provides methods for managing tenant entities,
 * including interacting with the underlying repository.
 * </p>
 */
@Service
@AllArgsConstructor
public class TenantService extends BaseService<Tenant, Integer> {

    private final TenantRepository tenantRepository;

    /**
     * Returns the tenant repository.
     * <p>
     * This method overrides the {@link BaseService#getRepository()} method to return the specific repository
     * for tenant entities.
     * </p>
     *
     * @return the tenant repository
     */
    @Override
    protected TenantRepository getRepository() {
        return tenantRepository;
    }
}