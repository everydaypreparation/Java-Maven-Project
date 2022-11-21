//package com.cooee.serviceImp;
//
//import java.sql.Timestamp;
//import java.text.DateFormat;
//import java.text.ParseException;
//import java.text.SimpleDateFormat;
//import java.util.Calendar;
//import java.util.Date;
//import java.util.List;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import com.cooee.model.SubscriptionPlan;
//import com.cooee.model.SubscriptionPlanDescription;
//import com.cooee.model.User;
//import com.cooee.model.UserSubscriptionPlan;
//import com.cooee.model.UserSubscriptionPlanDescription;
//import com.cooee.payload.UserSubscriptionPlanPayload;
//import com.cooee.repository.SubscriptionPlanDescriptionRepo;
//import com.cooee.repository.SubscriptionPlanRepository;
//import com.cooee.repository.UserRepository;
//import com.cooee.repository.UserSubscriptionPlanDescriptionRepo;
//import com.cooee.repository.UserSubscriptionPlanRepository;
//import com.cooee.service.UserSubscriptionPlanService;
//import com.cooee.util.IUtil;
//
//@Service
//public class UserSubscriptionPlanServiceImp implements UserSubscriptionPlanService {
//	@Autowired
//	private UserSubscriptionPlanRepository userSubscriptionPlanRepository;
//
//	@Autowired
//	private SubscriptionPlanRepository subscriptionPlanRepository;
//	
//	@Autowired
//	private SubscriptionPlanDescriptionRepo subscriptionPlanDescriptionRepo;
//
//	@Autowired
//	private UserRepository userRepository;
//	
//	//@Autowired
//	//private UserSubscriptionPlanDescription userSubscriptionPlanDescription;
//	
//	@Autowired
//	private UserSubscriptionPlanDescriptionRepo userSubscriptionPlanDescriptionRepo;
//
//	public String addUserSubscriptionPlan(UserSubscriptionPlanPayload request) {
//		try {
//
//			User user = userRepository.findById(request.getUserId()).orElse(null);
//			if (user != null) {
//			SubscriptionPlan subscriptionPlan=subscriptionPlanRepository.findById(request.getSubscriptionPlanId()).orElse(null);
//			
//			if(subscriptionPlan!=null) {
//				//SubscriptionPlanDescription subscriptionPlanDescription =SubscriptionPlanRepository.findBySubscriptionPlanId(subscriptionPlan.getId());
//
//				//UserSubscriptionPlan userPlan=	userSubscriptionPlanRepository.findBySubscriptionPlanId(subscriptionPlan.getId());
////				if(userPlan==null) {
//				UserSubscriptionPlan userSubscriptionPlan = new UserSubscriptionPlan();
////				Date date = Calendar.getInstance().getTime();  
////				DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");  
////				String strDate = dateFormat.format(date);
//				
//				DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
//			    Calendar c = Calendar.getInstance();
//			    String startDate=dateFormat.format(c.getTime());
//			    System.out.println("strDate  "+startDate);
//			    c.add(Calendar.DATE, Integer.parseInt(subscriptionPlan.getPlanValidity()));
//			   String renewalDate=    dateFormat.format(c.getTime());
//			    System.out.println("renewalDate "+renewalDate);
//
//				userSubscriptionPlan.setCountry(subscriptionPlan.getCountry());
//				userSubscriptionPlan.setPlanValidity(subscriptionPlan.getPlanValidity());
//				userSubscriptionPlan.setIncomingSms(subscriptionPlan.getIncomingSms());
//				userSubscriptionPlan.setIncomingCall(subscriptionPlan.getIncomingCall());
//				userSubscriptionPlan.setOutgoingSms(subscriptionPlan.getOutgoingSms());
//				userSubscriptionPlan.setOutgoingCall(subscriptionPlan.getOutgoingCall());
//				userSubscriptionPlan.setAmount(subscriptionPlan.getAmount());
//				userSubscriptionPlan.setDiscount(subscriptionPlan.getDiscount());
//				userSubscriptionPlan.setTotalAmount(subscriptionPlan.getTotalAmount());
//				userSubscriptionPlan.setPlan_Id(subscriptionPlan.getPlan_Id());
//				userSubscriptionPlan.setTitle(subscriptionPlan.getTitle());
//				userSubscriptionPlan.setStatus(subscriptionPlan.getStatus());
//				userSubscriptionPlan.setSubscriptionPlan(subscriptionPlan);
//				 
//				userSubscriptionPlan.setPlanStatus(true);
//				
//				userSubscriptionPlan.setSubscriptionPlanActiveDate(startDate);
//				userSubscriptionPlan.setSubscriptionPlanRenewalDate(renewalDate);
//				userSubscriptionPlan.setUser(user);
//				userSubscriptionPlan=userSubscriptionPlanRepository.save(userSubscriptionPlan);
//				 if(userSubscriptionPlan!=null) {
//
//						List<SubscriptionPlanDescription> list = subscriptionPlanDescriptionRepo.findBySubscriptionPlanId(subscriptionPlan.getId());
//						
//						for(SubscriptionPlanDescription result:list) {
//							UserSubscriptionPlanDescription userSubscriptionPlanDescription = new UserSubscriptionPlanDescription();
//					 userSubscriptionPlanDescription.setDescription(result.getDescription());
//					 userSubscriptionPlanDescription.setUserSubscriptionPlan(subscriptionPlan);
//					 userSubscriptionPlanDescriptionRepo.save(userSubscriptionPlanDescription);
//					 
//				 }
//					 }
//				}
//				else {
//					
//				}
//			
//			}
//			//}
//				 return "success";
//			
//			
//				
//			
//		} catch (Exception e) {
//			e.printStackTrace();
//
//		}
//		return null;
//	}
//}

package com.cooee.serviceImp;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.cooee.config.ResponseHandler;
import com.cooee.model.SubscriptionPlan;
import com.cooee.model.SubscriptionPlanDescription;
import com.cooee.model.User;
import com.cooee.model.UserSubscriptionPlan;
import com.cooee.model.UserSubscriptionPlanDescription;
import com.cooee.payload.EmptyJsonResponse;

import com.cooee.payload.UserSubscriptionPlanPayload;
import com.cooee.repository.SubscriptionPlanDescriptionRepo;
import com.cooee.repository.SubscriptionPlanRepository;
import com.cooee.repository.UserRepository;
import com.cooee.repository.UserSubscriptionPlanDescriptionRepo;
import com.cooee.repository.UserSubscriptionPlanRepository;
import com.cooee.responsemessgae.CONSTANTMESSAGE;
import com.cooee.service.UserSubscriptionPlanService;
import com.cooee.util.IUtil;

@Service
public class UserSubscriptionPlanServiceImp implements UserSubscriptionPlanService {
	@Autowired
	private UserSubscriptionPlanRepository userSubscriptionPlanRepository;

	@Autowired
	private SubscriptionPlanRepository subscriptionPlanRepository;

	@Autowired
	private SubscriptionPlanDescriptionRepo subscriptionPlanDescriptionRepo;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private UserSubscriptionPlanDescriptionRepo userSubscriptionPlanDescriptionRepo;

	public UserSubscriptionPlan addUserSubscriptionPlan(UserSubscriptionPlanPayload request) {
		
		UserSubscriptionPlan userSubscriptionPlan = new UserSubscriptionPlan();
		
		try {
			
			if (request.getUserId() != null && request.getSubscriptionPlanId()!=null) {
				
				SubscriptionPlan subscriptionPlan = subscriptionPlanRepository.findById(request.getSubscriptionPlanId())
						.get();
				User user = userRepository.findById(request.getUserId()).get();
				
					DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
					Calendar c = Calendar.getInstance();
					String startDate = dateFormat.format(c.getTime());
					String renewalDate = dateFormat.format(c.getTime());
					

					userSubscriptionPlan.setCountry(subscriptionPlan.getCountry());
					userSubscriptionPlan.setPlanValidity(subscriptionPlan.getPlanValidity());
					userSubscriptionPlan.setIncomingSms(subscriptionPlan.getIncomingSms());
					userSubscriptionPlan.setIncomingCall(subscriptionPlan.getIncomingCall());
					userSubscriptionPlan.setOutgoingSms(subscriptionPlan.getOutgoingSms());
					userSubscriptionPlan.setOutgoingCall(subscriptionPlan.getOutgoingCall());
					userSubscriptionPlan.setAmount(subscriptionPlan.getAmount());
					userSubscriptionPlan.setDiscount(subscriptionPlan.getDiscount());
					userSubscriptionPlan.setTotalAmount(subscriptionPlan.getTotalAmount());
					userSubscriptionPlan.setPlan_Id(subscriptionPlan.getPlan_Id());
					userSubscriptionPlan.setTitle(subscriptionPlan.getTitle());
					userSubscriptionPlan.setStatus(subscriptionPlan.getStatus());
				//	userSubscriptionPlan.setSubscriptionPlan(subscriptionPlan);
					userSubscriptionPlan.setCreationDate(subscriptionPlan.getCreationDate());
					userSubscriptionPlan.setUpdationDate(subscriptionPlan.getUpdationDate());

					userSubscriptionPlan.setPlanStatus(true);

					userSubscriptionPlan.setSubscriptionPlanActiveDate(startDate);
					userSubscriptionPlan.setSubscriptionPlanRenewalDate(renewalDate);
				//	userSubscriptionPlan.setUser(user);
					
					if (userSubscriptionPlan != null) {
						List<SubscriptionPlanDescription> list = subscriptionPlanDescriptionRepo
								.findBySubscriptionPlanId(subscriptionPlan.getId());
						for (SubscriptionPlanDescription result : list) {
							UserSubscriptionPlanDescription userSubscriptionPlanDescription = new UserSubscriptionPlanDescription();
							userSubscriptionPlanDescription.setDescription(result.getDescription());
							userSubscriptionPlanDescription.setUserSubscriptionPlan(subscriptionPlan);
							userSubscriptionPlanDescriptionRepo.save(userSubscriptionPlanDescription);
						}
					}
					userSubscriptionPlan = userSubscriptionPlanRepository.save(userSubscriptionPlan);
					return userSubscriptionPlan;
					
				} else {
				
				}

			
		
		} catch (Exception e) {
			e.printStackTrace();
		}
		return userSubscriptionPlan;
	
	}

	@Override
	public UserSubscriptionPlan getSubscriptionPlanByUserId(Long userId) {
		User user = userRepository.findById(userId).get();
		UserSubscriptionPlan userSubscriptionPlan = new UserSubscriptionPlan();
		
			if(user!=null && userId !=null ) {
				
				UserSubscriptionPlan userSubscriptionPlanID = userSubscriptionPlanRepository.findByUserId(userId);
				return userSubscriptionPlanID;
				
				}else {
						
				}
			return userSubscriptionPlan;	
		
	}
	
	
	
}

