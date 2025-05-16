package com.hrms.Human_Resource_Management_System_Back.service;


import com.hrms.Human_Resource_Management_System_Back.model.*;
import com.hrms.Human_Resource_Management_System_Back.model.dto.UserApplicationDto;
import com.hrms.Human_Resource_Management_System_Back.model.tenant.Application;
import com.hrms.Human_Resource_Management_System_Back.model.tenant.Document;
import com.hrms.Human_Resource_Management_System_Back.repository.*;
import com.hrms.Human_Resource_Management_System_Back.repository.tenant.ApplicationRepository;
import com.hrms.Human_Resource_Management_System_Back.repository.tenant.DocumentRepository;
import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;


@Service
@AllArgsConstructor
public class UserApplicationService extends BaseService<UserApplication,Integer>{

    private final UserApplicationRepository repository;
    private final DocumentRepository documentRepository;
    private final JobListingRepository jobListingRepository;
    private final JdbcTemplate jdbc;
    private final UserRepository userRepository;
    private final UserGeneralRepository userGeneralRepository;
    private final ApplicationRepository applicationRepository;


    @Override
    protected BaseRepository<UserApplication, Integer> getRepository() {
        return repository;
    }


    @Transactional
    public void apply(UserApplicationDto dto, MultipartFile cvFile) throws IOException {
        JobListing jobListing = jobListingRepository.getReferenceById(dto.getJobListingID());
        Tenant tenant = jobListing.getTenant();
        String tenantSchema = tenant.getSchemaName();

        jdbc.execute("SET search_path TO public");

        User user = userRepository.getReferenceById(dto.getUserID());
        UserGeneral userGeneral = userGeneralRepository.findByUser_Email(user.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        // ❗ Check if user already applied for this job
        boolean alreadyApplied = repository.existsByUser_UserIdAndJobListing_JobListingId(user.getUserId(), jobListing.getJobListingId());
        if (alreadyApplied) {
            throw new RuntimeException("You have already applied to this job.");
        }

        jdbc.execute("SET search_path TO \"" + tenantSchema + "\"");

        Document cv = Document.builder()
                .fileName(cvFile.getOriginalFilename())
                .contentType(cvFile.getContentType())
                .data(cvFile.getBytes())
                .build();
        Document savedDocument=documentRepository.save(cv);


        Application application = Application.builder()
                .jobListing(jobListing)
                .applicantName(userGeneral.getFirstName() + " " + userGeneral.getLastName())
                .applicantEmail(user.getEmail())
                .applicantGender(userGeneral.getGender())
                .applicantBirthDate(userGeneral.getBirthDate())
                .applicantPhone(userGeneral.getPhone())
                .experience(null)
                .applicantComment(dto.getApplicantComment())
                .cv(savedDocument)
                .status("PENDING")
                .timeOfApplication(LocalDateTime.now())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        applicationRepository.save(application);

        jdbc.execute("SET search_path TO public");

        UserApplication userApp = UserApplication.builder()
                .user(user)
                .jobListing(jobListing)
                .createdAt(LocalDateTime.now())
                .build();
        repository.save(userApp);
    }


}
