package com.hrms.Human_Resource_Management_System_Back.controller;

import com.hrms.Human_Resource_Management_System_Back.model.tenant.Position;
import com.hrms.Human_Resource_Management_System_Back.service.tenant.PositionService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller for managing job positions within a tenant.
 * <p>
 * This controller provides tenant-specific endpoints for performing CRUD operations
 * on {@link Position} entities. It inherits generic behavior from {@link BaseController}.
 * </p>
 */
@RestController
@RequestMapping("/api/v1/tenant/position")
@AllArgsConstructor
public class PositionController extends BaseController<Position, Integer> {
    /**
     * The service responsible for business logic related to job positions.
     */
    private final PositionService svc;

    /**
     * Returns the service used to manage {@link Position} entities.
     * Overrides the method in {@link BaseController}.
     *
     * @return the position service
     */
    @Override
    protected PositionService getService() {
        return svc;
    }
}

