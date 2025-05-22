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
 * - userTenant: The user tenant associated with the payroll.
 * - payPeriodStart: The start date of the pay period.
 * - payPeriodEnd: The end date of the pay period.
 * - baseSalary: The base salary for the employee during the pay period.
 * - bonuses: The bonuses given to the employee during the pay period.
 * - deductions: The deductions made from the employee's salary.
 * - netPay: The net pay after bonuses and deductions.
 * - paymentDate: The date the payroll was processed and paid.
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