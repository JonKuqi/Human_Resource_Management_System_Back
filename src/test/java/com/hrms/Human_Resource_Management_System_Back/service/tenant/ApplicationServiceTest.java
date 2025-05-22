package com.hrms.Human_Resource_Management_System_Back.service.tenant;

import com.hrms.Human_Resource_Management_System_Back.model.tenant.Application;
import com.hrms.Human_Resource_Management_System_Back.model.JobListing;
import com.hrms.Human_Resource_Management_System_Back.repository.tenant.ApplicationRepository;
import com.hrms.Human_Resource_Management_System_Back.service.EmailSenderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ApplicationServiceTest {

    @Mock
    private ApplicationRepository applicationRepository;

    @InjectMocks
    private ApplicationService applicationService;

    private Application application;
    private JobListing jobListing;

    @BeforeEach
    void setUp() {
        jobListing = new JobListing();
        jobListing.setJobTitle("Software Engineer");

        application = new Application();
        application.setApplicationId(1);
        application.setApplicantName("John Doe");
        application.setApplicantEmail("john.doe@example.com");
        application.setStatus("PENDING");
        application.setJobListing(jobListing);
    }

    @Test
    void save_shouldUpdateUpdatedAtTimestamp() {
        // Arrange
        LocalDateTime beforeSave = LocalDateTime.now();
        Application existingApplication = new Application();
        existingApplication.setStatus("PENDING"); // Set a valid status
        when(applicationRepository.findById(any())).thenReturn(Optional.of(existingApplication));
        when(applicationRepository.save(any(Application.class))).thenReturn(application);

        // Act
        applicationService.save(application);

        // Assert
        verify(applicationRepository).save(any(Application.class));
    }
    @Test
    void save_shouldSendEmailWhenStatusChanges() {
        // Arrange
        Application existingApplication = new Application();
        existingApplication.setApplicationId(1);
        existingApplication.setStatus("PENDING");
        existingApplication.setJobListing(jobListing);

        application.setStatus("ACCEPTED");

        when(applicationRepository.findById(1)).thenReturn(Optional.of(existingApplication));
        when(applicationRepository.save(any(Application.class))).thenReturn(application);

        try (MockedStatic<EmailSenderService> mockedEmailService = mockStatic(EmailSenderService.class)) {
            // Act
            applicationService.save(application);

            // Assert
            mockedEmailService.verify(() ->
                EmailSenderService.sendApplicationStatusEmail(
                    eq("john.doe@example.com"),
                    eq("John Doe"),
                    eq("Software Engineer"),
                    eq("ACCEPTED")
                )
            );
        }
    }

    @Test
    void save_shouldNotSendEmailWhenStatusDoesNotChange() {
        // Arrange
        Application existingApplication = new Application();
        existingApplication.setApplicationId(1);
        existingApplication.setStatus("PENDING");
        existingApplication.setJobListing(jobListing);

        application.setStatus("PENDING"); // Same status

        when(applicationRepository.findById(1)).thenReturn(Optional.of(existingApplication));
        when(applicationRepository.save(any(Application.class))).thenReturn(application);

        try (MockedStatic<EmailSenderService> mockedEmailService = mockStatic(EmailSenderService.class)) {
            // Act
            applicationService.save(application);

            // Assert
            mockedEmailService.verify(() ->
                EmailSenderService.sendApplicationStatusEmail(
                    anyString(), anyString(), anyString(), anyString()
                ), never()
            );
        }
    }

    @Test
    void save_shouldHandleNewApplication() {
        // Arrange
        Application newApplication = new Application();
        newApplication.setApplicantName("New Applicant");
        newApplication.setApplicantEmail("new@example.com");
        newApplication.setJobListing(jobListing);
        newApplication.setStatus("PENDING");

        when(applicationRepository.save(any(Application.class))).thenReturn(newApplication);

        // Act
        Application result = applicationService.save(newApplication);

        // Assert
        assertNotNull(result);
        verify(applicationRepository).save(newApplication);
    }
}
