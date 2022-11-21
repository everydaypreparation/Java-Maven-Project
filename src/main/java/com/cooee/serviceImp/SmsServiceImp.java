package com.cooee.serviceImp;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.cooee.model.DeletedSms;
import com.cooee.model.LoginDevice;
import com.cooee.model.Sms;
import com.cooee.model.SubscriptionPlanDescription;
import com.cooee.model.User;
import com.cooee.model.VirtualNumbers;
import com.cooee.payload.BasicResponsePayload;
import com.cooee.payload.DeletedSmsFromDid;
import com.cooee.payload.IDRequest;
import com.cooee.payload.SmsDeleteById;
import com.cooee.repository.LoginDeviceRepository;
import com.cooee.repository.SmsRepository;
import com.cooee.repository.UserRepository;
import com.cooee.service.SmsService;
import com.cooee.service.UserService;
import com.cooee.service.VirtualNumbersService;
import com.cooee.util.Constant;

@Service
public class SmsServiceImp implements SmsService {

	@Autowired
	private com.cooee.repository.DeletedSmsRepository DeletedSmsRepository;
	@Autowired
	private SmsRepository smsRepository;
	@Autowired
	private VirtualNumbersService virtualNumbersService;

	@Autowired
	@Lazy
	private UserService userService;

	@Autowired
	private LoginDeviceRepository loginDeviceRepository;

	@Autowired
	private UserRepository userRepository;

	
	@Override
	public Sms save(User user, String sourceNum, String message, String destination,String messageId,String date) {
		Sms sms = new Sms();
		sms.setUser(user);
		sms.setNumber(sourceNum);
		sms.setMessage(message);
		sms.setSmsDate(date);
		sms.setType("INCOMING");
		sms.setRead(false);
		sms.setActive(true);
		sms.setAmount(0.0);
		sms.setMessageId(messageId);
		sms.setSource(sourceNum);
		sms.setDestination(destination);

		return smsRepository.save(sms);
	}

	@Override
	public Integer countByUserId(Long id) {

		return smsRepository.countByUserId(id);
	}
	
	@Override
    public BasicResponsePayload receiveSms(String messageId,String source, String destination, String message,String date) throws Exception {



       Timestamp currentDateTime = new Timestamp(System.currentTimeMillis());



       Long timestamp = currentDateTime.getTime();
        System.out.println("Current Date Timestamp : " + timestamp);



       BasicResponsePayload response = new BasicResponsePayload();



       VirtualNumbers virtualNumber = virtualNumbersService.getByVirtualNumber(destination);
        //VirtualNumbers sourceVirtualNumber = virtualNumbersService.getByVirtualNumber(source);



       if (virtualNumber != null) {
            System.out.println("receiveSms called from = " + source + " Type");
//
//            String sourceNum = source.replace(" ", "").replace("-", "").replace("(", "").replace(")", "");
//
//            
//            
//            
//
//        //    User users = userRepository.findByDid(virtualNumber.getVirtualNumber());
//
//
    //    if (sourceNum != null ) {
                System.out.println("receiveSms user found for destinaion");
                

                Date dateWithoutT=new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").parse(date);
    			System.out.println("date--> "+dateWithoutT);
    			
    			DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");  
    			String strDate = dateFormat.format(dateWithoutT);

                Sms savedSms = save(virtualNumber.getUser(), source, message, destination,messageId,strDate);

                System.out.println("received date"  +date);

                   if (savedSms != null) {
                        response.setStatus("1");
                        response.setData(savedSms);
                        // ------------------------------------------------------Notification
                        // code--------------------------------------------------------------------------
                        List<LoginDevice> loginDeviceList = loginDeviceRepository.findByUserId(virtualNumber.getUser().getId());
                        for (LoginDevice loginDevice : loginDeviceList) {
                            if (loginDevice.getIsDeviceActive() != null && loginDevice.getIsDeviceActive()) {
                                if (loginDevice.getDeviceType().equalsIgnoreCase(Constant.ANDROID_DEVICE_TYPE)) {
                                    PushNotificationService.sendSMSPushNotificationFromAdndroid(
                                            loginDevice.getDeviceToken(), String.valueOf(timestamp), message,
                                            source,savedSms.getId(), "SMS");
                                    // return "success";



                               } else if (loginDevice.getDeviceType().equalsIgnoreCase(Constant.IOS_DEVICE_TYPE)) {
                                    PushNotificationService.sendSMSPushNotificationFromIos(loginDevice.getDeviceToken(), // remove
                                                                                                                        // voip
                                            String.valueOf(timestamp), message, source,savedSms.getId(), "SMS");
                                    // return "success";
                                }
                            }
                        }
//-------------------------------------------------------------------------------------------------------------------------------------------------
                    
                    }    else {
                        response.setStatus("3");
                        System.out.println("SMS Not Saved");
                        response.setMessage("SMS Not Saved");
                    }
        }
//            } else {
//                response.setStatus("2");
//                response.setMessage("Users Not Found!");
//            }
         else {
            System.out.println("Virtual number - " + destination + " Not Found");
            response.setStatus("4");
        }



       return response;
    }

//	@Override
//	public BasicResponsePayload receiveSms(String messageId,String source, String destination, String message,String date) throws Exception {
//
//		Timestamp currentDateTime = new Timestamp(System.currentTimeMillis());
//
//		Long timestamp = currentDateTime.getTime();
//		System.out.println("Current Date Timestamp : " + timestamp);
//
//		BasicResponsePayload response = new BasicResponsePayload();
//
//		VirtualNumbers virtualNumber = virtualNumbersService.getByVirtualNumber(destination);
//		//VirtualNumbers sourceVirtualNumber = virtualNumbersService.getByVirtualNumber(source);
//
//		if (virtualNumber != null) {
//			System.out.println("receiveSms called = " + sourceVirtualNumber + " Type- " + sourceVirtualNumber.getNumberType());
////
////			String sourceNum = source.replace(" ", "").replace("-", "").replace("(", "").replace(")", "");
////
////			
////			
////			
////
////		//	User users = userRepository.findByDid(virtualNumber.getVirtualNumber());
////
////
//	//	if (sourceNum != null ) {
//				System.out.println("receiveSms user found for destinaion");
//				
//
//					Sms savedSms = save(virtualNumber.getUser(), virtualNumber.getVirtualNumber(), message, destination,messageId,date);
//
//					if (savedSms != null) {
//						response.setStatus("1");
//						response.setData(savedSms);
//						// ------------------------------------------------------Notification
//						// code--------------------------------------------------------------------------
//						List<LoginDevice> loginDeviceList = loginDeviceRepository.findByUserId(virtualNumber.getUser().getId());
//						for (LoginDevice loginDevice : loginDeviceList) {
//							if (loginDevice.getIsDeviceActive() != null && loginDevice.getIsDeviceActive()) {
//								if (loginDevice.getDeviceType().equalsIgnoreCase(Constant.ANDROID_DEVICE_TYPE)) {
//									PushNotificationService.sendPushNotificationFromAdndroid(
//											loginDevice.getDeviceToken(), String.valueOf(timestamp), message,
//											sourceVirtualNumber.getVirtualNumber(), "SMS");
//									// return "success";
//
//								} else if (loginDevice.getDeviceType().equalsIgnoreCase(Constant.IOS_DEVICE_TYPE)) {
//									PushNotificationService.sendPushNotificationFromIos(loginDevice.getDeviceToken(), // remove
//																														// voip
//											String.valueOf(timestamp), message, sourceVirtualNumber.getVirtualNumber(), "SMS");
//									// return "success";
//								}
//							}
//						}
////-------------------------------------------------------------------------------------------------------------------------------------------------
//					
//					}	else {
//						response.setStatus("3");
//						System.out.println("SMS Not Saved");
//						response.setMessage("SMS Not Saved");
//					}
//		}
////			} else {
////				response.setStatus("2");
////				response.setMessage("Users Not Found!");
////			}
//		 else {
//			System.out.println("Virtual number - " + destination + " Not Found");
//			response.setStatus("4");
//		}
//
//		return response;
//	}



	@Override
	public BasicResponsePayload sendSms(String source, String destination, String message) throws Exception {
		BasicResponsePayload response = new BasicResponsePayload();

		User user = userRepository.findById(Long.parseLong(source)).orElse(null);
		// User user = userRepository.findByDid(source);

		if (user != null) {
			List<VirtualNumbers> virtualNumber = virtualNumbersService.findByUserId(user.getId());

			String sourceVN = "";

			if (virtualNumber != null) {
				sourceVN = virtualNumber.get(0).getVirtualNumber();
			}

			if (sourceVN != "") {

				try {
					// final String uri = "http://localhost:8080/asterisk/user/testNew";
					final String uri = "http://54.253.213.11:8080/asterisk/api/smpp/smppSendSMS?from=" + sourceVN
							+ "&message=" + message + "&to=" + destination;
					// http://54.253.213.11:8080/asterisk/api/smpp/smppSendSMS?from=61485833455&message=testing&to=61485833454
					RestTemplate restTemplate = new RestTemplate();
					HttpHeaders headers = new HttpHeaders();
					headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
					// headers.set("Authorization", secretKey);
					HttpEntity<String> entity = new HttpEntity<String>(headers);

					ResponseEntity<String> result = restTemplate.exchange(uri, HttpMethod.POST, entity, String.class);

					if (result != null) {
						String json = result.getBody();

						JSONObject jsonObject = new JSONObject(json); 

						String code = jsonObject.get("status").toString();
						String messageId = jsonObject.get("messageId").toString();

						if (code.equals("200")) {
							Sms sms = new Sms();
							sms.setUser(user);
							sms.setNumber(destination);
							sms.setMessage(message);
							Date date = Calendar.getInstance().getTime(); 
							System.out.println("date "+date);
							DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");  
							String strDate = dateFormat.format(date);
							System.out.println(strDate);
							sms.setSmsDate(strDate);
							sms.setType("OUTGOING");
							sms.setRead(true);
							sms.setMessageId(messageId);
							sms.setActive(true);
							sms.setSource(sourceVN);
							sms.setDestination(destination);

//							if (jsonObject.get("rate").toString() != null)
//								sms.setAmount(Double.parseDouble(jsonObject.get("rate").toString()));
//							else
							sms.setAmount(0.0);

							Sms savedSms = smsRepository.save(sms);
							if (savedSms != null) {
								response.setStatus("1");
//								response.setData(sms);
								response.setData(sms);
								response.setMessage("SMS sent successfully");

								// response.setData(savedSms);
							} else {
								response.setStatus("6");
							}
						} else {
							response.setStatus("8");
							response.setMessage("Failed to send SMS.");
						}
					} else {
						response.setStatus("7");
						response.setMessage("Failed to send SMS from SMPP");
					}

				} catch (Exception e) {
					e.printStackTrace();
					response.setStatus("10");
					response.setMessage("Virtual Number is not assigned for this user!");
				}

			} else {
				response.setStatus("9");
				response.setMessage("Virtual Number is not assigned for this user!");
			}

		} else {
			response.setStatus("2");
		}
		return response;
	}

	@Override
	public List<Sms> findByUserId(Long id) {

		return smsRepository.findByUserId(id);
	}

	@Override
	public void deleteAll(List<Sms> sms) {
		try {
			smsRepository.deleteAll(sms);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public Sms smsDeleteById(SmsDeleteById request) {
		try {
			if (request.getId() != null || request.getUserId() != null) {
				Sms sms = smsRepository.findByIdAndUserId(request.getId(), request.getUserId());
				if (sms != null) {
					DeletedSms deletedSms = new DeletedSms();

					deletedSms.setActive(sms.isActive());
					deletedSms.setAmount(sms.getAmount());
					deletedSms.setDestination(sms.getDestination());
					deletedSms.setMessage(sms.getMessage());
					deletedSms.setUser(sms.getUser());
					deletedSms.setNumber(sms.getNumber());
					deletedSms.setSmsDate(sms.getSmsDate());
					deletedSms.setType(sms.getType());
					deletedSms.setRead(sms.isRead());
					deletedSms.setSource(sms.getSource());
					DeletedSmsRepository.save(deletedSms);

				}
				smsRepository.delete(sms);
				return sms;

			}
		}

		catch (Exception e) {
			e.printStackTrace();

		}
		return null;
	}

//	public List<Sms> smsDeleteByUserId(IDRequest request) {
//		try {
//			if (request.getId() != null) {
//				List<Sms> sms = smsRepository.findAllByUserId(request.getId());
//				for (Sms result : sms) {
//					if (sms != null) {
//						DeletedSms deletedSms = new DeletedSms();
//						deletedSms.setActive(result.isActive());
//						deletedSms.setAmount(result.getAmount());
//						deletedSms.setDestination(result.getDestination());
//						deletedSms.setMessage(result.getMessage());
//						deletedSms.setUser(result.getUser());
//						deletedSms.setNumber(result.getNumber());
//						deletedSms.setSmsDate(result.getSmsDate());
//						deletedSms.setType(result.getType());
//						deletedSms.setRead(result.isRead());
//						deletedSms.setSource(result.getSource());
//						DeletedSmsRepository.save(deletedSms);
//					}
//					smsRepository.delete(result);
//				}
//				return sms;
//
//			}
//			// smsRepository.deleteAll(result);
//
//		} catch (Exception e) {
//			e.printStackTrace();
//
//		}
//		return null;
//	}
//	public static void main(String jkdh[]) {
//		Timestamp currentDateTime = new Timestamp(System.currentTimeMillis());
//		
//		Long timestamp=currentDateTime.getTime();
//        System.out.println("Current Date Timestamp : " + timestamp);            // 1616574866666
//
//					PushNotificationService.sendPushNotificationFromIos("7CDB91005F0B67609944650516EFA4A602553A8EA6E4E10AF630789A0B6DB046", // remove
//																										// voip
//							String.valueOf(timestamp), "ios testing", "61485833460", "SMS");
//					// return "success";
//				}
//			

	@Override
	public List<Sms> smsDeleteByUserId(DeletedSmsFromDid request) {
		// TODO Auto-generated method stub
		try {
			if (request.getUser_id() != null|| request.getDid()!=null) {
				List<Sms> sms = smsRepository.findAllByUserIdAndNumber(request.getUser_id(),request.getDid());
				for (Sms result : sms) {
					if (sms != null) {
						DeletedSms deletedSms = new DeletedSms();
						deletedSms.setActive(result.isActive());
						deletedSms.setAmount(result.getAmount());
						deletedSms.setDestination(result.getDestination());
						deletedSms.setMessage(result.getMessage());
						deletedSms.setUser(result.getUser());
						deletedSms.setNumber(result.getNumber());
						deletedSms.setSmsDate(result.getSmsDate());
						deletedSms.setType(result.getType());
						deletedSms.setRead(result.isRead());
						deletedSms.setSource(result.getSource());
						DeletedSmsRepository.save(deletedSms);
					}
					smsRepository.delete(result);
				}
				return sms;

			}
			// smsRepository.deleteAll(result);

		} catch (Exception e) {
			e.printStackTrace();

		}
		return null;

	}

	@Override
	public List<Sms> getSmsById(Long id) {
		ArrayList result=new ArrayList();
		try {
		
				Sms sms = smsRepository.findById(id).orElse(null);				
			
				 
					if (sms != null) {
						
						List<Sms>	list = smsRepository.findByUserIdOrderByIdDesc(sms.getUser().getId());
						System.out.print("count "+list.size());
						for(Sms response:list) {
							  if(response.getId()>=id) {
								  result.add(response);
								  
							  }
							  
						}
						System.out.print("count "+result.size());
						return result;
					}

		} catch (Exception e) {
			e.printStackTrace();

		}
		return null;
	}
		
	public static void main(String jhhdj[]) {
		String d="2022-08-22T10:38:18";
		
		 try {
			Date date=new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").parse(d);
			System.out.println("date--> "+date);
			
			DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");  
			String strDate = dateFormat.format(date);
			System.out.println("New Formated date--> "+strDate);
		} catch (ParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

			 
	}	

}
