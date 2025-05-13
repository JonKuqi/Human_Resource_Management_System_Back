package com.hrms.Human_Resource_Management_System_Back.controller;

import com.hrms.Human_Resource_Management_System_Back.model.tenant.EvaluationTemplate;
import com.hrms.Human_Resource_Management_System_Back.service.BaseService;
import com.hrms.Human_Resource_Management_System_Back.service.tenant.EvaluationTemplateService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/tenant/evaluation-templates")
public class EvaluationTemplateController extends BaseController<EvaluationTemplate, Integer> {

    private final EvaluationTemplateService service;

    public EvaluationTemplateController(EvaluationTemplateService service) {
        this.service = service;
    }

    @Override
    protected BaseService<EvaluationTemplate, Integer> getService() {
        return service;
    }
}
