package com.hrms.Human_Resource_Management_System_Back.service;

import com.hrms.Human_Resource_Management_System_Back.model.UserSkills;
import com.hrms.Human_Resource_Management_System_Back.repository.UserSkillsRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserSkillsService extends BaseService<UserSkills, Integer> {

    private final UserSkillsRepository userSkillsRepository;

    @Override
    protected UserSkillsRepository getRepository() {
        return userSkillsRepository;
    }
}