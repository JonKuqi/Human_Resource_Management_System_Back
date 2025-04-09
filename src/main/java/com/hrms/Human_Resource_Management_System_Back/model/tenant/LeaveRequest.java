package com.hrms.Human_Resource_Management_System_Back.model.tenant;

import com.hrms.Human_Resource_Management_System_Back.model.types.LeaveStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

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

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, columnDefinition = "leave_status default 'PENDING'")
    private LeaveStatus status;

    @Column(name = "reason", columnDefinition = "TEXT", nullable = false)
    private String reason;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
}