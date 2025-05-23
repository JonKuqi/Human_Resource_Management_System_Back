package com.hrms.Human_Resource_Management_System_Back.service.tenant;

import com.hrms.Human_Resource_Management_System_Back.model.tenant.Position;
import com.hrms.Human_Resource_Management_System_Back.repository.tenant.PositionRepository;
import com.hrms.Human_Resource_Management_System_Back.service.BaseService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * Service class responsible for handling business logic related to {@link Position} entities.
 * <p>
 * This class extends the generic {@link BaseService} to provide standard CRUD operations and
 * delegates data access to the {@link PositionRepository}.
 * </p>
 */
@Service
@AllArgsConstructor
public class PositionService extends BaseService<Position, Integer> {
    /**
     * The repository for performing database operations related to {@link Position} entities.
     */
    private final PositionRepository repo;

    /**
     * Returns the repository used by this service.
     *
     * @return the {@link PositionRepository} instance
     */
    @Override
    protected PositionRepository getRepository() {
        return repo;
    }
}
