package com.hrms.Human_Resource_Management_System_Back.service.tenant;


import com.hrms.Human_Resource_Management_System_Back.middleware.TenantCtx;
import com.hrms.Human_Resource_Management_System_Back.model.JobListing;
import com.hrms.Human_Resource_Management_System_Back.model.JobTag;
import com.hrms.Human_Resource_Management_System_Back.model.Tenant;
import com.hrms.Human_Resource_Management_System_Back.model.dto.AddJobListingRequest;
import com.hrms.Human_Resource_Management_System_Back.repository.BaseRepository;
import com.hrms.Human_Resource_Management_System_Back.repository.JobTagRepository;
import com.hrms.Human_Resource_Management_System_Back.repository.TenantRepository;
import com.hrms.Human_Resource_Management_System_Back.repository.tenant.JobApplicationRepository;
import com.hrms.Human_Resource_Management_System_Back.service.BaseService;
import com.hrms.Human_Resource_Management_System_Back.service.BaseUserSpecificService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class JobApplicationService extends BaseService<JobListing, Integer> {

    private final JobApplicationRepository repository;
    private final TenantRepository tenantRepository;
    private final JobTagRepository jobTagRepository;

    @Override
    protected BaseRepository<JobListing, Integer> getRepository() {
        return repository;
    }


//
//    @Transactional
//    public JobListing addJobList(AddJobListingRequest request) {
//        String schema = TenantCtx.getTenant();
//
//        Tenant tenant = tenantRepository.findBySchemaName(schema)
//                .orElseThrow(() -> new IllegalArgumentException("Tenant not found"));
//
//
//        JobListing jobListing= JobListing.builder()
//                .tenant(tenant)
//                .jobTitle(request.getJobTitle())
//                .customIndustry(request.getCustomIndustry())
//                .location(request.getLocation())
//                .employmentType(String.valueOf(request.getEmploymentType()))
//                .description(request.getDescription())
//                .aboutUs(request.getAboutUs())
//                .salaryFrom(request.getSalaryFrom())
//                .salaryTo(request.getSalaryTo())
//                .validUntil(request.getValidUntil())
//                .createdAt(LocalDateTime.now())
//                .build();
//
//
//
//        JobListing savedJobListing = repository.save(jobListing);
//
//        if (request.getTags() != null) {
//            for (String tagName : request.getTags()) {
//                JobTag tag = JobTag.builder()
//                        .jobListing(jobListing)
//                        .tagName(tagName)
//                        .build();
//                jobTagRepository.save(tag);
//            }
//        }
//
//        return savedJobListing;
//    }
}
