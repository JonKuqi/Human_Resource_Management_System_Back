package com.hrms.Human_Resource_Management_System_Back.repository.tenant;

import com.hrms.Human_Resource_Management_System_Back.model.tenant.UserTenant;
import com.hrms.Human_Resource_Management_System_Back.repository.BaseRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserTenantRepository extends BaseRepository<UserTenant, Integer> {
}
