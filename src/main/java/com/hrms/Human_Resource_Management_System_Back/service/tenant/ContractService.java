package com.hrms.Human_Resource_Management_System_Back.service.tenant;

import com.hrms.Human_Resource_Management_System_Back.model.tenant.Contract;
import com.hrms.Human_Resource_Management_System_Back.repository.tenant.ContractRepository;
import com.hrms.Human_Resource_Management_System_Back.service.BaseService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ContractService extends BaseService<Contract, Integer> {
    private final ContractRepository repo;

    @Override
    protected ContractRepository getRepository() {
        return repo;
    }
}
