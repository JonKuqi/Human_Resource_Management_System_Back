package com.hrms.Human_Resource_Management_System_Back.controller;

import com.hrms.Human_Resource_Management_System_Back.model.TenantPermission;
import com.hrms.Human_Resource_Management_System_Back.service.TenantPermissionService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/public/permission")
@AllArgsConstructor
public class PermissionController extends BaseController<TenantPermission, Integer> {
    private final TenantPermissionService svc;

    @Override
    protected TenantPermissionService getService() {
        return svc;
    }
}
