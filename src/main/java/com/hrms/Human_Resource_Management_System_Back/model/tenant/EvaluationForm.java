package com.hrms.Human_Resource_Management_System_Back.model.tenant;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "evaluation_form")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EvaluationForm {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "template_id", nullable = false)
    private EvaluationTemplate template;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "for_user_id", nullable = false)
    private UserTenant forUser;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "evaluator_id", nullable = false)
    private UserTenant evaluator;

    @Column(nullable = false, length = 20)
    private String status; // PENDING, COMPLETED, etj. — ruhet si tekst

    private LocalDateTime createdAt;
    private LocalDateTime submittedAt;
}
