package com.hrms.Human_Resource_Management_System_Back.model.tenant;

import jakarta.persistence.*;
import lombok.*;
import java.util.List;

@Entity
@Table(name = "evaluation_template")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EvaluationTemplate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String title;

    @OneToMany(mappedBy = "template", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<EvaluationQuestion> questions;
}
