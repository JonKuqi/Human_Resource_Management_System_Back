package com.hrms.Human_Resource_Management_System_Back.service.tenant;

import com.hrms.Human_Resource_Management_System_Back.model.tenant.LeaveRequest;
import com.hrms.Human_Resource_Management_System_Back.model.tenant.UserTenant;
import com.hrms.Human_Resource_Management_System_Back.repository.tenant.LeaveRequestRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class LeaveRequestServiseTest {

    @Mock
    private LeaveRequestRepository leaveRequestRepository;

    @Mock
    private NotificationService notificationService;

    @InjectMocks
    private LeaveRequestService leaveRequestService;

    private LeaveRequest leaveRequest;
    private UserTenant userTenant;

    @BeforeEach
    void setUp() {
        userTenant = new UserTenant();
        userTenant.setUserTenantId(1);
        userTenant.setFirstName("Test"); // This matches the model field
        userTenant.setLastName("User"); // This matches the model field

        leaveRequest = new LeaveRequest();
        leaveRequest.setLeaveRequestId(1);
        leaveRequest.setUserTenant(userTenant);
        leaveRequest.setStartDate(LocalDate.now());
        leaveRequest.setEndDate(LocalDate.now().plusDays(5));
        leaveRequest.setStatus("PENDING");
        leaveRequest.setLeaveText("Taking time off"); // Required field
        leaveRequest.setReason("Personal reasons"); // Required field
        leaveRequest.setCreatedAt(LocalDateTime.now());
    }



    @Test
    void save_shouldSetStatusAndCreatedAt_whenNotProvided() {
        // Arrange
        LeaveRequest newRequest = new LeaveRequest();
        newRequest.setUserTenant(userTenant);
        newRequest.setStartDate(LocalDate.now().plusDays(1));
        newRequest.setEndDate(LocalDate.now().plusDays(3));
        newRequest.setLeaveText("New leave request"); // Required field
        newRequest.setReason("Need a break"); // Required field

        when(leaveRequestRepository.existsByUserTenantUserTenantIdAndDateOverlap(
            anyInt(), any(LocalDate.class), any(LocalDate.class))).thenReturn(false);
        when(leaveRequestRepository.save(any(LeaveRequest.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        LeaveRequest savedRequest = leaveRequestService.save(newRequest);

        // Assert
        assertEquals("PENDING", savedRequest.getStatus());
        assertNotNull(savedRequest.getCreatedAt());
    }

    @Test
    void save_shouldThrowException_whenDatesOverlap() {
        // Arrange
        LeaveRequest newRequest = new LeaveRequest();
        newRequest.setUserTenant(userTenant);
        newRequest.setStartDate(LocalDate.now().plusDays(1));
        newRequest.setEndDate(LocalDate.now().plusDays(3));
        newRequest.setLeaveText("Overlapping leave"); // Required field
        newRequest.setReason("Going on vacation"); // Required field

        when(leaveRequestRepository.existsByUserTenantUserTenantIdAndDateOverlap(
            anyInt(), any(LocalDate.class), any(LocalDate.class))).thenReturn(true);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> leaveRequestService.save(newRequest));

        // Verify that save was never called since exception was thrown
        verify(leaveRequestRepository, never()).save(any(LeaveRequest.class));
    }
}
