package com.hrms.Human_Resource_Management_System_Back.service.tenant;

import com.hrms.Human_Resource_Management_System_Back.model.tenant.PerformanceEvaluation;
import com.hrms.Human_Resource_Management_System_Back.repository.tenant.PerformanceEvalRepository;
import com.hrms.Human_Resource_Management_System_Back.service.BaseService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class PerformanceEvalService extends BaseService<PerformanceEvaluation, Integer> {
    private final PerformanceEvalRepository repo;

    @Override
    protected PerformanceEvalRepository getRepository() {
        return repo;
    }

}