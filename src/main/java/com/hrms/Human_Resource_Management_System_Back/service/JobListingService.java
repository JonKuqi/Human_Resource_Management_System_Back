package com.hrms.Human_Resource_Management_System_Back.service;

import com.hrms.Human_Resource_Management_System_Back.model.*;
import com.hrms.Human_Resource_Management_System_Back.repository.JobListingRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * Service class for handling business logic related to job listings.
 * <p>
 * This class extends {@link BaseService} and provides methods for managing job listing entities,
 * including operations for persisting, retrieving, updating, and deleting job listings.
 * </p>
 */
@Service
@AllArgsConstructor
public class JobListingService extends BaseService<JobListing, Integer> {
    /**
     * The repository responsible for performing database operations on job listings.
     */
    private final JobListingRepository jobListingRepository;

    /**
     * Returns the job listing repository.
     * <p>
     * This method overrides the {@link BaseService#getRepository()} method to return the specific repository
     * for job listing entities.
     * </p>
     *
     * @return the job listing repository
     */

    @Override
    protected JobListingRepository getRepository() {
        return jobListingRepository;
    }


}