package com.hrms.Human_Resource_Management_System_Back.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hrms.Human_Resource_Management_System_Back.model.dto.UserApplicationDto;
import com.hrms.Human_Resource_Management_System_Back.service.UserApplicationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class UserApplicationControllerTest {

    private MockMvc mockMvc;
    private UserApplicationService service;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        service = mock(UserApplicationService.class);
        mockMvc = MockMvcBuilders.standaloneSetup(new UserApplicationController(service)).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    void apply_shouldReturnOk() throws Exception {
        // Sample DTO
        UserApplicationDto dto = new UserApplicationDto();
        dto.setJobListingID(1);
        dto.setUserID(1);
        dto.setApplicantComment("Looking forward to the opportunity.");


        // Convert to JSON string for the "data" part
        String json = objectMapper.writeValueAsString(dto);

        // Create multipart parts
        MockMultipartFile dataPart = new MockMultipartFile("data", "", "application/json", json.getBytes());
        MockMultipartFile filePart = new MockMultipartFile("cv", "cv.pdf", "application/pdf", "fake-pdf-content".getBytes());

        // Mock service behavior
        doNothing().when(service).apply(any(UserApplicationDto.class), any());

        // Perform the multipart request
        mockMvc.perform(multipart("/api/v1/public/user-application/apply")
                        .file(dataPart)
                        .file(filePart)
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isOk());
    }
}
