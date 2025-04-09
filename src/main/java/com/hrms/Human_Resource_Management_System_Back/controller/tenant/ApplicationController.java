package com.hrms.Human_Resource_Management_System_Back.controller.tenant;

import com.hrms.Human_Resource_Management_System_Back.controller.BaseController;
import com.hrms.Human_Resource_Management_System_Back.model.tenant.Application;
import com.hrms.Human_Resource_Management_System_Back.service.tenant.ApplicationService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/applications")
@AllArgsConstructor
public class ApplicationController extends BaseController<Application, Integer> {
    private final ApplicationService svc;

    @Override
    protected ApplicationService getService() {
        return svc;
    }
}
