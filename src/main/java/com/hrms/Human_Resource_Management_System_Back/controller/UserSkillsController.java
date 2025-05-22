package com.hrms.Human_Resource_Management_System_Back.controller;

import com.hrms.Human_Resource_Management_System_Back.model.JobListing;
import com.hrms.Human_Resource_Management_System_Back.model.UserSkills;
import com.hrms.Human_Resource_Management_System_Back.service.BaseService;
import com.hrms.Human_Resource_Management_System_Back.service.UserSkillsService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/public/user-skills")

public class UserSkillsController extends BaseController<UserSkills,Integer> {

    private final UserSkillsService userSkillsService;

    public UserSkillsController(UserSkillsService userSkillsService) {
        this.userSkillsService = userSkillsService;

    }

    @Override
    protected BaseService<UserSkills, Integer> getService() {
        return userSkillsService;
    }


    @PutMapping("/{id}/value")
    public ResponseEntity<UserSkills> updateValue(@PathVariable Integer id, @RequestBody Map<String, Object> updates) {
        Optional<UserSkills> existingOpt = userSkillsService.findById(id);
        if (existingOpt.isPresent()) {
            UserSkills existing = existingOpt.get();

            if (updates.containsKey("value")) {
                Integer newValue = (Integer) updates.get("value");
                existing.setValue(newValue);
            }

            UserSkills saved = userSkillsService.save(existing);
            return ResponseEntity.ok(saved);
        } else {
            return ResponseEntity.notFound().build();
        }
    }


}
