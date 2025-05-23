package com.hrms.Human_Resource_Management_System_Back.controller;

import com.hrms.Human_Resource_Management_System_Back.model.tenant.LeaveRequest;
import com.hrms.Human_Resource_Management_System_Back.service.BaseService;
import com.hrms.Human_Resource_Management_System_Back.service.tenant.LeaveRequestService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller for managing leave requests.
 * <p>
 * This controller exposes REST endpoints for creating, retrieving, updating, and deleting
 * leave requests for employees. It extends the {@link BaseController} to inherit generic
 * CRUD functionality and delegates business logic to the {@link LeaveRequestService}.
 * </p>
 * <p>
 * Endpoint path: <code>/api/v1/tenant/leave-request/</code>
 * </p>
 */
@RestController
@RequestMapping("/api/v1/tenant/leave-request/")
@AllArgsConstructor
public class LeaveRequestController extends BaseController<LeaveRequest, Integer> {

    /**
     * The service responsible for handling leave request operations.
     */
    private final LeaveRequestService svc;

    /**
     * Overrides {@link BaseController#getService()} to provide the specific leave request service.
     *
     * @return the leave request service
     */
    @Override
    protected BaseService<LeaveRequest, Integer> getService() {
        return svc;
    }

}
