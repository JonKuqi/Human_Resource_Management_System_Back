package com.hrms.Human_Resource_Management_System_Back.model.dto;

import lombok.Data;

@Data
public class ChangePasswordRequest {
    private String currentPassword;
    private String newPassword;   // confirm is handled on the front-end
}