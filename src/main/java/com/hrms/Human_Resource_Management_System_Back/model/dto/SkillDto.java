package com.hrms.Human_Resource_Management_System_Back.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data transfer object (DTO) for representing skill information.
 * <p>
 * - skillId: The unique identifier of the skill.
 * - name: The name of the skill (e.g., Java, React).
 * - type: The category or type of the skill (e.g., Technical, Soft).
 * </p>
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SkillDto {

    private Integer skillId;
    private String name;
    private String type;

    /**
     * Custom constructor for creating a skill with ID and name only.
     *
     * @param i     the skill ID
     * @param java  the skill name
     */
    public SkillDto(int i, String java) {
        this.skillId = i;
        this.name = java;
    }
}
