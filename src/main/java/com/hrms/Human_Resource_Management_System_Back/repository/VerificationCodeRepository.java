package com.hrms.Human_Resource_Management_System_Back.repository;

import com.hrms.Human_Resource_Management_System_Back.model.VerificationCode;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VerificationCodeRepository extends BaseRepository<VerificationCode, Integer> {
    @Query("""
    SELECT v FROM VerificationCode v
    WHERE v.user.email = :email AND v.isUsed = false
    ORDER BY v.expiresAt DESC
    LIMIT 1
""")
    Optional<VerificationCode> findLatestByUserEmail(@Param("email") String email);

    @Query("SELECT v FROM VerificationCode v WHERE v.user.email = :email")
    List<VerificationCode> findAllByUserEmail(@Param("email") String email);

}
