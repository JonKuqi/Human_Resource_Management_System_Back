//package com.hrms.Human_Resource_Management_System_Back.service.tenant;
//
//import com.hrms.Human_Resource_Management_System_Back.middleware.TenantCtx;
//import com.hrms.Human_Resource_Management_System_Back.model.*;
//import com.hrms.Human_Resource_Management_System_Back.model.dto.CreateEmployeeRequest;
//import com.hrms.Human_Resource_Management_System_Back.model.dto.RegisterTenantUserRequest;
//import com.hrms.Human_Resource_Management_System_Back.model.tenant.Contract;
//import com.hrms.Human_Resource_Management_System_Back.model.tenant.Department;
//import com.hrms.Human_Resource_Management_System_Back.model.tenant.Position;
//import com.hrms.Human_Resource_Management_System_Back.model.tenant.UserTenant;
//import com.hrms.Human_Resource_Management_System_Back.repository.AddressRepository;
//import com.hrms.Human_Resource_Management_System_Back.repository.TenantRepository;
//import com.hrms.Human_Resource_Management_System_Back.repository.TenantSubscriptionRepository;
//import com.hrms.Human_Resource_Management_System_Back.repository.UserRepository;
//import com.hrms.Human_Resource_Management_System_Back.repository.tenant.ContractRepository;
//import com.hrms.Human_Resource_Management_System_Back.repository.tenant.DepartmentRepository;
//import com.hrms.Human_Resource_Management_System_Back.repository.tenant.PositionRepository;
//import com.hrms.Human_Resource_Management_System_Back.repository.tenant.UserTenantRepository;
//import com.hrms.Human_Resource_Management_System_Back.service.EmailSenderService;
//import com.hrms.Human_Resource_Management_System_Back.service.JwtService;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockedStatic;
//import org.mockito.MockitoAnnotations;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.security.crypto.password.PasswordEncoder;
//
//import java.time.Duration;
//import java.time.LocalDate;
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.ArgumentMatchers.*;
//import static org.mockito.Mockito.*;
//
//@ExtendWith(MockitoExtension.class)
//public class UserTenantServiceTest {
//
//    @Mock
//    private UserTenantRepository userTenantRepository;
//
//    @Mock
//    private TenantRepository tenantRepository;
//
//    @Mock
//    private AddressRepository addressRepository;
//
//    @Mock
//    private UserRepository userRepository;
//
//    @Mock
//    private PasswordEncoder passwordEncoder;
//
//    @Mock
//    private JwtService jwtService;
//
//    @Mock
//    private TenantSubscriptionRepository tenantSubscriptionRepository;
//
//    @Mock
//    private DepartmentRepository departmentRepository;
//
//    @Mock
//    private PositionRepository positionRepository;
//
//    @Mock
//    private ContractRepository contractRepository;
//
//    @InjectMocks
//    private UserTenantService userTenantService;
//
//    private Tenant tenant;
//    private User user;
//    private UserTenant userTenant;
//    private Address address;
//    private TenantSubscription tenantSubscription;
//    private Subscription subscription;
//
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this);
//
//        tenant = new Tenant();
//        tenant.setTenantId(1);
//        tenant.setSchemaName("tenant_38de5bb40b96");
//
//        user = new User();
//        user.setUserId(1);
//        user.setUsername("testuser");
//        user.setEmail("test@example.com");
//        user.setTenantId(1);
//
//        address = new Address();
//        address.setAddressId(1);
//        address.setCountry("Test Country");
//        address.setCity("Test City");
//
//        userTenant = new UserTenant();
//        userTenant.setUserTenantId(1);
//        userTenant.setUser(user);
//        userTenant.setTenant(tenant);
//        userTenant.setFirstName("Test");
//        userTenant.setLastName("User");
//        userTenant.setAddress(address);
//
//        subscription = new Subscription();
//        subscription.setSubscriptionId(1);
//        subscription.setPlanName("Basic Plan");
//        subscription.setMaxUsers(10);
//
//        tenantSubscription = new TenantSubscription();
//        tenantSubscription.setTenantSubscriptionId(1);
//        tenantSubscription.setTenant(tenant);
//        tenantSubscription.setSubscription(subscription);
//    }
//
//    @Test
//    void register_shouldCreateUserAndUserTenant() {
//        // Arrange
//        RegisterTenantUserRequest request = new RegisterTenantUserRequest();
//        request.setEmail("new@example.com");
//        request.setUsername("newuser");
//        request.setPassword("password123");
//        request.setFirstName("New");
//        request.setLastName("User");
//        request.setPhone("1234567890");
//        request.setGender("Male");
//
//        com.hrms.Human_Resource_Management_System_Back.model.dto.AddressDto addressDto = new com.hrms.Human_Resource_Management_System_Back.model.dto.AddressDto();
//        addressDto.setCountry("Test Country");
//        addressDto.setCity("Test City");
//        addressDto.setStreet("Test Street");
//        addressDto.setZip("12345");
//        request.setAddress(addressDto);
//
//        try (MockedStatic<TenantCtx> mockedTenantCtx = mockStatic(TenantCtx.class)) {
//            mockedTenantCtx.when(TenantCtx::getTenant).thenReturn("test_tenant");
//
//            when(tenantRepository.findBySchemaName("test_tenant")).thenReturn(Optional.of(tenant));
//            when(passwordEncoder.encode(anyString())).thenReturn("encoded_password");
//            when(userRepository.save(any(User.class))).thenAnswer(invocation -> {
//                User savedUser = invocation.getArgument(0);
//                savedUser.setUserId(1);
//                return savedUser;
//            });
//            when(addressRepository.save(any(Address.class))).thenReturn(address);
//            when(userTenantRepository.save(any(UserTenant.class))).thenReturn(userTenant);
//            when(jwtService.generateToken(anyMap(), anyString(), any(Duration.class))).thenReturn("test.jwt.token");
//
//            // Act
//            var result = userTenantService.register(request);
//
//            // Assert
//            assertNotNull(result);
//            assertEquals("test.jwt.token", result.getToken());
//
//            verify(userRepository).save(any(User.class));
//            verify(addressRepository).save(any(Address.class));
//            verify(userTenantRepository).save(any(UserTenant.class));
//            verify(jwtService).generateToken(anyMap(), anyString(), any(Duration.class));
//        }
//    }
//    @Test
//    void createEmployee_shouldCreateUserWithTemporaryPassword() {
//        // Arrange
//        CreateEmployeeRequest request = new CreateEmployeeRequest();
//        request.setEmail("employee@example.com");
//        request.setFirstName("New");
//        request.setLastName("Employee");
//        request.setPhone("1234567890");
//        request.setGender("Female");
//        request.setCountry("Test Country");
//        request.setCity("Test City");
//        request.setStreet("Test Street");
//        request.setZip("12345");
//        request.setDepartmentName("HR");
//        request.setPositionTitle("Manager");
//        request.setSalary("5000");
//        request.setContractType("PERMANENT");
//        request.setContractEndDate(LocalDate.now().plusYears(1).toString());
//
//        Department department = new Department();
//        department.setDepartmentId(1);
//        department.setName("HR");
//
//        Position position = new Position();
//        position.setPositionId(1);
//        position.setTitle("Manager");
//        position.setDepartment(department);
//
//        try (MockedStatic<TenantCtx> mockedTenantCtx = mockStatic(TenantCtx.class);
//             MockedStatic<EmailSenderService> mockedEmailService = mockStatic(EmailSenderService.class)) {
//
//            // Mock TenantCtx to return the correct tenant schema
//            mockedTenantCtx.when(TenantCtx::getTenant).thenReturn("test_tenant");
//
//            // Mock repository calls
//            when(tenantRepository.findBySchemaName("test_tenant")).thenReturn(Optional.of(tenant));
//            when(tenantSubscriptionRepository.findByTenant(tenant)).thenReturn(Optional.of(tenantSubscription));
//            when(userTenantRepository.count()).thenReturn(5L);
//            when(passwordEncoder.encode(anyString())).thenReturn("encoded_temp_password");
//            when(userRepository.save(any(User.class))).thenAnswer(invocation -> {
//                User savedUser = invocation.getArgument(0);
//                savedUser.setUserId(2);
//                return savedUser;
//            });
//            when(addressRepository.save(any(Address.class))).thenReturn(address);
//            when(userTenantRepository.save(any(UserTenant.class))).thenReturn(userTenant);
//            when(departmentRepository.findByName("HR")).thenReturn(Optional.of(department));
//            when(positionRepository.findByTitle("Manager")).thenReturn(Optional.of(position));
//            when(contractRepository.save(any(Contract.class))).thenAnswer(invocation -> invocation.getArgument(0));
//
//            // Act
//            userTenantService.createEmployee(request);
//
//            // Assert
//            verify(userRepository).save(any(User.class));
//            verify(addressRepository).save(any(Address.class));
//            verify(userTenantRepository).save(any(UserTenant.class));
//            verify(contractRepository).save(any(Contract.class));
//
//            // Verify the email was sent
//            mockedEmailService.verify(() -> EmailSenderService.sendVerificationEmail(
//                eq("employee@example.com"),
//                eq("Welcome to NexHR"),
//                contains("temporary login password")
//            ));
//        }
//    }
//
//
//
//    @Test
//    void createEmployee_shouldThrowException_whenUserLimitExceeded() {
//        // Arrange
//        CreateEmployeeRequest request = new CreateEmployeeRequest();
//        request.setEmail("employee@example.com");
//
//        try (MockedStatic<TenantCtx> mockedTenantCtx = mockStatic(TenantCtx.class)) {
//            mockedTenantCtx.when(TenantCtx::getTenant).thenReturn("test_tenant");
//
//            // Only set up the stubs that will be used in the exception path
//            when(tenantRepository.findBySchemaName("test_tenant")).thenReturn(Optional.of(tenant));
//            when(tenantSubscriptionRepository.findByTenant(tenant)).thenReturn(Optional.of(tenantSubscription));
//            when(userTenantRepository.count()).thenReturn(10L); // Max users reached
//
//            // Act & Assert
//            assertThrows(RuntimeException.class, () -> userTenantService.createEmployee(request));
//
//            // Verify no user was created
//            verify(userRepository, never()).save(any(User.class));
//        }
//    }
//
//
//
//}
