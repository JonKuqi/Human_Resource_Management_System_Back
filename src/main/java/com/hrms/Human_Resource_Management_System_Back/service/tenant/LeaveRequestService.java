package com.hrms.Human_Resource_Management_System_Back.service.tenant;

import com.hrms.Human_Resource_Management_System_Back.model.tenant.LeaveRequest;
import com.hrms.Human_Resource_Management_System_Back.repository.tenant.LeaveRequestRepository;
import com.hrms.Human_Resource_Management_System_Back.service.BaseUserSpecificService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class LeaveRequestService extends BaseUserSpecificService<LeaveRequest, Integer> {
    private final LeaveRequestRepository repo;

    @Override
    protected LeaveRequestRepository getRepository() {
        return repo;
    }
}
