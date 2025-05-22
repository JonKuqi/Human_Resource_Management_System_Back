package com.hrms.Human_Resource_Management_System_Back.repository;

import com.hrms.Human_Resource_Management_System_Back.model.UserGeneral;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository interface for managing {@link UserGeneral} entities.
 * <p>
 * This repository extends {@link BaseRepository} and provides methods for accessing and modifying user general data.
 * It includes a method for finding a user by their email address.
 * </p>
 */
@Repository
public interface UserGeneralRepository extends BaseRepository<UserGeneral, Integer> {

    /**
     * Retrieves a {@link UserGeneral} entity by the associated user's email address.
     * <p>
     * This method fetches the {@link UserGeneral} entity based on the user's email. It is useful for looking up
     * user details using their email address.
     * </p>
     *
     * @param email the email address of the user
     * @return an {@link Optional} containing the {@link UserGeneral} if found, or empty if no user with the provided email exists
     */
    Optional<UserGeneral> findByUser_Email(String email);
}