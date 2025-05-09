package com.hrms.Human_Resource_Management_System_Back.service;

import com.hrms.Human_Resource_Management_System_Back.model.VerificationCode;
import com.hrms.Human_Resource_Management_System_Back.repository.VerificationCodeRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
@AllArgsConstructor
public class VerificationCodeService extends BaseService<VerificationCode, Integer> {

    private final VerificationCodeRepository verificationCodeRepository;

    @Override
    protected VerificationCodeRepository getRepository() {
        return verificationCodeRepository;
    }
    public static String generate6DigitCode() {
        return String.valueOf(new Random().nextInt(900000) + 100000); // 100000–999999
    }
}