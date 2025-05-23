package com.hrms.Human_Resource_Management_System_Back.service.tenant;

import com.hrms.Human_Resource_Management_System_Back.model.tenant.Contract;
import com.hrms.Human_Resource_Management_System_Back.repository.tenant.ContractRepository;
import com.hrms.Human_Resource_Management_System_Back.service.BaseService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * Service class for handling business logic related to contracts within a tenant schema.
 * <p>
 * This service provides methods to manage {@link Contract} entities, including standard CRUD operations.
 * It extends {@link BaseService} to inherit generic service functionality for multi-tenant support.
 * </p>
 */
@Service
@AllArgsConstructor
public class ContractService extends BaseService<Contract, Integer> {

    /**
     * The repository responsible for performing database operations on contracts.
     */
    private final ContractRepository repo;

    /**
     * Returns the contract repository.
     * <p>
     * This method overrides the {@link BaseService#getRepository()} method to return the specific repository
     * for contract entities.
     * </p>
     *
     * @return the contract repository
     */
    @Override
    protected ContractRepository getRepository() {
        return repo;
    }
}
