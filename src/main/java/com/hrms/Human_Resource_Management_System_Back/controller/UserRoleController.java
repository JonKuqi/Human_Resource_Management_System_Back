package com.hrms.Human_Resource_Management_System_Back.controller;

import com.hrms.Human_Resource_Management_System_Back.model.tenant.UserRole;
import com.hrms.Human_Resource_Management_System_Back.service.tenant.UserRoleService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/tenant/user-role")
@AllArgsConstructor
public class UserRoleController extends BaseController<UserRole, Integer> {
    private final UserRoleService svc;

    @Override
    protected UserRoleService getService() {
        return svc;
    }

    @GetMapping("/by-user-tenant/{userTenantId}")
    public ResponseEntity<List<UserRole>> getRolesByUserTenant(@PathVariable Integer userTenantId) {
        List<UserRole> list = svc.findByUserTenantId(userTenantId);
        return list.isEmpty()
                ? ResponseEntity.notFound().build()
                : ResponseEntity.ok(list);
    }

    @PutMapping("/replace/{userTenantId}")
    public ResponseEntity<Void> replaceUserRoles(
            @PathVariable Integer userTenantId,
            @RequestBody List<Integer> roleIds) {

        svc.replaceRoles(userTenantId, roleIds);
        return ResponseEntity.noContent().build();
    }

}
