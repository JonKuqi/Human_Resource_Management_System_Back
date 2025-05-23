package com.hrms.Human_Resource_Management_System_Back.service;

import com.hrms.Human_Resource_Management_System_Back.model.UserSkills;
import com.hrms.Human_Resource_Management_System_Back.repository.UserSkillsRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Service class for managing associations between users and their skills.
 * <p>
 * This service handles the creation and update logic for {@link UserSkills} entities.
 * It extends {@link BaseService} to reuse generic CRUD functionality, and includes
 * custom logic to prevent duplicate entries per user and skill combination.
 * </p>
 */
@Service
@AllArgsConstructor
public class UserSkillsService extends BaseService<UserSkills, Integer> {

    /**
     * Repository for performing data access operations on user skills.
     */
    private final UserSkillsRepository userSkillsRepository;

    /**
     * Returns the specific repository implementation for {@link UserSkills}.
     *
     * @return the {@link UserSkillsRepository} instance
     */
    @Override
    protected UserSkillsRepository getRepository() {
        return userSkillsRepository;
    }

    /**
     * Saves or updates a user skill record.
     * <p>
     * If a {@link UserSkills} entry already exists for the same user and skill,
     * the method updates the existing record. Otherwise, it creates a new entry.
     * </p>
     *
     * @param incoming the {@link UserSkills} object to be created or updated
     * @return the persisted {@link UserSkills} entity
     */
    @Override
    public UserSkills save(UserSkills incoming) {
        Integer userGeneralId = incoming.getUserGeneral().getUserGeneralId();
        Integer skillId = incoming.getSkill().getSkillId();

        Optional<UserSkills> existingOpt =
                userSkillsRepository.findByUserGeneralIdAndSkillId(userGeneralId, skillId);

        if (existingOpt.isPresent()) {
            UserSkills existing = existingOpt.get();

            if (incoming.getValue() != null) existing.setValue(incoming.getValue());
            if (incoming.getIssuedAt() != null) existing.setIssuedAt(incoming.getIssuedAt());
            if (incoming.getValidUntil() != null) existing.setValidUntil(incoming.getValidUntil());

            return userSkillsRepository.save(existing); // Update existing record
        }

        return userSkillsRepository.save(incoming); // Create new record
    }
}
