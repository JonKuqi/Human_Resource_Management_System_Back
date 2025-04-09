package com.hrms.Human_Resource_Management_System_Back.service.tenant;

import com.hrms.Human_Resource_Management_System_Back.model.tenant.Department;
import com.hrms.Human_Resource_Management_System_Back.repository.tenant.DepartmentRepository;
import com.hrms.Human_Resource_Management_System_Back.service.BaseService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class DepartmentService extends BaseService<Department, Integer> {
    private final DepartmentRepository repo;

    @Override
    protected DepartmentRepository getRepository() {
        return repo;
    }
}
