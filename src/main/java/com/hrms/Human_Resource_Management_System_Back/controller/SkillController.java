package com.hrms.Human_Resource_Management_System_Back.controller;

import com.hrms.Human_Resource_Management_System_Back.model.Skill;
import com.hrms.Human_Resource_Management_System_Back.model.dto.SkillDto;
import com.hrms.Human_Resource_Management_System_Back.service.SkillService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Controller for managing skills.
 * <p>
 * This controller provides public REST endpoints for performing CRUD operations and searching
 * for skills in the system. It extends {@link BaseController} to reuse generic CRUD functionality,
 * and defines an additional endpoint for text-based search.
 * </p>
 * <p>
 * Endpoint path: <code>/api/v1/public/skill</code>
 * </p>
 */
@RestController
@RequestMapping("/api/v1/public/skill")
@AllArgsConstructor
public class SkillController extends BaseController<Skill, Integer> {

    /**
     * The service responsible for handling business logic related to skills.
     */
    private final SkillService skillService;

    /**
     * Provides the specific service used for managing skills.
     *
     * @return the {@link SkillService} implementation
     */
    @Override
    protected SkillService getService() {
        return skillService;
    }

    /**
     * Searches for skills based on a query string.
     * <p>
     * This endpoint allows clients to search for skills whose names contain the provided query.
     * It returns a list of matching {@link SkillDto} objects.
     * </p>
     *
     * @param query the search keyword
     * @return a list of skills matching the query
     */

    @Operation(
            summary = "Search for skills",
            description = "Searches and returns a list of skills matching the query string."
    )
    @GetMapping("/search")
    public ResponseEntity<List<SkillDto>> searchSkills(@RequestParam("q") String query) {
        return ResponseEntity.ok(skillService.searchSkills(query));
    }
}
