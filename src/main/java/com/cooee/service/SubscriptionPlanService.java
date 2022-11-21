package com.cooee.service;

import java.util.List;

import com.cooee.model.SubscriptionPlan;
import com.cooee.payload.GetAllSubscriptionPlanPayload;
import com.cooee.payload.GetByStatusPayload;
import com.cooee.payload.IDRequest;
//import com.cooee.payload.SubscriptionPlanDescriptionPayload;
import com.cooee.payload.SubscriptionPlanPayload;
import com.cooee.payload.UpdateSubscriptionPlanPayload;

public interface SubscriptionPlanService {



	String subscriptionPlan(SubscriptionPlanPayload request);

	List<SubscriptionPlan> getAllSubscriptionPlan();

	SubscriptionPlan getByIdSubscriptionPlan(Long id);

	String deleteByIdSubscriptionPlan(IDRequest request);

	SubscriptionPlan updateSubscriptionPlan(UpdateSubscriptionPlanPayload request);

	List<SubscriptionPlan> getSubscriptionPlanByStatus(GetByStatusPayload request);

	SubscriptionPlan changeStatusById(Long id, Boolean status);


}
