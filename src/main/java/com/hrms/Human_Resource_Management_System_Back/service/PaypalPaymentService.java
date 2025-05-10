package com.hrms.Human_Resource_Management_System_Back.service;

import com.hrms.Human_Resource_Management_System_Back.middleware.TenantCtx;
import com.hrms.Human_Resource_Management_System_Back.model.Subscription;
import com.hrms.Human_Resource_Management_System_Back.model.Tenant;
import com.hrms.Human_Resource_Management_System_Back.model.TenantSubscription;
import com.hrms.Human_Resource_Management_System_Back.model.dto.PaymentRequestDto;
import com.hrms.Human_Resource_Management_System_Back.model.dto.PaymentResponseDto;
import com.hrms.Human_Resource_Management_System_Back.model.types.SubscriptionStatus;
import com.hrms.Human_Resource_Management_System_Back.repository.SubscriptionRepository;
import com.hrms.Human_Resource_Management_System_Back.repository.TenantRepository;
import com.hrms.Human_Resource_Management_System_Back.repository.TenantSubscriptionRepository;
import com.paypal.api.payments.*;
import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.PayPalRESTException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class PaypalPaymentService {

    private final APIContext apiContext;
    private final SubscriptionRepository subscriptionRepository;
    private final TenantRepository tenantRepository;
    private final TenantSubscriptionRepository tenantSubscriptionRepository;
    private final JwtService jwtService;

    public PaymentResponseDto createPayment(PaymentRequestDto dto) {
        Subscription plan = subscriptionRepository.findById(dto.getSubscriptionId())
                .orElseThrow(() -> new RuntimeException("Subscription not found"));

        if (plan.getPrice().compareTo(BigDecimal.ZERO) == 0) {
            createTenantSubscription(plan);
            return new PaymentResponseDto("SUBSCRIPTION_ACTIVATED");
        }

        Amount amount = new Amount();
        amount.setCurrency(dto.getCurrency());
        amount.setTotal(String.format(Locale.US, "%.2f", plan.getPrice()));

        Transaction transaction = new Transaction();
        transaction.setDescription("Subscription plan: " + plan.getPlanName() + " - " + dto.getBillingCycle());
        transaction.setAmount(amount);

        Map<String, Object> claims = new HashMap<>();
        claims.put("tenant", TenantCtx.getTenant());
        claims.put("subscriptionId", dto.getSubscriptionId());
        String callbackToken = jwtService.generateToken(claims, Duration.ofMinutes(10));
        transaction.setCustom(callbackToken);

        List<Transaction> transactions = Collections.singletonList(transaction);

        Payer payer = new Payer();
        payer.setPaymentMethod("paypal");

        RedirectUrls redirectUrls = new RedirectUrls();
        String encodedToken = URLEncoder.encode(callbackToken, StandardCharsets.UTF_8);
        redirectUrls.setReturnUrl("http://localhost:8081/api/v1/public/subscriptions/payments/success?token=" + encodedToken);
        redirectUrls.setCancelUrl("http://localhost:8081/api/v1/public/subscriptions/payments/cancel");

        Payment payment = new Payment();
        payment.setIntent("sale");
        payment.setPayer(payer);
        payment.setTransactions(transactions);
        payment.setRedirectUrls(redirectUrls);

        try {
            Payment createdPayment = payment.create(apiContext);
            for (Links link : createdPayment.getLinks()) {
                if ("approval_url".equalsIgnoreCase(link.getRel())) {
                    return new PaymentResponseDto(link.getHref());
                }
            }
        } catch (PayPalRESTException e) {
            throw new RuntimeException("Failed to create PayPal payment", e);
        }

        throw new RuntimeException("No approval URL returned by PayPal");
    }

    @Transactional
    public void executePayment(String paymentId, String payerId) {
        Payment payment = new Payment();
        payment.setId(paymentId);

        PaymentExecution paymentExecution = new PaymentExecution();
        paymentExecution.setPayerId(payerId);

        try {
            Payment executedPayment = payment.execute(apiContext, paymentExecution);
            System.out.println("Payment executed: " + executedPayment.getId());

            String rawToken = executedPayment.getTransactions().get(0).getCustom();
            String decodedToken = URLDecoder.decode(rawToken, StandardCharsets.UTF_8);

            String schema = (String) jwtService.extractClaim(decodedToken, claims -> claims.get("tenant"));
            Integer subscriptionId = (Integer) jwtService.extractClaim(decodedToken, claims -> claims.get("subscriptionId"));

            TenantCtx.setTenant(schema);

            Subscription subscription = subscriptionRepository.findById(subscriptionId)
                    .orElseThrow(() -> new RuntimeException("Subscription not found"));

            createTenantSubscription(subscription);

        } catch (PayPalRESTException e) {
            throw new RuntimeException("Error executing PayPal payment", e);
        }
    }

    private void createTenantSubscription(Subscription subscription) {
        String schema = TenantCtx.getTenant();

        Tenant tenant = tenantRepository.findBySchemaName(schema)
                .orElseThrow(() -> new RuntimeException("Tenant not found"));

        TenantSubscription tenantSubscription = TenantSubscription.builder()
                .tenant(tenant)
                .subscription(subscription)
                .startDate(LocalDate.now())
                .endDate(LocalDate.now().plusMonths(1))
                .status(SubscriptionStatus.ACTIVE.name())
                .createdAt(LocalDateTime.now())
                .build();

        tenantSubscriptionRepository.save(tenantSubscription);
    }
}