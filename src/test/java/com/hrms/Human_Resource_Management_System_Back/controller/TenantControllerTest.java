package com.hrms.Human_Resource_Management_System_Back.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hrms.Human_Resource_Management_System_Back.model.dto.AuthenticationResponse;
import com.hrms.Human_Resource_Management_System_Back.model.dto.OwnerCreationRequest;
import com.hrms.Human_Resource_Management_System_Back.model.dto.TenantRegistrationRequest;
import com.hrms.Human_Resource_Management_System_Back.service.TenantOnboardingService;
import com.hrms.Human_Resource_Management_System_Back.service.TenantService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class TenantControllerTest {

    private MockMvc mockMvc;
    private TenantService tenantService;
    private TenantOnboardingService onboardingService;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        tenantService = mock(TenantService.class);
        onboardingService = mock(TenantOnboardingService.class);
        TenantController controller = new TenantController(tenantService, onboardingService, "http://localhost:3000");
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    void register_shouldReturnAccepted() throws Exception {
        TenantRegistrationRequest request = new TenantRegistrationRequest();
        // Populate with example data if needed

        doNothing().when(onboardingService).registerTenant(any());

        mockMvc.perform(post("/api/v1/public/tenant/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isAccepted());
    }

    @Test
    void onboard_shouldReturnAuthResponse() throws Exception {
        OwnerCreationRequest request = new OwnerCreationRequest();
        // Populate with example data if needed

        AuthenticationResponse mockResponse = new AuthenticationResponse("mock-jwt-token");
        when(onboardingService.createOwnerAfterVerification(any())).thenReturn(mockResponse);

        mockMvc.perform(post("/api/v1/public/tenant/onboard")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("mock-jwt-token"));
    }
}
