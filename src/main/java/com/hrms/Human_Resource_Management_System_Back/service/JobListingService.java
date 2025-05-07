package com.hrms.Human_Resource_Management_System_Back.service;

import com.hrms.Human_Resource_Management_System_Back.middleware.TenantCtx;
import com.hrms.Human_Resource_Management_System_Back.model.*;
import com.hrms.Human_Resource_Management_System_Back.model.dto.AddJobListingRequest;
import com.hrms.Human_Resource_Management_System_Back.model.types.RoleUser;
import com.hrms.Human_Resource_Management_System_Back.repository.JobListingRepository;
import com.hrms.Human_Resource_Management_System_Back.repository.JobTagRepository;
import com.hrms.Human_Resource_Management_System_Back.repository.TenantRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class JobListingService extends BaseService<JobListing, Integer> {

    private final JobListingRepository jobListingRepository;
    private final TenantRepository tenantRepository;
    private final JobTagRepository jobTagRepository;


    @Override
    protected JobListingRepository getRepository() {
        return jobListingRepository;
    }



    @Transactional
    public JobListing addJobList(AddJobListingRequest request) {
        String schema = TenantCtx.getTenant();

        Tenant tenant = tenantRepository.findBySchemaName(schema)
                .orElseThrow(() -> new IllegalArgumentException("Tenant not found"));


        JobListing jobListing= JobListing.builder()
                .tenant(tenant)
                .jobTitle(request.getJobTitle())
                .customIndustry(request.getCustomIndustry())
                .location(request.getLocation())
                .employmentType(String.valueOf(request.getEmploymentType()))
                .description(request.getDescription())
                .aboutUs(request.getAboutUs())
                .salaryFrom(request.getSalaryFrom())
                .salaryTo(request.getSalaryTo())
                .validUntil(request.getValidUntil())
                .createdAt(LocalDateTime.now())
                .build();



        JobListing savedJobListing = jobListingRepository.save(jobListing);

        if (request.getTags() != null) {
            for (String tagName : request.getTags()) {
                JobTag tag = JobTag.builder()
                        .jobListing(jobListing)
                        .tagName(tagName)
                        .build();
                jobTagRepository.save(tag);
            }
        }

        return savedJobListing;
    }
}