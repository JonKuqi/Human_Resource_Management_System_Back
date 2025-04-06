package com.hrms.Human_Resource_Management_System_Back.service;

import com.hrms.Human_Resource_Management_System_Back.model.JobTag;
import com.hrms.Human_Resource_Management_System_Back.repository.JobTagRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class JobTagService extends BaseService<JobTag, Integer> {

    private final JobTagRepository jobTagRepository;

    @Override
    protected JobTagRepository getRepository() {
        return jobTagRepository;
    }
}