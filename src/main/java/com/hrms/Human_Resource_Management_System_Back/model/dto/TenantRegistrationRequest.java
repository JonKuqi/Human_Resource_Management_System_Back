package com.hrms.Human_Resource_Management_System_Back.model.dto;


import lombok.Data;

@Data
public class TenantRegistrationRequest {
    private String name;
    private String contactEmail;
    private AddressDto address;

}

