package com.hrms.Human_Resource_Management_System_Back.repository;

import com.hrms.Human_Resource_Management_System_Back.model.Tenant;
import com.hrms.Human_Resource_Management_System_Back.model.TenantSubscription;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TenantSubscriptionRepository extends BaseRepository<TenantSubscription, Integer> {
    Optional<TenantSubscription> findByTenant(Tenant tenant);
}
