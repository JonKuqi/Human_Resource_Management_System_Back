package com.hrms.Human_Resource_Management_System_Back.model.tenant;

import com.hrms.Human_Resource_Management_System_Back.model.types.LeaveStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Represents a leave request entity in the system.
 * <p>
 * - leaveRequestId: The unique identifier for the leave request.
 * - userTenant: The user tenant associated with the leave request.
 * - leaveText: The text description of the leave request.
 * - startDate: The start date of the leave.
 * - endDate: The end date of the leave.
 * - status: The status of the leave request (e.g., PENDING, APPROVED, REJECTED).
 * - reason: The reason for the leave request.
 * - createdAt: The timestamp when the leave request was created.
 * </p>
 */

@Entity
@Table(name = "leave_request")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LeaveRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "leave_request_id")
    private Integer leaveRequestId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_tenant_id", nullable = false)
    private UserTenant userTenant;

    @Column(name = "leave_text", columnDefinition = "TEXT", nullable = false)
    private String leaveText;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    //@Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, columnDefinition = "leave_status default 'PENDING'")
    private String status;

    @Column(name = "reason", columnDefinition = "TEXT", nullable = false)
    private String reason;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
}