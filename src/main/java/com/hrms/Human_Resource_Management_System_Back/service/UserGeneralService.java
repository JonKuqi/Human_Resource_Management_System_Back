package com.hrms.Human_Resource_Management_System_Back.service;

import com.hrms.Human_Resource_Management_System_Back.model.UserGeneral;
import com.hrms.Human_Resource_Management_System_Back.repository.UserGeneralRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserGeneralService extends BaseService<UserGeneral, Integer> {

    private final UserGeneralRepository userGeneralRepository;

    @Override
    protected UserGeneralRepository getRepository() {
        return userGeneralRepository;
    }
}
