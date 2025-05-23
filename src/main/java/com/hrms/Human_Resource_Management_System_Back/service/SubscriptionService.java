package com.hrms.Human_Resource_Management_System_Back.service;

import com.hrms.Human_Resource_Management_System_Back.model.Subscription;
import com.hrms.Human_Resource_Management_System_Back.repository.SubscriptionRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service class responsible for managing {@link Subscription} entities.
 * <p>
 * Provides business logic related to subscription plans, such as retrieving all available plans.
 * Inherits generic CRUD operations from {@link BaseService}.
 * </p>
 */
@Service
@AllArgsConstructor
public class SubscriptionService extends BaseService<Subscription, Integer> {

    /**
     * Repository for performing database operations on {@link Subscription} entities.
     */
    private final SubscriptionRepository subscriptionRepository;

    /**
     * Returns the repository used for subscription operations.
     *
     * @return the {@link SubscriptionRepository}
     */
    @Override
    protected SubscriptionRepository getRepository() {
        return subscriptionRepository;
    }

    /**
     * Retrieves all available subscription plans from the database.
     *
     * @return a list of {@link Subscription} entities
     */
    public List<Subscription> getAllPlans() {
        return subscriptionRepository.findAll();
    }
}