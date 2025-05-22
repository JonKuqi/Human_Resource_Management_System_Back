package com.hrms.Human_Resource_Management_System_Back.model.tenant;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "performance_evaluation")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
public class PerformanceEvaluation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "performance_evaluation_id")
    private Integer performanceEvaluationId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "from_user_tenant_id", nullable = false)
    private UserTenant fromUserTenant;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "to_user_tenant_id", nullable = false)
    private UserTenant toUserTenant;

    @Column(name = "comment", columnDefinition = "TEXT", nullable = false)
    private String comment;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
}