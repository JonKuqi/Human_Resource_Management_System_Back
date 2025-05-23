package com.hrms.Human_Resource_Management_System_Back.repository;


import com.hrms.Human_Resource_Management_System_Back.model.JobTag;
import org.springframework.stereotype.Repository;
/**
 * Repository interface for managing {@link JobTag} entities.
 * <p>
 * This repository extends {@link BaseRepository} and provides standard CRUD operations
 * for job tags, which are used to label or categorize job listings.
 * </p>
 */
@Repository
public interface JobTagRepository extends BaseRepository<JobTag, Integer> {
}