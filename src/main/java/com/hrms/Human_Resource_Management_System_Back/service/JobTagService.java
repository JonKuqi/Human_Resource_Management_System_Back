package com.hrms.Human_Resource_Management_System_Back.service;

import com.hrms.Human_Resource_Management_System_Back.model.JobTag;
import com.hrms.Human_Resource_Management_System_Back.repository.JobTagRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * Service class for handling business logic related to job tags.
 * <p>
 * This class extends {@link BaseService} and provides methods for managing job tag entities,
 * which are used to categorize or label job listings based on skills, technologies, or other attributes.
 * </p>
 */
@Service
@AllArgsConstructor
public class JobTagService extends BaseService<JobTag, Integer> {
    /**
     * The repository responsible for performing database operations on job tags.
     */
    private final JobTagRepository jobTagRepository;
    /**
     * Returns the job tag repository.
     * <p>
     * This method overrides the {@link BaseService#getRepository()} method to return the specific repository
     * for job tag entities.
     * </p>
     *
     * @return the job tag repository
     */
    @Override
    protected JobTagRepository getRepository() {
        return jobTagRepository;
    }
}