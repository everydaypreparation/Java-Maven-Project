package com.cooee.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.cooee.model.RejectKycDetails;
import com.cooee.model.SubscriptionPlanDescription;

@Repository
public interface SubscriptionPlanDescriptionRepo extends JpaRepository<SubscriptionPlanDescription, Long>{


	@Query("SELECT e FROM SubscriptionPlanDescription e ORDER BY subscription_plan_id")
	List<SubscriptionPlanDescription> findAllBySubscriptionPlan(String plan_Id);

	List<SubscriptionPlanDescription> findBySubscriptionPlanId(Long id);

//	List<SubscriptionPlanDescription> findBySubscriptionPlan(String plan_Id);

	//void deleteAllById(SubscriptionPlanDescription result);


	//List<RejectKycDetails> findByUserId(Long id);

}
