package com.hrms.Human_Resource_Management_System_Back.controller;

import com.hrms.Human_Resource_Management_System_Back.model.Tenant;
import com.hrms.Human_Resource_Management_System_Back.model.dto.AuthenticationResponse;
import com.hrms.Human_Resource_Management_System_Back.model.dto.OwnerCreationRequest;
import com.hrms.Human_Resource_Management_System_Back.model.dto.TenantRegistrationRequest;
import com.hrms.Human_Resource_Management_System_Back.service.BaseService;
import com.hrms.Human_Resource_Management_System_Back.service.TenantOnboardingService;
import com.hrms.Human_Resource_Management_System_Back.service.TenantService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/public/tenant")
public class TenantController extends BaseController<Tenant, Integer> {

    @Override
    protected BaseService<Tenant, Integer> getService() {
        return tenantService;
    }

    private final TenantService tenantService;

    private final TenantOnboardingService onboardingService;

    private String frontendUrl;
    public TenantController(
            TenantService tenantService, TenantOnboardingService onboardingService,
            @Value("${app.frontend.url}") String frontendUrl
    ) {
        this.tenantService = tenantService;
        this.onboardingService = onboardingService;
        this.frontendUrl      = frontendUrl;
    }

    /** Step 1: Register tenant and send verification email */
    @PostMapping("/register")
    public ResponseEntity<Void> register(@RequestBody TenantRegistrationRequest rq) {
  ;
        onboardingService.registerTenant(rq);
        return ResponseEntity.accepted().build();
    }

    /** Step 1b: Verify email link & redirect to owner‑setup page */
    @PostMapping("/onboard")
    public ResponseEntity<AuthenticationResponse> onboard(@RequestBody OwnerCreationRequest rq) {
        AuthenticationResponse res = onboardingService.createOwnerAfterVerification(rq);
        return ResponseEntity.ok(res);
    }



}