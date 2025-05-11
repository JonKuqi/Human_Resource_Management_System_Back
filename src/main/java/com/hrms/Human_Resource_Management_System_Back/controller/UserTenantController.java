package com.hrms.Human_Resource_Management_System_Back.controller;

import com.hrms.Human_Resource_Management_System_Back.model.dto.AuthenticationResponse;
import com.hrms.Human_Resource_Management_System_Back.model.dto.RegisterTenantUserRequest;
import com.hrms.Human_Resource_Management_System_Back.model.tenant.UserTenant;
import com.hrms.Human_Resource_Management_System_Back.service.tenant.UserTenantService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/tenant/user-tenant")
@AllArgsConstructor
public class UserTenantController extends BaseUserSpecificController<UserTenant, Integer> {
    private final UserTenantService service;

    @Override
    protected UserTenantService getServiceSpecific() {
        return service;
    }

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(
            @RequestBody RegisterTenantUserRequest rq) {
        return ResponseEntity.ok(service.register(rq));
    }
    @Operation(summary = "Upload or replace profile photo")
    @PutMapping(
            value = "/photo/{id}",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<Void> updatePhoto(
            @PathVariable Integer id,
            @RequestPart("file") MultipartFile file) {

        try {
            service.updateProfilePhoto(id, file);          // delegate to service
        } catch (IOException e) {
            return ResponseEntity.internalServerError().build();
        }
        return ResponseEntity.ok().build();
    }

}
