package com.hrms.Human_Resource_Management_System_Back.model.dto;

import lombok.Data;


@Data
public class InitiateEvaluationRequest {

    private Integer templateId;
    private Integer targetUserTenantId;
}
