package com.hrms.Human_Resource_Management_System_Back.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hrms.Human_Resource_Management_System_Back.model.dto.AuthenticationResponse;
import com.hrms.Human_Resource_Management_System_Back.model.dto.RegisterGeneralRequest;
import com.hrms.Human_Resource_Management_System_Back.model.dto.VerifyRequest;
import com.hrms.Human_Resource_Management_System_Back.service.JwtService;
import com.hrms.Human_Resource_Management_System_Back.service.UserGeneralService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Map;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class UserGeneralControllerTest {

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;
    private UserGeneralService userGeneralService;
    private JwtService jwtService;

    @BeforeEach
    void setUp() {
        userGeneralService = mock(UserGeneralService.class);
        jwtService = mock(JwtService.class);
        UserGeneralController controller = new UserGeneralController(userGeneralService, jwtService);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    void register_shouldReturnToken() throws Exception {
        RegisterGeneralRequest req = new RegisterGeneralRequest();
        req.setEmail("test@example.com");
        req.setPassword("pass123");

        AuthenticationResponse res = new AuthenticationResponse("mock-token");

        when(userGeneralService.register(any())).thenReturn(res);

        mockMvc.perform(post("/api/v1/public/user-general/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("mock-token"));
    }

    @Test
    void verifyEmail_shouldReturnOk() throws Exception {
        VerifyRequest verifyRequest = new VerifyRequest("test@example.com", "123456");

        when(userGeneralService.verifyEmail(any()))
                .thenAnswer(invocation -> ResponseEntity.ok(Map.of("message", "verified")));

        mockMvc.perform(post("/api/v1/public/user-general/verify")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(verifyRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("verified"));
    }

    @Test
    void resendVerificationCode_shouldReturnOk() throws Exception {
        Map<String, String> body = Map.of("email", "test@example.com");

        when(userGeneralService.resendVerificationCode("test@example.com"))
                .thenAnswer(invocation -> ResponseEntity.ok(Map.of("status", "sent")));

        mockMvc.perform(post("/api/v1/public/user-general/resend")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("sent"));
    }

    @Test
    void resendVerificationCode_shouldReturnBadRequestIfEmailMissing() throws Exception {
        Map<String, String> body = Map.of(); // empty
        when(userGeneralService.resendVerificationCode("test@example.com"))
                .thenAnswer(invocation -> ResponseEntity.ok(Map.of("status", "sent")));

        mockMvc.perform(post("/api/v1/public/user-general/resend")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Email is required"));
    }
}
