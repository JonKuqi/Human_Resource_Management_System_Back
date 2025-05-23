package com.hrms.Human_Resource_Management_System_Back.repository;

import com.hrms.Human_Resource_Management_System_Back.model.Subscription;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository interface for managing {@link Subscription} entities.
 * <p>
 * Provides methods for accessing and manipulating subscription plan data.
 * Extends {@link BaseRepository} to inherit standard CRUD operations.
 * </p>
 */
@Repository
public interface SubscriptionRepository extends BaseRepository<Subscription, Integer> {
    /**
     * Finds a list of subscriptions by their plan name.
     *
     * @param planName the name of the subscription plan to search for
     * @return a list of subscriptions matching the given plan name
     */
    List<Subscription> findByPlanName(String planName);
}