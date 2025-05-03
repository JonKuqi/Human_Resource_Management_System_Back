
package com.hrms.Human_Resource_Management_System_Back.controller;

import com.hrms.Human_Resource_Management_System_Back.model.tenant.Department;
import com.hrms.Human_Resource_Management_System_Back.service.tenant.DepartmentService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/departments")
@AllArgsConstructor
public class DepartmentController extends BaseController<Department, Integer> {
    private final DepartmentService svc;

    @Override
    protected DepartmentService getService() {
        return svc;
    }
}
