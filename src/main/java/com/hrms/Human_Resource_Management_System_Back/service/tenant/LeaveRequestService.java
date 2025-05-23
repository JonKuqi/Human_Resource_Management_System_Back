package com.hrms.Human_Resource_Management_System_Back.service.tenant;

import com.hrms.Human_Resource_Management_System_Back.model.tenant.LeaveRequest;
import com.hrms.Human_Resource_Management_System_Back.repository.tenant.LeaveRequestRepository;
import com.hrms.Human_Resource_Management_System_Back.service.BaseService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * Service class for managing leave requests within a tenant.
 * <p>
 * This service handles business logic for creating and updating {@link LeaveRequest} entities.
 * It extends {@link BaseService} to reuse generic CRUD functionality and adds validations specific
 * to leave request operations, such as conflict detection and default value initialization.
 * </p>
 */
@Service
@AllArgsConstructor
public class LeaveRequestService extends BaseService<LeaveRequest, Integer> {

    /**
     * The repository responsible for performing database operations on leave requests.
     */
    private final LeaveRequestRepository repo;

    /**
     * Notification service for triggering alerts or follow-up actions (future implementation).
     */
    private final NotificationService notificationService;

    /**
     * Provides the specific repository implementation for leave requests.
     *
     * @return the {@link LeaveRequestRepository} instance
     */
    @Override
    protected LeaveRequestRepository getRepository() {
        return repo;
    }

    /**
     * Saves a leave request entity with additional business logic.
     * <p>
     * - Detects if the request is new and checks for date conflicts with existing leave entries.
     * - Sets default status to "PENDING" if not provided.
     * - Sets creation time if not already set.
     * </p>
     *
     * @param leaveRequest the {@link LeaveRequest} entity to be saved
     * @return the persisted {@link LeaveRequest}
     * @throws IllegalArgumentException if the leave request conflicts with existing dates
     */
    @Override
    public LeaveRequest save(LeaveRequest leaveRequest) {
        boolean isNew = (leaveRequest.getLeaveRequestId() == null);
        LeaveRequest existing = null;

        if (!isNew) {
            existing = repo.findById(leaveRequest.getLeaveRequestId()).orElse(null);
        }

        if (isNew) {
            Integer userId = leaveRequest.getUserTenant().getUserTenantId();

            boolean conflict = repo.existsByUserTenantUserTenantIdAndDateOverlap(
                    userId,
                    leaveRequest.getStartDate(),
                    leaveRequest.getEndDate()
            );

            if (conflict) {
                throw new IllegalArgumentException("Leave request conflicts with existing dates.");
            }

            if (leaveRequest.getStatus() == null || leaveRequest.getStatus().isBlank()) {
                leaveRequest.setStatus("PENDING");
            }

            if (leaveRequest.getCreatedAt() == null) {
                leaveRequest.setCreatedAt(LocalDateTime.now());
            }
        }

        LeaveRequest saved = super.save(leaveRequest);
        return saved;
    }
}
