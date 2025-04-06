package com.hrms.Human_Resource_Management_System_Back.service;

import com.hrms.Human_Resource_Management_System_Back.model.Skill;
import com.hrms.Human_Resource_Management_System_Back.repository.SkillRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class SkillService extends BaseService<Skill, Integer> {

    private final SkillRepository skillRepository;

    @Override
    protected SkillRepository getRepository() {
        return skillRepository;
    }
}