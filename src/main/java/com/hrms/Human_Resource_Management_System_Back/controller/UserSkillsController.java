package com.hrms.Human_Resource_Management_System_Back.controller;

import com.hrms.Human_Resource_Management_System_Back.model.UserSkills;
import com.hrms.Human_Resource_Management_System_Back.model.dto.CreateUserSkill;
import com.hrms.Human_Resource_Management_System_Back.service.UserSkillsService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/user-skills")
@RequiredArgsConstructor
public class UserSkillsController {

    private final UserSkillsService userSkillsService;

    @PostMapping
    public ResponseEntity<UserSkills> addSkillToUser(@RequestBody CreateUserSkill request, HttpServletRequest httpRequest) {
        UserSkills userSkill = userSkillsService.addSkillToUser(request, httpRequest);
        return ResponseEntity.ok(userSkill);
    }

    @GetMapping
    public ResponseEntity<List<UserSkills>> getAll(@RequestParam Integer userId) {
        List<UserSkills> skills = userSkillsService.findByUserGeneralId(userId); // kjo metodë pret Integer direkt
        return ResponseEntity.ok(skills);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        Optional<UserSkills> existing = userSkillsService.findById(id);
        if (existing.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        userSkillsService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserSkills> update(
            @PathVariable Integer id,
            @RequestBody CreateUserSkill req) {

        Optional<UserSkills> existing = userSkillsService.findById(id);
        if (existing.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        UserSkills updated = userSkillsService.updateUserSkill(existing.get(), req);
        return ResponseEntity.ok(updated);
    }





}
