package com.hrms.Human_Resource_Management_System_Back.controller;

import com.hrms.Human_Resource_Management_System_Back.model.tenant.LeaveRequest;
import com.hrms.Human_Resource_Management_System_Back.service.tenant.LeaveRequestService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/tenant/leave-request")
@AllArgsConstructor
public class LeaveRequestController extends BaseUserSpecificController<LeaveRequest, Integer> {
    private final LeaveRequestService svc;

    @Override
    protected LeaveRequestService getServiceSpecific() {
        return svc;
    }
}
