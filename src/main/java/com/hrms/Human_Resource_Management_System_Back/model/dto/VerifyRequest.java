package com.hrms.Human_Resource_Management_System_Back.model.dto;

import lombok.Data;

@Data
public class VerifyRequest {
    private String email;
    private String code;
}