package com.hrms.Human_Resource_Management_System_Back.controller;
import com.hrms.Human_Resource_Management_System_Back.service.BaseService;
import com.hrms.Human_Resource_Management_System_Back.service.tenant.EvaluationFormService;
import org.springframework.web.bind.annotation.*;
import com.hrms.Human_Resource_Management_System_Back.model.tenant.EvaluationForm;

/**
 * Controller for managing evaluation forms within a tenant context.
 * <p>
 * Provides CRUD operations for {@link EvaluationForm} entities.
 * Inherits base functionality from {@link BaseController}.
 * </p>
 */
@RestController
@RequestMapping("/api/v1/tenant/evaluation-forms")
public class EvaluationFormController extends BaseController<EvaluationForm, Integer> {

    /**
     * Service responsible for handling business logic related to evaluation forms.
     */
    private final EvaluationFormService service;

    /**
     * Constructs an {@code EvaluationFormController} with the given service.
     *
     * @param service the evaluation form service
     */
    public EvaluationFormController(EvaluationFormService service) {
        this.service = service;
    }

    /**
     * Overrides {@link BaseController#getService()} to provide the evaluation form service.
     *
     * @return the evaluation form service
     */
    @Override
    protected BaseService<EvaluationForm, Integer> getService() {
        return service;
    }


}
