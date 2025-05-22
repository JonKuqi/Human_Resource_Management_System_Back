package com.hrms.Human_Resource_Management_System_Back.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Represents a tenant subscription entity in the public schema of the system.
 * <p>
 * - tenantSubscriptionId: The unique identifier for the tenant subscription.
 * - tenant: The tenant associated with the subscription.
 * - subscription: The subscription plan associated with the tenant.
 * - startDate: The start date of the subscription.
 * - endDate: The end date of the subscription.
 * - status: The status of the subscription (e.g., active, expired).
 * - createdAt: The date and time when the tenant subscription was created.
 * </p>
 */
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


    @Column(name = "status", nullable = false)
    private String status;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
}