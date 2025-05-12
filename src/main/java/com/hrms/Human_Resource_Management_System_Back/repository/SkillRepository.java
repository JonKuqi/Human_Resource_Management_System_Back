package com.hrms.Human_Resource_Management_System_Back.repository;

import com.hrms.Human_Resource_Management_System_Back.model.Skill;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SkillRepository extends BaseRepository<Skill, Integer> {

    List<Skill> findByNameContainingIgnoreCase(String name);
    Optional<Skill> findByName(String name);

}