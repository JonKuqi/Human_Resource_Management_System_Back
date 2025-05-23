package com.hrms.Human_Resource_Management_System_Back.controller;

import com.hrms.Human_Resource_Management_System_Back.model.UserSkills;
import com.hrms.Human_Resource_Management_System_Back.service.BaseService;
import com.hrms.Human_Resource_Management_System_Back.service.UserSkillsService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller for managing user skills.
 * <p>
 * This controller exposes public REST endpoints for performing CRUD operations on user skill data.
 * It extends the generic {@link BaseController} and provides access to the {@link UserSkillsService},
 * which contains the business logic for managing associations between users and their skills.
 * </p>
  */
@RestController
@RequestMapping("/api/v1/public/user-skills")
public class UserSkillsController extends BaseController<UserSkills, Integer> {

    /**
     * Service for handling logic related to user skills.
     */
    private final UserSkillsService userSkillsService;

    /**
     * Constructor-based dependency injection for {@link UserSkillsService}.
     *
     * @param userSkillsService the service instance
     */
    public UserSkillsController(UserSkillsService userSkillsService) {
        this.userSkillsService = userSkillsService;
    }

    /**
     * Provides the specific service used by this controller.
     *
     * @return the {@link UserSkillsService} implementation
     */
    @Override
    protected BaseService<UserSkills, Integer> getService() {
        return userSkillsService;
    }
}
