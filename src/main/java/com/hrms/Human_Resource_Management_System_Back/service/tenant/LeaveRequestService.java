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

    @Override
    public LeaveRequest save(LeaveRequest leaveRequest) {
        boolean isNew = (leaveRequest.getLeaveRequestId() == null);
        LeaveRequest existing = null;

        if (!isNew) {
            existing = repo.findById(leaveRequest.getLeaveRequestId()).orElse(null);
        }

        if (isNew) {
            // Kontroll për konflikt
            Integer userId = leaveRequest.getUserTenant().getUserTenantId();

            boolean conflict = repo.existsByUserTenantUserTenantIdAndDateOverlap(
                    userId,
                    leaveRequest.getStartDate(),
                    leaveRequest.getEndDate()
            );

            if (conflict) {
                throw new IllegalArgumentException("Leave request conflicts with existing dates.");
            }

            // Vendos status në PENDING nëse nuk është dhënë
            if (leaveRequest.getStatus() == null || leaveRequest.getStatus().isBlank()) {
                leaveRequest.setStatus("PENDING");
            }

            // Vendos kohën e krijimit nëse nuk është dhënë
            if (leaveRequest.getCreatedAt() == null) {
                leaveRequest.setCreatedAt(LocalDateTime.now());
            }

            // Vendos validUntil në fund të pushimit
        }

        LeaveRequest saved = super.save(leaveRequest);

        // Logjika e notifikimeve për status
//        if (!isNew && existing != null && !leaveRequest.getStatus().equalsIgnoreCase(existing.getStatus())) {
//            if ("APPROVED".equalsIgnoreCase(leaveRequest.getStatus())) {
//                Notification notification = Notification.builder()
//                        .title("Leave Request Approved")
//                        .description("Your leave request from " + leaveRequest.getStartDate() + " to " + leaveRequest.getEndDate() + " was approved.")
//                        .userTenant(leaveRequest.getUserTenant())
//                        .createdAt(LocalDateTime.now())
//                        .expiresAt(LocalDateTime.now().plusDays(30))
//                        .build();
//                notificationService.save(notification);
//            } else if ("REJECTED".equalsIgnoreCase(leaveRequest.getStatus())) {
//                Notification notification = Notification.builder()
//                        .title("Leave Request Rejected")
//                        .description("Your leave request from " + leaveRequest.getStartDate() + " to " + leaveRequest.getEndDate() + " was rejected.")
//                        .userTenant(leaveRequest.getUserTenant())
//                        .createdAt(LocalDateTime.now())
//                        .expiresAt(LocalDateTime.now().plusDays(30))
//                        .build();
//                notificationService.save(notification);
//            }
//        }

        return saved;
    }



}
