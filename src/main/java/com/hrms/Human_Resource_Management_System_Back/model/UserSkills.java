package com.hrms.Human_Resource_Management_System_Back.model;


import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Table(name = "user_skills", schema = "public")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserSkills {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_skills_id")
    private Integer userSkillsId;

    @ManyToOne
    @JoinColumn(name = "user_general_id", nullable = false)
    private UserGeneral userGeneral;

    @ManyToOne
    @JoinColumn(name = "skill_id", nullable = false)
    private Skill skill;

    @Column(name = "value")
    private Integer value;

    @Column(name = "issued_at", nullable = false)
    private LocalDate issuedAt;

    @Column(name = "valid_until")
    private LocalDate validUntil;
}