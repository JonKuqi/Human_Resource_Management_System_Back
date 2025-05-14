package com.hrms.Human_Resource_Management_System_Back.service;

import com.hrms.Human_Resource_Management_System_Back.model.VerificationCode;
import com.hrms.Human_Resource_Management_System_Back.repository.VerificationCodeRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Random;

/**
 * Service class for handling verification code operations.
 * <p>
 * This class provides methods for generating and managing verification codes,
 * including generating random 6-digit codes used for email or phone number verification.
 * </p>
 */
@Service
@AllArgsConstructor
public class VerificationCodeService extends BaseService<VerificationCode, Integer> {

    /**
     * The repository responsible for handling verification code CRUD operations.
     */
    private final VerificationCodeRepository verificationCodeRepository;

    /**
     * Returns the verification code repository.
     * <p>
     * This method overrides the {@link BaseService#getRepository()} method to return the specific repository
     * for verification code entities.
     * </p>
     *
     * @return the verification code repository
     */
    @Override
    protected VerificationCodeRepository getRepository() {
        return verificationCodeRepository;
    }

    /**
     * Generates a random 6-digit verification code.
     * <p>
     * This method generates a random 6-digit number between 100000 and 999999, inclusive.
     * This code can be used for verifying email addresses or other user operations.
     * </p>
     *
     * @return a 6-digit random verification code as a string
     */
    public static String generate6DigitCode() {
        return String.valueOf(new Random().nextInt(900000) + 100000); // 100000–999999
    }
}