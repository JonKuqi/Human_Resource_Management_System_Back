package com.hrms.Human_Resource_Management_System_Back;

import com.hrms.Human_Resource_Management_System_Back.middleware.TenantCtx;
import org.springframework.boot.SpringApplication;

public class Main {
    public static void main(String[] args) {
        TenantCtx.setTenant("public");  // or a valid tenant schema like "tenant_abc"
        SpringApplication.run(HumanResourceManagementSystemBackApplication.class, args);
    }
}
