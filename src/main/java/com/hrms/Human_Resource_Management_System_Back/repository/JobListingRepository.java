package com.hrms.Human_Resource_Management_System_Back.repository;

import com.hrms.Human_Resource_Management_System_Back.model.JobListing;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JobListingRepository extends BaseRepository<JobListing, Integer> {
    List<JobListing> findByTenant_TenantId(Integer tenantId);
}