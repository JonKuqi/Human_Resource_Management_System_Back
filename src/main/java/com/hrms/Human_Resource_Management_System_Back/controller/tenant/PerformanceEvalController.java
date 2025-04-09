package com.hrms.Human_Resource_Management_System_Back.controller.tenant;

import com.hrms.Human_Resource_Management_System_Back.controller.BaseController;
import com.hrms.Human_Resource_Management_System_Back.model.tenant.PerformanceEvaluation;
import com.hrms.Human_Resource_Management_System_Back.service.tenant.PerformanceEvalService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/performance-evals")
@AllArgsConstructor
public class PerformanceEvalController extends BaseController<PerformanceEvaluation, Integer> {
    private final PerformanceEvalService svc;

    @Override
    protected PerformanceEvalService getService() {
        return svc;
    }
}
