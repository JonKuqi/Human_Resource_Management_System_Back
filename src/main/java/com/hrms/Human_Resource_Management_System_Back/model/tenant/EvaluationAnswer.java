package com.hrms.Human_Resource_Management_System_Back.model.tenant;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "evaluation_answer")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EvaluationAnswer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "form_id", nullable = false)
    private EvaluationForm form;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id", nullable = false)
    private EvaluationQuestion question;

    @Column(name = "rating")
    private Integer rating;

    @Column(name = "text_response", columnDefinition = "TEXT")
    private String textResponse;
}
