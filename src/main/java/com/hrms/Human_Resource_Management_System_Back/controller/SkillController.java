package com.hrms.Human_Resource_Management_System_Back.controller;

import com.hrms.Human_Resource_Management_System_Back.model.Skill;
import com.hrms.Human_Resource_Management_System_Back.model.dto.SkillDto;
import com.hrms.Human_Resource_Management_System_Back.service.SkillService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/public/skill")
@AllArgsConstructor
public class SkillController extends BaseController<Skill, Integer> {

    private final SkillService skillService;

    @Override
    protected SkillService getService() {
        return skillService;
    }

    @GetMapping("/search")
    public ResponseEntity<List<SkillDto>> searchSkills(@RequestParam("q") String query) {
        return ResponseEntity.ok(skillService.searchSkills(query));
    }

}