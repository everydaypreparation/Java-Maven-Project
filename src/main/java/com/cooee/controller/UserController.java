package com.cooee.controller;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.cooee.config.ResponseHandler;
import com.cooee.model.AccountNumberDetails;
import com.cooee.model.LoginDevice;
import com.cooee.model.RejectKycDetails;
import com.cooee.model.SubscriptionPlan;
import com.cooee.model.User;
import com.cooee.model.UserContactNoBlock;
import com.cooee.model.UserSubscriptionPlan;
import com.cooee.model.VirtualNumbers;
import com.cooee.payload.ChangeMail;
import com.cooee.payload.ChangePassword;
import com.cooee.payload.DashboardResponse;
import com.cooee.payload.EmailAndPasswordRequest;
import com.cooee.payload.EmptyJsonResponse;
import com.cooee.payload.FileUploadRequest;
import com.cooee.payload.GetAllResponse;
import com.cooee.payload.IDRequest;
import com.cooee.payload.LoginRequest;
import com.cooee.payload.MultipleResponse;
import com.cooee.payload.OtpVerificationForEmailRequest;
import com.cooee.payload.OtpVerificationRequest;
import com.cooee.payload.ResetPassword;
import com.cooee.payload.SendOtp;
import com.cooee.payload.SocialSignupRequest;
import com.cooee.payload.SourceDestinationTypePayload;
import com.cooee.payload.SubscriptionPlanPayload;
import com.cooee.payload.UserContactNoBlockPayload;
import com.cooee.payload.UserContactNoUnblockPayload;
import com.cooee.payload.UserSubscriptionPlanPayload;
import com.cooee.payload.UsernameAndPasswordRequest;
import com.cooee.payload.WordpressLoginRequest;
import com.cooee.repository.UserContactNoBlockRepository;
//import com.cooee.repository.UserSubscriptionPlanRepository;
import com.cooee.responsemessgae.CONSTANTMESSAGE;
import com.cooee.service.AccountNumberDetailsService;
import com.cooee.service.LoginDeviceService;
import com.cooee.service.RejectKycDetailsService;
import com.cooee.service.UserService;
import com.cooee.service.UserSubscriptionPlanService;
//import com.cooee.service.UserSubscriptionPlanService;
import com.cooee.service.VirtualNumbersService;
import com.cooee.serviceImp.PushNotificationService;
import com.cooee.util.AESCipher;

import io.swagger.v3.oas.annotations.Operation;

@CrossOrigin /* (origins = "", allowedHeaders = "") */
@RestController
@RequestMapping("/user")
public class UserController {

	@Autowired
	private LoginDeviceService loginDeviceService;

	@Autowired
	private UserService userService;

	@Autowired
	private VirtualNumbersService virtualNumbersService;

	@Autowired
	RejectKycDetailsService rejectKycDetailsService;

	@Autowired
	UserContactNoBlockRepository userContactNoBlockRepository;

	@Autowired
	private AccountNumberDetailsService accountDetailsService;
	
	@Autowired
	private UserSubscriptionPlanService userSubscriptionPlanService;

	@Value("${asteriskUrl}")
	private String asteriskUrl;

	@Value("${asteriskApISecretKey}")
	private String asteriskApISecretKey;

	@Operation(summary = "Get Welcome Carousel", description = "Get welcome screen content with img url,title,description")
	@GetMapping(value = "/test")
	public ResponseEntity<String> test() {
		try {
			RestTemplate restTemplate = new RestTemplate();
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			headers.set("Authorization", asteriskApISecretKey);
			HttpEntity<String> entity = new HttpEntity<String>(headers);

			final String uri = asteriskUrl + "user/test";

			ResponseEntity<Integer> result = restTemplate.exchange(uri, HttpMethod.GET, entity, Integer.class);
			Integer response = result.getBody();

			if (response != null) {
				if (response == 1) {
					System.out.println("Success");
					return ResponseEntity.ok("Success");
				} else {
					System.out.println("Fail");
					return ResponseEntity.ok("Fail");
				}
			} else {
				System.out.println("Fail 2");
				return ResponseEntity.ok("Fail 2");
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Error");
			return ResponseEntity.ok("Error");
		}

	}

	@GetMapping(value = "/testPost/{extension}")
	public ResponseEntity<String> test2(@PathVariable("extension") String extension) {
		try {

			JSONObject jsonObject = new JSONObject();
			jsonObject.put("username", extension);

			RestTemplate restTemplate = new RestTemplate();
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			headers.set("Authorization", asteriskApISecretKey);
			HttpEntity<String> entity = new HttpEntity<String>(jsonObject.toString(), headers);

			final String uri = asteriskUrl + "user/countByExtension";

			ResponseEntity<Long> result = restTemplate.exchange(uri, HttpMethod.POST, entity, Long.class);

			Long response = result.getBody();

			if (response != null) {
				if (response > 0) {
					System.out.println("Success - " + response);
					return ResponseEntity.ok("Success - " + response);
				} else {
					System.out.println("Fail");
					return ResponseEntity.ok("Fail");
				}
			} else {
				System.out.println("Fail 2");
				return ResponseEntity.ok("Fail 2");
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Error");
			return ResponseEntity.ok("Error");
		}

	}

	@PostMapping(value = "/signup")
	public ResponseEntity<Object> add(@RequestBody EmailAndPasswordRequest signUp) {
		try {

			if (signUp.getEmail() == null || signUp.getEmail().isEmpty()) {

				return ResponseHandler.generateResponse(CONSTANTMESSAGE.EMAIL_REQUIRED, HttpStatus.BAD_REQUEST,
						new EmptyJsonResponse());

			}

			if (userService.findByEmail(signUp.getEmail()) != null) {
				return ResponseHandler.generateResponse(CONSTANTMESSAGE.EMAIL_ALREADY_EXIST, HttpStatus.BAD_REQUEST,
						new EmptyJsonResponse());
			}

			if (signUp.getPassword() == null || signUp.getPassword().isEmpty()) {

				return ResponseHandler.generateResponse(CONSTANTMESSAGE.PASSWORD_REQUIRED, HttpStatus.BAD_REQUEST,
						new EmptyJsonResponse());

			}

			User result = userService.save(signUp);
			if (result != null)
				return ResponseHandler.generateResponse(CONSTANTMESSAGE.REGISTRATION_SUCCESSFULLY, HttpStatus.OK,
						new EmptyJsonResponse());
			else
				return ResponseHandler.generateResponse(CONSTANTMESSAGE.RECORD_ADDED_UNSUCCESSFULLY,
						HttpStatus.BAD_REQUEST, new EmptyJsonResponse());

		} catch (Exception e) {
			e.printStackTrace();
			return ResponseHandler.generateResponse(CONSTANTMESSAGE.RECORD_ADDED_UNSUCCESSFULLY,
					HttpStatus.INTERNAL_SERVER_ERROR, new EmptyJsonResponse());
		}

	}

	@PostMapping(value = "/verifyOtp")
	public ResponseEntity<Object> otpVerfication(@RequestBody OtpVerificationRequest request) {
		try {

			if (request.getEmail() == null || request.getEmail().isEmpty()) {

				return ResponseHandler.generateResponse(CONSTANTMESSAGE.EMAIL_REQUIRED, HttpStatus.BAD_REQUEST,
						new EmptyJsonResponse());
			}

			if (request.getOtp() == null || request.getOtp() == 0) {

				return ResponseHandler.generateResponse(CONSTANTMESSAGE.OTP_REQUIRED, HttpStatus.BAD_REQUEST,
						new EmptyJsonResponse());
			}

			MultipleResponse result = userService.otpVerification(request);

			if (result != null) {
				if (result.getType() == 1 && result.getUser() != null) {
					loginDeviceService.saveLoginDevice(request.getDeviceId(), request.getDeviceIp(),
							request.getDeviceToken(), request.getDeviceType(), request.getCallToken(),
							request.getDevice(),request.getIsDeviceActive(), result.getUser());

					AccountNumberDetails ac = accountDetailsService.findByUserId(result.getUser().getId());
					if (ac != null)
						result.getUser().setAccountNumberDetails(ac);

					return ResponseHandler.generateResponse(CONSTANTMESSAGE.OTP_VERIFICATION_SUCCESSFULLY,
							HttpStatus.OK, result);
				} else if (result.getType() == 2) {
					return ResponseHandler.generateResponse(CONSTANTMESSAGE.EMAIL_NOT_EXIST, HttpStatus.BAD_REQUEST,
							new EmptyJsonResponse());
				} else if (result.getType() == 3) {
					return ResponseHandler.generateResponse(CONSTANTMESSAGE.OTP_WRONG, HttpStatus.BAD_REQUEST,
							new EmptyJsonResponse());
				} else {
					return ResponseHandler.generateResponse(CONSTANTMESSAGE.OTP_EXPIRE, HttpStatus.BAD_REQUEST,
							new EmptyJsonResponse());
				}
			} else
				return ResponseHandler.generateResponse(CONSTANTMESSAGE.OTP_VERIFICATION__UNSUCCESSFULLY,
						HttpStatus.BAD_REQUEST, new EmptyJsonResponse());
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseHandler.generateResponse(CONSTANTMESSAGE.OTP_VERIFICATION__UNSUCCESSFULLY,
					HttpStatus.INTERNAL_SERVER_ERROR, new EmptyJsonResponse());

		}
	}

	@PostMapping("/login")
	public ResponseEntity<Object> login(@RequestBody LoginRequest request) {
		try {
			if (request.getEmail() == null || request.getEmail().isEmpty()) {

				return ResponseHandler.generateResponse(CONSTANTMESSAGE.EMAIL_REQUIRED, HttpStatus.BAD_REQUEST,
						new EmptyJsonResponse());
			}

			if (request.getPassword() == null || request.getPassword().isEmpty()) {

				return ResponseHandler.generateResponse(CONSTANTMESSAGE.PASSWORD_REQUIRED, HttpStatus.BAD_REQUEST,
						new EmptyJsonResponse());
			}

			MultipleResponse result = userService.login(request);
			if (result != null && result.getType() != null) {
				if (result.getType() == 1) {
					// Pass result object and request object to login device service method.
					loginDeviceService.saveLoginDevice(request.getDeviceId(), request.getDeviceIp(),
							request.getDeviceToken(), request.getDeviceType(), request.getCallToken(),
							request.getDevice(),request.getIsDeviceActive(), result.getUser());

					return ResponseHandler.generateResponse(CONSTANTMESSAGE.LOGIN_SUCCESSFULLY, HttpStatus.OK, result);
				} else if (result.getType() == 2) {
					return ResponseHandler.generateResponse(CONSTANTMESSAGE.USER_NOT_VERIFIED, HttpStatus.FORBIDDEN,
							result);
				} else {
					return ResponseHandler.generateResponse(CONSTANTMESSAGE.INVALID_EMAIL_PASSWORD,
							HttpStatus.BAD_REQUEST, new EmptyJsonResponse());
				}
			} else
				return ResponseHandler.generateResponse(CONSTANTMESSAGE.LOGIN_UNSUCCESSFULLY, HttpStatus.BAD_REQUEST,
						new EmptyJsonResponse());
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseHandler.generateResponse(CONSTANTMESSAGE.LOGIN_UNSUCCESSFULLY,
					HttpStatus.INTERNAL_SERVER_ERROR, new EmptyJsonResponse());
		}
	}

	// Will only take email in request
	@PostMapping(value = "/resetPassword")
	public ResponseEntity<Object> resetPassword(@RequestBody ResetPassword request) {
		try {
			if (request.getEmail() == null || request.getEmail().isEmpty()) {

				return ResponseHandler.generateResponse(CONSTANTMESSAGE.EMAIL_REQUIRED, HttpStatus.BAD_REQUEST,
						new EmptyJsonResponse());
			}

			User result = userService.resetPassword(request);

			if (result != null)
				return ResponseHandler.generateResponse(CONSTANTMESSAGE.RESET_PASSWORD_EMAIL + result.getEmail()
						+ CONSTANTMESSAGE.RESET_PASSWORD_EMAIL2, HttpStatus.OK, new EmptyJsonResponse());
			else
				return ResponseHandler.generateResponse(CONSTANTMESSAGE.RESET_PASSWORD_EMAIL_REQUIRED,
						HttpStatus.BAD_REQUEST, new EmptyJsonResponse());
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseHandler.generateResponse(CONSTANTMESSAGE.PASSWORD_UPDATED_UNSUCCESSFULLY,
					HttpStatus.INTERNAL_SERVER_ERROR, new EmptyJsonResponse());

		}
	}

	@PostMapping(value = "/updatePassword")
	public ResponseEntity<Object> updatePassword(@RequestBody EmailAndPasswordRequest request) {
		try {

			if (request.getEmail() == null || request.getEmail().isEmpty()) {

				return ResponseHandler.generateResponse(CONSTANTMESSAGE.EMAIL_REQUIRED, HttpStatus.BAD_REQUEST,
						new EmptyJsonResponse());
			}

			User result = userService.updatePassword(request.getEmail(), request.getPassword());

			if (result != null)
				return ResponseHandler.generateResponse(CONSTANTMESSAGE.PASSWORD_UPDATED_SUCCESSFULLY, HttpStatus.OK,
						new EmptyJsonResponse());
			else
				return ResponseHandler.generateResponse(CONSTANTMESSAGE.EMAIL_NOT_EXIST, HttpStatus.BAD_REQUEST,
						new EmptyJsonResponse());
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseHandler.generateResponse(CONSTANTMESSAGE.PASSWORD_UPDATED_UNSUCCESSFULLY,
					HttpStatus.INTERNAL_SERVER_ERROR, new EmptyJsonResponse());

		}
	}

	@PostMapping(value = "/sendOtp")
	public ResponseEntity<Object> sendOTP(@RequestBody SendOtp request) {
		try {
			if (request.getEmail() == null || request.getEmail().isEmpty()) {

				return ResponseHandler.generateResponse(CONSTANTMESSAGE.EMAIL_REQUIRED, HttpStatus.BAD_REQUEST,
						new EmptyJsonResponse());
			}

			User result = userService.sendOtp(request);
			if (result != null)
				return ResponseHandler.generateResponse(CONSTANTMESSAGE.SENDOTP_SUCCESSFULLY, HttpStatus.OK,
						new EmptyJsonResponse());
			else
				return ResponseHandler.generateResponse(CONSTANTMESSAGE.USER_NOT_FOUND, HttpStatus.BAD_REQUEST,
						new EmptyJsonResponse());
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseHandler.generateResponse(CONSTANTMESSAGE.SENDOTP_UNSUCCESSFULLY,
					HttpStatus.INTERNAL_SERVER_ERROR, new EmptyJsonResponse());

		}
	}

	@PostMapping(value = "/resendOtp")
	public ResponseEntity<Object> resendOtp(@RequestBody SendOtp request) {

		try {
			if (request.getEmail() == null || request.getEmail().isEmpty()) {

				return ResponseHandler.generateResponse(CONSTANTMESSAGE.EMAIL_REQUIRED, HttpStatus.BAD_REQUEST,
						new EmptyJsonResponse());
			}

			User result = userService.resendOTP(request);

			if (result != null)
				return ResponseHandler.generateResponse(CONSTANTMESSAGE.RESEND_OTP_SUCCESSFULLY, HttpStatus.OK,
						new EmptyJsonResponse());
			else
				return ResponseHandler.generateResponse(CONSTANTMESSAGE.RESEND_OTP_UNSUCCESSFULLY,
						HttpStatus.BAD_REQUEST, new EmptyJsonResponse());
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseHandler.generateResponse(CONSTANTMESSAGE.RESEND_OTP_UNSUCCESSFULLY,
					HttpStatus.INTERNAL_SERVER_ERROR, new EmptyJsonResponse());

		}
	}

	@PostMapping(path = "/submitKycDetails", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
	public ResponseEntity<Object> fileUploadAPI(@ModelAttribute FileUploadRequest fileUploadRequest) {

		try {
			if (fileUploadRequest.getId() == null) {

				return ResponseHandler.generateResponse(CONSTANTMESSAGE.ID_REQUIRED, HttpStatus.BAD_REQUEST,
						new EmptyJsonResponse());
			}

			if (fileUploadRequest.getDob() == null || fileUploadRequest.getDob().isEmpty()) {

				return ResponseHandler.generateResponse(CONSTANTMESSAGE.DOB_REQUIRED, HttpStatus.BAD_REQUEST,
						new EmptyJsonResponse());
			}
			if (fileUploadRequest.getDidNumber() == null || fileUploadRequest.getDidNumber().isEmpty()) {

				return ResponseHandler.generateResponse(CONSTANTMESSAGE.DID_NUMBER_REQUIRED, HttpStatus.BAD_REQUEST,
						new EmptyJsonResponse());
			}
			User result = userService.submitKYC(fileUploadRequest/* , idCardFront, idCardBack, idCardSelfie */);

			if (result != null) {
				return ResponseHandler.generateResponse(CONSTANTMESSAGE.FILE_UPLOAD_SUCCESSFULLY, HttpStatus.OK,
						new EmptyJsonResponse());
			} else
				return ResponseHandler.generateResponse(CONSTANTMESSAGE.FILE_UPLOAD_FAILED, HttpStatus.BAD_REQUEST,
						new EmptyJsonResponse());
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseHandler.generateResponse(CONSTANTMESSAGE.FILE_UPLOAD_FAILED,
					HttpStatus.INTERNAL_SERVER_ERROR, new EmptyJsonResponse());

		}
	}

	@PostMapping(value = "/getProfile")
	public ResponseEntity<Object> getProfile(@RequestBody IDRequest request) {

		try {
			if (request.getId() == null) {

				return ResponseHandler.generateResponse(CONSTANTMESSAGE.ID_REQUIRED, HttpStatus.BAD_REQUEST,
						new EmptyJsonResponse());
			}

			User result = userService.findById(request.getId());

			if (result != null) {
				System.out.println("Result found - " + result.getId());
				AccountNumberDetails ac = accountDetailsService.findByUserId(result.getId());
				if (ac != null) {
					System.out.println("Result found for ac- " + ac.getId());
					result.setAccountNumberDetails(ac);
				}
				return ResponseHandler.generateResponse(CONSTANTMESSAGE.USER_FOUND, HttpStatus.OK, result);
			} else
				return ResponseHandler.generateResponse(CONSTANTMESSAGE.USERNOT_FOUND, HttpStatus.BAD_REQUEST,
						new EmptyJsonResponse());
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseHandler.generateResponse(CONSTANTMESSAGE.EXCEPTION_CASE, HttpStatus.INTERNAL_SERVER_ERROR,
					new EmptyJsonResponse());

		}
	}

	@GetMapping("/testEncryption")
	public String testEncry() {
		System.out.println("Running java program");
		String plainText = "vishal.lashkari@advantal.net";

		String key16Byte = "advantaladvantal";

		try {
			System.out.println("Plain Text  : " + plainText);

			String enCodedString = AESCipher.aesEncryptString(plainText, key16Byte);
			System.out.println("Encoded String  : " + enCodedString + " || length = " + enCodedString.length());

			String hexValue = AESCipher.bytesToHex(enCodedString.getBytes());
			System.out.println("Hex Value - " + hexValue);

			String deCodedString = AESCipher.aesDecryptString(enCodedString, key16Byte);
			System.out.println("Decoded String : " + deCodedString + " || length = " + deCodedString.length());

//			String originalURL = "https://www.google.co.nz/?gfe_rd=cr&ei=dzbFV&gws_rd=ssl#q=java";
//			String encodedUrl = Base64.URLgetUrlEncoder().encodeToString(originalURL.getBytes());
//			
//			byte[] decodedBytes = Base64.getUrlDecoder().decode(encodedUrl);
//			String decodedUrl = new String(decodedBytes);

			try {
				char[] result = Hex.encodeHex(enCodedString.getBytes(StandardCharsets.UTF_8));
				String hex = new String(result);
				System.out.println(hex);

				System.out.println(new String(Hex.decodeHex(hex)));
			} catch (Exception e) {
				e.printStackTrace();
			}

			return "success";

		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
		} catch (InvalidAlgorithmParameterException e) {
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
		} catch (BadPaddingException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		return "failed";
	}

	@GetMapping("/getAll")
	public ResponseEntity<Object> getAll(@RequestParam(defaultValue = "0") Integer pageNo,
			@RequestParam(defaultValue = "1") Integer pageSize, @RequestParam(required = false) String keyword,
			@RequestParam(required = false) Integer approved) {
		try {

			GetAllResponse response = userService.getAllUsers(pageNo, pageSize, keyword, approved);

			if (response != null)

				return ResponseHandler.generateResponse(CONSTANTMESSAGE.USER_FOUND, HttpStatus.OK, response);
			else
				return ResponseHandler.generateResponse(CONSTANTMESSAGE.NO_RECORD_FOUND, HttpStatus.BAD_REQUEST,
						new EmptyJsonResponse());
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseHandler.generateResponse(CONSTANTMESSAGE.EXCEPTION_CASE, HttpStatus.INTERNAL_SERVER_ERROR,
					new EmptyJsonResponse());
		}
	}

	/*
	 * @GetMapping("/getAll") public ResponseEntity<Object> getAll(){ try {
	 * 
	 * List<User> users = userService.getAllUsers();
	 * 
	 * if (users != null && !users.isEmpty()) return
	 * ResponseHandler.generateResponse(CONSTANTMESSAGE.USER_FOUND, HttpStatus.OK,
	 * users); else return
	 * ResponseHandler.generateResponse(CONSTANTMESSAGE.NO_RECORD_FOUND,
	 * HttpStatus.BAD_REQUEST, new EmptyJsonResponse()); } catch (Exception e) {
	 * e.printStackTrace(); return
	 * ResponseHandler.generateResponse(CONSTANTMESSAGE.EXCEPTION_CASE,
	 * HttpStatus.INTERNAL_SERVER_ERROR, new EmptyJsonResponse()); } }
	 */

	@GetMapping("/getAllAvailableDidNumber")
	public ResponseEntity<Object> getAllAvailableDidNumber() {
		try {

			List<VirtualNumbers> numbers = virtualNumbersService.findAllAvailableNumbers();

			if (numbers != null && !numbers.isEmpty())
				return ResponseHandler.generateResponse(CONSTANTMESSAGE.All_AVAILABLE_DID_NUMBER_FOUND, HttpStatus.OK,
						numbers);
			else
				return ResponseHandler.generateResponse(CONSTANTMESSAGE.NO_RECORD_FOUND, HttpStatus.BAD_REQUEST,
						new EmptyJsonResponse());
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseHandler.generateResponse(CONSTANTMESSAGE.NO_RECORD_FOUND, HttpStatus.INTERNAL_SERVER_ERROR,
					new EmptyJsonResponse());
		}
	}

	@GetMapping("/getAllUsers")
	public ResponseEntity<Object> getAllUsers() {
		try {

			List<User> numbers = userService.getAllUsers();

			if (numbers != null && !numbers.isEmpty())
				return ResponseHandler.generateResponse(CONSTANTMESSAGE.All_AVAILABLE_DID_NUMBER_FOUND, HttpStatus.OK,
						numbers);
			else
				return ResponseHandler.generateResponse(CONSTANTMESSAGE.NO_RECORD_FOUND, HttpStatus.BAD_REQUEST,
						new EmptyJsonResponse());
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseHandler.generateResponse(CONSTANTMESSAGE.NO_RECORD_FOUND, HttpStatus.INTERNAL_SERVER_ERROR,
					new EmptyJsonResponse());
		}
	}

	@PostMapping(path = "/sendCallPush", consumes = { "application/x-www-form-urlencoded" })
	public ResponseEntity<String> sendCallPush(SourceDestinationTypePayload request) {

		try {

			String source = request.getSource().trim();
			String destination = request.getDestination().trim();
			String type = request.getType().trim();

			String responseTwo = "";

			responseTwo = userService.sendCallPushNotifications(source, destination, type);

			if (!responseTwo.equals("")) {
				return ResponseEntity.ok(responseTwo);

			} else {
				return ResponseEntity.ok("00000,0");
			}
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.ok("00000,0");
		}
	}

	@PostMapping(value = "/getDashboardData")
	public ResponseEntity<Object> getDashboardData(@RequestBody IDRequest iDRequest) {
		try {

			if (iDRequest.getId() == null || iDRequest.getId() == 0) {

				return ResponseHandler.generateResponse(CONSTANTMESSAGE.ID_REQUIRED, HttpStatus.BAD_REQUEST,
						new EmptyJsonResponse());

			}

			DashboardResponse result = userService.getDashboardData(iDRequest);
			if (result != null && result.getType() != null) {
				if (result.getType() == 1) {
					return ResponseHandler.generateResponse(CONSTANTMESSAGE.DATA_FETCH_SUCCESSFULLY, HttpStatus.OK,
							result);
				} else {
					return ResponseHandler.generateResponse(CONSTANTMESSAGE.USER_NOT_FOUND, HttpStatus.BAD_REQUEST,
							new EmptyJsonResponse());
				}
			} else {
				return ResponseHandler.generateResponse(CONSTANTMESSAGE.DATA_FETCH_UNSUCCESSFULLY,
						HttpStatus.BAD_REQUEST, new EmptyJsonResponse());
			}
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseHandler.generateResponse(CONSTANTMESSAGE.DATA_FETCH_UNSUCCESSFULLY,
					HttpStatus.INTERNAL_SERVER_ERROR, new EmptyJsonResponse());
		}

	}

	@PostMapping("/socialSignUp")
	public ResponseEntity<Object> socialSignUp(@RequestBody SocialSignupRequest request) {
		try {

			if (request.getSocialId() == null || request.getSocialId().isEmpty()) {

				return ResponseHandler.generateResponse(CONSTANTMESSAGE.ID_REQUIRED, HttpStatus.BAD_REQUEST,
						new EmptyJsonResponse());
			}

			MultipleResponse result = userService.socialSignUp(request);
			if (result != null && result.getType() != null) {
				if (result.getType() == 1) {
					// Pass result object and request object to login device service method.
					LoginDevice device = loginDeviceService.saveLoginDevice(request.getDeviceId(),
							request.getDeviceIp(), request.getDeviceToken(), request.getDeviceType(),
							request.getCallToken(), request.getDevice(),request.getIsDeviceActive(), result.getUser());

					AccountNumberDetails ac = accountDetailsService.findByUserId(result.getUser().getId());
					if (ac != null)
						device.getUser().setAccountNumberDetails(ac);

					return ResponseHandler.generateResponse(CONSTANTMESSAGE.SUCCESSFULLY, HttpStatus.OK, device);
				} else {
					LoginDevice device = loginDeviceService.saveLoginDevice(request.getDeviceId(),
							request.getDeviceIp(), request.getDeviceToken(), request.getDeviceType(),
							request.getCallToken(), request.getDevice(),request.getIsDeviceActive(), result.getUser());
					return ResponseHandler.generateResponse(CONSTANTMESSAGE.SUCCESSFULLY, HttpStatus.OK, device);
				}
			} else
				return ResponseHandler.generateResponse(CONSTANTMESSAGE.UNSUCCESSFULLY, HttpStatus.BAD_REQUEST,
						new EmptyJsonResponse());
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseHandler.generateResponse(CONSTANTMESSAGE.UNSUCCESSFULLY, HttpStatus.INTERNAL_SERVER_ERROR,
					new EmptyJsonResponse());
		}
	}

	@PostMapping(value = "/twoFactorEmailOtp")
	public ResponseEntity<Object> twoFaEmailOtp(@RequestBody SendOtp request) {
		try {
			if (request.getEmail() == null || request.getEmail().isEmpty()) {

				return ResponseHandler.generateResponse(CONSTANTMESSAGE.EMAIL_REQUIRED, HttpStatus.BAD_REQUEST,
						new EmptyJsonResponse());
			}

			User result = userService.twoFaEmailOtp(request);
			if (result != null)
				return ResponseHandler.generateResponse(CONSTANTMESSAGE.SENDOTP_SUCCESSFULLY, HttpStatus.OK,
						result.getOtp());
			else
				return ResponseHandler.generateResponse(CONSTANTMESSAGE.USER_NOT_FOUND, HttpStatus.BAD_REQUEST,
						new EmptyJsonResponse());
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseHandler.generateResponse(CONSTANTMESSAGE.SENDOTP_UNSUCCESSFULLY,
					HttpStatus.INTERNAL_SERVER_ERROR, new EmptyJsonResponse());

		}
	}

	@PostMapping("/logout")
	public ResponseEntity<Object> logout(@RequestBody IDRequest request) {
		try {
			List<LoginDevice> response = userService.logout(request);
			if (response != null) {
				return ResponseHandler.generateResponse(CONSTANTMESSAGE.LOGOUT_SUCCESSFULLY, HttpStatus.OK,
						new EmptyJsonResponse());
			} else {
				return ResponseHandler.generateResponse(CONSTANTMESSAGE.USER_NOT_FOUND, HttpStatus.BAD_REQUEST,
						new EmptyJsonResponse());
			}
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseHandler.generateResponse(CONSTANTMESSAGE.LOGOUT_UNSUCCESSFULLY,
					HttpStatus.INTERNAL_SERVER_ERROR, new EmptyJsonResponse());
		}
	}

	@GetMapping(value = "/test_android_push_notification")
	public ResponseEntity<Object> testAndroidPushNotification(@RequestParam String token) {
		try {
			String result = PushNotificationService.sendPushNotificationFromAdndroid(token, "Title", "Hiiiii....", null,
					"type1");
			System.out.println("android push notification result==>" + result);

			return null;
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseHandler.generateResponse(CONSTANTMESSAGE.DATA_FETCH_UNSUCCESSFULLY,
					HttpStatus.INTERNAL_SERVER_ERROR, new EmptyJsonResponse());
		}

	}

	@GetMapping(value = "/test_ios_push_notification")
	public ResponseEntity<Object> testIosPushNotification(@RequestParam String token) {
		try {
			String result = PushNotificationService.sendPushNotificationFromIos(token, "ios titile", "ios msg", null,
					"type2");
			System.out.println("ios push notification result==>" + result);
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseHandler.generateResponse(CONSTANTMESSAGE.DATA_FETCH_UNSUCCESSFULLY,
					HttpStatus.INTERNAL_SERVER_ERROR, new EmptyJsonResponse());
		}

	}

	@GetMapping(value = "/test_ios_push_notification_voip")
	public ResponseEntity<Object> testIosPushNotificationVoip(@RequestParam String token) {
		try {
			String result = PushNotificationService.sendPushNotificationFromIosVoip(token, "ios titile", "ios msg",
					null, "type2");
			System.out.println("ios push notification result==>" + result);
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseHandler.generateResponse(CONSTANTMESSAGE.DATA_FETCH_UNSUCCESSFULLY,
					HttpStatus.INTERNAL_SERVER_ERROR, new EmptyJsonResponse());
		}

	}

	@GetMapping(value = "/sipCall")
	public ResponseEntity<Object> sipCall(@RequestParam String to, @RequestParam String from) {

		String result = userService.sipCall(to, from);

		try {
			if (result != null) {
				System.out.println("ios push notification result==>" + result);
				return ResponseHandler.generateResponse("user feteched", HttpStatus.OK, result);
			} else {
				return ResponseHandler.generateResponse("user not found", HttpStatus.BAD_REQUEST,
						new EmptyJsonResponse());

			}
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseHandler.generateResponse(CONSTANTMESSAGE.DATA_FETCH_UNSUCCESSFULLY,
					HttpStatus.INTERNAL_SERVER_ERROR, new EmptyJsonResponse());
		}

	}

	@PostMapping(value = "/getRejectKycDescriptions")
	public ResponseEntity<Object> getRejectKycDescriptions(@RequestBody IDRequest request) {

		try {
			if (request.getId() == null||request.getId()==0) {

				return ResponseHandler.generateResponse(CONSTANTMESSAGE.ID_REQUIRED, HttpStatus.BAD_REQUEST,
						new EmptyJsonResponse());
			}
			List<RejectKycDetails> result = rejectKycDetailsService.getRejectKycDescriptions(request);
			if (result != null)
				return ResponseHandler.generateResponse(CONSTANTMESSAGE.USER_FOUND, HttpStatus.OK, result);
			else
				return ResponseHandler.generateResponse(CONSTANTMESSAGE.USERNOT_FOUND, HttpStatus.BAD_REQUEST,
						new EmptyJsonResponse());
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseHandler.generateResponse(CONSTANTMESSAGE.EXCEPTION_CASE, HttpStatus.INTERNAL_SERVER_ERROR,
					new EmptyJsonResponse());

		}
	}

	@PostMapping(value = "/signupWordpress")
	public ResponseEntity<Object> signupWordpress(@RequestBody WordpressLoginRequest request) {
		try {

			if (request.getEmail() == null || request.getEmail().isEmpty()) {

				return ResponseHandler.generateResponse(CONSTANTMESSAGE.EMAIL_REQUIRED, HttpStatus.BAD_REQUEST,
						new EmptyJsonResponse());

			}
			if (request.getCountry() == null || request.getCountry().isEmpty()) {

				return ResponseHandler.generateResponse(CONSTANTMESSAGE.COUNTRY_REQUIRED, HttpStatus.BAD_REQUEST,
						new EmptyJsonResponse());

			}
			if (request.getFirstName() == null || request.getFirstName().isEmpty()) {

				return ResponseHandler.generateResponse(CONSTANTMESSAGE.USERNAME_REQUIRED, HttpStatus.BAD_REQUEST,
						new EmptyJsonResponse());

			}
			if (request.getMobileNumber() == null || request.getMobileNumber().isEmpty()) {

				return ResponseHandler.generateResponse(CONSTANTMESSAGE.MOBILE_NUMBER_REQUIRED, HttpStatus.BAD_REQUEST,
						new EmptyJsonResponse());

			}
			if (userService.findByEmail(request.getEmail()) != null) {
				return ResponseHandler.generateResponse(CONSTANTMESSAGE.EMAIL_ALREADY_EXIST, HttpStatus.BAD_REQUEST,
						new EmptyJsonResponse());
			}
			if (request.getPassword() == null || request.getPassword().isEmpty()) {

				return ResponseHandler.generateResponse(CONSTANTMESSAGE.PASSWORD_REQUIRED, HttpStatus.BAD_REQUEST,
						new EmptyJsonResponse());

			}

			User result = userService.signupWordpress(request);
			if (result != null)
				return ResponseHandler.generateResponse(CONSTANTMESSAGE.SIGNUP_SUCCESSFULLY, HttpStatus.OK, request);
			else
				return ResponseHandler.generateResponse(CONSTANTMESSAGE.RECORD_ADDED_UNSUCCESSFULLY,
						HttpStatus.BAD_REQUEST, new EmptyJsonResponse());

		} catch (Exception e) {
			e.printStackTrace();
			return ResponseHandler.generateResponse(CONSTANTMESSAGE.RECORD_ADDED_UNSUCCESSFULLY,
					HttpStatus.INTERNAL_SERVER_ERROR, new EmptyJsonResponse());
		}

	}

	@PostMapping(value = "/changePassword")
	public ResponseEntity<Object> changePassword(@RequestBody ChangePassword request) {
		try {
			if (request.getId() == null || request.getId() == 0) {

				return ResponseHandler.generateResponse(CONSTANTMESSAGE.ID_REQUIRED, HttpStatus.BAD_REQUEST,
						new EmptyJsonResponse());
			}
			if (request.getOldPassword() == null || request.getOldPassword().isEmpty()) {

				return ResponseHandler.generateResponse(CONSTANTMESSAGE.PASSWORD_REQUIRED, HttpStatus.BAD_REQUEST,
						new EmptyJsonResponse());
			}
			if (request.getNewPassword() == null || request.getNewPassword().isEmpty()) {

				return ResponseHandler.generateResponse(CONSTANTMESSAGE.NEW_PASSWORD_REQUIRED, HttpStatus.BAD_REQUEST,
						new EmptyJsonResponse());
			}
			User result = userService.changePassword(request);

			if (result != null)
				return ResponseHandler.generateResponse(CONSTANTMESSAGE.PASSWORD_UPDATED_SUCCESSFULLY, HttpStatus.OK,
						new EmptyJsonResponse());
			else
				return ResponseHandler.generateResponse(CONSTANTMESSAGE.PASSWORD_UPDATED_UNSUCCESSFULLY, HttpStatus.BAD_REQUEST,
						new EmptyJsonResponse());
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseHandler.generateResponse(CONSTANTMESSAGE.PASSWORD_UPDATED_UNSUCCESSFULLY,
					HttpStatus.INTERNAL_SERVER_ERROR, new EmptyJsonResponse());

		}
	}

	@PostMapping(value = "/changeMail")
	public ResponseEntity<Object> changeMail(@RequestBody ChangeMail request) {
		try {
//			if (request.getId() == null || request.getId() == 0) {
//
//				return ResponseHandler.generateResponse(CONSTANTMESSAGE.ID_REQUIRED, HttpStatus.BAD_REQUEST,
//						new EmptyJsonResponse());
//			}

			if (request.getOldEmail().equalsIgnoreCase(request.getNewEmail())) {
				return ResponseHandler.generateResponse("Both Emails Can't be Same!", HttpStatus.BAD_REQUEST,
						new EmptyJsonResponse());
			}

			if (request.getOldEmail() == null || request.getOldEmail().isEmpty()) {

				return ResponseHandler.generateResponse(CONSTANTMESSAGE.EMAIL_REQUIRED, HttpStatus.BAD_REQUEST,
						new EmptyJsonResponse());
			}

			if (request.getNewEmail() == null || request.getNewEmail().isEmpty()) {

				return ResponseHandler.generateResponse(CONSTANTMESSAGE.NEW_EMAIL_REQUIRED, HttpStatus.BAD_REQUEST,
						new EmptyJsonResponse());
			}

			User result = userService.changeMail(request);

			if (result != null)
				return ResponseHandler.generateResponse(CONSTANTMESSAGE.EAMIL_UPDATED_SUCCESFULLY, HttpStatus.OK,
						new EmptyJsonResponse());
			else
				return ResponseHandler.generateResponse(CONSTANTMESSAGE.EMAIL_NOT_EXIST, HttpStatus.BAD_REQUEST,
						new EmptyJsonResponse());
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseHandler.generateResponse(CONSTANTMESSAGE.UNSUCCESSFULLY, HttpStatus.INTERNAL_SERVER_ERROR,
					new EmptyJsonResponse());

		}
	}

	@PostMapping(value = "/verifyOtpChangeEmail")
	public ResponseEntity<Object> verifyOtpChangeEmail(@RequestBody OtpVerificationForEmailRequest request) {
		try {

			if (request.getEmail() == null || request.getEmail().isEmpty()) {

				return ResponseHandler.generateResponse(CONSTANTMESSAGE.EMAIL_REQUIRED, HttpStatus.BAD_REQUEST,
						new EmptyJsonResponse());
			}

			if (request.getOtp() == null || request.getOtp() == 0) {

				return ResponseHandler.generateResponse(CONSTANTMESSAGE.OTP_REQUIRED, HttpStatus.BAD_REQUEST,
						new EmptyJsonResponse());
			}

			MultipleResponse result = userService.otpVerificationForEmail(request);

			if (result != null) {
				if (result.getType() == 1 && result.getUser() != null) {

					return ResponseHandler.generateResponse(CONSTANTMESSAGE.OTP_VERIFICATION_SUCCESSFULLY,
							HttpStatus.OK, result);
				} else if (result.getType() == 2) {
					return ResponseHandler.generateResponse(CONSTANTMESSAGE.EMAIL_NOT_EXIST, HttpStatus.BAD_REQUEST,
							new EmptyJsonResponse());
				} else if (result.getType() == 3) {
					return ResponseHandler.generateResponse(CONSTANTMESSAGE.OTP_WRONG, HttpStatus.BAD_REQUEST,
							new EmptyJsonResponse());
				} else {
					return ResponseHandler.generateResponse(CONSTANTMESSAGE.OTP_EXPIRE, HttpStatus.BAD_REQUEST,
							new EmptyJsonResponse());
				}
			} else
				return ResponseHandler.generateResponse(CONSTANTMESSAGE.OTP_VERIFICATION__UNSUCCESSFULLY,
						HttpStatus.BAD_REQUEST, new EmptyJsonResponse());
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseHandler.generateResponse(CONSTANTMESSAGE.OTP_VERIFICATION__UNSUCCESSFULLY,
					HttpStatus.INTERNAL_SERVER_ERROR, new EmptyJsonResponse());

		}
	}

	@PostMapping(value = "/userContactNumberBlock")
	public ResponseEntity<Object> userContactNoBlock(@RequestBody UserContactNoBlockPayload request) {
		try {
			if (request.getUserId() == null || request.getUserId() == 0) {

				return ResponseHandler.generateResponse(CONSTANTMESSAGE.USER_ID_REQUIRED, HttpStatus.BAD_REQUEST,
						new EmptyJsonResponse());

			}
			if (request.getMobileNumber() == null || request.getMobileNumber().isEmpty()) {

				return ResponseHandler.generateResponse(CONSTANTMESSAGE.MOBILE_NUMBER_REQUIRED, HttpStatus.BAD_REQUEST,
						new EmptyJsonResponse());

			}
			String msg = "";
			for (String num : request.getMobileNumber()) {
				if (num == null || num.isEmpty()) {
					msg = "mobileNumber can nor be empty or null";
					break;
				}
			}
			if (!msg.isEmpty()) {
				return ResponseHandler.generateResponse(msg, HttpStatus.BAD_REQUEST, new EmptyJsonResponse());
			}
			List<String> alreadyExistsNumbers = userService.checkExistsContact(request);

			if (alreadyExistsNumbers != null && !alreadyExistsNumbers.isEmpty()) {
				request.getMobileNumber().removeAll(alreadyExistsNumbers);

				Set<String> set = new HashSet<>(alreadyExistsNumbers);
				String str = String.join(",", set);

				msg = "The mobile numbers - " + str + " already exists.";
			}

			if (request.getMobileNumber().isEmpty()) {
				// return ResponseHandler.generateResponse(msg, HttpStatus.BAD_REQUEST, new
				// EmptyJsonResponse());
				return ResponseHandler.generateResponse(msg, HttpStatus.OK, new EmptyJsonResponse());
			}

			List<UserContactNoBlock> result = userService.userContactNoBlock(request);
			if (result != null)
				return ResponseHandler.generateResponse(CONSTANTMESSAGE.NUMBER_BLOCKED_SUCCESS, HttpStatus.OK,
						new EmptyJsonResponse());
			else
				return ResponseHandler.generateResponse(CONSTANTMESSAGE.FAILED_TO_BLOCK, HttpStatus.BAD_REQUEST,
						new EmptyJsonResponse());

		} catch (Exception e) {
			e.printStackTrace();
			return ResponseHandler.generateResponse(CONSTANTMESSAGE.FAILED, HttpStatus.INTERNAL_SERVER_ERROR,
					new EmptyJsonResponse());
		}

	}

	@PostMapping(value = "/getUserContactNumberBlock")
	public ResponseEntity<Object> getUserContactNoBlock(@RequestBody IDRequest request) {

		try {
			if (request.getId() == null || request.getId() == 0) {

				return ResponseHandler.generateResponse(CONSTANTMESSAGE.ID_REQUIRED, HttpStatus.BAD_REQUEST,
						new EmptyJsonResponse());
			}

			List<UserContactNoBlock> result = userService.getUserContactNoBlock(request);

			if (result != null || result.isEmpty())
				return ResponseHandler.generateResponse(CONSTANTMESSAGE.USER_FOUND, HttpStatus.OK, result);
			else
				return ResponseHandler.generateResponse(CONSTANTMESSAGE.USERNOT_FOUND, HttpStatus.BAD_REQUEST,
						new EmptyJsonResponse());
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseHandler.generateResponse(CONSTANTMESSAGE.EXCEPTION_CASE, HttpStatus.INTERNAL_SERVER_ERROR,
					new EmptyJsonResponse());

		}
	}

	@PostMapping(value = "/userContactNumberUnBlock")
	public ResponseEntity<Object> deleteUserContactNoBlock(@RequestBody UserContactNoBlockPayload request) {

		try {

			if (request.getMobileNumber() == null || request.getMobileNumber().isEmpty()) {

				return ResponseHandler.generateResponse(CONSTANTMESSAGE.MOBILE_NUMBER_REQUIRED, HttpStatus.BAD_REQUEST,
						new EmptyJsonResponse());

			}
			if (request.getUserId() == null || request.getUserId() == 0) {

				return ResponseHandler.generateResponse(CONSTANTMESSAGE.ID_REQUIRED, HttpStatus.BAD_REQUEST,
						new EmptyJsonResponse());

			}
			List<String> result = userService.deleteUserContactNoBlock(request);

			if (result != null)
				return ResponseHandler.generateResponse("Number Unblocked", HttpStatus.OK, new EmptyJsonResponse());

			else
				return ResponseHandler.generateResponse("Number Not Found!", HttpStatus.BAD_REQUEST,
						new EmptyJsonResponse());

		} catch (Exception e) {
			e.printStackTrace();
			return ResponseHandler.generateResponse("Number Not Deleted!", HttpStatus.INTERNAL_SERVER_ERROR,
					new EmptyJsonResponse());
		}
	}

	@PostMapping(value = "/verifyOtpforWordPress")
	public ResponseEntity<Object> verifyOtpforWordPress(@RequestBody OtpVerificationForEmailRequest request) {
		try {

			if (request.getEmail() == null || request.getEmail().isEmpty()) {

				return ResponseHandler.generateResponse(CONSTANTMESSAGE.EMAIL_REQUIRED, HttpStatus.BAD_REQUEST,
						new EmptyJsonResponse());
			}

			if (request.getOtp() == null || request.getOtp() == 0 ) {

				return ResponseHandler.generateResponse(CONSTANTMESSAGE.OTP_REQUIRED, HttpStatus.BAD_REQUEST,
						new EmptyJsonResponse());
			}

			MultipleResponse result = userService.verifyOtpforWordPress(request);

			if (result != null) {
				if (result.getType() == 1 && result.getUser() != null) {

					return ResponseHandler.generateResponse(CONSTANTMESSAGE.OTP_VERIFICATION_SUCCESSFULLY,
							HttpStatus.OK, result);
				} else if (result.getType() == 2) {
					return ResponseHandler.generateResponse(CONSTANTMESSAGE.EMAIL_NOT_EXIST, HttpStatus.BAD_REQUEST,
							new EmptyJsonResponse());
				} else if (result.getType() == 3) {
					return ResponseHandler.generateResponse(CONSTANTMESSAGE.OTP_WRONG, HttpStatus.BAD_REQUEST,
							new EmptyJsonResponse());
				} else {
					return ResponseHandler.generateResponse(CONSTANTMESSAGE.OTP_EXPIRE, HttpStatus.BAD_REQUEST,
							new EmptyJsonResponse());
				}
			} else
				return ResponseHandler.generateResponse(CONSTANTMESSAGE.OTP_VERIFICATION__UNSUCCESSFULLY,
						HttpStatus.BAD_REQUEST, new EmptyJsonResponse());
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseHandler.generateResponse(CONSTANTMESSAGE.OTP_VERIFICATION__UNSUCCESSFULLY,
					HttpStatus.INTERNAL_SERVER_ERROR, new EmptyJsonResponse());

		}
	}

	@GetMapping(value = "/encryptAllUsersPassword")
	public ResponseEntity<Object> encryptAllUsersPassword() {
		try {
			List<User> users = userService.encryptAllUsersPassword();

			if (users != null && !users.isEmpty()) {
				return ResponseHandler.generateResponse(CONSTANTMESSAGE.DATA_FETCH_SUCCESSFULLY, HttpStatus.OK, users);

			} else {
				return ResponseHandler.generateResponse(CONSTANTMESSAGE.DATA_FETCH_UNSUCCESSFULLY,
						HttpStatus.BAD_REQUEST, new EmptyJsonResponse());
			}
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseHandler.generateResponse(CONSTANTMESSAGE.UNSUCCESSFULLY, HttpStatus.INTERNAL_SERVER_ERROR,
					new EmptyJsonResponse());

		}
	
	}

	@PostMapping("/wordpressLogin")
	public ResponseEntity<Object> wordpressLogin(@RequestBody EmailAndPasswordRequest request) {
		try {
			if (request.getEmail() == null || request.getEmail().isEmpty()) {

				return ResponseHandler.generateResponse(CONSTANTMESSAGE.EMAIL_REQUIRED, HttpStatus.BAD_REQUEST,
						new EmptyJsonResponse());
			}

			if (request.getPassword() == null || request.getPassword().isEmpty()) {

				return ResponseHandler.generateResponse(CONSTANTMESSAGE.PASSWORD_REQUIRED, HttpStatus.BAD_REQUEST,
						new EmptyJsonResponse());
			}

			MultipleResponse result = userService.wordpressLogin(request.getEmail(),request.getPassword());
			if (result != null && result.getType() != null) {
				if (result.getType() == 1) {
				
					return ResponseHandler.generateResponse(CONSTANTMESSAGE.LOGIN_SUCCESSFULLY, HttpStatus.OK, result);
				} else if (result.getType() == 2) {
					return ResponseHandler.generateResponse(CONSTANTMESSAGE.USER_NOT_VERIFIED, HttpStatus.FORBIDDEN,
							result);
				} else {
					return ResponseHandler.generateResponse(CONSTANTMESSAGE.INVALID_EMAIL_PASSWORD,
							HttpStatus.BAD_REQUEST, new EmptyJsonResponse());
				}
			} else
				return ResponseHandler.generateResponse(CONSTANTMESSAGE.LOGIN_UNSUCCESSFULLY, HttpStatus.BAD_REQUEST,
						new EmptyJsonResponse());
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseHandler.generateResponse(CONSTANTMESSAGE.LOGIN_UNSUCCESSFULLY,
					HttpStatus.INTERNAL_SERVER_ERROR, new EmptyJsonResponse());
		}
	}
	
	// Add user subscription plan into the database
	@PostMapping("/addUserSubscriptionlan")
	public ResponseEntity<Object> addUserSubscriptionlan(@RequestBody UserSubscriptionPlanPayload request) {
		try {
			
			 UserSubscriptionPlan addUserSubscriptionPlan = userSubscriptionPlanService.addUserSubscriptionPlan(request);
//			 User user = addUserSubscriptionPlan.getUser();
//			 Long userId = user.getId();
			 
			 if(request.getUserId()!=null && request.getSubscriptionPlanId() !=null) {
				 
//				 SubscriptionPlan subscriptionPlanId = addUserSubscriptionPlan.
//				 Long userSubscriptionPlanId = subscriptionPlanId.getId();
				 
//				 if (request.getSubscriptionPlanId().equals(userSubscriptionPlanId)) {

						return ResponseHandler.generateResponse(CONSTANTMESSAGE.RECORD_ADDED_SUCCESSFULLY, HttpStatus.OK,
								addUserSubscriptionPlan);
//					} else {
//						return ResponseHandler.generateResponse(CONSTANTMESSAGE.ID_REQUIRED, HttpStatus.BAD_REQUEST,
//								new EmptyJsonResponse());
//					}
//				 
			 }else {
				 return ResponseHandler.generateResponse(CONSTANTMESSAGE.ID_REQUIRED, HttpStatus.BAD_REQUEST,
							new EmptyJsonResponse());
			 }
			 
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseHandler.generateResponse(CONSTANTMESSAGE.ID_NOT_FOUND,
					HttpStatus.INTERNAL_SERVER_ERROR, new EmptyJsonResponse());

		}

	}
	
	@GetMapping(value = "/userSubscriptionPlan/{userId}")
	public ResponseEntity<Object> getUserSubscriptionPlanByUserId(@PathVariable Long userId) {
		try {
			UserSubscriptionPlan planByUserId = userSubscriptionPlanService.getSubscriptionPlanByUserId(userId);
			if (userId != null && planByUserId != null) {
                    
				return ResponseHandler.generateResponse(CONSTANTMESSAGE.DATA_FETCH_SUCCESSFULLY, HttpStatus.OK,
						planByUserId);
                    
			} else {
				return ResponseHandler.generateResponse(CONSTANTMESSAGE.NO_RECORD_FOUND, HttpStatus.BAD_REQUEST,
						new EmptyJsonResponse());
			}
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseHandler.generateResponse(CONSTANTMESSAGE.NO_RECORD_FOUND, HttpStatus.BAD_REQUEST,
					new EmptyJsonResponse());
		}

	}
	
//	@PostMapping("/addUserSubscriptionlan")
//	public ResponseEntity<Object> addUserSubscriptionlan(@RequestBody UserSubscriptionPlanPayload request) {

		
//			try {
//				
//				
//				if (request.getUserId()==null||request.getUserId()==0) {
//
//					return ResponseHandler.generateResponse(CONSTANTMESSAGE.ID_REQUIRED, HttpStatus.BAD_REQUEST,
//							new EmptyJsonResponse());
//
//				}
//				if (request.getSubscriptionPlanId()==null||request.getSubscriptionPlanId()==0) {
//
//					return ResponseHandler.generateResponse(CONSTANTMESSAGE.ID_REQUIRED, HttpStatus.BAD_REQUEST,
//							new EmptyJsonResponse());
//
//				}
//				if (request.getDiscount()==null) {
//
//					return ResponseHandler.generateResponse(CONSTANTMESSAGE.DISCOUNT_REQUIRED, HttpStatus.BAD_REQUEST,
//							new EmptyJsonResponse());
//
//				}
//				if (request.getTitle()!=null||request.getTitle().isEmpty()) {
//
//					return ResponseHandler.generateResponse(CONSTANTMESSAGE.TITLE, HttpStatus.BAD_REQUEST,
//							new EmptyJsonResponse());
//
//				}
//				
//				String msg = "";
//				for (String num : request.getDescription()) {
//					if (num == null || num.isEmpty()) {
//						msg = CONSTANTMESSAGE.DESCRIPTION_REQUIRED;
//						break;
//					}
//				} 
//				if (!msg.isEmpty()) {
//					return ResponseHandler.generateResponse(msg, HttpStatus.BAD_REQUEST, new EmptyJsonResponse());
//				}
//
//				String result = subscriptionPlanService.subscriptionPlan(request);
//				if (result != null)
//					return ResponseHandler.generateResponse(CONSTANTMESSAGE.RECORD_ADDED_SUCCESSFULLY, HttpStatus.OK,
//							new EmptyJsonResponse());
//				else
//					return ResponseHandler.generateResponse(CONSTANTMESSAGE.DATA_NOT_FOUND,
//							HttpStatus.BAD_REQUEST, new EmptyJsonResponse());
//
//			} catch (Exception e) {
//				e.printStackTrace();
//				return ResponseHandler.generateResponse(CONSTANTMESSAGE.EXCEPTION_CASE,
//						HttpStatus.INTERNAL_SERVER_ERROR, new EmptyJsonResponse());
//			}
//return null;
//		}	
	

}