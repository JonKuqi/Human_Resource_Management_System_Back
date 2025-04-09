package com.hrms.Human_Resource_Management_System_Back.controller.tenant;

import com.hrms.Human_Resource_Management_System_Back.controller.BaseController;
import com.hrms.Human_Resource_Management_System_Back.model.tenant.UserTenant;
import com.hrms.Human_Resource_Management_System_Back.service.tenant.UserTenantService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user-tenants")
@AllArgsConstructor
public class UserTenantController extends BaseController<UserTenant, Integer> {
    private final UserTenantService svc;

    @Override
    protected UserTenantService getService() {
        return svc;
    }
}
