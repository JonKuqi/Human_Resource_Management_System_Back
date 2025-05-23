package com.hrms.Human_Resource_Management_System_Back.service.tenant;

import com.hrms.Human_Resource_Management_System_Back.model.tenant.Payroll;
import com.hrms.Human_Resource_Management_System_Back.repository.tenant.PayrollRepository;
import com.hrms.Human_Resource_Management_System_Back.service.BaseService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * Service class for managing payroll data within a tenant context.
 * <p>
 * This service handles business logic for {@link Payroll} entities and delegates
 * data access to the {@link PayrollRepository}. It extends {@link BaseService}
 * to inherit standard CRUD operations with optional RBAC extensions.
 * </p>
 */
@Service
@AllArgsConstructor
public class PayrollService extends BaseService<Payroll, Integer> {

    /**
     * The repository responsible for database operations related to payroll.
     */
    private final PayrollRepository repo;

    /**
     * Provides the repository instance used for managing payroll entities.
     *
     * @return the {@link PayrollRepository} implementation
     */
    @Override
    protected PayrollRepository getRepository() {
        return repo;
    }
}
