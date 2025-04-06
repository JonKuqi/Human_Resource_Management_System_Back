package com.hrms.Human_Resource_Management_System_Back.controller;


import com.hrms.Human_Resource_Management_System_Back.model.JobListing;
import com.hrms.Human_Resource_Management_System_Back.service.JobListingService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/job-listings")
@AllArgsConstructor
public class JobListingController extends BaseController<JobListing, Integer> {

    private final JobListingService jobListingService;

    @Override
    protected JobListingService getService() {
        return jobListingService;
    }
}