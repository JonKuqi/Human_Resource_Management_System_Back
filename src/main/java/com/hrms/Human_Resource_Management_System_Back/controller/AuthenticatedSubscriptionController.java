package com.hrms.Human_Resource_Management_System_Back.controller;
import com.hrms.Human_Resource_Management_System_Back.model.dto.PaymentRequestDto;
import com.hrms.Human_Resource_Management_System_Back.model.dto.PaymentResponseDto;
import com.hrms.Human_Resource_Management_System_Back.service.PaypalPaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;



/**
 * Controller for handling authenticated subscription-related operations.
 * <p>
 * This controller provides endpoints for tenants to initiate subscription payments
 * through the integrated PayPal service. It handles authenticated payment creation requests.
 * </p>
 */
@RestController
@RequestMapping("/api/v1/tenant/subscriptions")
@RequiredArgsConstructor
public class AuthenticatedSubscriptionController{

    /**
     * Service responsible for handling PayPal payment logic.
     */
    private final PaypalPaymentService paypalPaymentService;

    /**
     * Creates a PayPal payment for a tenant subscription.
     * <p>
     * This endpoint is used by authenticated tenants to initiate a new payment
     * for a subscription plan. It delegates the payment creation to the {@link PaypalPaymentService}.
     * </p>
     *
     * @param dto the payment request data including the selected plan and tenant context
     * @return a response entity containing the payment approval URL and other relevant details
     */
    @PostMapping("/payments/create")
    public ResponseEntity<PaymentResponseDto> createPayment(@RequestBody PaymentRequestDto dto) {

        PaymentResponseDto response = paypalPaymentService.createPayment(dto);
        return ResponseEntity.ok(response);
    }

}


