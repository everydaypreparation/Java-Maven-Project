package com.cooee.serviceImp;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.apache.commons.codec.binary.Hex;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.support.ReplaceOverride;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.cooee.model.AccountNumberDetails;
import com.cooee.model.LoginDevice;
import com.cooee.model.SendByEmail;
import com.cooee.model.Sms;
import com.cooee.model.User;
import com.cooee.model.UserContactNoBlock;
import com.cooee.model.VirtualNumbers;
import com.cooee.payload.ChangeMail;
import com.cooee.payload.ChangePassword;
import com.cooee.payload.DashboardResponse;
import com.cooee.payload.EmailAndPasswordRequest;
import com.cooee.payload.FileUploadRequest;
import com.cooee.payload.GetAllResponse;
import com.cooee.payload.IDRequest;
import com.cooee.payload.LoginRequest;
import com.cooee.payload.MultipleResponse;
import com.cooee.payload.OtpVerificationForEmailRequest;
import com.cooee.payload.OtpVerificationRequest;
import com.cooee.payload.PushNotificationPayload;
import com.cooee.payload.PushNotificationRequest;
import com.cooee.payload.ResetPassword;
import com.cooee.payload.SendOtp;
import com.cooee.payload.SignUp;
import com.cooee.payload.SocialSignupRequest;
import com.cooee.payload.UserContactNoBlockPayload;
import com.cooee.payload.UserContactNoUnblockPayload;
import com.cooee.payload.WordpressLoginRequest;
import com.cooee.repository.AccountNumberRepository;
import com.cooee.repository.EmailTemplatesRepository;
import com.cooee.repository.LoginDeviceRepository;
import com.cooee.repository.RejectKycDetailsRepo;
import com.cooee.repository.SendByEmailRepo;
import com.cooee.repository.UserContactNoBlockRepository;
import com.cooee.repository.UserRepository;
import com.cooee.repository.VirtualNumbersRepository;
import com.cooee.responsemessgae.CONSTANTMESSAGE;
import com.cooee.service.EmailTemplatesService;
import com.cooee.service.UserService;
import com.cooee.util.AESCipher;
import com.cooee.util.Constant;
import com.cooee.util.EmailBody;
import com.cooee.util.EmailSender;
import com.cooee.util.FileUploader;
import com.cooee.util.IUtil;
import com.stripe.Stripe;
import com.stripe.model.Customer;

@Service
public class UserServiceImp implements UserService {

	@Value("${asteriskUrl}")
	private String asteriskUrl;

	@Value("${asteriskApISecretKey}")
	private String asteriskApISecretKey;

	@Autowired
	private SmsServiceImp smsServiceImp;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private EmailSender emailSender;

	@Autowired
	private PushNotificationService pushNotificationService;

	@Value("${image-upload-location}")
	private String imageUploadLocation;

	@Value("${image-location-url}")
	private String imageLocationURL;

	@Value("${base-url}")
	private String baseURL;

	@Autowired
	private VirtualNumbersRepository virtualNumbersRepository;

	@Autowired
	private LoginDeviceRepository loginDeviceRepository;

	@Autowired
	private AccountNumberRepository accountNumberRepository;

	@Autowired
	private RejectKycDetailsRepo rejectKycDetailsRepo;

	@Autowired
	private EmailTemplatesService emailTemplatesService;

	@Autowired
	private UserContactNoBlockRepository userContactNoBlockRepository;

	@Autowired
	private EmailTemplatesRepository emailTemplatesRepository;

	@Autowired
	private SendByEmailRepo sendByEmailRepo;

	@Override
	public User save(EmailAndPasswordRequest request) {
		try {
			User user = new User();

			String cooeeId = "CI" + IUtil.getSixDigitRandomNumbers();

			user.setCooeeId(cooeeId);
			Timestamp currentDateTime = new Timestamp(System.currentTimeMillis());

			user.setEmail(request.getEmail());

			// Encyption
			String enCodedPassword = AESCipher.aesEncryptString(request.getPassword(), CONSTANTMESSAGE.key16Byte);
			user.setPassword(enCodedPassword);

			user.setActive(true);
			user.setCreationDate(currentDateTime);
			user.setUpdationDate(currentDateTime);
			user.setIsVerified(false);

			int otp = IUtil.getSixDigitRandomNumbers();
			user.setOtp(otp);

			user.setOtpCreationTime(currentDateTime);

			String mailBody = EmailBody.otpVerifictionForSignup(otp,
					emailTemplatesService.getEmailByType(CONSTANTMESSAGE.SIGNUP));
			emailSender.sendHTMLMessage(user.getEmail(), "Account Registration  ", mailBody);

			user.setApproved(0);
			user.setIsDocumentVerified(false);
			user.setIsBlocked(false);
			user.setIsPorting(false);

			Stripe.apiKey = CONSTANTMESSAGE.Secretkey;
			Map<String, Object> params = new HashMap<>();
			params.put("email", request.getEmail());
			Customer customer = Customer.create(params);
			user.setStipeCustomerId(customer.getId());

			return userRepository.save(user);

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public MultipleResponse otpVerification(OtpVerificationRequest request) {
		MultipleResponse response = new MultipleResponse();

		User user = userRepository.findByEmail(request.getEmail());

		if (user != null) {
			if (IUtil.checkOtpExpire(user.getOtpCreationTime())) {

				if (user.getOtp().equals(request.getOtp())) {
					user.getCreationDate();
					user.setIsVerified(true);
					response.setUser(userRepository.save(user));
					response.setType(1);
				} else {
					response.setType(3);
				}
			} else {
				response.setType(4);
			}
		} else {
			response.setType(2);
		}
		return response;
	}

	@Override
	public MultipleResponse otpVerificationForEmail(OtpVerificationForEmailRequest request) {
		MultipleResponse response = new MultipleResponse();

		User user = userRepository.findByEmail(request.getEmail());
		if (user != null) {
			if (IUtil.checkOtpExpire(user.getOtpCreationTime())) {

				if (user.getOtp().equals(request.getOtp())) {

					user.setEmail(user.getTempEmail());
					response.setUser(userRepository.save(user));
					response.setType(1);
				} else {
					response.setType(3);
				}
			} else {
				response.setType(4);
			}
		} else {
			response.setType(2);
		}
		return response;
	}


	@Override
	public MultipleResponse login(LoginRequest request) {

		MultipleResponse response = new MultipleResponse();
		try {

			User user = this.findByEmailAndPassword(request);
			if (user != null) {

				if (user.getIsVerified()) {
					response.setType(1);

					if (user.getStipeCustomerId() == null) {
						Stripe.apiKey = CONSTANTMESSAGE.Secretkey;
						Map<String, Object> params = new HashMap<>();
						params.put("email", request.getEmail());
						Customer customer = Customer.create(params);
						user.setStipeCustomerId(customer.getId());
						userRepository.save(user);
					}

					AccountNumberDetails ac = accountNumberRepository.findByUserId(user.getId());
					if (ac != null)
						user.setAccountNumberDetails(ac);

					response.setUser(user);
				} else {
					int otp = IUtil.getSixDigitRandomNumbers();
					user.setOtp(otp);

					String mailBody = EmailBody.otpVerifiction(otp,
							emailTemplatesService.getEmailByType(CONSTANTMESSAGE.LOGIN));
					emailSender.sendHTMLMessage(user.getEmail(), "OTP for login", mailBody);

					Timestamp currentDateTime = new Timestamp(System.currentTimeMillis());
					user.setOtpCreationTime(currentDateTime);

					userRepository.save(user);
					response.setType(2);
				}
			} else

				response.setType(3);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return response;
	}

	@Override
	public User addUser(SignUp signUp) {
		User user = new User();

		try {
			Date date1 = new java.sql.Date(
					((java.util.Date) new SimpleDateFormat("dd/MM/yyyy").parse(signUp.getDob())).getTime());

			user.setDob(date1);

			user.setFirstName(signUp.getFirstName());
			user.setLastName(signUp.getLastName());
			user.setEmail(signUp.getEmail());
			user.setPassword(signUp.getPassword());
			user.setDob(date1);
			user.setCurrentServiceProvider(signUp.getCurrentServiceProvider());
			user.setCountry(signUp.getCountry());
			user.setAddress(signUp.getAddress());
			user.setCity(signUp.getCity());
			user.setState(signUp.getState());
			user.setMobileNumber(signUp.getMobileNumber());
			user.setPostalCode(signUp.getPostalCode());

			user.setActive(true);
			user.setCreationDate(new Timestamp(System.currentTimeMillis()));
			user.setIsVerified(false);
			user = userRepository.save(user);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return user;
	}

	@Override
	public User findByEmailAndPassword(LoginRequest request) {
		String enCodedPassword = request.getPassword();

		try {
			enCodedPassword = AESCipher.aesEncryptString(request.getPassword(), CONSTANTMESSAGE.key16Byte);
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
		return userRepository.findByEmailAndPassword(request.getEmail(), enCodedPassword);
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

			String url = "http://secureparcelservice.com:8080/cooeeadmin/#/reset?id=" + user.getId();

			//String url = "http://dollarsimclub.com:8080/cooeeadmin/#/reset?id=" + user.getId();

			String body = EmailBody.passwordReset(url,
					emailTemplatesService.getEmailByType(CONSTANTMESSAGE.RESETPASSWORD));

			emailSender.sendHTMLMessage(user.getEmail(), "Resest Password", body);
		}
		return user;
	}

	@Override
	public User sendOtp(SendOtp request) {
		User user = userRepository.findByEmail(request.getEmail());
		if (user != null) {

			int otp = IUtil.getSixDigitRandomNumbers();
			user.setOtp(otp);
			user.setOtpCreationTime(new Timestamp(System.currentTimeMillis()));
			emailSender.sendHTMLMessage(user.getEmail(), "OTP", "Your OTP is: " + otp);
		}
		return user;
	}

	@Override
	public User resendOTP(SendOtp request) {
		User user = userRepository.findByEmail(request.getEmail());
		if (user != null) {
			// Generate New OTP

			int otp = IUtil.getSixDigitRandomNumbers();
			user.setOtp(otp);
			user.setOtpCreationTime(new Timestamp(System.currentTimeMillis()));
			emailSender.sendHTMLMessage(user.getEmail(), "OTP", "Your OTP is: " + otp);
			return userRepository.save(user);
		}
		return user;
	}

	@Override
	public User findByEmail(String email) {
		return userRepository.findByEmail(email);
	}

	@Override
	public User resetPasswordUrl(ResetPassword request) {
		User user = userRepository.findByEmail(request.getEmail());

		if (user != null) {

		}
		return user;
	}

	

	@Override
	public User updatePassword(String email, String password) {
		User user = null;
		try {

			user = this.findByEmail(email);
			if (user != null) {
				String enCodedPassword = AESCipher.aesEncryptString(password, CONSTANTMESSAGE.key16Byte);
				user.setPassword(enCodedPassword);
				return userRepository.save(user);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return user;
	}

	@Override
	public User findById(Long id) {
		return userRepository.findById(id).orElse(null);
	}

	@Override
	public User submitKYC(FileUploadRequest request) {

		System.out.println("User Id - " + request.getId());
		System.out.println("idCardFront - " + request.getIdCardType());
		System.out.println("idCardFront - " + request.getIdCardFront());
		System.out.println("idCardBack - " + request.getIdCardBack());
		System.out.println("idCardSelfie - " + request.getIdCardSelfie());
		System.out.println("isPoritng - " + request.getIsPorting());

		User user = this.findById(request.getId());
		if (request.getIsPorting() == null) {
			request.setIsPorting(false);
		}
		if (user != null) {

			Date date1 = null;
			try {
				date1 = new java.sql.Date(
						((java.util.Date) new SimpleDateFormat("dd/MM/yyyy").parse(request.getDob())).getTime());
			} catch (ParseException e) {
				e.printStackTrace();
			}

			user.setDob(date1);

			user.setFirstName(request.getFirstName());
			user.setLastName(request.getLastName());
			user.setCurrentServiceProvider(request.getCurrentServiceProvider());
			user.setOptusAccountNumber(request.getOptusAccountNumber());

			user.setCountry(request.getCountry());
			user.setAddress(request.getAddress());
			user.setCity(request.getCity());
			user.setState(request.getState());
			user.setPostalCode(request.getPostalCode());

			user.setActive(true);
			user.setApproved(1);
			user.setCreationDate(new Timestamp(System.currentTimeMillis()));
			user.setUpdationDate(new Timestamp(System.currentTimeMillis()));

			user.setIdCardType(request.getIdCardType());


			user.setIsPorting(request.getIsPorting());
			user.setMobileNumber(request.getMobileNumber());

			user.setDid(request.getDidNumber());

			User savedUser = userRepository.save(user);
			if (savedUser != null) {
				String fileName = "";
				if (request.getIdCardFront() != null) {
					fileName = FileUploader.uploadIdCard(request.getIdCardFront(), imageUploadLocation,
							savedUser.getId());
					String fileURL = imageLocationURL + "images/" + fileName;
					savedUser.setIdCardFront(fileURL);

				}
				if (request.getIdCardBack() != null) {
					fileName = FileUploader.uploadIdCard(request.getIdCardBack(), imageUploadLocation,
							savedUser.getId());
					String fileURL = imageLocationURL + "images/" + fileName;
					savedUser.setIdCardBack(fileURL);

				}
				if (request.getIdCardSelfie() != null) {
					fileName = FileUploader.uploadIdCard(request.getIdCardSelfie(), imageUploadLocation,
							savedUser.getId());
					String fileURL = imageLocationURL + "images/" + fileName;
					savedUser.setIdCardSelfie(fileURL);
				}

				return userRepository.save(savedUser);
			}
		}
		return user;
	}

	@Override
	public GetAllResponse getAllUsers(Integer pageNo, Integer pageSize, String keyword, Integer approved) {
		GetAllResponse getAllResponse = new GetAllResponse();
		Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by("id").descending());
		Page<User> pagedResult = null;
		Long count = 0l;
		try {
			if (keyword == null || keyword.trim().isEmpty()) {
				if (approved != null) {
					pagedResult = userRepository.findByApproved(approved, paging);
					count = userRepository.countByApproved(approved);
				} else {
					pagedResult = userRepository.findAll(paging);
					count = userRepository.count();
				}
			} else {
				if (approved != null) {
					pagedResult = userRepository
							.findByApprovedAndFirstNameIsContainingIgnoreCaseOrEmailIsContainingIgnoreCase(approved,
									keyword, keyword, paging);
					count = pagedResult.getTotalElements();
				} else {
					pagedResult = userRepository.findAllByFirstNameIsContainingIgnoreCaseOrEmailIsContainingIgnoreCase(
							paging, keyword, keyword);
					count = pagedResult.getTotalElements();
				}
			}
			getAllResponse.setUser(pagedResult.getContent());
			getAllResponse.setTotalRecord(count);
		} catch (Exception e) {
			e.printStackTrace();
			getAllResponse.setUser(new ArrayList<>());
			getAllResponse.setTotalRecord(0l);
		}
		return getAllResponse;
	}

	@Override
	public User saveUser(User user) {
		return userRepository.save(user);
	}

	@Override
	public String sendCallPushNotifications(String source, String destination, String type) {
//		System.out.println("Send Call Push Calling - source = " + source + " | destination = " + destination
//				+ " | type = " + type);
//
//		if (type.equalsIgnoreCase("internal")) {
//
//			if ((source.equalsIgnoreCase("801") || source.equalsIgnoreCase("802") || source.equalsIgnoreCase("803")
//					|| source.equalsIgnoreCase("804") || source.equalsIgnoreCase("805"))
//					&& (destination.equalsIgnoreCase("801") || destination.equalsIgnoreCase("802")
//							|| destination.equalsIgnoreCase("803") || destination.equalsIgnoreCase("804")
//							|| destination.equalsIgnoreCase("805"))) {
//				return destination + ",0";
//			}
//
//			User user = userRepository.findByUsername(source);
//			String usernameDest = "";
//
//			if (user != null) {
//				Organization org = user.getOrganizationId();
//				Long orgId = 0l;
//
//				if (org == null)
//					return "00,0";
//				else
//					orgId = org.getId();
//
//				User userDestination = userRepository.findByUsernameAndOrganizationIdId(destination, orgId);
//
//				if (userDestination != null) {
//					// both cases should be there.
//					boolean blackListStatus = blackListUserRepository
//							.existsBySourceMobileNumberUsernameAndDestinationMobileNumberUsernameOrSourceMobileNumberUsernameAndDestinationMobileNumberUsername(
//									source, destination, destination, source);
//
//					if (blackListStatus) {
//						return "0000000000000";
//					} else {
//						usernameDest = userDestination.getUsername();
//					}
//				} else {
//					usernameDest = Util.createUserNameByOrgIdAndDisplayName(orgId, destination);
//				}
//
//				List<UserTokens> userTokens = userTokensService.getUserListByUsernameAndDeviceType(usernameDest,
//						"android");
//				if (!userTokens.isEmpty()) {
//
//					String tempSource = "";
//					if (source.length() > 10) {
//						tempSource = source.substring(9);
//					} else {
//						tempSource = source;
//					}
//
//					PushNotificationRequest request = new PushNotificationRequest();
//					request.setTitle("Incoming Call");
//
//					userTokensService.sendCallPushNotifications(request, tempSource, userTokens);
//
//				}
//
//				// new
//				userCallTokensService.sendCallPushNotificationsIos(usernameDest);
//
//				return usernameDest + "," + orgId;
//			} else {
//				return usernameDest;
//			}
//		} else if (type.equalsIgnoreCase("external")) {
//			VirtualNumbers virtualNumber = virtualNumberService.getByVirtualNumber(destination);
//
//			System.out.println("Send Call push called = " + source + " Type- " + virtualNumber.getCallDestination());
//
//			if (virtualNumber != null) {
//
//				if (virtualNumber.getCallDestination().equals("EXTENSION")) {
//
//					User user = get(Long.parseLong(virtualNumber.getCallDestinationId()));
//
//					if (user != null) {
//						System.out.println("receiveSms user found for source");
//
//						List<UserTokens> userTokens = userTokensService
//								.getUserListByUsernameAndDeviceType(user.getUsername(), "android");
//						if (!userTokens.isEmpty()) {
//
//							PushNotificationRequest request = new PushNotificationRequest();
//							userTokensService.sendCallPushNotifications(request, source, userTokens);
//						}
//
//						userCallTokensService.sendCallPushNotificationsIos(user.getUsername());
//
//						return destination + "," + user.getOrganizationId().getId();
//					} else {
//						return destination + ",0";
//					}
//				} else if (virtualNumber.getCallDestination().equals("DEPARTMENT")) {
//
//					Department department = departmentService.get(Long.parseLong(virtualNumber.getCallDestinationId()));
//
//					if (department != null) {
//
//						List<User> users = userRepository.findByDepartmentId(department.getId());
//
//						for (User user : users) {
//							LOGGER.info("receiveSms Department found for source");
//
//							List<UserTokens> userTokens = userTokensService
//									.getUserListByUsernameAndDeviceType(user.getUsername(), "android");
//							if (!userTokens.isEmpty()) {
//								LOGGER.info("user token is not empty");
//
//								PushNotificationRequest request = new PushNotificationRequest();
//								userTokensService.sendCallPushNotifications(request, source, userTokens);
//							} else {
//								LOGGER.info("user tokens are empty");
//							}
//
//							userCallTokensService.sendCallPushNotificationsIos(user.getUsername());
//						}
//						return destination + "," + department.getOrganizationId().getId();
//					} else {
//						LOGGER.info("Departmenet is null in given virtualNumber");
//						return destination + ",0";
//					}
//				} else {
//					LOGGER.info("Type not available for given virtual number - " + destination);
//					return destination + ",0";
//				}
//			}
//		} else if (type.equalsIgnoreCase("outbound")) {
//			User user = userRepository.findByUsername(source);
//			if (user != null) {
//				return destination + "," + user.getOrganizationId().getId();
//			} else {
//				return "00000,0";
//			}
//		}
//		return destination + ",0";
		return "fail";
	}

	@Override
	public int sendSmsPushNotifications(String userToken, String source, Sms savedSms, long unreadCount,
			String deviceType) {

		PushNotificationRequest request = new PushNotificationRequest();
		request.setTitle("New Sms");
		request.setMessage("You have received a new Sms from " + source);
		request.setTopic("SMS");

		if (userToken.length() > 10)
			System.out.println("Usertoken - " + userToken.substring(0, 5) + "***");
		else
			System.out.println("Usertoken - " + userToken);

		if (userToken != null && !userToken.equals("")) {
			request.setToken(userToken);
			request.setType(deviceType);
			pushNotificationService.sendPushNotificationToApp(request, savedSms, unreadCount);
		} else {
			System.out.println("user token is ''");
		}

		return 1;

	}

	@Override
	public DashboardResponse getDashboardData(IDRequest iDRequest) {
		DashboardResponse dashboardResponse = new DashboardResponse();
		User user = this.findById(iDRequest.getId());
		if (user != null) {
			List<VirtualNumbers> numbers = virtualNumbersRepository.findByUserId(iDRequest.getId());
			dashboardResponse.setVirtualNumbers(numbers);
			Integer smsCount = smsServiceImp.countByUserId(iDRequest.getId());
			dashboardResponse.setSmsCount(smsCount);
			dashboardResponse.setBalance(user.getBalance());
			dashboardResponse.setType(1);

		} else {

			dashboardResponse.setType(2);
		}
		return dashboardResponse;
	}

	@Override
	public MultipleResponse socialSignUp(SocialSignupRequest request) {

		MultipleResponse response = new MultipleResponse();
		try {
			User user = null;
			Timestamp currentDateTime = new Timestamp(System.currentTimeMillis());

			if (!request.getEmail().isEmpty()) {
				user = userRepository.findByEmail(request.getEmail());
				if (user != null) {
					// update social id and return suces
					user.setUpdationDate(currentDateTime);

					user.setSocialId(request.getSocialId());
					user.setLoginType(request.getLoginType());

					User savedUser = userRepository.save(user);

					response.setUser(savedUser);
					response.setType(1);

					return response;
				} else {
					// save email and social id
					// return success;
					user = new User();

					user.setEmail(request.getEmail());
					user.setPassword(request.getSocialId());

					user.setActive(true);
					user.setCreationDate(currentDateTime);
					user.setIsVerified(false);
					user.setUpdationDate(currentDateTime);

					user.setApproved(0);
					user.setIsDocumentVerified(false);
					user.setIsBlocked(false);
					user.setIsPorting(false);
					user.setAgreedToTermsAndConfition(" ");

					user.setSocialId(request.getSocialId());
					user.setLoginType(request.getLoginType());

					String cooeeId = "CI" + IUtil.getSixDigitRandomNumbers();

					user.setCooeeId(cooeeId);

					Stripe.apiKey = CONSTANTMESSAGE.Secretkey;
					Map<String, Object> params = new HashMap<>();
					params.put("email", request.getEmail());
					Customer customer = Customer.create(params);
					user.setStipeCustomerId(customer.getId());

					User savedUser = userRepository.save(user);

					response.setUser(savedUser);
					response.setType(1);

					return response;
				}
			}

			user = userRepository.findBySocialId(request.getSocialId());
			if (user != null) {

				user.setLoginType(request.getLoginType());

				User savedUser = userRepository.save(user);

				response.setUser(savedUser);
				response.setType(1);
			} else {

				user = new User();

				user.setEmail("");
				user.setPassword(request.getSocialId());

				user.setActive(true);
				user.setCreationDate(currentDateTime);
				user.setIsVerified(false);

				user.setApproved(0);
				user.setIsDocumentVerified(false);
				user.setIsBlocked(false);
				user.setIsPorting(false);
				user.setAgreedToTermsAndConfition(" ");
				user.setUpdationDate(currentDateTime);

				user.setSocialId(request.getSocialId());
				user.setLoginType(request.getLoginType());

				String cooeeId = "CI" + IUtil.getSixDigitRandomNumbers();

				user.setCooeeId(cooeeId);

				Stripe.apiKey = CONSTANTMESSAGE.Secretkey;
				Map<String, Object> params = new HashMap<>();
				params.put("email", request.getEmail());
				Customer customer = Customer.create(params);
				user.setStipeCustomerId(customer.getId());

				User savedUser = userRepository.save(user);

				response.setUser(savedUser);
				response.setType(1);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return response;
	}


	@Override
	public User twoFaEmailOtp(SendOtp request) {
		User user = userRepository.findByEmail(request.getEmail());
		if (user != null) {
			int otp = IUtil.getSixDigitRandomNumbers();
			user.setOtp(otp);
			emailSender.sendHTMLMessage(user.getEmail(), "OTP", "Your OTP is: " + otp);
		}
		return user;
	}

	@Override
	public List<LoginDevice> logout(IDRequest request) {

		List<LoginDevice> list = loginDeviceRepository.findByUserId(request.getId());

		if (list != null && !list.isEmpty()) {

			System.out.println(list.size());

			for (LoginDevice loginDevice : list) {
				loginDevice.setIsDeviceActive(false);
			}

			return loginDeviceRepository.saveAll(list);
		}

		return null;
	}

	@Override
	public User signupWordpress(WordpressLoginRequest request) {
		try {

			User user = new User();

			String cooeeId = "CI" + IUtil.getSixDigitRandomNumbers();

			user.setCooeeId(cooeeId);
			Timestamp currentDateTime = new Timestamp(System.currentTimeMillis());

			user.setEmail(request.getEmail());
			user.setActive(true);
			user.setCreationDate(currentDateTime);
			user.setIsVerified(false);
			user.setApproved(0);
			user.setIsDocumentVerified(false);
			user.setIsBlocked(false);
			user.setIsPorting(false);
			user.setUpdationDate(currentDateTime);
			user.setFirstName(request.getFirstName());
			user.setCountry(request.getCountry());
			user.setMobileNumber(request.getMobileNumber());

			// Encyption
			String enCodedPassword = AESCipher.aesEncryptString(request.getPassword(), CONSTANTMESSAGE.key16Byte);
			user.setPassword(enCodedPassword);

			// Otp
			int otp = IUtil.getSixDigitRandomNumbers();
			user.setOtp(otp);
			user.setOtpCreationTime(currentDateTime);

			String mailBody = EmailBody.otpVerifictionForSignup(otp,
					emailTemplatesService.getEmailByType(CONSTANTMESSAGE.SIGNUP));
			emailSender.sendHTMLMessage(user.getEmail(), "Account Registration  ", mailBody);

			Stripe.apiKey = CONSTANTMESSAGE.Secretkey;
			Map<String, Object> params = new HashMap<>();
			params.put("email", request.getEmail());
			Customer customer = Customer.create(params);
			user.setStipeCustomerId(customer.getId());

			return userRepository.save(user);

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public List<User> getAllUsersByIds(Set<Long> ids) {
		return userRepository.findByActiveAndIdIn(true, ids);
	}

	@Override
	public User changePassword(ChangePassword request) {
		User user = userRepository.findById(request.getId()).orElse(null);
		if (user.getId() != null) {
			try {	
				String enCodedPassword1  = AESCipher.aesEncryptString(request.getOldPassword(), CONSTANTMESSAGE.key16Byte);
					
					if (user.getPassword().equals(enCodedPassword1)) {
						String enCodedPassword = AESCipher.aesEncryptString(request.getNewPassword(),
								CONSTANTMESSAGE.key16Byte);
						user.setPassword(enCodedPassword);
						// user.setPassword(request.getNewPassword());
						userRepository.save(user);

						return user;
					}
				}
					catch (Exception e) {
						e.printStackTrace();
						
					}
			}
			return null;

					

					
				}

	@Override
	public User changeMail(ChangeMail request) {

		try {
			User user = userRepository.findByEmail(request.getOldEmail());

			if (user != null) {

				int otp = IUtil.getSixDigitRandomNumbers();
				user.setOtp(otp);

				Timestamp currentDateTime = new Timestamp(System.currentTimeMillis());
				user.setOtpCreationTime(currentDateTime);

				String mailBody = EmailBody.otpVerifictionForSignup(otp,
						emailTemplatesService.getEmailByType(CONSTANTMESSAGE.SIGNUP));
				emailSender.sendHTMLMessage(request.getNewEmail(), "Email Change Request", mailBody);

				user.setTempEmail(request.getNewEmail());
               // user.setEmail(user.getTempEmail());
				return userRepository.save(user);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;

	}

	@Override
	public String sipCall(String to, String from) {
		AccountNumberDetails accountNumberDetails = accountNumberRepository.findBySipUsername(to);
		if (accountNumberDetails != null) {

			List<LoginDevice> loginDeviceList = loginDeviceRepository.findAllByUser(accountNumberDetails.getUser());
			for (LoginDevice loginDevice : loginDeviceList) {

				if (loginDevice.getIsDeviceActive() != null && loginDevice.getIsDeviceActive()) {
					if (loginDevice.getDeviceType().equalsIgnoreCase(Constant.ANDROID_DEVICE_TYPE)) { // send
																										// notification
																										// to android
																										// device
						PushNotificationService.sendPushNotificationFromAdndroid(loginDevice.getDeviceToken(), "Title",
								"Hiiiii....", accountNumberDetails.getDidNumber(), "CALL");
						System.out.println("Push Notification Params : to: " + to);
						return "success";

					} else if (loginDevice.getDeviceType().equalsIgnoreCase(Constant.IOS_DEVICE_TYPE)) { // send
																											// notification
																											// to ios
																											// device
						PushNotificationService.sendPushNotificationFromIosVoip(loginDevice.getCallToken(),
								"You have incoming call from ", "You have incoming call from ", from, "CALL");
						System.out.println("Push Notification Params : to: " + to);

						return "success";
					}
				}
			}

		}
		return null;

	}

	@Override
	public List<UserContactNoBlock> userContactNoBlock(UserContactNoBlockPayload request) {
		User user = this.findById(request.getUserId());
		if (user != null) {

			List<UserContactNoBlock> list = new ArrayList<UserContactNoBlock>();
			for (String contact : request.getMobileNumber()) {
				UserContactNoBlock response = new UserContactNoBlock();

				Timestamp currentDateTime = new Timestamp(System.currentTimeMillis());

				response.setMobileNumber(contact);
				response.setUser(user);
				response.setCreationDate(currentDateTime);
				response.setUpdationDate(currentDateTime);

				list.add(response);
			}

			if (!list.isEmpty())
				return userContactNoBlockRepository.saveAll(list);

		}
		return null;
	}

	@Override
	public List<UserContactNoBlock> getUserContactNoBlock(IDRequest request) {

		return userContactNoBlockRepository.findByUserId(request.getId());

	}

//@Override
	public List<String> checkExistsContact(UserContactNoBlockPayload request) {

		List<String> listno = new ArrayList<String>();
		try {
			for (String number : request.getMobileNumber()) {
				UserContactNoBlock userContactNoBlock = userContactNoBlockRepository.findByMobileNumberAndUserId(number,
						request.getUserId());
				if (userContactNoBlock != null) {

					listno.add(number);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return listno;
	}
	@Override
	public List<String> deleteUserContactNoBlock(UserContactNoBlockPayload request) {
		List<String> listno1 = new ArrayList<String>();
		try {
			for (String number : request.getMobileNumber()) {
				UserContactNoBlock userContactNoBlock = userContactNoBlockRepository.findByMobileNumberAndUserId(number,
						request.getUserId());
				if (userContactNoBlock != null) {

					userContactNoBlockRepository.delete(userContactNoBlock);;
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return listno1;
	}

	@Override
	public String getAllUsersByIds(PushNotificationPayload request) {
		// List<User> list=userRepository.findAllById(request.getUserId());
		// if(list!=null) {
		try {
			String userId = "";
			Timestamp currentDateTime = new Timestamp(System.currentTimeMillis());

			Long timestamp = currentDateTime.getTime();
			System.out.println("Current Date Timestamp : " + timestamp);
			for (Long number : request.getUserId()) {
				User user = userRepository.findById(number).get();

				if (user != null) {
					userId = userId + "," + String.valueOf(number);
					System.out.println(userId);

					List<LoginDevice> loginDeviceList = loginDeviceRepository.findByUserId(user.getId());
					for (LoginDevice loginDevice : loginDeviceList) {
						if (loginDevice.getIsDeviceActive() != null && loginDevice.getIsDeviceActive()) {
							if (loginDevice.getDeviceType().equalsIgnoreCase(Constant.ANDROID_DEVICE_TYPE)) {
								PushNotificationService.sendPushNotificationFromAdndroid(loginDevice.getDeviceToken(),
										String.valueOf(timestamp), request.getDescription(), request.getTitle(),
										"Admin Notification");
								// return "success";

							} else if (loginDevice.getDeviceType().equalsIgnoreCase(Constant.IOS_DEVICE_TYPE)) {
								PushNotificationService.sendPushNotificationFromIos(loginDevice.getDeviceToken(), // remove
																													// voip
										String.valueOf(timestamp), request.getTitle(), request.getDescription(),
										"Admin Notification");
							
							}
						}
					}
				}

			}
			System.out.println(userId);
			SendByEmail response = new SendByEmail();
			response.setTitle(request.getTitle());
			response.setDsecription(request.getDescription());
			response.setType("notification");
			response.setUserId(userId.replaceFirst(",", ""));
			sendByEmailRepo.save(response);

			return "success";
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override 
	public String getAllUsersByIdsEmail(PushNotificationPayload request) {

		try {
			String userId = "";
			for (Long number : request.getUserId()) {

				User user = userRepository.findById(number).orElse(null);
				if (user != null) {
					userId = userId + "," + String.valueOf(number);
					System.out.println(userId);
					String mailBody = EmailBody.emailNotification(request.getDescription());
					emailSender.sendHTMLMessage(user.getEmail(), request.getTitle(), mailBody);

				}
			}
			System.out.println(userId);
			SendByEmail response = new SendByEmail();
			response.setTitle(request.getTitle());
			response.setDsecription(request.getDescription());
			response.setType("email");
			response.setUserId(userId.replaceFirst(",", ""));
			sendByEmailRepo.save(response);

			return "success";
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		
	}



	@Override
	public MultipleResponse verifyOtpforWordPress(OtpVerificationForEmailRequest request) {
		MultipleResponse response = new MultipleResponse();

		User user = userRepository.findByEmail(request.getEmail());

		if (user != null) {
			if (IUtil.checkOtpExpire(user.getOtpCreationTime())) {

				if (user.getOtp().equals(request.getOtp())) {
					user.getCreationDate();
					user.setIsVerified(true);
					response.setUser(userRepository.save(user));
					response.setType(1);
				} else {
					response.setType(3);
				}
			} else {
				response.setType(4);
			}
		} else {
			response.setType(2);
		}
		return response;
	}

	@Override
	public List<User> encryptAllUsersPassword() {

		List<User> list = userRepository.findAll();
		if (list != null) {
			for (User user : list) {
				String enCodedPassword = user.getPassword();
				try {
					enCodedPassword = AESCipher.aesEncryptString(user.getPassword(), CONSTANTMESSAGE.key16Byte);
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

				user.setPassword(enCodedPassword);
			}

			return userRepository.saveAll(list);
		}
		return null;

	}

	@Override
	public List<User> getAllUsers() {
		try {
			return userRepository.findAll();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ArrayList<>();
	}

	public static void main(String kjhdkjfp[]) {

	} 

	@Override
	public MultipleResponse wordpressLogin(String email, String password) {
		try {
			MultipleResponse response = new MultipleResponse();
			// Encyption
			String enCodedPassword = AESCipher.aesEncryptString(password, CONSTANTMESSAGE.key16Byte);
			User user = userRepository.findByEmailAndPassword(email, enCodedPassword);
			if (user != null) {

				if (user.getIsVerified()) {
					response.setType(1);

					AccountNumberDetails ac = accountNumberRepository.findByUserId(user.getId());
					if (ac != null)
						user.setAccountNumberDetails(ac);

					response.setUser(user);
				} else {
					int otp = IUtil.getSixDigitRandomNumbers();
					user.setOtp(otp);

					String mailBody = EmailBody.otpVerifiction(otp,
							emailTemplatesService.getEmailByType(CONSTANTMESSAGE.LOGIN));
					emailSender.sendHTMLMessage(user.getEmail(), "OTP for login", mailBody);

					Timestamp currentDateTime = new Timestamp(System.currentTimeMillis());
					user.setOtpCreationTime(currentDateTime);

					userRepository.save(user);
					response.setType(2);
				}
			} else

				response.setType(3);
			return response;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}

	

}
