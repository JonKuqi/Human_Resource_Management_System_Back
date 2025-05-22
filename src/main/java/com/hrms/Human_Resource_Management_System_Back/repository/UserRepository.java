package com.hrms.Human_Resource_Management_System_Back.repository;

import com.hrms.Human_Resource_Management_System_Back.model.User;
import org.springframework.stereotype.Repository;

import java.util.Optional;


/**
 * Repository interface for managing {@link User} entities.
 * <p>
 * This repository extends {@link BaseRepository} and provides methods for accessing and modifying user data.
 * It includes a method for finding a user by their email address.
 * </p>
 */
@Repository
public interface UserRepository extends BaseRepository<User, Integer> {

    /**
     * Retrieves a {@link User} entity by their email address.
     * <p>
     * This method allows the user to be fetched by their email address, which is typically used for user authentication
     * and user-related operations.
     * </p>
     *
     * @param email the email address of the user
     * @return an {@link Optional} containing the {@link User} if found, or empty if no user with the given email exists
     */
    Optional<User> findByEmail(String email);
}
