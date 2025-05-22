package com.hrms.Human_Resource_Management_System_Back.service;

import com.hrms.Human_Resource_Management_System_Back.model.TenantSubscription;
import com.hrms.Human_Resource_Management_System_Back.repository.TenantSubscriptionRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * Service class for handling business logic related to tenant subscriptions.
 * <p>
 * This class extends {@link BaseService} and provides methods for managing tenant subscription entities,
 * including interacting with the underlying repository.
 * </p>
 */
@Service
@AllArgsConstructor
public class TenantSubscriptionService extends BaseService<TenantSubscription, Integer> {

    private final TenantSubscriptionRepository tenantSubscriptionRepository;

    /**
     * Returns the tenant subscription repository.
     * <p>
     * This method overrides the {@link BaseService#getRepository()} method to return the specific repository
     * for tenant subscription entities.
     * </p>
     *
     * @return the tenant subscription repository
     */
    @Override
    protected TenantSubscriptionRepository getRepository() {
        return tenantSubscriptionRepository;
    }
}