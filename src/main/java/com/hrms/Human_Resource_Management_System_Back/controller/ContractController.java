package com.hrms.Human_Resource_Management_System_Back.controller;

import com.hrms.Human_Resource_Management_System_Back.model.tenant.Contract;
import com.hrms.Human_Resource_Management_System_Back.service.BaseService;
import com.hrms.Human_Resource_Management_System_Back.service.tenant.ContractService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller for managing employee contracts.
 * <p>
 * This controller provides endpoints for handling contract-related operations in the HRMS system.
 * It extends the {@link BaseController} to inherit generic CRUD functionality for {@link Contract} entities.
 * The operations are scoped per tenant as part of a multi-tenant architecture.
 * </p>
 * <p>
 * Endpoint path: <code>/api/v1/tenant/contracts</code>
 * </p>
 */
@RestController
@RequestMapping("/api/v1/tenant/contracts")
@AllArgsConstructor
public class ContractController extends BaseController<Contract, Integer> {

    /**
     * The service responsible for handling contract-related business logic.
     */
    private final ContractService svc;

    /**
     * Overrides {@link BaseController#getService()} to return the contract service.
     *
     * @return the contract service
     */
    @Override
    protected BaseService<Contract, Integer> getService() {
        return svc;
    }
}
