package com.hrms.Human_Resource_Management_System_Back.repository;

import com.hrms.Human_Resource_Management_System_Back.model.UserSkills;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserSkillsRepository extends BaseRepository<UserSkills, Integer> {
    List<UserSkills> findByUserGeneral_UserGeneralId(Integer userGeneralId);

    Optional<UserSkills> findByUserGeneral_UserGeneralIdAndSkill_SkillId(Integer userGeneralId, Integer skillId);

}
