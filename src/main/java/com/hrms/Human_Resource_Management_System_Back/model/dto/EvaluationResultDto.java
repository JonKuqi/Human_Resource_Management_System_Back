package com.hrms.Human_Resource_Management_System_Back.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class EvaluationResultDto {

    private Integer evaluatedUserId;
    private Integer completedForms;
    private Double averageRating;

    private List<QuestionAverageDto> questionAverages;
    private List<String> comments;
    private Integer suggestedRaisePercentage;
}
