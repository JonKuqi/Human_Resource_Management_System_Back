package com.hrms.Human_Resource_Management_System_Back.controller;

import com.hrms.Human_Resource_Management_System_Back.model.UserTenantUpdateDTO;
import com.hrms.Human_Resource_Management_System_Back.model.dto.AuthenticationResponse;
import com.hrms.Human_Resource_Management_System_Back.model.dto.CreateEmployeeRequest;
import com.hrms.Human_Resource_Management_System_Back.model.dto.RegisterTenantUserRequest;
import com.hrms.Human_Resource_Management_System_Back.model.tenant.UserTenant;
import com.hrms.Human_Resource_Management_System_Back.service.tenant.UserTenantService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * Controller for managing user tenants with role-based access control.
 * <p>
 * This controller provides endpoints for managing user-tenant relationships, including user registration,
 * and profile photo updates.
 * </p>
 */
@RestController
@RequestMapping("/api/v1/tenant/user-tenant")
@AllArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class UserTenantController extends BaseUserSpecificController<UserTenant, Integer> {

    /**
     * The service responsible for handling user tenant business logic.
     */
    private final UserTenantService service;

    /**
     * Overrides {@link BaseUserSpecificController#getServiceSpecific()} to return the user tenant service.
     *
     * @return the user tenant service
     */
    @Override
    protected UserTenantService getServiceSpecific() {
        return service;
    }

    /**
     * Registers a new user as a User Tenant Owner.
     * <p>
     * This method handles the registration of a new user within the context of a tenant.
     * Upon successful registration, an authentication response containing necessary tokens is returned.
     * </p>
     *
     * @param rq the registration request containing user details
     * @return a {@link ResponseEntity} containing the authentication response
     */
    @Operation(summary = "Register a user tenant", description = "Registers a new user in the current tenant and returns an authentication response.")

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(
            @RequestBody RegisterTenantUserRequest rq) {
        return ResponseEntity.ok(service.register(rq));
    }

    /**
     * Creating a new employee within the current tenant by the User Tenant Owner.
     * <p>
     * Accepts a JSON request body with employee details, including personal information,
     * address, department, position, and contract data. Delegates the creation logic
     * to {@link UserTenantService#createEmployee(CreateEmployeeRequest)}.
     * </p>
     *
     * @param rq the request payload containing employee information
     * @return {@link ResponseEntity} with status 200 OK upon success
     */


    /**
     * Uploads or replaces the profile photo of a user tenant.
     * <p>
     * This method accepts a multipart file and updates the profile photo of the user tenant identified by the provided ID.
     * If the update is successful, it returns a 200 OK status. If an error occurs during the file processing, a 500 Internal Server Error is returned.
     * </p>
     *
     * @param id the ID of the user tenant whose profile photo is to be updated
     * @param file the multipart file containing the new profile photo
     * @return a {@link ResponseEntity} with a 200 status on success, or 500 if an error occurs
     */
    @Operation(summary = "Upload or replace profile photo")
    @PutMapping(
            value = "/photo/{id}",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<Void> updatePhoto(
            @PathVariable Integer id,
            @RequestPart("file") MultipartFile file) {

        try {
            service.updateProfilePhoto(id, file);
        } catch (IOException e) {
            return ResponseEntity.internalServerError().build();
        }
        return ResponseEntity.ok().build();
    }

    @PutMapping(value = "/update", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> updateUserTenantInfo(@ModelAttribute UserTenantUpdateDTO dto) {
        boolean updated = service.updateBasicInfo(dto);
        return updated ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }


}