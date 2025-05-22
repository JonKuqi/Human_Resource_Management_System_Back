package com.hrms.Human_Resource_Management_System_Back.controller;

import com.hrms.Human_Resource_Management_System_Back.model.dto.RolePermissionReplaceRequest;
import com.hrms.Human_Resource_Management_System_Back.model.tenant.RolePermission;
import com.hrms.Human_Resource_Management_System_Back.service.tenant.RolePermissionService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/tenant/role-permission")
@AllArgsConstructor
public class RolePermissionController extends BaseController<RolePermission, Integer> {
    private final RolePermissionService svc;

    @Override
    protected RolePermissionService getService() {
        return svc;
    }

    @GetMapping("/by-role/{roleId}")
    public ResponseEntity<List<RolePermission>> getByRoleId(@PathVariable Integer roleId) {
        List<RolePermission> list = svc.findByRoleId(roleId);
        return ResponseEntity.ok(list);
    }

    /** PUT /api/v1/tenant/role-permission/replace/{roleId} */
    @PutMapping("/replace/{roleId}")
    public ResponseEntity<Void> replaceRolePermissions(
            @PathVariable Integer roleId,
            @RequestBody RolePermissionReplaceRequest body) {

        svc.replacePermissions(roleId, body);
        return ResponseEntity.noContent().build();
    }
}
