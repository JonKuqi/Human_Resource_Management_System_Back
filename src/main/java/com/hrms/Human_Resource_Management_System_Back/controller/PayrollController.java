package com.hrms.Human_Resource_Management_System_Back.controller;

import com.hrms.Human_Resource_Management_System_Back.model.tenant.Payroll;
import com.hrms.Human_Resource_Management_System_Back.service.BaseService;
import com.hrms.Human_Resource_Management_System_Back.service.tenant.PayrollService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller for managing payroll records within a tenant.
 * <p>
 * This controller exposes REST endpoints for performing CRUD operations on {@link Payroll} entities.
 * It extends {@link BaseController} to inherit standard functionality and delegates business logic
 * to the {@link PayrollService}.
 * </p>
 * <p>
 * Endpoint path: <code>/api/v1/tenant/payroll</code>
 * </p>
 */
@RestController
@RequestMapping("/api/v1/tenant/payroll")
@AllArgsConstructor
public class PayrollController extends BaseController<Payroll, Integer> {

    /**
     * The service responsible for handling payroll-related operations.
     */
    private final PayrollService svc;

    /**
     * Provides the specific service used by this controller for payroll operations.
     *
     * @return the {@link PayrollService} implementation
     */
    @Override
    protected BaseService<Payroll, Integer> getService() {
        return svc;
    }
}
