package com.hrms.Human_Resource_Management_System_Back.controller;

import com.hrms.Human_Resource_Management_System_Back.model.JobTag;
import com.hrms.Human_Resource_Management_System_Back.service.BaseService;
import com.hrms.Human_Resource_Management_System_Back.service.JobTagService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller for managing job tags.
 * <p>
 * This controller exposes public endpoints for managing job tags, which are used to categorize
 * job listings by skills, technologies, or other relevant labels. It extends the {@link BaseController}
 * to reuse generic CRUD operations.
 * </p>
 */
@RestController
@RequestMapping("/api/v1/public/job-tag")
@AllArgsConstructor
public class JobTagController extends BaseController<JobTag, Integer>{
    /**
     * Service responsible for handling job tag-related business logic.
     */
    private final JobTagService service;
    /**
     * Overrides the base service getter to return the job tag-specific service.
     *
     * @return the service used for managing job tag entities
     */
    @Override
    protected BaseService<JobTag, Integer> getService() {
        return service;
    }
}
