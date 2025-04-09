package com.hrms.Human_Resource_Management_System_Back.controller.tenant;

import com.hrms.Human_Resource_Management_System_Back.controller.BaseController;
import com.hrms.Human_Resource_Management_System_Back.model.tenant.Permission;
import com.hrms.Human_Resource_Management_System_Back.service.tenant.PermissionService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/permissions")
@AllArgsConstructor
public class PermissionController extends BaseController<Permission, Integer> {
    private final PermissionService svc;

    @Override
    protected PermissionService getService() {
        return svc;
    }
}
