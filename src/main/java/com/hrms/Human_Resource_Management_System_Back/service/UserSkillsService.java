package com.hrms.Human_Resource_Management_System_Back.service;

import com.hrms.Human_Resource_Management_System_Back.model.Skill;
import com.hrms.Human_Resource_Management_System_Back.model.User;
import com.hrms.Human_Resource_Management_System_Back.model.UserGeneral;
import com.hrms.Human_Resource_Management_System_Back.model.UserSkills;
import com.hrms.Human_Resource_Management_System_Back.model.dto.CreateUserSkill;
import com.hrms.Human_Resource_Management_System_Back.repository.SkillRepository;
import com.hrms.Human_Resource_Management_System_Back.repository.UserGeneralRepository;
import com.hrms.Human_Resource_Management_System_Back.repository.UserSkillsRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UserSkillsService extends BaseService<UserSkills, Integer> {

    private final UserSkillsRepository userSkillsRepository;

    @Override
    protected UserSkillsRepository getRepository() {
        return userSkillsRepository;
    }



    @Override
    public UserSkills save(UserSkills incoming) {
        Integer userGeneralId = incoming.getUserGeneral().getUserGeneralId();
        Integer skillId = incoming.getSkill().getSkillId();

        // Kontrollo ekzistencën me kombinim unik
        Optional<UserSkills> existingOpt =
                userSkillsRepository.findByUserGeneral_UserGeneralIdAndSkill_SkillId(userGeneralId, skillId);

        if (existingOpt.isPresent()) {
            UserSkills existing = existingOpt.get();

            // Përditëso vetëm ato që vijnë nga frontend
            if (incoming.getValue() != null) existing.setValue(incoming.getValue());
            if (incoming.getIssuedAt() != null) existing.setIssuedAt(incoming.getIssuedAt());
            if (incoming.getValidUntil() != null) existing.setValidUntil(incoming.getValidUntil());

            return userSkillsRepository.save(existing); // update
        }

        // Nuk ekziston, bëje insert
        return userSkillsRepository.save(incoming);
    }


}


