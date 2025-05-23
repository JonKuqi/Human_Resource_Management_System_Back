package com.hrms.Human_Resource_Management_System_Back.repository;

import com.hrms.Human_Resource_Management_System_Back.model.Tenant;
import com.hrms.Human_Resource_Management_System_Back.model.TenantSubscription;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface TenantSubscriptionRepository extends BaseRepository<TenantSubscription, Integer> {



    @Query("SELECT ts FROM TenantSubscription ts WHERE ts.tenant.schemaName = :schema AND ts.status = 'ACTIVE'")
    Optional<TenantSubscription> findActiveSubscription(@Param("schema") String schema);


    @Query("""
    SELECT ts FROM TenantSubscription ts
    WHERE ts.tenant.tenantId = :tenantId AND ts.status = 'ACTIVE'
    ORDER BY ts.createdAt DESC
""")
    List<TenantSubscription> findActiveByTenantOrdered(@Param("tenantId") Integer tenantId);

    @Modifying
    @Query("UPDATE TenantSubscription ts SET ts.status = 'INACTIVE' WHERE ts.tenant.tenantId = :tenantId AND ts.status = 'ACTIVE'")
    void deactivateAllForTenant(@Param("tenantId") Integer tenantId);



    @Query("SELECT ts FROM TenantSubscription ts WHERE ts.tenant = :tenant ORDER BY ts.createdAt DESC")
    List<TenantSubscription> findAllByTenantOrderByCreatedAtDesc(@Param("tenant") Tenant tenant);





}
