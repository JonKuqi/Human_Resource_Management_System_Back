package com.hrms.Human_Resource_Management_System_Back.service;

import com.hrms.Human_Resource_Management_System_Back.model.JobListing;
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
}