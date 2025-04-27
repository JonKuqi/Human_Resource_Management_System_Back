package com.hrms.Human_Resource_Management_System_Back.repository;

import com.hrms.Human_Resource_Management_System_Back.model.User;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface UserRepository extends BaseRepository<User, Integer> {

    Optional<User> findByEmail(String email);

}
