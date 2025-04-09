package com.hrms.Human_Resource_Management_System_Back.service.tenant;

import com.hrms.Human_Resource_Management_System_Back.model.tenant.Application;
import com.hrms.Human_Resource_Management_System_Back.repository.tenant.ApplicationRepository;
import com.hrms.Human_Resource_Management_System_Back.service.BaseService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ApplicationService extends BaseService<Application, Integer> {
    private final ApplicationRepository repo;

    @Override
    protected ApplicationRepository getRepository() {
        return repo;
    }
}
