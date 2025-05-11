package com.hrms.Human_Resource_Management_System_Back.model.dto;

import lombok.Data;

import java.util.List;

@Data
public class CreateEvaluationTemplateRequest {
    private String title;
    private List<String> questions;
}
