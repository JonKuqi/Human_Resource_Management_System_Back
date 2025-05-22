package com.hrms.Human_Resource_Management_System_Back.model;


import jakarta.persistence.*;
import lombok.*;

/**
 * Represents a skill entity in the public schema of the system.
 * <p>
 * - skillId: The unique identifier for the skill.
 * - type: The type of skill (e.g., technical, soft).
 * - name: The name of the skill.
 * </p>
 */
@Entity
@Table(name = "skill", schema = "public")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Skill {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "skill_id")
    private Integer skillId;

    @Column(name = "type", nullable = false, length = 50)
    private String type;

    @Column(name = "name", nullable = false, unique = true, length = 100)
    private String name;
}