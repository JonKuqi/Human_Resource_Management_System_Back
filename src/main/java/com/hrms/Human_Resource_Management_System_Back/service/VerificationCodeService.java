package com.hrms.Human_Resource_Management_System_Back.service;

import com.hrms.Human_Resource_Management_System_Back.model.VerificationCode;
import com.hrms.Human_Resource_Management_System_Back.repository.VerificationCodeRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class VerificationCodeService extends BaseService<VerificationCode, Integer> {

    private final VerificationCodeRepository verificationCodeRepository;

    @Override
    protected VerificationCodeRepository getRepository() {
        return verificationCodeRepository;
    }
}