package com.hrms.Human_Resource_Management_System_Back.service;

import com.hrms.Human_Resource_Management_System_Back.model.JobListing;
import com.hrms.Human_Resource_Management_System_Back.model.Tenant;
import com.hrms.Human_Resource_Management_System_Back.model.User;
import com.hrms.Human_Resource_Management_System_Back.model.UserGeneral;
import com.hrms.Human_Resource_Management_System_Back.model.dto.UserApplicationDto;
import com.hrms.Human_Resource_Management_System_Back.model.tenant.Application;
import com.hrms.Human_Resource_Management_System_Back.model.tenant.Document;
import com.hrms.Human_Resource_Management_System_Back.repository.JobListingRepository;
import com.hrms.Human_Resource_Management_System_Back.repository.UserApplicationRepository;
import com.hrms.Human_Resource_Management_System_Back.repository.UserGeneralRepository;
import com.hrms.Human_Resource_Management_System_Back.repository.UserRepository;
import com.hrms.Human_Resource_Management_System_Back.repository.tenant.ApplicationRepository;
import com.hrms.Human_Resource_Management_System_Back.repository.tenant.DocumentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

import static org.mockito.Mockito.*;

class UserApplicationServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserGeneralRepository userGeneralRepository;


    @Mock private JdbcTemplate jdbc;    // <- satisfies service dependency

    @Mock private UserApplicationRepository userApplicationRepository;
    @Mock
    private DocumentRepository documentRepository;

    @Mock
    private ApplicationRepository applicationRepository;

    @InjectMocks
    private UserApplicationService userApplicationService;

    @Mock
    private JobListingRepository jobListingRepository; // Add this mock

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        doNothing().when(jdbc).execute(anyString());
    }


    @Test
    void apply_shouldSaveApplication() throws IOException {
        // ── Arrange ──────────────────────────────────────────────
        UserApplicationDto dto = new UserApplicationDto();
        dto.setUserID(1);
        dto.setJobListingID(1);
        dto.setApplicantComment("Looking forward to this opportunity.");

        // user & profile
        User user = new User();            user.setUserId(1); user.setEmail("test@example.com");
        when(userRepository.getReferenceById(1)).thenReturn(user);

        UserGeneral ug = new UserGeneral(); ug.setFirstName("John"); ug.setLastName("Doe");
        when(userGeneralRepository.findByUser_Email("test@example.com"))
                .thenReturn(Optional.of(ug));

        // job-listing & tenant
        JobListing jl   = mock(JobListing.class);
        Tenant tnt  = mock(Tenant.class);
        when(jobListingRepository.getReferenceById(1)).thenReturn(jl);
        when(jl.getTenant()).thenReturn(tnt);
        when(tnt.getSchemaName()).thenReturn("tenant_schema");

        // **duplicate-check must be stubbed**
        when(userApplicationRepository.existsByUser_UserIdAndJobListing_JobListingId(1, 1))
                .thenReturn(false);

        // CV upload
        MultipartFile cv = mock(MultipartFile.class);
        when(cv.getBytes()).thenReturn("pdf bytes".getBytes());
        when(cv.getOriginalFilename()).thenReturn("cv.pdf");
        when(cv.getContentType()).thenReturn("application/pdf");

        when(documentRepository.save(any(Document.class))).thenReturn(new Document());

        // ── Act ──────────────────────────────────────────────────
        userApplicationService.apply(dto, cv);

        // ── Assert ───────────────────────────────────────────────
        verify(applicationRepository).save(any(Application.class));
        verify(documentRepository).save(any(Document.class));
    }
}
