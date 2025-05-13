package com.hrms.Human_Resource_Management_System_Back.repository;

import com.hrms.Human_Resource_Management_System_Back.model.VerificationCode;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for managing {@link VerificationCode} entities.
 * <p>
 * This repository extends {@link BaseRepository} and provides methods for querying verification codes
 * associated with users, including finding the latest unverified code and fetching all verification codes for a user.
 * </p>
 */
@Repository
public interface VerificationCodeRepository extends BaseRepository<VerificationCode, Integer> {

    /**
     * Retrieves the latest unverified verification code for a user based on their email.
     * <p>
     * This query fetches the most recent (unverified) verification code for a user, ordered by the expiration date.
     * It ensures that only unverified codes are considered, and the most recent one is returned.
     * </p>
     *
     * @param email the email of the user whose latest verification code is to be retrieved
     * @return an {@link Optional} containing the latest unverified {@link VerificationCode} if found, or empty if no such code exists
     */
    @Query("""
    SELECT v FROM VerificationCode v
    WHERE v.user.email = :email AND v.isUsed = false
    ORDER BY v.expiresAt DESC
    LIMIT 1
""")
    Optional<VerificationCode> findLatestByUserEmail(@Param("email") String email);

    /**
     * Retrieves all verification codes associated with a specific user email.
     * <p>
     * This method fetches all verification codes linked to a user's email, regardless of their verification status.
     * </p>
     *
     * @param email the email of the user whose verification codes are to be retrieved
     * @return a list of {@link VerificationCode} associated with the given email
     */
    @Query("SELECT v FROM VerificationCode v WHERE v.user.email = :email")
    List<VerificationCode> findAllByUserEmail(@Param("email") String email);
}