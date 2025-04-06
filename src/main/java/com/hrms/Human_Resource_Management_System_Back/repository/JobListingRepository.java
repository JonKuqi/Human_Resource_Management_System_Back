package com.hrms.Human_Resource_Management_System_Back.repository;

import com.hrms.Human_Resource_Management_System_Back.model.JobListing;
import org.springframework.stereotype.Repository;

@Repository
public interface JobListingRepository extends BaseRepository<JobListing, Integer> {
}