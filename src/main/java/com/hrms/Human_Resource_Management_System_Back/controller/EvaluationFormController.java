package com.hrms.Human_Resource_Management_System_Back.controller;
import com.hrms.Human_Resource_Management_System_Back.service.BaseService;
import com.hrms.Human_Resource_Management_System_Back.service.tenant.EvaluationFormService;
import org.springframework.web.bind.annotation.*;
import com.hrms.Human_Resource_Management_System_Back.model.tenant.EvaluationForm;

@RestController
@RequestMapping("/api/v1/tenant/evaluation-forms")
public class EvaluationFormController extends BaseController<EvaluationForm, Integer> {

    private final EvaluationFormService service;

    public EvaluationFormController(EvaluationFormService service) {
        this.service = service;
    }

    @Override
    protected BaseService<EvaluationForm, Integer> getService() {
        return service;
    }

    /*
    @PostMapping("/initiate")
    public ResponseEntity<Void> initiateEvaluation(@RequestBody InitiateEvaluationRequest request) {
        service.initiateEvaluation(request);
        return ResponseEntity.ok().build();
    }
*/





}
