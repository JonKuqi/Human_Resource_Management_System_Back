package com.hrms.Human_Resource_Management_System_Back.model.dto;


import lombok.Data;

/**
 * Data transfer object (DTO) for registering a tenant.
 * <p>
 * - name: The name of the tenant.
 * - contactEmail: The contact email of the tenant.
 * - address: The address of the tenant.
 * </p>
 */
@Data
public class TenantRegistrationRequest {
    private String name;
    private String contactEmail;
    private AddressDto address;

}

