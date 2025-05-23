package com.hrms.Human_Resource_Management_System_Back.repository;

import com.hrms.Human_Resource_Management_System_Back.model.JobListing;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository interface for managing {@link JobListing} entities.
 * <p>
 * This repository extends {@link BaseRepository} and provides custom methods
 * for accessing and managing job listings, including filtering by tenant.
 * </p>
 */
@Repository
public interface JobListingRepository extends BaseRepository<JobListing, Integer> {
}