package com.hrms.Human_Resource_Management_System_Back.controller;

import com.hrms.Human_Resource_Management_System_Back.model.dto.RolePermissionReplaceRequest;
import com.hrms.Human_Resource_Management_System_Back.model.tenant.RolePermission;
import com.hrms.Human_Resource_Management_System_Back.service.tenant.RolePermissionService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller for managing the assignment of permissions to roles.
 * <p>
 * Provides endpoints to retrieve and update the permissions associated with specific roles
 * within a tenant's context. This controller supports role-based access control (RBAC)
 * by enabling dynamic permission updates per role.
 * </p>
 */
@RestController
@RequestMapping("/api/v1/tenant/role-permission")
@AllArgsConstructor
public class RolePermissionController extends BaseController<RolePermission, Integer> {
    /**
     * Service that handles the business logic related to role-permission management.
     */
    private final RolePermissionService svc;

    /**
     * Provides the service layer to the base controller.
     *
     * @return an instance of {@link RolePermissionService}
     */
    @Override
    protected RolePermissionService getService() {
        return svc;
    }

    /**
     * Retrieves all permissions assigned to a specific role.
     *
     * @param roleId the unique identifier of the role
     * @return a list of {@link RolePermission} associated with the given role
     */
    @GetMapping("/by-role/{roleId}")
    public ResponseEntity<List<RolePermission>> getByRoleId(@PathVariable Integer roleId) {
        List<RolePermission> list = svc.findByRoleId(roleId);
        return ResponseEntity.ok(list);
    }
    /**
     * Replaces the current set of permissions assigned to a role with a new set.
     * <p>
     * This operation removes all existing role-permission mappings for the given role
     * and inserts the new ones provided in the request body.
     * </p>
     *
     * @param roleId the ID of the role whose permissions are being replaced
     * @param body   the request payload containing permission and target role IDs
     * @return HTTP 204 No Content if the update is successful
     */
    @PutMapping("/replace/{roleId}")
    public ResponseEntity<Void> replaceRolePermissions(
            @PathVariable Integer roleId,
            @RequestBody RolePermissionReplaceRequest body) {

        svc.replacePermissions(roleId, body);
        return ResponseEntity.noContent().build();
    }
}
