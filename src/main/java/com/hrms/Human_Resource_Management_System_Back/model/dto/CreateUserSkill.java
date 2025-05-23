package com.hrms.Human_Resource_Management_System_Back.model.dto;

import lombok.Data;

/**
 * Data transfer object (DTO) for creating a new user skill.
 * <p>
 * - skillName: The name of the skill (e.g., Java, SQL).
 * - skillType: The type/category of the skill (e.g., Technical, Soft).
 * - value: The proficiency level or rating of the skill (e.g., 1–10 scale).
 * </p>
 */
@Data
public class CreateUserSkill {
    private String skillName;
    private String skillType;
    private int value;
}
