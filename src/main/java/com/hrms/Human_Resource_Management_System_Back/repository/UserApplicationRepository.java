package com.hrms.Human_Resource_Management_System_Back.repository;


import com.hrms.Human_Resource_Management_System_Back.model.UserApplication;
import org.springframework.stereotype.Repository;
/**
 * Repository interface for managing {@link UserApplication} entities.
 * <p>
 * This repository extends {@link BaseRepository} and provides methods for accessing
 * and managing job applications submitted by users. It includes a custom method
 * to check if a user has already applied for a specific job listing.
 * </p>
 */
@Repository
public interface UserApplicationRepository extends BaseRepository<UserApplication,Integer>{

    /**
     * Checks if a user has already applied to a specific job listing.
     * <p>
     * This method is useful for preventing duplicate applications and enforcing application rules.
     * </p>
     *
     * @param userId the ID of the user
     * @param jobListingId the ID of the job listing
     * @return {@code true} if the user has already applied to the given job, {@code false} otherwise
     */
    boolean existsByUser_UserIdAndJobListing_JobListingId(Integer userId, Integer jobListingId);


}
