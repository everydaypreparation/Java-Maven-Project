package com.cooee.serviceImp;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cooee.model.AccountNumberDetails;
import com.cooee.model.RejectKycDetails;
import com.cooee.model.SubscriptionPlan;
import com.cooee.model.SubscriptionPlanDescription;
import com.cooee.model.User;
import com.cooee.payload.GetAllSubscriptionPlanPayload;
import com.cooee.payload.GetByStatusPayload;
import com.cooee.payload.IDRequest;
//import com.cooee.payload.SubscriptionPlanDescriptionPayload;
import com.cooee.payload.SubscriptionPlanPayload;
import com.cooee.payload.UpdateSubscriptionPlanPayload;
import com.cooee.repository.SubscriptionPlanDescriptionRepo;
import com.cooee.repository.SubscriptionPlanRepository;
import com.cooee.responsemessgae.CONSTANTMESSAGE;
import com.cooee.service.SubscriptionPlanService;
import com.cooee.util.IUtil;
import com.stripe.Stripe;
import com.stripe.model.Plan;
import com.stripe.model.Subscription;

@Service
public class SubscriptionPlanServiceImp implements SubscriptionPlanService {
	@Autowired
	private SubscriptionPlanRepository subscriptionPlanRepository;

	@Autowired
	private SubscriptionPlanDescriptionRepo subscriptionPlanDescriptionRepo;

	// @SuppressWarnings("null")
	@Override
	public String subscriptionPlan(SubscriptionPlanPayload request) throws IllegalArgumentException {
		try {
			// Map<Object, Object> response = new HashMap<Object, Object>();
			SubscriptionPlan subscriptionPlan = new SubscriptionPlan();
			Timestamp currentDateTime = new Timestamp(System.currentTimeMillis());

			subscriptionPlan.setCountry(request.getCountry());
			subscriptionPlan.setPlanValidity(request.getPlanValidity());
			subscriptionPlan.setIncomingSms(request.getIncomingSms());
			subscriptionPlan.setIncomingCall(request.getIncomingCall());
			subscriptionPlan.setOutgoingSms(request.getOutgoingSms());
			subscriptionPlan.setOutgoingCall(request.getOutgoingCall());
			subscriptionPlan.setAmount(request.getAmount());
			subscriptionPlan.setDiscount(request.getDiscount());
			subscriptionPlan.setStatus(request.getStatus());
			subscriptionPlan.setTitle(request.getTitle());

			subscriptionPlan.setTotalAmount(request.getAmount() - request.getDiscount());

//			String plan_id = "SP" + IUtil.getSixDigitRandomNumbers();
//
//			subscriptionPlan.setPlan_Id(plan_id);

			subscriptionPlan.setCreationDate(currentDateTime);
			subscriptionPlan.setUpdationDate(currentDateTime);
			subscriptionPlanRepository.save(subscriptionPlan);

			List<SubscriptionPlanDescription> newEntries = new ArrayList<>();
			for (String description : request.getDescription()) {
				SubscriptionPlanDescription response = new SubscriptionPlanDescription();

				response.setDescription(description);
				response.setCreationDate(currentDateTime);
				response.setSubscriptionPlan(subscriptionPlan);

				newEntries.add(response);
			}

			if (!newEntries.isEmpty()) {
				subscriptionPlanDescriptionRepo.saveAll(newEntries);
			}
//--------------------------------------------Stripe----------------------------------------------------------------------------			
			Map<Object, Object> response = new HashMap<Object, Object>();
			Stripe.apiKey = CONSTANTMESSAGE.Secretkey;

			Map<String, Object> params = new HashMap<>();
			Integer amnt=request.getAmount();
            Integer currency=amnt*100;
			params.put("amount", currency);
			params.put("currency", "usd");
			String s = request.getPlanValidity();
			String[] splited = s.split(" ");
			//for(String foreach[]:splited[])
			String hold=splited[0];
			
			String hold1=splited[1];
			System.out.println(hold+""+hold1);
			params.put("interval",hold1);
			params.put("interval_count",hold);
			params.put("product", CONSTANTMESSAGE.Product_ID);

			Plan plan = Plan.create(params);

			subscriptionPlan.setPlan_Id(plan.getId());
			subscriptionPlanRepository.save(subscriptionPlan);

////----------------------------------------------------------------------------------------------------------------------------
			return "success";
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public List<SubscriptionPlan> getAllSubscriptionPlan() {

		try {

			List<SubscriptionPlan> list = subscriptionPlanRepository.findAll();
			if (list != null) {
				for (SubscriptionPlan result : list) {
					List<SubscriptionPlanDescription> list1 = subscriptionPlanDescriptionRepo
							.findBySubscriptionPlanId(result.getId());
					result.setSubscriptionPlanDescription(list1);
//					for (SubscriptionPlanDescription result1 : list1) {
//						result.setSubscriptionPlanDescription(result1);
//
//					}
//					

				}
				return list;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

		return null;
	}

	@Override
	public SubscriptionPlan getByIdSubscriptionPlan(Long id) {
		try {

			SubscriptionPlan subscriptionPlan = subscriptionPlanRepository.findById(id).orElse(null);
			if (subscriptionPlan != null) {
				return subscriptionPlan;
			}

		} catch (Exception e) {
			e.printStackTrace();

		}
		return null;
	}

//	@Override
//	public SubscriptionPlan updateSubscriptionPlan(UpdateSubscriptionPlanPayload request) {
//
//		try {
//
//			SubscriptionPlan subscriptionPlan = subscriptionPlanRepository.findById(request.getId()).orElse(null);
//			if (subscriptionPlan != null) {
//				Timestamp currentDateTime = new Timestamp(System.currentTimeMillis());
//
//				subscriptionPlan.setCountry(request.getCountry());
//				subscriptionPlan.setPlanValidity(request.getPlanValidity());
//				subscriptionPlan.setIncomingSms(request.getIncomingSms());
//				subscriptionPlan.setIncomingCall(request.getIncomingCall());
//				subscriptionPlan.setOutgoingSms(request.getOutgoingSms());
//				subscriptionPlan.setOutgoingCall(request.getOutgoingCall());
//				subscriptionPlan.setAmount(request.getAmount());
//				subscriptionPlan.setDiscount(request.getDiscount());
//				subscriptionPlan.setStatus(request.getStatus());
//				subscriptionPlan.setTitle(request.getTitle());
//
//				subscriptionPlan.setTotalAmount(request.getAmount() - request.getDiscount());
//
//				subscriptionPlan.setCreationDate(currentDateTime);
//				subscriptionPlan.setUpdationDate(currentDateTime);
//
//				subscriptionPlanRepository.save(subscriptionPlan);
//
//				List<SubscriptionPlanDescription> newEntries = subscriptionPlanDescriptionRepo
//						.findBySubscriptionPlanId(subscriptionPlan.getId());
//
//				for (SubscriptionPlanDescription result : request.getSubscriptionPlanDescription()) {
//					if (result.getId() != null) {
//						SubscriptionPlanDescription response = subscriptionPlanDescriptionRepo.findById(result.getId())
//								.orElse(null);
//						if (response != null) {
//							response.setDescription(result.getDescription());
//							response.setCreationDate(currentDateTime);
//							response.setSubscriptionPlan(subscriptionPlan);
//
//							subscriptionPlanDescriptionRepo.save(response);
//						}
//					} else {
//						SubscriptionPlanDescription response1 = new SubscriptionPlanDescription();
//
//						response1.setDescription(result.getDescription());
//						response1.setCreationDate(currentDateTime);
//						response1.setSubscriptionPlan(subscriptionPlan);
//						subscriptionPlanDescriptionRepo.save(response1);
//					}
//				}
//			}
//
//			return subscriptionPlan;
//		} catch (Exception e) {
//			e.printStackTrace();
//
//		}
//		return null;
//	}
	
	@Override
	public SubscriptionPlan updateSubscriptionPlan(UpdateSubscriptionPlanPayload request) {

		try {

			SubscriptionPlan subscriptionPlan = subscriptionPlanRepository.findById(request.getId()).orElse(null);
			if (subscriptionPlan != null) {
				Timestamp currentDateTime = new Timestamp(System.currentTimeMillis());

				List<SubscriptionPlanDescription> planDescriptionList = request.getSubscriptionPlanDescription();
				List<SubscriptionPlanDescription> newSubPlanList = new ArrayList<SubscriptionPlanDescription>();
				SubscriptionPlanDescription response = null;
				for (SubscriptionPlanDescription result : planDescriptionList) {
					if (result.getId() != null) {
						response = subscriptionPlanDescriptionRepo.findById(result.getId()).orElse(null);
						if (response != null) {
							response.setDescription(result.getDescription());
							response.setCreationDate(currentDateTime);
							response.setSubscriptionPlan(subscriptionPlan);
							newSubPlanList.add(response);
						} else {
							SubscriptionPlanDescription response1 = new SubscriptionPlanDescription();
							response1.setId(result.getId());
							response1.setDescription(result.getDescription());
							response1.setCreationDate(currentDateTime);
							response1.setSubscriptionPlan(subscriptionPlan);
							newSubPlanList.add(response1);
						}
					}
				}
				List<SubscriptionPlanDescription> descriptionList = new ArrayList<>();
				SubscriptionPlanDescription description=new SubscriptionPlanDescription();
				List<SubscriptionPlanDescription> subscriptionPlanDescriptionList = subscriptionPlan.getSubscriptionPlanDescription();
				for(SubscriptionPlanDescription planDescription:subscriptionPlanDescriptionList) {
					description.setCreationDate(currentDateTime);
					description.setDescription(planDescription.getDescription());
					description.setSubscriptionPlan(planDescription.getSubscriptionPlan());
					descriptionList.add(description);
					subscriptionPlanDescriptionRepo.deleteById(planDescription.getId());
			
				}
				newSubPlanList=subscriptionPlanDescriptionRepo.saveAll(newSubPlanList);
				
				subscriptionPlan.setSubscriptionPlanDescription(newSubPlanList);
				subscriptionPlan.setCountry(request.getCountry());
				subscriptionPlan.setPlanValidity(request.getPlanValidity());
				subscriptionPlan.setIncomingSms(request.getIncomingSms());
				subscriptionPlan.setIncomingCall(request.getIncomingCall());
				subscriptionPlan.setOutgoingSms(request.getOutgoingSms());
				subscriptionPlan.setOutgoingCall(request.getOutgoingCall());
				subscriptionPlan.setAmount(request.getAmount());
				subscriptionPlan.setDiscount(request.getDiscount());
				subscriptionPlan.setStatus(request.getStatus());
				subscriptionPlan.setTitle(request.getTitle());

				subscriptionPlan.setTotalAmount(request.getAmount() - request.getDiscount());
				subscriptionPlan.setCreationDate(currentDateTime);
				subscriptionPlan.setUpdationDate(currentDateTime);
				subscriptionPlan = subscriptionPlanRepository.save(subscriptionPlan);
			}

			return subscriptionPlan;
		} catch (Exception e) {
			e.printStackTrace();

		}
		return null;
	}

	@Override
	public String deleteByIdSubscriptionPlan(IDRequest request) {
		try {
			SubscriptionPlan subscriptionPlan = subscriptionPlanRepository.findById(request.getId()).orElse(null);
			List<SubscriptionPlanDescription> entries = subscriptionPlanDescriptionRepo
					.findBySubscriptionPlanId(subscriptionPlan.getId());
			if (entries != null) {

				subscriptionPlanDescriptionRepo.deleteAll(entries);
			}

			if (subscriptionPlan != null) {
				subscriptionPlanRepository.delete(subscriptionPlan);
			}

			return "success";

		} catch (Exception e) {
			e.printStackTrace();

		}
		return null;
	}

	@Override
	public List<SubscriptionPlan> getSubscriptionPlanByStatus(GetByStatusPayload request) {
		try {
			List<SubscriptionPlan> list = subscriptionPlanRepository.findByStatus(request.getStatus());

			if (list != null) {
				for (SubscriptionPlan result : list) {
					List<SubscriptionPlanDescription> list1 = subscriptionPlanDescriptionRepo
							.findBySubscriptionPlanId(result.getId());
					result.setSubscriptionPlanDescription(list1);
//				for (SubscriptionPlanDescription result1 : list1) {
//					result.setSubscriptionPlanDescription(result1);
//
//				}
//				

				}
				return list;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

		return null;
	}

	@Override
	public SubscriptionPlan changeStatusById(Long id, Boolean status) {
		try {
			SubscriptionPlan subscriptionPlan = subscriptionPlanRepository.findById(id).orElse(null);
			if (subscriptionPlan != null) {
				subscriptionPlan.setStatus(status);
				return subscriptionPlanRepository.save(subscriptionPlan);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
