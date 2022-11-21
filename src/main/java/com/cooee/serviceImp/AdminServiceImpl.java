package com.cooee.serviceImp;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.util.List;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.apache.commons.codec.binary.Hex;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import com.cooee.model.AccountNumberDetails;
import com.cooee.model.Admin;
import com.cooee.model.EmailTemplates;
import com.cooee.model.LoginDevice;
import com.cooee.model.RejectKycDetails;
import com.cooee.model.User;
import com.cooee.model.VirtualNumbers;
import com.cooee.payload.BasicResponsePayload;
import com.cooee.payload.BlockOrUnblockRequest;
import com.cooee.payload.EmailDataPayload;
import com.cooee.payload.IDRequest;
import com.cooee.payload.MultipleResponse;
import com.cooee.payload.RejectKycRequest;
import com.cooee.payload.ResetPassword;
import com.cooee.payload.ResetUserPassword;
import com.cooee.payload.UsernameAndPasswordRequest;
import com.cooee.repository.AccountNumberRepository;
import com.cooee.repository.AdminRepository;
import com.cooee.repository.EmailTemplatesRepository;
import com.cooee.repository.LoginDeviceRepository;
import com.cooee.repository.RejectKycDetailsRepo;
import com.cooee.repository.UserRepository;
import com.cooee.repository.VirtualNumbersRepository;
import com.cooee.responsemessgae.CONSTANTMESSAGE;
import com.cooee.service.AccountNumberDetailsService;
import com.cooee.service.AdminService;
import com.cooee.service.EmailTemplatesService;
import com.cooee.service.RejectKycDetailsService;
import com.cooee.service.UserService;
import com.cooee.service.VirtualNumbersService;
import com.cooee.util.AESCipher;
import com.cooee.util.Constant;
import com.cooee.util.EmailBody;
import com.cooee.util.EmailSender;
import com.cooee.util.IUtil;

@Service
public class AdminServiceImpl implements AdminService {

	@Value("${file-location-url}")
	private String fileLocationURL;

	@Value("${file-upload-location}")
	private String fileURL;

	@Value("${asteriskUrl}")
	private String asteriskUrl;

	@Value("${asteriskApISecretKey}")
	private String asteriskApISecretKey;

	@Autowired
	private VirtualNumbersRepository virtualNumbersRepository;

	@Autowired
	private LoginDeviceRepository loginDeviceRepository;

	@Autowired
	private AccountNumberRepository accountNumberRepository;

	@Autowired
	private RejectKycDetailsRepo rejectKycDetailsRepo;

	@Autowired
	private AdminRepository adminRepository;

	@Autowired
	private RejectKycDetailsService rejectKycService;

	@Autowired
	private EmailSender emailSender;

	@Autowired
	private UserService userService;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private AccountNumberDetailsService accountNumberDetailsService;

	@Autowired
	private VirtualNumbersService virtualNumbersService;

	@Autowired
	LoginDeviceRepository loginDeviceRepo;

	@Autowired
	private EmailTemplatesService emailTemplatesService;
	
	@Autowired
	private RejectKycDetailsRepo rejectKycSetailsRepo;

	@Override
	public Admin findByUsernameAndPassword(UsernameAndPasswordRequest request) {
		return adminRepository.findByUsernameAndPassword(request.getUsername(), request.getPassword());
	}

	@Override
	public MultipleResponse approveKyc(IDRequest iDRequest) {

		MultipleResponse response = new MultipleResponse();

		User user = userService.findById(iDRequest.getId());

		if (user != null) {
			if (user.getApproved() != 2) {
				VirtualNumbers number = virtualNumbersRepository.findByVirtualNumber(user.getDid());
				if (number != null) {
					if (number.getIsAvailable()) {
//--------------------------------------------sip username and sip password------------------------------------------------------------------
						String password = IUtil.randomPassword(8);
						
						// 1. Create SIP Credentials

						String username = "480000001";

						AccountNumberDetails accountNumberDetail = accountNumberDetailsService.findLastEntry();

						if (accountNumberDetail != null) {
							String sipUsername = accountNumberDetail.getSipUsername();
							System.out.println("sipUsername is " + sipUsername);
							// String fixed = sipUsername.substring(8);
							if (sipUsername.startsWith("4800")) {
								Long add = Long.parseLong(sipUsername) + 1;
								username = String.valueOf(add);

							}

						}
						
//-------------------------------------------------------------------------------------------------------------------------------------------
						AccountNumberDetails savedSip = accountNumberDetailsService.add(user.getDid(), password, "",
								user.getDid(), user);
						if (savedSip != null) {
							user.setAccountNumberDetails(savedSip);
						} else {
							response.setType(4);
							return response;
						}

						MultipleResponse didResponse = virtualNumbersService.mapDID(user.getDid(), user);
						if (didResponse.getType() == 2) {
							System.out.println(user);
							response.setType(2);
							return response;
						} else if (didResponse.getType() == 3) {
							response.setType(3);
							return response;
						}

						String mailBody = EmailBody
								.approveKycEmail(emailTemplatesService.getEmailByType(CONSTANTMESSAGE.APPROVEKYC));
						emailSender.sendHTMLMessage(user.getEmail(), "Approve Kyc", mailBody);

												user.setIsDocumentVerified(true);
						user.setApproved(2);
						User savedUser = userService.saveUser(user);

						response.setType(1);
						response.setUser(savedUser);

						} else {
						response.setType(3);
					}
				} else {
					response.setType(2);
				}
			} else {
				response.setType(6);
			}
		} else {
			response.setType(5);
		}

		return response;
	}

	@Override
	public MultipleResponse rejectKyc(RejectKycRequest rejectKycRequest) {
		MultipleResponse multipleResponse = new MultipleResponse();
		try {
				User user = userService.findById(rejectKycRequest.getUserId());
				if (user != null) {
					
				
				VirtualNumbers virtualNumbers = virtualNumbersRepository.findByVirtualNumber(user.getDid());
				if(virtualNumbers!=null) {
					if(!virtualNumbers.getIsAvailable()) {
				if (virtualNumbers.getUser().getId() !=rejectKycRequest.getUserId() ) {
					

						Timestamp currentDateTime = new Timestamp(System.currentTimeMillis());
						RejectKycDetails newKyc = new RejectKycDetails();

						newKyc.setActive(true);
						newKyc.setDescription("did already assigned");
						newKyc.setCreationDate(currentDateTime);
						newKyc.setUser(user);

						
						rejectKycSetailsRepo.save(newKyc);
						user.setApproved(3);
						//multipleResponse.setType(1);
						user = userRepository.save(user);
						multipleResponse.setType(4);//did already assigned
						
				}
					
				
				
				else if (virtualNumbers.getUser().getId() ==rejectKycRequest.getUserId()) { 
	
 
					
					AccountNumberDetails accountNumber = accountNumberRepository.findByUserId(user.getId());
					if (accountNumber != null) {

						JSONObject jsonObject = new JSONObject();
						jsonObject.put("username", accountNumber.getSipUsername());
						jsonObject.put("email", user.getEmail());
						jsonObject.put("ringGrpNum", "0");

						RestTemplate restTemplate = new RestTemplate();
						HttpHeaders headers = new HttpHeaders();
						headers.setContentType(MediaType.APPLICATION_JSON);
						headers.set("Authorization", asteriskApISecretKey);
						HttpEntity<String> entity = new HttpEntity<String>(jsonObject.toString(), headers);

						final String uri = asteriskUrl + "user/deleteUser";
						ResponseEntity<BasicResponsePayload> result = restTemplate.exchange(uri, HttpMethod.POST, entity,
								BasicResponsePayload.class);

						BasicResponsePayload response = result.getBody();
					 accountNumberRepository.delete(accountNumber);
					}
					
					VirtualNumbers number = virtualNumbersRepository.findByVirtualNumber(user.getDid());
					if (number != null) {
						if (!number.getIsAvailable()) {
							number.setIsAvailable(true);

							number.setCallDestinationId("");
							number.setUser(null);
							virtualNumbersRepository.save(number);
						}
					}

					user.setApproved(3);
					multipleResponse.setType(1);
					user = userRepository.save(user);
					
				
						rejectKycService.rejectKycDetails(rejectKycRequest.getDescriptions(), user);

						// send push notification on Reject kyc details
//						List<LoginDevice> loginDeviceList = loginDeviceRepo.findAllByUser(user);
//						String kycRejectTitle = Constant.KYC_REJECT_TITLE;
//						String kycRejectMessage = Constant.KYC_REJECT_MESSAGE;
//						String notificationType = Constant.NOTI_KYC_REJECTED;
//						for (LoginDevice loginDevice : loginDeviceList) {
//
//							if (loginDevice.getIsDeviceActive() != null && loginDevice.getIsDeviceActive()) {
//								if (loginDevice.getDeviceType().equalsIgnoreCase(Constant.ANDROID_DEVICE_TYPE)) {
//									// send notification to android device
//									PushNotificationService.sendPushNotificationFromAdndroid(
//											loginDevice.getDeviceToken(), kycRejectTitle, kycRejectMessage, null,
//											notificationType);
//								} else if (loginDevice.getDeviceType().equalsIgnoreCase(Constant.IOS_DEVICE_TYPE)) {
//									// send notification to ios device
//									PushNotificationService.sendPushNotificationFromAdndroid(
//											loginDevice.getDeviceToken(), kycRejectTitle, kycRejectMessage, null,
//											notificationType);
//								}
//							}
//						}
					
//-----------------------------------------------------------------------------------------------------------------------------------
					
				}
				}
					
				else {
					rejectKycService.rejectKycDetails(rejectKycRequest.getDescriptions(), user);
					user.setApproved(3);
					multipleResponse.setType(1);
					user = userRepository.save(user);
					//multipleResponse.setType(2);//already reject
				}
				
					}	
				else {
					multipleResponse.setType(3);//did does not exist
				}
				
				}
				
				
					else {
				multipleResponse.setType(2);//user not found
			}

		} catch (Exception e) {
			multipleResponse.setType(6);
			e.printStackTrace();
			// multipleResponse.setType(4);
		}

		return multipleResponse;

	}

	@Override
	public User updatePassword(String email, String password) {
		User user = null;
		try {
			user = this.findByEmail(email);
			if (user != null) {
				String enCodedPassword = AESCipher.aesEncryptString(password, CONSTANTMESSAGE.key16Byte);
				user.setPassword(enCodedPassword);
				// user.setPassword(password);
				return userRepository.save(user);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return user;
	}

	public User findByEmail(String email) {
		return userRepository.findByEmail(email);
	}

	@Override
	public User resetPassword(ResetPassword request) {
		User user = userRepository.findByEmail(request.getEmail());

		if (user != null) {
			String hex = "";

			try {
				String enCodedString = AESCipher.aesEncryptString(user.getEmail(), CONSTANTMESSAGE.key16Byte);

				char[] result = Hex.encodeHex(enCodedString.getBytes(StandardCharsets.UTF_8));
				hex = new String(result);

			} catch (InvalidKeyException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (NoSuchAlgorithmException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (NoSuchPaddingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvalidAlgorithmParameterException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalBlockSizeException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (BadPaddingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			// String url = baseURL + "resetPasswordForm/" + hex;

			// String url = "http://36.255.3.15:8086/cooeeadmin/#/reset?id="+user.getId();
			//String url = "http://dollarsimclub.com/cooeeadmin/#/reset?id=" + user.getId();
			String url = "http://secureparcelservice.com/cooeeadmin/#/reset?id=" + user.getId();

			String body = EmailBody.passwordReset(url,
					emailTemplatesService.getEmailByType(CONSTANTMESSAGE.RESETPASSWORD));// "http://36.255.3.15:8086/cooeeadmin/#/reset"
			// String body =
			// EmailBody.passwordReset(CONSTANTMESSAGE.url);//"http://36.255.3.15:8086/cooeeadmin/#/reset"
			emailSender.sendHTMLMessage(user.getEmail(), "Resest Password", body);
		}
		return user;
	}

//	@Override
//	public User resetPassword(ResetPassword request) {
//		User user = userRepository.findByEmail(request.getEmail());
//
//		if (user != null) {
//			String hex = "";
//
//			try {
//				String enCodedString = AESCipher.aesEncryptString(user.getEmail(), CONSTANTMESSAGE.key16Byte);
//
//				char[] result = Hex.encodeHex(enCodedString.getBytes(StandardCharsets.UTF_8));
//				hex = new String(result);
//
//			} catch (InvalidKeyException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			} catch (NoSuchAlgorithmException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			} catch (NoSuchPaddingException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			} catch (InvalidAlgorithmParameterException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			} catch (IllegalBlockSizeException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			} catch (BadPaddingException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			} catch (UnsupportedEncodingException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//
////			String url = baseURL + "resetPasswordForm/" + hex;
//
//			// String url = CONSTANTMESSAGE.url;
//			String body = EmailBody.passwordReset(CONSTANTMESSAGE.url);
//
//			emailSender.sendHTMLMessage(user.getEmail(), "Resest Password", body);
//		}
//		return user;
//	}

	@Override
	public User blockOrUnblock(BlockOrUnblockRequest blockOrUnblockRequest) {
		User user = userRepository.findById(blockOrUnblockRequest.getId()).orElse(null);

		try {
			if (user != null) {
				user.setIsBlocked(blockOrUnblockRequest.getIsBlocked());
				return userRepository.save(user);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return user;
	}

	@Override
	public String uploadPdfWithName(MultipartFile mulfile, String filename) {

		if (!mulfile.isEmpty()) {

			try {
				File file = new File(fileURL + filename);
				if (file.exists()) {
					file.delete();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

			try {
				byte[] bytes = mulfile.getBytes();

				BufferedOutputStream buffStream = new BufferedOutputStream(
						new FileOutputStream(fileURL + filename, true));
				buffStream.write(bytes);
				buffStream.close();
				System.out.println(fileURL);

				return fileLocationURL + filename;

			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return null;
	}

	@Override
	public User resetUserPassword(ResetUserPassword request) {
		try {
			User user = userRepository.findById(request.getId()).orElse(null);
			if (user != null) {
				String enCodedPassword = AESCipher.aesEncryptString(request.getPassword(), CONSTANTMESSAGE.key16Byte);
				user.setPassword(enCodedPassword);
				// user.setPassword(request.getPassword());

				String mailBody = EmailBody.resetPasswordForUser(request.getPassword(),
						emailTemplatesService.getEmailByType(CONSTANTMESSAGE.RESETPASSWORDFORUSER));
				emailSender.sendHTMLMessage(user.getEmail(), "Reset Password  ", mailBody);

				return userRepository.save(user);

			}
		} catch (Exception e) {
			e.printStackTrace();

		}
		return null;

	}

//	@Override
//	@Transactional(value = "transactionManager")
//	public int deleteUser(Long id) {
//		try {
//			User user = userRepository.findById(id).orElse(null);
//			if (user != null) {
//
//				AccountNumberDetails accountNumber = accountNumberRepository.findByUserId(user.getId());
//				if (accountNumber != null) {
//
//					JSONObject jsonObject = new JSONObject();
//					jsonObject.put("username", accountNumber.getSipUsername());
//					jsonObject.put("email", user.getEmail());
//					jsonObject.put("ringGrpNum", "0");
//
//					RestTemplate restTemplate = new RestTemplate();
//					HttpHeaders headers = new HttpHeaders();
//					headers.setContentType(MediaType.APPLICATION_JSON);
//					headers.set("Authorization", asteriskApISecretKey);
//					HttpEntity<String> entity = new HttpEntity<String>(jsonObject.toString(), headers);
//
//					final String uri = asteriskUrl + "user/deleteUser";
//					ResponseEntity<BasicResponsePayload> result = restTemplate.exchange(uri, HttpMethod.POST, entity,
//							BasicResponsePayload.class);
//
//					BasicResponsePayload response = result.getBody();
//
//					if (response.getStatus().equals("1")) {
//
//						List<VirtualNumbers> virtualNumbers = virtualNumbersRepository.findByUserId(user.getId());
//
//						if (virtualNumbers != null && !virtualNumbers.isEmpty()) {
//							for (VirtualNumbers number : virtualNumbers) {
//								number.setUser(null);
//								number.setIsAvailable(true);
//							}
//							virtualNumbersRepository.saveAll(virtualNumbers);
//						}
//
//						
//
//						List<RejectKycDetails> rejectKycDetails = rejectKycDetailsRepo.findByUserId(user.getId());
//						if (rejectKycDetails != null && !rejectKycDetails.isEmpty()) {
//							rejectKycDetailsRepo.deleteAll(rejectKycDetails);
//						}
//
////				List<Sms> sms = smsServiceImp.findByUserId(user.getId());
////				if (sms != null && !sms.isEmpty()) {
////					smsServiceImp.deleteAll(sms);
////				}
//
////				sendPushNotificationsToOrgUserTokens(user.getOrganizationId().getId(), null, "delete user", userId);
//
//					}
//					accountNumberRepository.delete(accountNumber);
//
//				} 
//
//				userRepository.delete(user);
//
//				return 1;
//			} else
//				return 2;
//		} catch (Exception e) {794
//			e.printStackTrace();
//			return 0;
//		}
//	}
	@Override
	@Transactional(value = "transactionManager")
	public int deleteUser(Long id) {
		try {
			User user = userRepository.findById(id).orElse(null);
			if (user != null) {

				AccountNumberDetails accountNumber = accountNumberRepository.findByUserId(user.getId());
				if (accountNumber != null) {

					JSONObject jsonObject = new JSONObject();
					jsonObject.put("username", accountNumber.getSipUsername());
					jsonObject.put("email", user.getEmail());
					jsonObject.put("ringGrpNum", "0");

					RestTemplate restTemplate = new RestTemplate();
					HttpHeaders headers = new HttpHeaders();
					headers.setContentType(MediaType.APPLICATION_JSON);
					headers.set("Authorization", asteriskApISecretKey);
					HttpEntity<String> entity = new HttpEntity<String>(jsonObject.toString(), headers);

					final String uri = asteriskUrl + "user/deleteUser";
					ResponseEntity<BasicResponsePayload> result = restTemplate.exchange(uri, HttpMethod.POST, entity,
							BasicResponsePayload.class);

					BasicResponsePayload response = result.getBody();
					accountNumberRepository.delete(accountNumber);
				}

				List<VirtualNumbers> virtualNumbers = virtualNumbersRepository.findByUserId(user.getId());

				if (virtualNumbers != null && !virtualNumbers.isEmpty()) {
					for (VirtualNumbers number : virtualNumbers) {
						number.setUser(null);
						number.setIsAvailable(true);
					}
					virtualNumbersRepository.saveAll(virtualNumbers);
				}

				List<RejectKycDetails> rejectKycDetails = rejectKycDetailsRepo.findByUserId(user.getId());
				if (rejectKycDetails != null && !rejectKycDetails.isEmpty()) {
					rejectKycDetailsRepo.deleteAll(rejectKycDetails);
				}

				List<LoginDevice> loginDevice = loginDeviceRepo.findByUserId(user.getId());
				if (loginDevice != null && !loginDevice.isEmpty()) {
					loginDeviceRepo.deleteAll(loginDevice);
				}

				userRepository.delete(user);

				return 1;
			} else
				return 2;
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}

}
