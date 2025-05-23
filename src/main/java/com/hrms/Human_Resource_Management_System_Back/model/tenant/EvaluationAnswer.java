package com.hrms.Human_Resource_Management_System_Back.model.tenant;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents an answer provided for a specific evaluation question.
 * <p>
 * This entity links an evaluation form to a question and stores the respondent's answer,
 * including a numeric rating and an optional comment.
 * </p>
 */
@Entity
@Table(name = "evaluation_answer")
@Data
@Builder
@NoArgsConstructor
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
@AllArgsConstructor
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


    @Column(nullable = false)
    private Integer rating;


    @Column(length = 1000)
    private String comment;
}
