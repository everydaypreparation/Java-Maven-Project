package com.cooee.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cooee.model.UserSubscriptionPlanDescription;
@Repository
public interface UserSubscriptionPlanDescriptionRepo extends JpaRepository<UserSubscriptionPlanDescription, Long> {

}
