package com.hrms.Human_Resource_Management_System_Back.repository;

import com.hrms.Human_Resource_Management_System_Back.model.Tenant;
import com.hrms.Human_Resource_Management_System_Back.model.TenantSubscription;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TenantSubscriptionRepository extends BaseRepository<TenantSubscription, Integer> {
    Optional<TenantSubscription> findByTenant(Tenant tenant);
    @Query("SELECT ts FROM TenantSubscription ts WHERE ts.tenant.schemaName = :schema AND ts.status = 'ACTIVE' AND ts.endDate > CURRENT_DATE")
    Optional<TenantSubscription> findActiveSubscription(@Param("schema") String schema);

}
