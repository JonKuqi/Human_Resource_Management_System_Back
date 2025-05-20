package com.hrms.Human_Resource_Management_System_Back.repository.tenant;

import com.hrms.Human_Resource_Management_System_Back.model.tenant.Application;
import com.hrms.Human_Resource_Management_System_Back.repository.BaseRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ApplicationRepository extends BaseRepository<Application, Integer> {
    boolean existsByUser_UserIdAndJobListing_JobListingId(Integer userId, Integer jobListingId);
}
