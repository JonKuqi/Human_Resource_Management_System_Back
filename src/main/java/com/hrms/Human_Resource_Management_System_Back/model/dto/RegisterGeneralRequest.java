package com.hrms.Human_Resource_Management_System_Back.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * Data transfer object (DTO) for registering a general user.
 * <p>
 * - email: The email address of the user.
 * - username: The username of the user.
 * - password: The password of the user.
 * - firstName: The first name of the user.
 * - lastName: The last name of the user.
 * - phone: The phone number of the user.
 * - gender: The gender of the user.
 * - birthDate: The birth date of the user.
 * </p>
 */
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
