package com.hrms.Human_Resource_Management_System_Back.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hrms.Human_Resource_Management_System_Back.model.dto.SkillDto;
import com.hrms.Human_Resource_Management_System_Back.service.SkillService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class SkillControllerTest {

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;
    private SkillService skillService;

    @BeforeEach
    void setUp() {
        skillService = mock(SkillService.class);
        SkillController controller = new SkillController(skillService);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    void searchSkills_shouldReturnSkills() throws Exception {
        List<SkillDto> mockSkills = List.of(
                new SkillDto(1, "Java"),
                new SkillDto(2, "JavaScript")
        );

        when(skillService.searchSkills("jav")).thenReturn(mockSkills);

        mockMvc.perform(get("/api/v1/public/skill/search").param("q", "jav"))
                .andDo(result -> System.out.println(result.getResponse().getContentAsString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Java"))
                .andExpect(jsonPath("$[1].name").value("JavaScript"));
    }
}
