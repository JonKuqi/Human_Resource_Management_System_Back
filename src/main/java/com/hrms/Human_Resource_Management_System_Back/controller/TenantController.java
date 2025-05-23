package com.hrms.Human_Resource_Management_System_Back.controller;

import com.hrms.Human_Resource_Management_System_Back.model.Tenant;
import com.hrms.Human_Resource_Management_System_Back.model.dto.AuthenticationResponse;
import com.hrms.Human_Resource_Management_System_Back.model.dto.OwnerCreationRequest;
import com.hrms.Human_Resource_Management_System_Back.model.dto.TenantRegistrationRequest;
import com.hrms.Human_Resource_Management_System_Back.service.BaseService;
import com.hrms.Human_Resource_Management_System_Back.service.TenantOnboardingService;
import com.hrms.Human_Resource_Management_System_Back.service.TenantService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller for managing tenant operations such as registration and onboarding.
 * <p>
 * This controller handles tenant-related endpoints, including tenant registration, email verification,
 * and owner setup during the onboarding process.
 * </p>
 */
@RestController
@RequestMapping("/api/v1/public/tenant")
public class TenantController extends BaseController<Tenant, Integer> {

    /**
     * The service responsible for handling tenant business logic.
     */
    private final TenantService tenantService;

    /**
     * The service responsible for managing tenant onboarding operations.
     */
    private final TenantOnboardingService onboardingService;

    /**
     * The frontend URL, injected from application properties.
     */
    private String frontendUrl;

    /**
     * Constructor for initializing the tenant controller with necessary services.
     *
     * @param tenantService the service for managing tenants
     * @param onboardingService the service for managing tenant onboarding
     * @param frontendUrl the URL of the frontend, injected from the application properties
     */
    public TenantController(
            TenantService tenantService, TenantOnboardingService onboardingService,
            @Value("${app.frontend.url}") String frontendUrl
    ) {
        this.tenantService = tenantService;
        this.onboardingService = onboardingService;
        this.frontendUrl      = frontendUrl;
    }

    /**
     * Registers a new tenant and sends a verification email.
     * <p>
     * This method registers a tenant based on the provided registration request and sends a verification email
     * to the tenant's email address. The registration includes setting up basic tenant information.
     * </p>
     *
     * @param rq the registration request containing tenant details
     * @return a {@link ResponseEntity} with a status of ACCEPTED if the tenant registration is successful
     */
    @Operation(
            summary = "Register a new tenant",
            description = "Registers a new tenant and sends a verification email to the provided email address."
    )
    @PostMapping("/register")
    public ResponseEntity<Void> register(@RequestBody TenantRegistrationRequest rq) {
        onboardingService.registerTenant(rq);
        return ResponseEntity.accepted().build();
    }

    /**
     * Verifies the email link and redirects the user to the owner setup page.
     * <p>
     * This method handles the verification of the tenant owner's email. After verification, it proceeds
     * to create the owner and redirects them to the setup page for configuring the tenant's owner details.
     * </p>
     *
     * @param rq the owner creation request containing necessary information for setting up the owner
     * @return an {@link AuthenticationResponse} containing authentication details after successful onboarding
     */
    @Operation(
            summary = "Onboard tenant owner after email verification",
            description = "Completes onboarding after email verification and returns an authentication response."
    )
    @PostMapping("/onboard")
    public ResponseEntity<AuthenticationResponse> onboard(@RequestBody OwnerCreationRequest rq) {
        AuthenticationResponse res = onboardingService.createOwnerAfterVerification(rq);
        return ResponseEntity.ok(res);
    }

    /**
     * Overrides {@link BaseController#getService()} to return the tenant service.
     *
     * @return the tenant service
     */
    @Override
    protected BaseService<Tenant, Integer> getService() {
        return tenantService;
    }
}