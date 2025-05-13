package com.hrms.Human_Resource_Management_System_Back.model.tenant;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "evaluation_question")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EvaluationQuestion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, length = 500)
    private String questionText;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "template_id", nullable = false)
    private EvaluationTemplate template;
}
