package com.hrms.Human_Resource_Management_System_Back.controller.tenant;

import com.hrms.Human_Resource_Management_System_Back.controller.BaseController;
import com.hrms.Human_Resource_Management_System_Back.model.tenant.Payroll;
import com.hrms.Human_Resource_Management_System_Back.service.tenant.PayrollService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/payrolls")
@AllArgsConstructor
public class PayrollController extends BaseController<Payroll, Integer> {
    private final PayrollService svc;

    @Override
    protected PayrollService getService() {
        return svc;
    }
}
