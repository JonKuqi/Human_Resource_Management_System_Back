package com.hrms.Human_Resource_Management_System_Back.repository.tenant;

import com.hrms.Human_Resource_Management_System_Back.model.tenant.LeaveRequest;
import com.hrms.Human_Resource_Management_System_Back.repository.BaseRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LeaveRequestRepository extends BaseRepository<LeaveRequest, Integer> {
}
