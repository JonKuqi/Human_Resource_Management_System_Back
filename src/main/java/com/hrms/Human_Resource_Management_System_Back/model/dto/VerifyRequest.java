package com.hrms.Human_Resource_Management_System_Back.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Data transfer object (DTO) for verifying a user's email.
 * <p>
 * - email: The email address of the user to verify.
 * - code: The verification code sent to the user's email.
 * </p>
 */
@Data
@AllArgsConstructor
public class VerifyRequest {
    private String email;
    private String code;

    public VerifyRequest() {

    }
}