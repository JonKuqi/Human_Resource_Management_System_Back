
package com.hrms.Human_Resource_Management_System_Back.controller;

import com.hrms.Human_Resource_Management_System_Back.model.tenant.Department;
import com.hrms.Human_Resource_Management_System_Back.service.tenant.DepartmentService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller for managing departments within a tenant context.
 * <p>
 * This controller provides CRUD operations for department entities.
 * All operations are tenant-specific and inherit logic from {@link BaseController}.
 * </p>
 */
@RestController
@RequestMapping("/api/v1/tenant/department")
@AllArgsConstructor
public class DepartmentController extends BaseController<Department, Integer> {
    /**
     * The service layer handling business logic for departments.
     */
    private final DepartmentService svc;

    /**
     * Overrides {@link BaseController#getService()} to provide the department-specific service.
     *
     * @return the department service instance
     */
    @Override
    protected DepartmentService getService() {
        return svc;
    }
}
