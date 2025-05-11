package com.hrms.Human_Resource_Management_System_Back.model.dto;

import lombok.Data;

@Data
public class AnswerDto {
    private Integer questionId;
    private Integer rating;
    private String textResponse;
}
