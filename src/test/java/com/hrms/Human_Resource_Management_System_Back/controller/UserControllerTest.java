package com.hrms.Human_Resource_Management_System_Back.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hrms.Human_Resource_Management_System_Back.model.dto.AuthenticationRequest;
import com.hrms.Human_Resource_Management_System_Back.model.dto.AuthenticationResponse;
import com.hrms.Human_Resource_Management_System_Back.model.dto.ChangePasswordRequest;
import com.hrms.Human_Resource_Management_System_Back.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class UserControllerTest {

    private MockMvc mockMvc;
    private UserService userService;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        userService = mock(UserService.class);
        UserController controller = new UserController(userService);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    void authenticate_shouldReturnToken() throws Exception {
        AuthenticationRequest request = new AuthenticationRequest("user@example.com", "password123");
        AuthenticationResponse response = new AuthenticationResponse("mock-token");

        when(userService.authenticate(any())).thenReturn(response);

        mockMvc.perform(post("/api/v1/public/user/authenticate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("mock-token"));
    }

    @Test
    void changePassword_shouldReturnNoContent() throws Exception {
        ChangePasswordRequest req = new ChangePasswordRequest();
        req.setCurrentPassword("oldpass");
        req.setNewPassword("newpass");

        doNothing().when(userService).changePassword(any());

        mockMvc.perform(post("/api/v1/public/user/change-password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isNoContent());
    }
}
