package com.hrms.Human_Resource_Management_System_Back.service.tenant;

import com.hrms.Human_Resource_Management_System_Back.model.tenant.Position;
import com.hrms.Human_Resource_Management_System_Back.repository.tenant.PositionRepository;
import com.hrms.Human_Resource_Management_System_Back.service.BaseService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class PositionService extends BaseService<Position, Integer> {
    private final PositionRepository repo;

    @Override
    protected PositionRepository getRepository() {
        return repo;
    }
}
