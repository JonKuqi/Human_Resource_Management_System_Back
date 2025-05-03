package com.hrms.Human_Resource_Management_System_Back.repository;

import com.hrms.Human_Resource_Management_System_Back.model.UserGeneral;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserGeneralRepository extends BaseRepository<UserGeneral, Integer> {
    Optional<UserGeneral> findByUser_Email(String email);
}
