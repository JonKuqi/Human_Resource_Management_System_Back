package com.hrms.Human_Resource_Management_System_Back.service;

import com.hrms.Human_Resource_Management_System_Back.middleware.TenantCtx;
import com.hrms.Human_Resource_Management_System_Back.model.*;
import com.hrms.Human_Resource_Management_System_Back.model.dto.PaymentRequestDto;
import com.hrms.Human_Resource_Management_System_Back.model.dto.PaymentResponseDto;
import com.hrms.Human_Resource_Management_System_Back.repository.SubscriptionRepository;
import com.hrms.Human_Resource_Management_System_Back.repository.TenantRepository;
import com.hrms.Human_Resource_Management_System_Back.repository.TenantSubscriptionRepository;
import com.paypal.api.payments.*;
import com.paypal.base.rest.APIContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PaypalPaymentServiceTest {

    @Mock  private APIContext apiContext;
    @Mock  private SubscriptionRepository subscriptionRepository;
    @Mock  private TenantRepository tenantRepository;
    @Mock  private TenantSubscriptionRepository tenantSubscriptionRepository;
    @Mock  private JwtService jwtService;

    @InjectMocks
    private PaypalPaymentService paypalPaymentService;

    private static final String CALLBACK_TOKEN = "dummyCallbackToken";
    private static final String ENCODED_TOKEN  = URLEncoder.encode(CALLBACK_TOKEN, StandardCharsets.UTF_8);

    @BeforeEach
    void init() { MockitoAnnotations.openMocks(this); }

    /* ────────────────────────────────────────────────────────────────
     * createPayment … shouldReturnApprovalUrl
     * ─────────────────────────────────────────────────────────────── */
    @Test
    void createPayment_shouldReturnApprovalUrl() throws Exception {
        // Plan in DB
        Subscription plan = new Subscription();
        plan.setSubscriptionId(1);
        plan.setPlanName("Basic");
        plan.setPrice(BigDecimal.TEN);
        when(subscriptionRepository.findById(1)).thenReturn(Optional.of(plan));

        // correct overload → Map , Duration
        when(jwtService.generateToken(anyMap(), any(Duration.class)))
                .thenReturn(CALLBACK_TOKEN);

        // Intercept "new Payment()"
        try (MockedConstruction<Payment> paymentCtor =
                     mockConstruction(Payment.class, (mock, ctx) -> {
                         // Payment#create returns itself
                         when(mock.create(any(APIContext.class))).thenReturn(mock);

                         // Links list with approval_url
                         Links approval = new Links();
                         approval.setRel("approval_url");
                         approval.setHref("http://approval.url");
                         when(mock.getLinks()).thenReturn(List.of(approval));
                     });
             MockedStatic<TenantCtx> tenantCtx = mockStatic(TenantCtx.class)) {

            tenantCtx.when(TenantCtx::getTenant).thenReturn("testTenant");

            // Build DTO (currency & billingCycle cannot be null!)
            PaymentRequestDto dto = new PaymentRequestDto();
            dto.setSubscriptionId(1);
            dto.setCurrency("USD");
            dto.setBillingCycle("MONTHLY");

            PaymentResponseDto response = paypalPaymentService.createPayment(dto);

            assertNotNull(response);
            assertEquals("http://approval.url", response.getApprovalUrl());
        }
    }

    /* ────────────────────────────────────────────────────────────────
     * executePayment … shouldCreateTenantSubscription
     * ─────────────────────────────────────────────────────────────── */
    @Test
    void executePayment_shouldCreateTenantSubscription() throws Exception {

        // DB look-ups
        Subscription plan = new Subscription();
        plan.setSubscriptionId(1);
        plan.setPlanName("Basic");
        plan.setPrice(BigDecimal.TEN);
        when(subscriptionRepository.findById(1)).thenReturn(Optional.of(plan));

        Tenant tenant = new Tenant();
        tenant.setTenantId(1);
        tenant.setSchemaName("testTenant");
        when(tenantRepository.findBySchemaName("testTenant")).thenReturn(Optional.of(tenant));

        // jwtService.extractClaim → first call returns tenant, second returns subId
        when(jwtService.extractClaim(eq(CALLBACK_TOKEN), any()))
                .thenReturn("testTenant")   // tenant
                .thenReturn(1);             // subscriptionId

        try (MockedConstruction<Payment> paymentCtor =
                     mockConstruction(Payment.class, (mock, ctx) -> {
                         // 1) make payment.execute(...) return the same mock
                         when(mock.execute(any(APIContext.class), any(PaymentExecution.class)))
                                 .thenReturn(mock);

                         // 2) attach a Transaction that holds the encoded callback token
                         Transaction tx = new Transaction();
                         tx.setCustom(ENCODED_TOKEN);          // your test constant
                         when(mock.getTransactions()).thenReturn(List.of(tx));
                     });
             MockedStatic<TenantCtx> tenantCtx = mockStatic(TenantCtx.class)) {

            // allow schema to be placed in the static context
            tenantCtx.when(() -> TenantCtx.setTenant(anyString())).thenAnswer(inv -> null);

            // later, createTenantSubscription() calls TenantCtx.getTenant()
            tenantCtx.when(TenantCtx::getTenant).thenReturn("testTenant");

            // stub repository so the tenant “exists” for the unit test
            when(tenantRepository.findBySchemaName("testTenant"))
                    .thenReturn(Optional.of(new Tenant()));

            // ------------------------------------------------------------------
            paypalPaymentService.executePayment("PAYID", "PAYERID");
            // ------------------------------------------------------------------

            verify(tenantSubscriptionRepository).save(any(TenantSubscription.class));
        }
    }
}
