package com.hrms.Human_Resource_Management_System_Back.repository;

import com.hrms.Human_Resource_Management_System_Back.model.Skill;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for managing {@link Skill} entities.
 * <p>
 * This repository extends {@link BaseRepository} and provides custom query methods
 * for accessing skill data. It supports searching skills by name (case-insensitive)
 * and retrieving skills by exact name.
 * </p>
 */
@Repository
public interface SkillRepository extends BaseRepository<Skill, Integer> {

    /**
     * Retrieves a list of skills whose name contains the given keyword, ignoring case.
     *
     * @param name the partial name to search for
     * @return a list of matching {@link Skill} entities
     */
    List<Skill> findByNameContainingIgnoreCase(String name);


}
