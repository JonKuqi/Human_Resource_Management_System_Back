package com.hrms.Human_Resource_Management_System_Back.model.tenant;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Represents a contract entity in the system.
 * <p>
 * - contractId: The unique identifier for the contract.
 * - userTenant: The user tenant associated with the contract.
 * - position: The position held by the user tenant under the contract.
 * - contractType: The type of the contract (e.g., full-time, part-time).
 * - startDate: The start date of the contract.
 * - endDate: The end date of the contract.
 * - salary: The salary for the contract, with precision and scale for decimal values.
 * - terms: The terms of the contract.
 * - createdAt: The timestamp when the contract was created.
 * </p>
 */

@Entity
@Table(name = "contract")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Contract {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "contract_id")
    private Integer contractId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_tenant_id", nullable = false)
    private UserTenant userTenant;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "position_id", nullable = false)
    private Position position;

    @Column(name = "contract_type", nullable = false, length = 50)
    private String contractType;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    @Column(name = "salary", nullable = false, precision = 12, scale = 2)
    private BigDecimal salary;

    @Column(name = "terms", columnDefinition = "TEXT", nullable = false)
    private String terms;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
}