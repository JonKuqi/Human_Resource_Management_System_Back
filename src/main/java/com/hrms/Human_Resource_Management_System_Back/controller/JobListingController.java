package com.hrms.Human_Resource_Management_System_Back.controller;


import com.hrms.Human_Resource_Management_System_Back.model.JobListing;
import com.hrms.Human_Resource_Management_System_Back.service.JobListingService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;


/**
 * Controller for managing job listings.
 * <p>
 * This controller handles public endpoints related to job listings,
 * such as retrieving available positions or posting a new one.
 * It extends {@link BaseController} to inherit standard CRUD operations for job listings.
 * </p>
 */
@RestController
@RequestMapping("/api/v1/public/job-listing")
@AllArgsConstructor
public class JobListingController extends BaseController<JobListing, Integer> {
    /**
     * Service responsible for handling job listing business logic.
     */
    private final JobListingService jobListingService;
    /**
     * Overrides the base service getter to return the job listing-specific service.
     *
     * @return the service used for managing job listing entities
     */
    @Override
    protected JobListingService getService() {
        return jobListingService;
    }




}