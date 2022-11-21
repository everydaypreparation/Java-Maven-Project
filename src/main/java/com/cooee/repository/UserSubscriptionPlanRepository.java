package com.cooee.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cooee.model.UserSubscriptionPlan;

@Repository
public interface UserSubscriptionPlanRepository extends JpaRepository<UserSubscriptionPlan, Long> {

	UserSubscriptionPlan findByUserId(Long userId);

	//UserSubscriptionPlan findBySubscriptionPlanId(Long subscriptionPlanId);

}
