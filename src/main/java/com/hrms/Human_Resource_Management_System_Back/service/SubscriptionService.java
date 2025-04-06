package com.hrms.Human_Resource_Management_System_Back.service;

import com.hrms.Human_Resource_Management_System_Back.model.Subscription;
import com.hrms.Human_Resource_Management_System_Back.repository.SubscriptionRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class SubscriptionService extends BaseService<Subscription, Integer> {

    private final SubscriptionRepository subscriptionRepository;

    @Override
    protected SubscriptionRepository getRepository() {
        return subscriptionRepository;
    }
}