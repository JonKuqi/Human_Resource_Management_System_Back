package com.hrms.Human_Resource_Management_System_Back.controller;

import com.hrms.Human_Resource_Management_System_Back.middleware.TenantCtx;
import com.hrms.Human_Resource_Management_System_Back.model.Subscription;
import com.hrms.Human_Resource_Management_System_Back.service.BaseService;
import com.hrms.Human_Resource_Management_System_Back.service.JwtService;
import com.hrms.Human_Resource_Management_System_Back.service.PaypalPaymentService;
import com.hrms.Human_Resource_Management_System_Back.service.SubscriptionService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/v1/public/subscriptions")
@RequiredArgsConstructor
public class SubscriptionController extends BaseController<Subscription, Integer> {

    private final SubscriptionService subscriptionService;
    private final PaypalPaymentService paypalPaymentService;

    @Override
    protected BaseService<Subscription, Integer> getService() {
        return subscriptionService;
    }

    @GetMapping("/payments/success")
    public ResponseEntity<Void> executePayment(@RequestParam("paymentId") String paymentId, @RequestParam("PayerID") String payerId, @RequestParam("token") String token) {
        try {
            paypalPaymentService.executePayment(paymentId, payerId);
            URI redirectUri = URI.create("http://localhost:3000");
            return ResponseEntity.status(HttpStatus.FOUND).location(redirectUri).build();
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }


}
