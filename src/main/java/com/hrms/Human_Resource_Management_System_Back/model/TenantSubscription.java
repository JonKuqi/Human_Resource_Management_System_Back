package com.hrms.Human_Resource_Management_System_Back.model;

import com.hrms.Human_Resource_Management_System_Back.model.types.SubscriptionStatus;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "tenant_subscription", schema = "public")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TenantSubscription {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tenant_subscription_id")
    private Integer tenantSubscriptionId;

    @ManyToOne
    @JoinColumn(name = "tenant_id", nullable = false)
    private Tenant tenant;

    @ManyToOne
    @JoinColumn(name = "subscription_id", nullable = false)
    private Subscription subscription;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, columnDefinition = "subscription_status default 'ACTIVE'")
    private SubscriptionStatus status;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
}