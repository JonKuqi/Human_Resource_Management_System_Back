package com.hrms.Human_Resource_Management_System_Back.repository.tenant;

import com.hrms.Human_Resource_Management_System_Back.model.tenant.Application;
import com.hrms.Human_Resource_Management_System_Back.repository.BaseRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ApplicationRepository extends BaseRepository<Application, Integer> {

    boolean existsByUser_UserIdAndJobListing_JobListingId(Integer userId, Integer jobListingId);

    List<Application> findByCv_DocumentIdIn(List<Long> documentIds);


}
