package com.hrms.Human_Resource_Management_System_Back.repository;

import com.hrms.Human_Resource_Management_System_Back.model.Subscription;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubscriptionRepository extends BaseRepository<Subscription, Integer> {
    List<Subscription> findByPlanName(String planName);
}