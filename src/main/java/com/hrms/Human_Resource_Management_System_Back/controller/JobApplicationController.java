package com.hrms.Human_Resource_Management_System_Back.controller;


import com.hrms.Human_Resource_Management_System_Back.model.JobListing;
import com.hrms.Human_Resource_Management_System_Back.model.dto.AddJobListingRequest;
import com.hrms.Human_Resource_Management_System_Back.service.BaseService;
import com.hrms.Human_Resource_Management_System_Back.service.tenant.JobApplicationService;
import com.hrms.Human_Resource_Management_System_Back.service.tenant.UserTenantService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/tenant/job-application")
@AllArgsConstructor
public class JobApplicationController extends BaseController<JobListing, Integer>{


    private final JobApplicationService service;


    @Override
    protected BaseService<JobListing, Integer> getService() {
        return service;
    }


//    @Operation(
//            summary = "Create a new job listing",
//            description = "Allows HR to post a new job that applicants can view and apply for."
//    ) @SecurityRequirement(name = "bearerAuth")
//    @PostMapping("/addJobList")
//    public ResponseEntity<JobListing> addJobList(@RequestBody AddJobListingRequest request) {
//        JobListing jobListing = jobListingService.addJobList(request);
//        return ResponseEntity.ok(jobListing);
//    }
}
