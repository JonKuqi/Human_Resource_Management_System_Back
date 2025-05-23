package com.hrms.Human_Resource_Management_System_Back.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserTenantUpdateDTO {
    private Integer userTenantId;
    private String firstName;
    private String lastName;
    private String phone;
    private String gender;
    private Integer addressId;
}