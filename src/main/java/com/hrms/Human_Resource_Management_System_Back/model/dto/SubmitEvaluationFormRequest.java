package com.hrms.Human_Resource_Management_System_Back.model.dto;

import lombok.Data;

import java.util.List;

@Data
public class SubmitEvaluationFormRequest {
    private List<AnswerDto> answers;
}
