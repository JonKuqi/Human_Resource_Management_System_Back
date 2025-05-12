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



    @Override
    protected JobListingRepository getRepository() {
        return jobListingRepository;
    }



}