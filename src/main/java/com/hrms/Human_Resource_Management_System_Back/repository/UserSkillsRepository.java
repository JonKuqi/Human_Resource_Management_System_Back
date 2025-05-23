package com.hrms.Human_Resource_Management_System_Back.repository;

import com.hrms.Human_Resource_Management_System_Back.model.UserSkills;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for managing {@link UserSkills} entities.
 * <p>
 * This repository extends {@link BaseRepository} and provides custom query methods
 * for accessing user-skill associations. It allows querying skills by user ID and
 * checking whether a specific skill is assigned to a given user.
 * </p>
 */
@Repository
public interface UserSkillsRepository extends BaseRepository<UserSkills, Integer> {

    /**
     * Retrieves a list of skills associated with the specified user.
     *
     * @param userGeneralId the ID of the user
     * @return a list of {@link UserSkills} linked to the given user
     */
    List<UserSkills> findByUserGeneral_UserGeneralId(Integer userGeneralId);

    /**
     * Finds a specific skill associated with a user by user ID and skill ID.
     *
     * @param userGeneralId the ID of the user
     * @param skillId the ID of the skill
     * @return an {@link Optional} containing the matching {@link UserSkills}, if found
     */
    Optional<UserSkills> findByUserGeneral_UserGeneralIdAndSkill_SkillId(Integer userGeneralId, Integer skillId);
}
