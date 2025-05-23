package com.hrms.Human_Resource_Management_System_Back.controller;

import com.hrms.Human_Resource_Management_System_Back.model.TenantPermission;
import com.hrms.Human_Resource_Management_System_Back.service.TenantPermissionService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller for managing tenant-level permissions.
 * <p>
 * This controller provides public endpoints for accessing {@link TenantPermission}
 * entities. It exposes CRUD operations through the {@link BaseController}.
 * </p>
 */
@RestController
@RequestMapping("/api/v1/public/permission")
@AllArgsConstructor
public class PermissionController extends BaseController<TenantPermission, Integer> {
    /**
     * The service responsible for tenant permission business logic and data access.
     */
    private final TenantPermissionService svc;

    /**
     * Returns the service implementation used to manage {@link TenantPermission} entities.
     * Overrides the abstract method in {@link BaseController}.
     *
     * @return the tenant permission service
     */
    @Override
    protected TenantPermissionService getService() {
        return svc;
    }
}
