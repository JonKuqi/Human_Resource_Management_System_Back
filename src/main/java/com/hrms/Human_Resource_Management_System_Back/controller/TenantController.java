package com.hrms.Human_Resource_Management_System_Back.controller;

import com.hrms.Human_Resource_Management_System_Back.model.Tenant;
import com.hrms.Human_Resource_Management_System_Back.service.TenantService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/tenants")
@AllArgsConstructor
public class TenantController extends BaseController<Tenant, Integer> {

    private final TenantService tenantService;

    @Override
    protected TenantService getService() {
        return tenantService;
    }
}