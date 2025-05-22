package com.hrms.Human_Resource_Management_System_Back.service;

import com.hrms.Human_Resource_Management_System_Back.model.VerificationCode;
import com.hrms.Human_Resource_Management_System_Back.repository.VerificationCodeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class VerificationCodeServiceTest {

    @Mock
    private VerificationCodeRepository verificationCodeRepository;

    @InjectMocks
    private VerificationCodeService verificationCodeService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void generate6DigitCode_shouldReturn6DigitCode() {
        // Act
        String code = VerificationCodeService.generate6DigitCode();

        // Assert
        assertNotNull(code);
        assertEquals(6, code.length());
        assertTrue(code.matches("\\d{6}")); // Ensure it's a 6-digit number
    }

    @Test
    void getRepository_shouldReturnVerificationCodeRepository() {
        // Act
        var repository = verificationCodeService.getRepository();

        // Assert
        assertNotNull(repository);
        assertEquals(verificationCodeRepository, repository);
    }
}
