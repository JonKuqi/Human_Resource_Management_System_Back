package com.hrms.Human_Resource_Management_System_Back.model.dto;

import lombok.Data;

@Data
public class CreateUserSkill {
    private String skillName;
    private String skillType;
    private int value;
}
