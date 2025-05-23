package com.hrms.Human_Resource_Management_System_Back.controller;

import com.hrms.Human_Resource_Management_System_Back.model.tenant.UserRole;
import com.hrms.Human_Resource_Management_System_Back.service.tenant.UserRoleService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller for managing user roles within a tenant.
 * <p>
 * This controller provides endpoints for retrieving user roles associated with a specific tenant and
 * updating the roles of users within that tenant.
 * </p>
 */
@RestController
@RequestMapping("/api/v1/tenant/user-role")
@AllArgsConstructor
public class UserRoleController extends BaseController<UserRole, Integer> {

    /**
     * The service responsible for handling user role business logic.
     */
    private final UserRoleService svc;

    /**
     * Overrides {@link BaseController#getService()} to return the user role service.
     *
     * @return the user role service
     */
    @Override
    protected UserRoleService getService() {
        return svc;
    }

    /**
     * Retrieves a list of user roles associated with a specific user tenant.
     * <p>
     * This method fetches all roles associated with the given {@code userTenantId}. If no roles are found,
     * a 404 Not Found status is returned.
     * </p>
     *
     * @param userTenantId the ID of the user tenant whose roles are to be fetched
     * @return a {@link ResponseEntity} containing the list of user roles, or 404 if no roles are found
     */
    @GetMapping("/by-user-tenant/{userTenantId}")
    public ResponseEntity<List<UserRole>> getRolesByUserTenant(@PathVariable Integer userTenantId) {
        List<UserRole> list = svc.findByUserTenantId(userTenantId);
        return list.isEmpty()
                ? ResponseEntity.notFound().build()
                : ResponseEntity.ok(list);
    }

    /**
     * Replaces the roles of a user tenant.
     * <p>
     * This method replaces the roles associated with a specific user tenant with the provided list of role IDs.
     * It returns a 204 No Content response upon success.
     * </p>
     *
     * @param userTenantId the ID of the user tenant whose roles are to be replaced
     * @param roleIds a list of role IDs to associate with the user tenant
     * @return a {@link ResponseEntity} with a 204 status on success
     */
//    @PutMapping("/replace/{userTenantId}")
//    public ResponseEntity<Void> replaceUserRoles(
//            @PathVariable Integer userTenantId,
//            @RequestBody List<Integer> roleIds) {
//
//        svc.replaceRoles(userTenantId, roleIds);
//        return ResponseEntity.noContent().build();
//    }

}
