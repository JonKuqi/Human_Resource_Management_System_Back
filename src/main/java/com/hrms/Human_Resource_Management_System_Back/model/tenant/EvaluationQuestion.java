package com.hrms.Human_Resource_Management_System_Back.model.tenant;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "evaluation_question")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EvaluationQuestion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "template_id", nullable = false)
    private EvaluationTemplate template;

    @Column(name = "question_text", nullable = false, columnDefinition = "TEXT")
    private String questionText;

    @Column(nullable = false)
    private Boolean comment;
}
