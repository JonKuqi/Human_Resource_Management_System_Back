package com.hrms.Human_Resource_Management_System_Back.controller;

import com.hrms.Human_Resource_Management_System_Back.middleware.TenantCtx;
import com.hrms.Human_Resource_Management_System_Back.model.Subscription;
import com.hrms.Human_Resource_Management_System_Back.service.BaseService;
import com.hrms.Human_Resource_Management_System_Back.service.JwtService;
import com.hrms.Human_Resource_Management_System_Back.service.PaypalPaymentService;
import com.hrms.Human_Resource_Management_System_Back.service.SubscriptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/plans")
    public ResponseEntity<List<Subscription>> getPlans() {
        return ResponseEntity.ok(subscriptionService.getAllPlans());
    }

    @GetMapping("/payments/success")
    public ResponseEntity<String> executePayment(
            @RequestParam("paymentId") String paymentId,
            @RequestParam("PayerID") String payerId) {

        try {
            paypalPaymentService.executePayment(paymentId, payerId);
            return ResponseEntity.ok("Payment successful and subscription activated.");

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(403).body("Payment failed: " + e.getMessage());
        }
    }

    @GetMapping("/payments/cancel")
    public ResponseEntity<String> cancelPayment() {
        return ResponseEntity.ok("Subscription process was cancelled by the user.");
    }
}
