package com.hrms.Human_Resource_Management_System_Back.controller;
import com.hrms.Human_Resource_Management_System_Back.model.dto.PaymentRequestDto;
import com.hrms.Human_Resource_Management_System_Back.model.dto.PaymentResponseDto;

import com.hrms.Human_Resource_Management_System_Back.repository.TenantRepository;
import com.hrms.Human_Resource_Management_System_Back.repository.TenantSubscriptionRepository;
import com.hrms.Human_Resource_Management_System_Back.service.JwtService;
import com.hrms.Human_Resource_Management_System_Back.service.PaypalPaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/v1/tenant/subscriptions")
@RequiredArgsConstructor
public class AuthenticatedSubscriptionController{

    private final PaypalPaymentService paypalPaymentService;

    @PostMapping("/payments/create")
    public ResponseEntity<PaymentResponseDto> createPayment(@RequestBody PaymentRequestDto dto) {

        PaymentResponseDto response = paypalPaymentService.createPayment(dto);
        return ResponseEntity.ok(response);
    }


}


