package com.hrms.Human_Resource_Management_System_Back.service;

import com.hrms.Human_Resource_Management_System_Back.middleware.TenantCtx;
import com.hrms.Human_Resource_Management_System_Back.model.Address;
import com.hrms.Human_Resource_Management_System_Back.model.Tenant;
import com.hrms.Human_Resource_Management_System_Back.model.User;
import com.hrms.Human_Resource_Management_System_Back.model.dto.AddressDto;
import com.hrms.Human_Resource_Management_System_Back.model.dto.AuthenticationResponse;
import com.hrms.Human_Resource_Management_System_Back.model.dto.OwnerCreationRequest;
import com.hrms.Human_Resource_Management_System_Back.model.tenant.Role;
import com.hrms.Human_Resource_Management_System_Back.model.tenant.UserRole;
import com.hrms.Human_Resource_Management_System_Back.model.tenant.UserTenant;
import com.hrms.Human_Resource_Management_System_Back.repository.AddressRepository;
import com.hrms.Human_Resource_Management_System_Back.repository.TenantRepository;
import com.hrms.Human_Resource_Management_System_Back.repository.UserRepository;
import com.hrms.Human_Resource_Management_System_Back.repository.tenant.RoleRepository;
import com.hrms.Human_Resource_Management_System_Back.repository.tenant.UserRoleRepository;
import com.hrms.Human_Resource_Management_System_Back.repository.tenant.UserTenantRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
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

    @InjectMocks
    private TenantOnboardingService svc;

    @BeforeEach
    void init() { MockitoAnnotations.openMocks(this); }



    @Test
    void createOwnerAfterVerification_shouldCreateOwnerAndReturnToken() {

        // ----- stub JWT token parsing -----
        Claims claims = Jwts.claims();
        claims.put("tenantId", 7);
        claims.put("type", "TENANT_REG");
        // ✅ fix – return Claims with wrong "type" so service throws 400 itself

// ADD THIS ↓↓↓ so the happy-path token is recognised
        when(jwtService.parseToken("good")).thenReturn(claims);


        // ----- tenant already registered -----
        Tenant tenant = new Tenant();
        tenant.setTenantId(7);
        tenant.setSchemaName("tenant_"+UUID.randomUUID().toString().substring(0,8));
        when(tenantRepo.findById(7)).thenReturn(Optional.of(tenant));

        // ----- password hashing & login token -----
        when(passwordEncoder.encode("pwd")).thenReturn("hash");
        when(jwtService.generateToken(anyMap(), eq("owner@acme.com"), any()))
                .thenReturn("loginJWT");

        // ----- address saving in tenant schema -----
        when(addressRepo.save(any(Address.class)))
                .thenAnswer(inv -> inv.getArgument(0));

        // ----- repositories in tenant schema -----
        when(userRepo.save(any(User.class))).thenAnswer(inv -> inv.getArgument(0));
        when(userTenantRepo.save(any(UserTenant.class))).thenAnswer(inv -> inv.getArgument(0));
        when(roleRepo.save(any(Role.class))).thenAnswer(inv -> inv.getArgument(0));
        when(userRoleRepo.save(any(UserRole.class))).thenAnswer(inv -> inv.getArgument(0));

        // ----- intercept SQL resource so no real file is needed -----
        try (MockedConstruction<ClassPathResource> cp =
                     mockConstruction(ClassPathResource.class,
                             (mock, ctx) -> when(mock.getInputStream())
                                     .thenReturn(new ByteArrayInputStream("SELECT 1;".getBytes())) );
             MockedStatic<TenantCtx> tc = mockStatic(TenantCtx.class)) {

            // jdbc.execute is void — just swallow
            doNothing().when(jdbc).execute(anyString());

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

            assertEquals("loginJWT", resp.getToken());
            verify(userRepo).save(argThat(u -> u.getEmail().equals("owner@acme.com")));
            verify(userRoleRepo).save(any(UserRole.class));
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
