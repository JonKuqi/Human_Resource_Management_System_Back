package com.hrms.Human_Resource_Management_System_Back.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

/**
 * Data transfer object (DTO) for creating the first user (owner) after tenant verification.
 * <p>
 * - token: The verification JWT token.
 * - username: The username of the new owner.
 * - email: The email address of the new owner.
 * - password: The password for the new owner.
 * - firstName: The first name of the new owner.
 * - lastName: The last name of the new owner.
 * - phone: The phone number of the new owner.
 * - gender: The gender of the new owner.
 * - address: The address of the new owner.
 * </p>
 */
@JsonIgnoreProperties(ignoreUnknown = true)
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

