package com.hrms.Human_Resource_Management_System_Back.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterGeneralRequest {
    private String email;
    private String username;
    private String password;
    private String firstName;
    private String lastName;
    private String phone;
    private String gender;
    private LocalDate birthDate;
}
