package com.hrms.Human_Resource_Management_System_Back.controller;

import com.hrms.Human_Resource_Management_System_Back.model.Industry;
import com.hrms.Human_Resource_Management_System_Back.service.BaseService;
import com.hrms.Human_Resource_Management_System_Back.service.IndustryService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * Controller for managing industry entities.
 * <p>
 * This controller exposes public endpoints for CRUD operations on industries,
 * which represent sectors used across the system (e.g., during job posting).
 * It extends the {@link BaseController} to reuse generic CRUD functionality.
 * </p>
 */
@RestController
@RequestMapping("/api/v1/public/industry")
@AllArgsConstructor
public class IndustryController extends BaseController<Industry, Integer>{

    /**
     * Service responsible for handling business logic related to industries.
     */
    private final IndustryService service;
    /**
     * Overrides the base service getter to return the industry-specific service.
     *
     * @return the service used for managing industry entities
     */
    @Override
    protected BaseService<Industry, Integer> getService() {
        return service;
    }
}
