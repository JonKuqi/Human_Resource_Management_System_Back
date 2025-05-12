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
    private final UserGeneralRepository userGeneralRepository;
    private final SkillRepository skillRepository;
    @Override
    protected UserSkillsRepository getRepository() {
        return userSkillsRepository;
    }

    public UserSkills addSkillToUser(CreateUserSkill req , HttpServletRequest request) {

        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        Optional<UserGeneral> optionalUsers = userGeneralRepository.findByUser_Email(email);

        if (optionalUsers.isEmpty()) {
            System.out.println(" Nuk u gjet UserGeneral me email: " + email);
            throw new RuntimeException("User not found");
        }
        UserGeneral user = optionalUsers.get();
        Skill skill = skillRepository.findByName(req.getSkillName())
                .orElseGet(() -> {
                    // Nëse nuk ekziston, krijo skill të ri në schema "public"
                    Skill newSkill = Skill.builder()
                            .name(req.getSkillName())
                            .type(req.getSkillType())
                            .build();
                    return skillRepository.save(newSkill);
                });

        UserSkills userSkill = UserSkills.builder()
                .userGeneral(user)
                .skill(skill)
                .value(req.getValue())
                .issuedAt(LocalDate.now())
                .build();

        return userSkillsRepository.save(userSkill);
    }

    public List<UserSkills> findByUserGeneralId(Integer userId) {
    return userSkillsRepository.findByUserGeneral_UserGeneralId(userId);
    }

    public UserSkills updateUserSkill(UserSkills existing, CreateUserSkill req) {
        Skill skill = skillRepository.findByName(req.getSkillName())
                .orElseGet(() -> skillRepository.save(
                        Skill.builder()
                                .name(req.getSkillName())
                                .type(req.getSkillType())
                                .build()
                ));

        existing.setSkill(skill);
        existing.setIssuedAt(LocalDate.now());
        return userSkillsRepository.save(existing);
    }

}