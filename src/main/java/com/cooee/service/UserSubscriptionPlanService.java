 package com.cooee.service;

import com.cooee.model.UserSubscriptionPlan;
import com.cooee.payload.UserSubscriptionPlanPayload;

public interface UserSubscriptionPlanService {

	UserSubscriptionPlan getSubscriptionPlanByUserId(Long userId);

	UserSubscriptionPlan addUserSubscriptionPlan(UserSubscriptionPlanPayload request);
  
  
  }
