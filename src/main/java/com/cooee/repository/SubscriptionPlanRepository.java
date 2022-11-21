package com.cooee.repository;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import com.cooee.model.SubscriptionPlan;
import com.cooee.payload.GetByStatusPayload;


@Repository
public interface SubscriptionPlanRepository extends JpaRepository<SubscriptionPlan, Long> {

	List<SubscriptionPlan> findByStatus(Boolean status);

	

	
	
	
}