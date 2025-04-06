package com.hrms.Human_Resource_Management_System_Back.service;

import com.hrms.Human_Resource_Management_System_Back.model.TenantSubscription;
import com.hrms.Human_Resource_Management_System_Back.repository.TenantSubscriptionRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class TenantSubscriptionService extends BaseService<TenantSubscription, Integer> {

    private final TenantSubscriptionRepository tenantSubscriptionRepository;

    @Override
    protected TenantSubscriptionRepository getRepository() {
        return tenantSubscriptionRepository;
    }
}