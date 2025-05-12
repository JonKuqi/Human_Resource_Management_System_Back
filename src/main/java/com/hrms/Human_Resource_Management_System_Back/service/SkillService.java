package com.hrms.Human_Resource_Management_System_Back.service;

import com.hrms.Human_Resource_Management_System_Back.model.Skill;
import com.hrms.Human_Resource_Management_System_Back.model.dto.SkillDto;
import com.hrms.Human_Resource_Management_System_Back.repository.SkillRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class SkillService extends BaseService<Skill, Integer> {

    private final SkillRepository skillRepository;

    @Override
    protected SkillRepository getRepository() {
        return skillRepository;
    }


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