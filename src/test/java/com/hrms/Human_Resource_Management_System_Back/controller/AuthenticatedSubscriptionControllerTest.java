package com.hrms.Human_Resource_Management_System_Back.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hrms.Human_Resource_Management_System_Back.model.dto.PaymentRequestDto;
import com.hrms.Human_Resource_Management_System_Back.model.dto.PaymentResponseDto;
import com.hrms.Human_Resource_Management_System_Back.service.PaypalPaymentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class AuthenticatedSubscriptionControllerTest {

    private MockMvc mockMvc;
    private PaypalPaymentService paypalPaymentService;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        paypalPaymentService = mock(PaypalPaymentService.class);
        AuthenticatedSubscriptionController controller = new AuthenticatedSubscriptionController(paypalPaymentService);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    void createPayment_shouldReturnRedirectUrl() throws Exception {
        // Arrange
        PaymentRequestDto requestDto = new PaymentRequestDto(1, "USD", "MONTHLY");
        PaymentResponseDto responseDto = new PaymentResponseDto("https://paypal.com/approve");

        when(paypalPaymentService.createPayment(any(PaymentRequestDto.class))).thenReturn(responseDto);

        // Act & Assert
        mockMvc.perform(post("/api/v1/tenant/subscriptions/payments/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andDo(result -> System.out.println(result.getResponse().getContentAsString())) // 👈 debug line
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.approvalUrl").value("https://paypal.com/approve"));
    }
}