package com.hrms.Human_Resource_Management_System_Back.repository.tenant;

import com.hrms.Human_Resource_Management_System_Back.model.tenant.UserRole;
import com.hrms.Human_Resource_Management_System_Back.repository.BaseRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRoleRepository extends BaseRepository<UserRole, Integer> {
    @Query("""
  SELECT ur FROM UserRole ur\s
  JOIN FETCH ur.role\s
  WHERE ur.userTenant.user.userId = :userId
""")
    List<UserRole> findByUserId(@Param("userId") Integer userId);

    List<UserRole> findAllByUserTenant_UserTenantId(Integer userTenantId);

    @Modifying
    @Query("DELETE FROM UserRole ur WHERE ur.userTenant.userTenantId = :userTenantId")
    void deleteByUserTenantId(@Param("userTenantId") Integer userTenantId);
}

