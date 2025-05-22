package com.hrms.Human_Resource_Management_System_Back.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hrms.Human_Resource_Management_System_Back.model.TestEntity;
import com.hrms.Human_Resource_Management_System_Back.service.TestEntityService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.*;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class BaseControllerTest {

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;
    private TestEntityService testEntityService;
    private TestEntityController testEntityController;

    private TestEntity testEntity1;
    private TestEntity testEntity2;

    @BeforeEach
    void setUp() {
        testEntityService = mock(TestEntityService.class);
        testEntityController = new TestEntityController(testEntityService);
        mockMvc = MockMvcBuilders.standaloneSetup(testEntityController).build();
        objectMapper = new ObjectMapper();

        testEntity1 = new TestEntity(1, "Test Entity 1", "Description 1");
        testEntity2 = new TestEntity(2, "Test Entity 2", "Description 2");
    }

    @Test
    void getAll_shouldReturnAllEntities() throws Exception {
        when(testEntityService.findAll()).thenReturn(List.of(testEntity1, testEntity2));

        mockMvc.perform(get("/api/v1/test-entities"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[1].id").value(2));
    }

    @Test
    void getById_shouldReturnEntity() throws Exception {
        when(testEntityService.findById(1)).thenReturn(Optional.of(testEntity1));

        mockMvc.perform(get("/api/v1/test-entities/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Test Entity 1"));
    }

    @Test
    void create_shouldReturnCreated() throws Exception {
        TestEntity input = new TestEntity(null, "New", "Desc");
        TestEntity saved = new TestEntity(3, "New", "Desc");

        when(testEntityService.save(any())).thenReturn(saved);

        mockMvc.perform(post("/api/v1/test-entities")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(3));
    }

    @Test
    void update_shouldModifyEntity() throws Exception {
        TestEntity updated = new TestEntity(1, "Updated", "Updated Desc");

        when(testEntityService.findById(1)).thenReturn(Optional.of(testEntity1));
        when(testEntityService.save(any())).thenReturn(updated);

        mockMvc.perform(put("/api/v1/test-entities/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updated)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated"));
    }

    @Test
    void delete_shouldReturnNoContent() throws Exception {
        doNothing().when(testEntityService).deleteById(1);

        mockMvc.perform(delete("/api/v1/test-entities/1"))
                .andExpect(status().isNoContent());
    }
}
