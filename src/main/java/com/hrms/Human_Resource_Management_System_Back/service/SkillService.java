package com.hrms.Human_Resource_Management_System_Back.service;

import com.hrms.Human_Resource_Management_System_Back.model.Skill;
import com.hrms.Human_Resource_Management_System_Back.model.dto.SkillDto;
import com.hrms.Human_Resource_Management_System_Back.repository.SkillRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service class for managing skills.
 * <p>
 * This service handles business logic related to {@link Skill} entities, including
 * CRUD operations and keyword-based skill search. It extends {@link BaseService}
 * to inherit generic functionality and uses {@link SkillRepository} for data access.
 * </p>
 */
@Service
@AllArgsConstructor
public class SkillService extends BaseService<Skill, Integer> {

    /**
     * The repository used to perform database operations for skills.
     */
    private final SkillRepository skillRepository;

    /**
     * Returns the skill repository implementation.
     *
     * @return the {@link SkillRepository} instance
     */
    @Override
    protected SkillRepository getRepository() {
        return skillRepository;
    }

    /**
     * Searches for skills containing the given keyword in their name (case-insensitive).
     * <p>
     * Converts the matching {@link Skill} entities to {@link SkillDto} objects for
     * presentation or API response.
     * </p>
     *
     * @param keyword the keyword to search for in skill names
     * @return a list of matching skills in DTO format
     */
    public List<SkillDto> searchSkills(String keyword) {
        return skillRepository.findByNameContainingIgnoreCase(keyword).stream()
                .map(skill -> SkillDto.builder()
                        .skillId(skill.getSkillId())
                        .name(skill.getName())
                        .type(skill.getType())
                        .build())
                .toList();
    }
}
