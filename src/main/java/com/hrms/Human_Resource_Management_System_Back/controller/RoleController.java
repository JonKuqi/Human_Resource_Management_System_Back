package com.hrms.Human_Resource_Management_System_Back.controller;

import com.hrms.Human_Resource_Management_System_Back.model.tenant.Role;
import com.hrms.Human_Resource_Management_System_Back.service.tenant.RoleService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/tenant/role")
@AllArgsConstructor
public class RoleController extends BaseController<Role, Integer> {
    private final RoleService svc;

    @Override
    protected RoleService getService() {
        return svc;
    }
}
