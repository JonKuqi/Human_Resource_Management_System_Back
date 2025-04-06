package com.hrms.Human_Resource_Management_System_Back.controller;

import com.hrms.Human_Resource_Management_System_Back.model.Skill;
import com.hrms.Human_Resource_Management_System_Back.service.SkillService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/skills")
@AllArgsConstructor
public class SkillController extends BaseController<Skill, Integer> {

    private final SkillService skillService;

    @Override
    protected SkillService getService() {
        return skillService;
    }
}