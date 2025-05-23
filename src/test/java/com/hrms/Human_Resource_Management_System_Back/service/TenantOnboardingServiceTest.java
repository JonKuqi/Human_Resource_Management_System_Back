package com.hrms.Human_Resource_Management_System_Back.service;

import com.hrms.Human_Resource_Management_System_Back.model.Address;
import com.hrms.Human_Resource_Management_System_Back.model.Tenant;
import com.hrms.Human_Resource_Management_System_Back.model.TenantPermission;
import com.hrms.Human_Resource_Management_System_Back.model.User;
import com.hrms.Human_Resource_Management_System_Back.model.dto.AddressDto;
import com.hrms.Human_Resource_Management_System_Back.model.dto.AuthenticationResponse;
import com.hrms.Human_Resource_Management_System_Back.model.dto.OwnerCreationRequest;
import com.hrms.Human_Resource_Management_System_Back.model.tenant.Role;
import com.hrms.Human_Resource_Management_System_Back.model.tenant.RolePermission;
import com.hrms.Human_Resource_Management_System_Back.model.tenant.UserRole;
import com.hrms.Human_Resource_Management_System_Back.model.tenant.UserTenant;
import com.hrms.Human_Resource_Management_System_Back.repository.AddressRepository;
import com.hrms.Human_Resource_Management_System_Back.repository.TenantPermissionRepository;
import com.hrms.Human_Resource_Management_System_Back.repository.TenantRepository;
import com.hrms.Human_Resource_Management_System_Back.repository.UserRepository;
import com.hrms.Human_Resource_Management_System_Back.repository.tenant.RolePermissionRepository;
import com.hrms.Human_Resource_Management_System_Back.repository.tenant.RoleRepository;
import com.hrms.Human_Resource_Management_System_Back.repository.tenant.UserRoleRepository;
import com.hrms.Human_Resource_Management_System_Back.repository.tenant.UserTenantRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedConstruction;
import org.mockito.MockitoAnnotations;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.server.ResponseStatusException;

import java.io.ByteArrayInputStream;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class TenantOnboardingServiceTest {

    /* ── repositories & helpers ─────────────────────────────────── */
    @Mock private TenantRepository   tenantRepo;
    @Mock private AddressRepository  addressRepo;
    @Mock private JavaMailSender     mailSender;
    @Mock private JwtService         jwtService;
    @Mock private JdbcTemplate       jdbc;
    @Mock private PasswordEncoder    passwordEncoder;
    @Mock private UserRepository     userRepo;
    @Mock private UserTenantRepository userTenantRepo;
    @Mock private UserRoleRepository  userRoleRepo;
    @Mock private RoleRepository      roleRepo;
    @Mock private RolePermissionRepository rolePermissionRepo;
    @Mock private TenantPermissionRepository tenantPermissionRepo;

    @InjectMocks
    private TenantOnboardingService svc;

    @BeforeEach
    void init() { MockitoAnnotations.openMocks(this); }



    @Test
    void createOwnerAfterVerification_shouldCreateOwnerAndReturnToken() {

        /* ──────────────────────────────────────
         * 1️⃣  Stub JWT parsing (happy-path)
         * ────────────────────────────────────── */
        Claims claims = Jwts.claims();
        claims.put("tenantId", 7);
        claims.put("type", "TENANT_REG");          // must match REG_TYPE in service
        when(jwtService.parseToken("good")).thenReturn(claims);

        /* ──────────────────────────────────────
         * 2️⃣  Stub existing Tenant
         * ────────────────────────────────────── */
        Tenant tenant = new Tenant();
        tenant.setTenantId(7);
        tenant.setSchemaName("tenant_" + UUID.randomUUID().toString().substring(0, 8));
        when(tenantRepo.findById(7)).thenReturn(Optional.of(tenant));

        /* ──────────────────────────────────────
         * 3️⃣  Password hash & login token
         * ────────────────────────────────────── */
        when(passwordEncoder.encode("pwd")).thenReturn("hash");
        when(jwtService.generateToken(anyMap(), eq("owner@acme.com"), any()))
                .thenReturn("loginJWT");

        /* ──────────────────────────────────────
         * 4️⃣  Save-methods return passed entity
         * ────────────────────────────────────── */
        when(addressRepo.save(any(Address.class))).thenAnswer(inv -> inv.getArgument(0));
        when(userRepo.save(any(User.class))).thenAnswer(inv -> inv.getArgument(0));
        when(userTenantRepo.save(any(UserTenant.class))).thenAnswer(inv -> inv.getArgument(0));
        when(roleRepo.save(any(Role.class))).thenAnswer(inv -> inv.getArgument(0));
        when(userRoleRepo.save(any(UserRole.class))).thenAnswer(inv -> inv.getArgument(0));

        /* ── NEW ── mocked permission look-ups & save */
        TenantPermission dummyTp = new TenantPermission();
        when(tenantPermissionRepo.findByResourceAndVerb(anyString(), anyString()))
                .thenReturn(Optional.of(dummyTp));
        when(rolePermissionRepo.save(any(RolePermission.class))).thenAnswer(inv -> inv.getArgument(0));

        /* ──────────────────────────────────────
         * 5️⃣  Intercept file read + JDBC
         * ────────────────────────────────────── */
        try (MockedConstruction<ClassPathResource> cp =
                     mockConstruction(ClassPathResource.class,
                             (mock, ctx) -> when(mock.getInputStream())
                                     .thenReturn(new ByteArrayInputStream("SELECT 1;".getBytes())))) {

            doNothing().when(jdbc).execute(anyString());

            /* ────────────────────────────────
             * 6️⃣  Build request & call service
             * ──────────────────────────────── */
            OwnerCreationRequest rq = new OwnerCreationRequest();
            rq.setToken("good");
            rq.setUsername("owner");
            rq.setEmail("owner@acme.com");
            rq.setPassword("pwd");
            rq.setFirstName("Ada");
            rq.setLastName("Lovelace");
            rq.setPhone("123");
            rq.setGender("F");

            AddressDto addrDto = mock(AddressDto.class);
            when(addrDto.toEntity()).thenReturn(new Address());
            rq.setAddress(addrDto);

            AuthenticationResponse resp = svc.createOwnerAfterVerification(rq);

            /* ────────────────────────────────
             * 7️⃣  Assertions & verifications
             * ──────────────────────────────── */
            assertEquals("loginJWT", resp.getToken());
            verify(userRepo).save(argThat(u -> u.getEmail().equals("owner@acme.com")));
            verify(userRoleRepo).save(any(UserRole.class));
            verify(rolePermissionRepo, atLeastOnce()).save(any(RolePermission.class));
        }
    }

    /* ─────────────────────────────────────────────────────────────
     * 3) invalid token path
     * ──────────────────────────────────────────────────────────── */
    @Test
    void createOwnerAfterVerification_invalidToken_throws400() {
        // ✅ fix – return Claims with wrong "type" so service throws 400 itself
        Claims badClaims = Jwts.claims();
        badClaims.put("tenantId", 7);
        badClaims.put("type", "WRONG");          // not "TENANT_REG"
        when(jwtService.parseToken("bad")).thenReturn(badClaims);

        OwnerCreationRequest rq = new OwnerCreationRequest();
        rq.setToken("bad");

        ResponseStatusException ex = assertThrows(
                ResponseStatusException.class,
                () -> svc.createOwnerAfterVerification(rq)
        );
        assertEquals(400, ex.getStatusCode().value());
    }
}
