package com.hrms.Human_Resource_Management_System_Back.repository;

import com.hrms.Human_Resource_Management_System_Back.model.TenantPermission;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TenantPermissionRepository extends BaseRepository<TenantPermission, Integer> {
    List<TenantPermission> findByNameIn(List<String> starterPermissionNames);
}
