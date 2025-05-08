package com.hrms.Human_Resource_Management_System_Back.controller;

import com.hrms.Human_Resource_Management_System_Back.model.tenant.Payroll;
import com.hrms.Human_Resource_Management_System_Back.service.tenant.PayrollService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/tenant/payroll")
@AllArgsConstructor
public class PayrollController extends BaseUserSpecificController<Payroll, Integer> {
    private final PayrollService svc;

    @Override
    protected PayrollService getServiceSpecific() {
        return svc;
    }
}
