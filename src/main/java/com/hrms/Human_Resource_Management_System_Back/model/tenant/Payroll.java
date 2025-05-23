package com.hrms.Human_Resource_Management_System_Back.model.tenant;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Represents a payroll entity in the system.
 * <p>
 * - payrollId: The unique identifier for the payroll.
 * - userTenant: The user tenant (employee) associated with this payroll.
 * - payPeriodStart: The start date of the pay period.
 * - payPeriodEnd: The end date of the pay period.
 * - baseSalary: The gross salary allocated to the employee.
 * - bonuses: Additional bonus amount given during the pay period.
 * - deductions: Amount deducted from the base salary (e.g., taxes, penalties).
 * - netPay: Final net amount paid after applying bonuses and deductions.
 * - paymentDate: The date on which the payment was processed.
 * - createdAt: The timestamp when the payroll record was created.
 * </p>
 */
@Entity
@Table(name = "payroll")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
public class Payroll {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "payroll_id")
    private Integer payrollId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_tenant_id", nullable = false)
    private UserTenant userTenant;

    @Column(name = "pay_period_start", nullable = false)
    private LocalDate payPeriodStart;

    @Column(name = "pay_period_end", nullable = false)
    private LocalDate payPeriodEnd;

    @Column(name = "base_salary", nullable = false, precision = 12, scale = 2)
    private BigDecimal baseSalary;

    @Column(name = "bonuses", nullable = false, precision = 12, scale = 2)
    private BigDecimal bonuses;

    @Column(name = "deductions", nullable = false, precision = 12, scale = 2)
    private BigDecimal deductions;

    @Column(name = "net_pay", nullable = false, precision = 12, scale = 2)
    private BigDecimal netPay;

    @Column(name = "payment_date", nullable = false)
    private LocalDate paymentDate;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
}
