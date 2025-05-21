package com.hrms.Human_Resource_Management_System_Back.service;

import com.hrms.Human_Resource_Management_System_Back.model.TestEntity;
import com.hrms.Human_Resource_Management_System_Back.repository.BaseRepository;
import com.hrms.Human_Resource_Management_System_Back.repository.TestEntityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TestEntityService extends BaseService<TestEntity, Integer> {

    private final TestEntityRepository testEntityRepository;

    @Override
    protected BaseRepository<TestEntity, Integer> getRepository() {
        return testEntityRepository;
    }
}
