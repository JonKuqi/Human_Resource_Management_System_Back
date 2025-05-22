package com.hrms.Human_Resource_Management_System_Back.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SkillDto {
    private Integer skillId;
    private String name;
    private String type;

    public SkillDto(int i, String java) {
        this.skillId = i;
        this.name = java;
    }
}