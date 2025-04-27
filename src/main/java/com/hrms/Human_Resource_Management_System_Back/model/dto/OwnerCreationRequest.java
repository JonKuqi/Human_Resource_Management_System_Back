package com.hrms.Human_Resource_Management_System_Back.model.dto;

import lombok.Data;

@Data
public class OwnerCreationRequest {
    private String token;       // the verification JWT
    private String username;
    private String email;
    private String password;
    private String firstName;
    private String lastName;
    private String phone;
    private String gender;
    private AddressDto address;

    //private AddressDto address;
}

