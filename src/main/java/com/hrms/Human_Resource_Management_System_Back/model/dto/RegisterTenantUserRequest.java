package com.hrms.Human_Resource_Management_System_Back.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterTenantUserRequest {

    // base user creds
    private String email;
    private String username;
    private String password;

    // profile
    private String firstName;
    private String lastName;
    private String phone;
    private String gender;

    // address fields (simplified; adapt to your AddressDTO)
    private AddressDto address;

    // which tenant we’re creating this user for
   // private Integer tenantId;
}