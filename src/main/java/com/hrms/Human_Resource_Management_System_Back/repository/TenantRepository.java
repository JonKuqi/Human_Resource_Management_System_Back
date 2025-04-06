package com.hrms.Human_Resource_Management_System_Back.repository;

import com.hrms.Human_Resource_Management_System_Back.model.Tenant;
import org.springframework.stereotype.Repository;

@Repository
public interface TenantRepository extends BaseRepository<Tenant, Integer> {
}
