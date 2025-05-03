package com.hrms.Human_Resource_Management_System_Back.controller;

import com.hrms.Human_Resource_Management_System_Back.model.tenant.UserRole;
import com.hrms.Human_Resource_Management_System_Back.service.tenant.UserRoleService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user-roles")
@AllArgsConstructor
public class UserRoleController extends BaseController<UserRole, Integer> {
    private final UserRoleService svc;

    @Override
    protected UserRoleService getService() {
        return svc;
    }
}
