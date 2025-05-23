package com.hrms.Human_Resource_Management_System_Back.controller;

import com.hrms.Human_Resource_Management_System_Back.model.tenant.Role;
import com.hrms.Human_Resource_Management_System_Back.service.tenant.RoleService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller for managing roles within a tenant.
 * <p>
 * This controller exposes endpoints for performing CRUD operations on {@link Role} entities,
 * enabling tenant-specific role management functionality. It inherits basic operations from {@link BaseController}.
 * </p>
 */
@RestController
@RequestMapping("/api/v1/tenant/role")
@AllArgsConstructor
public class RoleController extends BaseController<Role, Integer> {
    /**
     * The service responsible for role-related business logic.
     */
    private final RoleService svc;

    /**
     * Returns the role service used for CRUD operations on roles.
     * This overrides the method in {@link BaseController}.
     *
     * @return the {@link RoleService} instance
     */
    @Override
    protected RoleService getService() {
        return svc;
    }
}
