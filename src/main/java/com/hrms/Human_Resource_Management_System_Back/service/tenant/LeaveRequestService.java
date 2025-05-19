package com.hrms.Human_Resource_Management_System_Back.service.tenant;

import com.hrms.Human_Resource_Management_System_Back.model.tenant.LeaveRequest;
import com.hrms.Human_Resource_Management_System_Back.model.tenant.Notification;
import com.hrms.Human_Resource_Management_System_Back.repository.tenant.LeaveRequestRepository;
import com.hrms.Human_Resource_Management_System_Back.service.BaseService;
import com.hrms.Human_Resource_Management_System_Back.service.BaseUserSpecificService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class LeaveRequestService extends BaseService<LeaveRequest, Integer> {
    private final LeaveRequestRepository repo;

    @Override
    protected LeaveRequestRepository getRepository() {
        return repo;
    }
    private final NotificationService notificationService;

    @Transactional
    public void approveLeaveRequest(Integer id) {
        LeaveRequest leave = getRepository().findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Leave request not found with id: " + id));

        if (!"PENDING".equalsIgnoreCase(leave.getStatus())) {
            throw new IllegalStateException("Leave request is not pending.");
        }

        leave.setStatus("APPROVED");
        getRepository().save(leave);
        Notification notification = Notification.builder()
                .title("Leave Request Approved")
                .description("Your leave request from " + leave.getStartDate() + " to " + leave.getEndDate() + " was approved.")
                .userTenant(leave.getUserTenant())
                .createdAt(LocalDateTime.now())
                .expiresAt(LocalDateTime.now().plusDays(30))
                .build();

        notificationService.save(notification);
    }


    @Transactional
    public void rejectLeaveRequest(Integer id) {
        LeaveRequest leave = getRepository().findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Leave request not found with id: " + id));

        if (!"PENDING".equalsIgnoreCase(leave.getStatus())) {
            throw new IllegalStateException("Leave request is not pending.");
        }

        leave.setStatus("REJECTED");
        getRepository().save(leave);
        Notification notification = Notification.builder()
                .title("Leave Request Rejected")
                .description("Your leave request from " + leave.getStartDate() + " to " + leave.getEndDate() + " was rejected.")
                .userTenant(leave.getUserTenant())
                .createdAt(LocalDateTime.now())
                .expiresAt(LocalDateTime.now().plusDays(30))
                .build();

        notificationService.save(notification);
    }


    @Override
    public LeaveRequest save(LeaveRequest leaveRequest) {
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

        return super.save(leaveRequest);
    }



}
