package com.hrms.Human_Resource_Management_System_Back.model.tenant;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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

    // Lidhja me formën që po plotësohet
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "form_id", nullable = false)
    private EvaluationForm form;

    // Pyetja që po i jepet përgjigje
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id", nullable = false)
    private EvaluationQuestion question;

    // Nota nga 1 deri në 5
    @Column(nullable = false)
    private Integer rating;

    // Koment opsional
    @Column(length = 1000)
    private String comment;
}
