package com.hrms.Human_Resource_Management_System_Back.service;

import com.hrms.Human_Resource_Management_System_Back.model.Skill;
import com.hrms.Human_Resource_Management_System_Back.model.dto.SkillDto;
import com.hrms.Human_Resource_Management_System_Back.repository.SkillRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SkillServiceTest {

    @Mock
    private SkillRepository skillRepository;

    @InjectMocks
    private SkillService skillService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void searchSkills_shouldReturnMatchingSkills() {
        // Arrange
        Skill skill1 = new Skill(1, "Java", "Programming");
        Skill skill2 = new Skill(2, "JavaScript", "Programming");
        when(skillRepository.findByNameContainingIgnoreCase("Java")).thenReturn(List.of(skill1, skill2));

        // Act
        List<SkillDto> result = skillService.searchSkills("Java");

        // Assert
        assertEquals(2, result.size());
        assertEquals("Programming", result.get(0).getName());
        assertEquals("Programming", result.get(1).getName());
        verify(skillRepository).findByNameContainingIgnoreCase("Java");
    }

    @Test
    void searchSkills_shouldReturnEmptyListWhenNoMatch() {
        // Arrange
        when(skillRepository.findByNameContainingIgnoreCase("Python")).thenReturn(List.of());

        // Act
        List<SkillDto> result = skillService.searchSkills("Python");

        // Assert
        assertTrue(result.isEmpty());
        verify(skillRepository).findByNameContainingIgnoreCase("Python");
    }
}
