package com.hrms.Human_Resource_Management_System_Back.service;

import com.hrms.Human_Resource_Management_System_Back.model.*;
import com.hrms.Human_Resource_Management_System_Back.repository.JobListingRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class JobListingService extends BaseService<JobListing, Integer> {

    private final JobListingRepository jobListingRepository;



    @Override
    protected JobListingRepository getRepository() {
        return jobListingRepository;
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