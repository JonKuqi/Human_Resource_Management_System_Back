package com.hrms.Human_Resource_Management_System_Back.repository;

import com.hrms.Human_Resource_Management_System_Back.model.Tenant;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository interface for managing tenant entities.
 * <p>
 * This repository extends {@link BaseRepository} and provides methods for accessing and modifying tenant data.
 * It includes a method for finding tenants by their schema name.
 * </p>
 */
@Repository
public interface TenantRepository extends BaseRepository<Tenant, Integer> {

    /**
     * Retrieves a tenant by its schema name.
     * <p>
     * This method fetches a tenant entity based on the provided schema name. The schema name is used to identify
     * the tenant in the multi-tenant system, ensuring that the correct schema is selected for tenant-specific data.
     * </p>
     *
     * @param schemaName the schema name of the tenant
     * @return an {@link Optional} containing the tenant if found, or empty if no tenant with the given schema name exists
     */
    Optional<Tenant> findBySchemaName(String schemaName);
}