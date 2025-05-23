package com.hrms.Human_Resource_Management_System_Back.repository;

import com.hrms.Human_Resource_Management_System_Back.model.TenantPermission;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
/**
 * Repository interface for managing {@link TenantPermission} entities.
 * <p>
 * This interface extends {@link BaseRepository} and provides custom query methods
 * for retrieving permissions based on specific criteria such as permission names,
 * HTTP verbs, and API resource paths.
 * </p>
 */

@Repository
public interface TenantPermissionRepository extends BaseRepository<TenantPermission, Integer> {
    /**
     * Finds a list of {@link TenantPermission} entities by their names.
     *
     * @param starterPermissionNames the list of permission names to search for
     * @return a list of matching {@link TenantPermission} entities
     */
    List<TenantPermission> findByNameIn(List<String> starterPermissionNames);


    /**
     * Finds a {@link TenantPermission} by its API resource and HTTP verb.
     * <p>
     * Useful when assigning permissions to roles based on specific endpoint access.
     * </p>
     *
     * @param resource the API endpoint (e.g., "/api/v1/tenant/user-role")
     * @param verb     the HTTP verb (e.g., "POST", "GET")
     * @return an {@link Optional} containing the matching {@link TenantPermission}, if found
     */
    Optional<TenantPermission> findByResourceAndVerb(String resource, String verb);

}
