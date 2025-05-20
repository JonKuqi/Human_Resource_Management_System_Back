package com.hrms.Human_Resource_Management_System_Back.model.dto;

import lombok.Data;

/**
 * Data transfer object (DTO) for changing the user's password.
 * <p>
 * - currentPassword: The current password of the user.
 * - newPassword: The new password the user wants to set.
 * </p>
 */
@Data
public class ChangePasswordRequest {
    private String currentPassword;
    private String newPassword;   // confirm is handled on the front-end

    public ChangePasswordRequest(String wrongPassword, String newPassword123) {
    }

    public ChangePasswordRequest() {

    }
}