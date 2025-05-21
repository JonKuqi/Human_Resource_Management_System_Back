package com.hrms.Human_Resource_Management_System_Back.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hrms.Human_Resource_Management_System_Back.service.PaypalPaymentService;
import com.hrms.Human_Resource_Management_System_Back.service.SubscriptionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class SubscriptionControllerTest {

    private MockMvc mockMvc;
    private PaypalPaymentService paypalPaymentService;
    private SubscriptionService subscriptionService;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        paypalPaymentService = mock(PaypalPaymentService.class);
        subscriptionService = mock(SubscriptionService.class);

        SubscriptionController controller = new SubscriptionController(subscriptionService, paypalPaymentService);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    void executePayment_shouldRedirectOnSuccess() throws Exception {
        // Arrange
        doNothing().when(paypalPaymentService).executePayment("PAY-123", "PAYER-456");

        // Act & Assert
        mockMvc.perform(get("/api/v1/public/subscriptions/payments/success")
                        .param("paymentId", "PAY-123")
                        .param("PayerID", "PAYER-456")
                        .param("token", "abc123"))
                .andExpect(status().isFound())
                .andExpect(header().string("Location", "http://localhost:3000/tenant/subscription?status=success"));
    }

    @Test
    void executePayment_shouldReturn500OnError() throws Exception {
        // Arrange
        doThrow(new RuntimeException("fail")).when(paypalPaymentService).executePayment(anyString(), anyString());

        // Act & Assert
        mockMvc.perform(get("/api/v1/public/subscriptions/payments/success")
                        .param("paymentId", "BAD")
                        .param("PayerID", "BAD")
                        .param("token", "xyz"))
                .andExpect(status().isInternalServerError());
    }
}