package com.hrms.Human_Resource_Management_System_Back.controller;

import com.hrms.Human_Resource_Management_System_Back.model.tenant.RolePermission;
import com.hrms.Human_Resource_Management_System_Back.service.tenant.RolePermissionService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/tenant/role-permission")
@AllArgsConstructor
public class RolePermissionController extends BaseController<RolePermission, Integer> {
    private final RolePermissionService svc;

    @Override
    protected RolePermissionService getService() {
        return svc;
    }
}
