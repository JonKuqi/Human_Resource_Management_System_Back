package com.hrms.Human_Resource_Management_System_Back.service.tenant;

import com.hrms.Human_Resource_Management_System_Back.model.tenant.Payroll;
import com.hrms.Human_Resource_Management_System_Back.repository.tenant.PayrollRepository;
import com.hrms.Human_Resource_Management_System_Back.service.BaseService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class PayrollService extends BaseService<Payroll, Integer> {
    private final PayrollRepository repo;

    @Override
    protected PayrollRepository getRepository() {
        return repo;
    }
}
