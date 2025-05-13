package com.hrms.Human_Resource_Management_System_Back.model.tenant;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "evaluation_form")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EvaluationForm {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    // Template-i mbi të cilin bazohet forma
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "template_id", nullable = false)
    private EvaluationTemplate template;

    // Kush po e vlerëson (from)
    @Column(name = "from_user_tenant_id", nullable = false)
    private Integer fromUserTenantId;

    // Kush po vlerësohet (to)
    @Column(name = "to_user_tenant_id", nullable = false)
    private Integer toUserTenantId;

    @Column(nullable = false)
    private String status; // e.g. PENDING, SUBMITTED

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "form", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<EvaluationAnswer> answers;
}
