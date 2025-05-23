package com.hrms.Human_Resource_Management_System_Back.service;

import com.hrms.Human_Resource_Management_System_Back.model.TenantPermission;
import com.hrms.Human_Resource_Management_System_Back.repository.TenantPermissionRepository;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service class for handling business logic related to tenant permissions.
 * <p>
 * This class extends {@link BaseService} and provides methods for managing tenant permission entities,
 * including interacting with the underlying repository.
 * </p>
 */
@Service
@AllArgsConstructor
public class TenantPermissionService extends BaseService<TenantPermission, Integer> {
    private final TenantPermissionRepository repo;

    /**
     * Returns the tenant permission repository.
     * <p>
     * This method overrides the {@link BaseService#getRepository()} method to return the specific repository
     * for tenant permission entities.
     * </p>
     *
     * @return the tenant permission repository
     */
    @Override
    protected TenantPermissionRepository getRepository() {
        return repo;
    }

    @Override
    @Cacheable("permissions")
    public List<TenantPermission> findAll() {
        return super.findAll();
    }

    /**
     * Evict cache when a permission is added or updated.
     */
    @Override
    @CacheEvict(value = "permissions", allEntries = true)
    public TenantPermission save(TenantPermission entity) {
        return super.save(entity);
    }

    /**
     * Evict cache when a permission is deleted.
     */
    @Override
    @CacheEvict(value = "permissions", allEntries = true)
    public void deleteById(Integer id) {
        super.deleteById(id);
    }
}
