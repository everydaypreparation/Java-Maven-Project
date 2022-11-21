package com.cooee.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.cooee.config.ResponseHandler;
import com.cooee.model.AccountNumberDetails;
import com.cooee.model.Admin;
import com.cooee.model.Country;
import com.cooee.model.State;
import com.cooee.model.SubscriptionPlan;
import com.cooee.model.SubscriptionPlanDescription;
import com.cooee.model.User;
import com.cooee.model.VirtualNumbers;
import com.cooee.payload.BlockOrUnblockRequest;
import com.cooee.payload.BulkUploadRequest;
import com.cooee.payload.ChangeMail;
import com.cooee.payload.ChangePassword;
import com.cooee.payload.DidRequest;
import com.cooee.payload.EmailAndPasswordRequest;
import com.cooee.payload.EmailDataPayload;
import com.cooee.payload.EmptyJsonResponse;
import com.cooee.payload.GetAllResponse;
import com.cooee.payload.GetAllSubscriptionPlanPayload;
import com.cooee.payload.GetByStatusPayload;
import com.cooee.payload.GetSubcriptionPlanByIdPayload;
import com.cooee.payload.IDRequest;
import com.cooee.payload.MultipleResponse;
import com.cooee.payload.PushNotificationPayload;
import com.cooee.payload.RejectKycRequest;
import com.cooee.payload.ResetPassword;
import com.cooee.payload.ResetUserPassword;
import com.cooee.payload.SubscriptionPlanPayload;
import com.cooee.payload.UpdateSubscriptionPlanPayload;
import com.cooee.payload.UsernameAndPasswordRequest;
import com.cooee.repository.SendByEmailRepo;
import com.cooee.repository.SubscriptionPlanDescriptionRepo;
import com.cooee.repository.VirtualNumbersRepository;
import com.cooee.responsemessgae.CONSTANTMESSAGE;
import com.cooee.service.AdminService;
import com.cooee.service.CountryService;
import com.cooee.service.StateService;
import com.cooee.service.SubscriptionPlanService;
import com.cooee.service.UserService;
import com.cooee.service.VirtualNumbersService;

@CrossOrigin
@RestController
@RequestMapping("/admin")
public class AdminController {

	@Autowired
	private AdminService adminService;

	@Autowired
	private VirtualNumbersService virtualNumbersService;

	@Autowired
	private VirtualNumbersRepository virtualNumbersRepository;

	@Autowired
	private CountryService countryService;
	@Autowired
	private UserService userService;

	@Autowired
	private StateService stateService;
	
	@Autowired
	private SubscriptionPlanService subscriptionPlanService;
	
	@Autowired
	private SendByEmailRepo sendByEmailRepo;
	
	@Autowired
	SubscriptionPlanDescriptionRepo subscriptionPlanDescriptionRepo;

	@PostMapping("/login")
	public ResponseEntity<Object> login(@RequestBody UsernameAndPasswordRequest request) {

		try {

			System.out.println(request.getUsername() + " - " + request.getPassword());

			if (request.getUsername() == null || request.getUsername().isEmpty()) {

				return ResponseHandler.generateResponse(CONSTANTMESSAGE.USERNAME_REQUIRED, HttpStatus.BAD_REQUEST,
						new EmptyJsonResponse());
			}

			if (request.getPassword() == null || request.getPassword().isEmpty()) {

				return ResponseHandler.generateResponse(CONSTANTMESSAGE.PASSWORD_REQUIRED, HttpStatus.BAD_REQUEST,
						new EmptyJsonResponse());
			}

			Admin admin = adminService.findByUsernameAndPassword(request);

			if (admin != null) {
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("loginStatus", true);
//		            map.put("status", 200);
//		            map.put("message", new EmptyJsonResponse);
// 					map.put("data", new EmptyJsonResponse);
//		            return new ResponseEntity<Object>(map,status);

				return ResponseHandler.generateResponse(CONSTANTMESSAGE.LOGIN_SUCCESSFULLY, HttpStatus.OK, map);
			} else
				return ResponseHandler.generateResponse(CONSTANTMESSAGE.USERNOT_FOUND, HttpStatus.BAD_REQUEST,
						new EmptyJsonResponse());

		} catch (Exception e) {
			return ResponseHandler.generateResponse(CONSTANTMESSAGE.LOGIN_UNSUCCESSFULLY,
					HttpStatus.INTERNAL_SERVER_ERROR, new EmptyJsonResponse());
		}
	}

	@PostMapping("/approveKyc")
	public ResponseEntity<Object> approveKyc(@RequestBody IDRequest iDRequest) {

		try {
			if (iDRequest.getId() == null || iDRequest.getId() == 0) {

				return ResponseHandler.generateResponse(CONSTANTMESSAGE.ID_REQUIRED, HttpStatus.BAD_REQUEST,
						new EmptyJsonResponse());
			}

			MultipleResponse multiResponse = adminService.approveKyc(iDRequest);
			if (multiResponse != null) {
				if (multiResponse.getType() == 1 && multiResponse.getUser() != null) {

					return ResponseHandler.generateResponse(CONSTANTMESSAGE.APPROVED_KYC_SUCCESSFULLY, HttpStatus.OK,
							multiResponse.getUser());
				} else if (multiResponse.getType() == 2) {

					return ResponseHandler.generateResponse(CONSTANTMESSAGE.DIDNUMBER_NOT_EXIST, HttpStatus.BAD_REQUEST,
							new EmptyJsonResponse());
				} else if (multiResponse.getType() == 3) {

					return ResponseHandler.generateResponse(CONSTANTMESSAGE.DIDNUMBER_ALREADY_ASSIGNED,
							HttpStatus.BAD_REQUEST, new EmptyJsonResponse());
				} else if (multiResponse.getType() == 4) {

					return ResponseHandler.generateResponse("Extension Creation Failed!", HttpStatus.BAD_REQUEST,
							new EmptyJsonResponse());
				} else if (multiResponse.getType() == 5) {
					return ResponseHandler.generateResponse(CONSTANTMESSAGE.USERNOT_FOUND, HttpStatus.BAD_REQUEST,
							new EmptyJsonResponse());
				} else
					return ResponseHandler.generateResponse("User is Already Approved", HttpStatus.BAD_REQUEST,
							new EmptyJsonResponse());
			} else {
				return ResponseHandler.generateResponse(CONSTANTMESSAGE.USERNOT_FOUND, HttpStatus.BAD_REQUEST,
						new EmptyJsonResponse());
			}
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseHandler.generateResponse(CONSTANTMESSAGE.APPROVED_KYC_UNSUCCESSFULLY,
					HttpStatus.INTERNAL_SERVER_ERROR, new EmptyJsonResponse());
		}
	}

	@PostMapping("/rejectKyc")
	public ResponseEntity<Object> rejectKyc(@RequestBody RejectKycRequest rejectKycRequest) {

		try {
			if (rejectKycRequest.getUserId() == null || rejectKycRequest.getUserId() == 0) {

				return ResponseHandler.generateResponse(CONSTANTMESSAGE.ID_REQUIRED, HttpStatus.BAD_REQUEST,
						new EmptyJsonResponse());
			}
			if (rejectKycRequest.getDescriptions() == null || rejectKycRequest.getDescriptions().isEmpty()) {

				return ResponseHandler.generateResponse(CONSTANTMESSAGE.DESCRIPTIONS_REQUIRED, HttpStatus.BAD_REQUEST,
						new EmptyJsonResponse());
			}

			MultipleResponse multiResponse = adminService.rejectKyc(rejectKycRequest);
			if (multiResponse != null) {
				if (multiResponse.getType() == 1) {

					return ResponseHandler.generateResponse(CONSTANTMESSAGE.REJECT_KYC_SUCCESSFULLY, HttpStatus.OK,
							new EmptyJsonResponse());
				} else if (multiResponse.getType() == 2) {

					return ResponseHandler.generateResponse(CONSTANTMESSAGE.DID_DOENST_EXIST, HttpStatus.BAD_REQUEST,
							new EmptyJsonResponse());
				} else if (multiResponse.getType() == 3) {
					return ResponseHandler.generateResponse(CONSTANTMESSAGE.USERNOT_FOUND, HttpStatus.BAD_REQUEST,
							new EmptyJsonResponse());

				}else if (multiResponse.getType() == 4) {
					return ResponseHandler.generateResponse(CONSTANTMESSAGE.DIDNUMBER_ALREADY_ASSIGNED, HttpStatus.BAD_REQUEST,
							new EmptyJsonResponse());

				} 
				else {
					return ResponseHandler.generateResponse(CONSTANTMESSAGE.REJECT_KYC_UNSUCCESSFULLY,
							HttpStatus.BAD_REQUEST, new EmptyJsonResponse());

				}
			} else
				return ResponseHandler.generateResponse(CONSTANTMESSAGE.REJECT_KYC_UNSUCCESSFULLY,
						HttpStatus.BAD_REQUEST, new EmptyJsonResponse());

		} catch (Exception e) {
			e.printStackTrace();
			return ResponseHandler.generateResponse(CONSTANTMESSAGE.REJECT_KYC_UNSUCCESSFULLY,
					HttpStatus.INTERNAL_SERVER_ERROR, new EmptyJsonResponse());
		}
	}

	@PostMapping("/addDidNumber")
	public ResponseEntity<Object> didNumber(@RequestBody DidRequest didRequest) {

		try {
			if (didRequest.getDidNumber() == null || didRequest.getDidNumber().isEmpty()) {

				return ResponseHandler.generateResponse(CONSTANTMESSAGE.DID_NUMBER_REQUIRED, HttpStatus.NOT_ACCEPTABLE,
						new EmptyJsonResponse());
			}
			if (didRequest.getCountry() == null || didRequest.getCountry().isEmpty()) {

				return ResponseHandler.generateResponse(CONSTANTMESSAGE.COUNTRY_REQUIRED, HttpStatus.NOT_ACCEPTABLE,
						new EmptyJsonResponse());
			}
			if (virtualNumbersService.getByVirtualNumber(didRequest.getDidNumber()) != null) {
				// if (virtualNumbersService.getByVirtualNumber(didRequest.getDidNumber() !=
				// null) {
				return ResponseHandler.generateResponse(CONSTANTMESSAGE.DID_NUMBER_ALREADY_EXIST,
						HttpStatus.ALREADY_REPORTED, new EmptyJsonResponse());
			}
			VirtualNumbers number = virtualNumbersService.addVirtualNumber(didRequest.getDidNumber(),
					didRequest.getCountry(), "", "", "");
			if (number != null) {

				return ResponseHandler.generateResponse(CONSTANTMESSAGE.ADD_DIDNUMBER_SUCCESSFULLY, HttpStatus.OK,
						number);
			} else
				return ResponseHandler.generateResponse(CONSTANTMESSAGE.ADD_DIDNUMBER_UNSUCCESSFULLY,
						HttpStatus.BAD_REQUEST, new EmptyJsonResponse());

		} catch (Exception e) {
			return ResponseHandler.generateResponse(CONSTANTMESSAGE.ADD_DIDNUMBER_UNSUCCESSFULLY,
					HttpStatus.INTERNAL_SERVER_ERROR, new EmptyJsonResponse());
		}
	}

	@GetMapping("/getAllDID")
	public ResponseEntity<Object> getAllDidNumber() {
		try {

			List<VirtualNumbers> result = virtualNumbersRepository.findAllByOrderByIdDesc();// findAll();

			if (result != null && !result.isEmpty())
				return ResponseHandler.generateResponse(CONSTANTMESSAGE.All_DID_NUMBER_FOUND, HttpStatus.OK, result);
			else
				return ResponseHandler.generateResponse(CONSTANTMESSAGE.NO_RECORD_FOUND, HttpStatus.BAD_REQUEST,
						new EmptyJsonResponse());
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseHandler.generateResponse(CONSTANTMESSAGE.NO_RECORD_FOUND, HttpStatus.INTERNAL_SERVER_ERROR,
					new EmptyJsonResponse());
		}
	}

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

	@PostMapping(value = "/resetPassword")
	public ResponseEntity<Object> resetPassword(@RequestBody ResetPassword request) {
		try {
			if (request.getEmail() == null || request.getEmail().isEmpty()) {

				return ResponseHandler.generateResponse(CONSTANTMESSAGE.EMAIL_REQUIRED, HttpStatus.BAD_REQUEST,
						new EmptyJsonResponse());
			}

			User result = adminService.resetPassword(request);

			if (result != null)
				return ResponseHandler.generateResponse(CONSTANTMESSAGE.RESET_PASSWORD_EMAIL, HttpStatus.OK,
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

	@PostMapping(value = "/updatePassword")
	public ResponseEntity<Object> updatePassword(@RequestBody EmailAndPasswordRequest request) {
		try {

			if (request.getEmail() == null || request.getEmail().isEmpty()) {

				return ResponseHandler.generateResponse(CONSTANTMESSAGE.EMAIL_REQUIRED, HttpStatus.BAD_REQUEST,
						new EmptyJsonResponse());
			}

			User result = adminService.updatePassword(request.getEmail(), request.getPassword());

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

	@PostMapping("/blockOrUnblock")
	public ResponseEntity<Object> blockOrUnblock(@RequestBody BlockOrUnblockRequest blockOrUnblockRequest) {

		try {
			if (blockOrUnblockRequest.getId() == null || blockOrUnblockRequest.getId() == 0) {
				return ResponseHandler.generateResponse(CONSTANTMESSAGE.ID_REQUIRED, HttpStatus.BAD_REQUEST,
						new EmptyJsonResponse());
			}

			User result = adminService.blockOrUnblock(blockOrUnblockRequest);
			if (result != null) {

				return ResponseHandler.generateResponse(CONSTANTMESSAGE.SUCCESSFULLY, HttpStatus.OK,
						new EmptyJsonResponse());
			} else
				return ResponseHandler.generateResponse(CONSTANTMESSAGE.ID_NOT_FOUND, HttpStatus.BAD_REQUEST,
						new EmptyJsonResponse());

		} catch (Exception e) {
			return ResponseHandler.generateResponse(CONSTANTMESSAGE.EXCEPTION_CASE, HttpStatus.INTERNAL_SERVER_ERROR,
					new EmptyJsonResponse());
		}
	}

	@PostMapping("/bulkUpload")
	public ResponseEntity<Object> bulkUpload(@RequestBody BulkUploadRequest bulkUploadRequest) {

		try {
			if (bulkUploadRequest.getDidNumber() == null || bulkUploadRequest.getDidNumber().isEmpty()) {

				return ResponseHandler.generateResponse(CONSTANTMESSAGE.DID_NUMBER_REQUIRED, HttpStatus.BAD_REQUEST,
						new EmptyJsonResponse());
			}
			if (bulkUploadRequest.getCountry() == null || bulkUploadRequest.getCountry().isEmpty()) {

				return ResponseHandler.generateResponse(CONSTANTMESSAGE.COUNTRY_REQUIRED, HttpStatus.BAD_REQUEST,
						new EmptyJsonResponse());
			}

			String msg = "";

			for (String num : bulkUploadRequest.getDidNumber()) {
				if (num == null || num.isEmpty()) {
					msg = "The DID number can not be empty or null!";
					break;
				}
			}
			if (!msg.isEmpty()) {
				return ResponseHandler.generateResponse(msg, HttpStatus.BAD_REQUEST, new EmptyJsonResponse());
			}

			List<String> alreadyExistsNumbers = virtualNumbersService.checkExistsBuld(bulkUploadRequest);
			if (alreadyExistsNumbers != null && !alreadyExistsNumbers.isEmpty()) {
				bulkUploadRequest.getDidNumber().removeAll(alreadyExistsNumbers);

				Set<String> set = new HashSet<>(alreadyExistsNumbers);
				String str = String.join(",", set);

				msg = "The DID numbers - " + str + " already exists.";
			}

			if (bulkUploadRequest.getDidNumber().isEmpty()) {
				// return ResponseHandler.generateResponse(msg, HttpStatus.BAD_REQUEST, new
				// EmptyJsonResponse());
				return ResponseHandler.generateResponse(msg, HttpStatus.OK, new EmptyJsonResponse());
			}

			List<VirtualNumbers> number = virtualNumbersService.bulkUpload(bulkUploadRequest);

			if (number != null) {
				if (msg.isEmpty()) {
					return ResponseHandler.generateResponse(CONSTANTMESSAGE.ADD_DIDNUMBER_SUCCESSFULLY, HttpStatus.OK,
							number);
				} else
					return ResponseHandler.generateResponse(msg, HttpStatus.OK, number);
			} else
				return ResponseHandler.generateResponse(CONSTANTMESSAGE.ADD_DIDNUMBER_UNSUCCESSFULLY,
						HttpStatus.BAD_REQUEST, new EmptyJsonResponse());

		} catch (Exception e) {
			return ResponseHandler.generateResponse(CONSTANTMESSAGE.ADD_DIDNUMBER_UNSUCCESSFULLY,
					HttpStatus.INTERNAL_SERVER_ERROR, new EmptyJsonResponse());
		}
	}

	@GetMapping("/getAllCountries")
	public ResponseEntity<?> getAllCountry() {
		List<Country> response = countryService.getAllCountry();
		if (response != null) {

			return ResponseHandler.generateResponse(CONSTANTMESSAGE.DATA_FETCH_SUCCESSFULLY, HttpStatus.OK, response);

		} else
			return ResponseHandler.generateResponse(CONSTANTMESSAGE.DATA_FETCH_UNSUCCESSFULLY,
					HttpStatus.INTERNAL_SERVER_ERROR, new EmptyJsonResponse());

	}

	@PostMapping("/getStateByCountryId")
	public ResponseEntity<Object> getState(@RequestBody IDRequest request) {
		try {
			if (request.getId() == null || request.getId() == 0) {

				return ResponseHandler.generateResponse(CONSTANTMESSAGE.ID_REQUIRED, HttpStatus.BAD_REQUEST,
						new EmptyJsonResponse());
			}

			List<State> response = stateService.findByCountryIdOrderByIdAsc(request.getId());

			return ResponseHandler.generateResponse(CONSTANTMESSAGE.DATA_FETCH_SUCCESSFULLY, HttpStatus.OK, response);

		} catch (Exception e) {
			e.printStackTrace();
			return ResponseHandler.generateResponse(CONSTANTMESSAGE.EXCEPTION_CASE, HttpStatus.INTERNAL_SERVER_ERROR,
					new EmptyJsonResponse());
		}

	}

	@PostMapping(path = "/upload_term_and_condition", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
	public ResponseEntity<Object> termAndConditionPdf(
			@RequestParam(value = "file", required = false) MultipartFile multipartFile) {

		try {
			String filename = CONSTANTMESSAGE.TERM_AND_CONDITION;
			String result = adminService.uploadPdfWithName(multipartFile, filename);

			if (result != null) {
				return ResponseHandler.generateResponse(CONSTANTMESSAGE.FILE_UPLOAD_SUCCESSFULLY, HttpStatus.OK,
						result);
			} else
				return ResponseHandler.generateResponse(CONSTANTMESSAGE.FILE_UPLOAD_FAILED, HttpStatus.BAD_REQUEST,
						new EmptyJsonResponse());
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseHandler.generateResponse(CONSTANTMESSAGE.FILE_UPLOAD_FAILED,
					HttpStatus.INTERNAL_SERVER_ERROR, new EmptyJsonResponse());

		}
	}

	@PostMapping(path = "/upload_privacy_policy", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
	public ResponseEntity<Object> privacyPolicyPdf(
			@RequestParam(value = "file", required = false) MultipartFile multipartFile) {
		try {
			String result = adminService.uploadPdfWithName(multipartFile, CONSTANTMESSAGE.PRIVACY_POLICY);
			if (result != null) {
				return ResponseHandler.generateResponse(CONSTANTMESSAGE.FILE_UPLOAD_SUCCESSFULLY, HttpStatus.OK,
						result);
			} else
				return ResponseHandler.generateResponse(CONSTANTMESSAGE.FILE_UPLOAD_FAILED, HttpStatus.BAD_REQUEST,
						new EmptyJsonResponse());
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseHandler.generateResponse(CONSTANTMESSAGE.FILE_UPLOAD_FAILED,
					HttpStatus.INTERNAL_SERVER_ERROR, new EmptyJsonResponse());

		}
	}

	@PostMapping(path = "/upload_cooeefaq", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
	public ResponseEntity<Object> cooeefaqPdf(
			@RequestParam(value = "file", required = false) MultipartFile multipartFile) {
		try {
			String result = adminService.uploadPdfWithName(multipartFile, CONSTANTMESSAGE.FAQ);
			if (result != null) {
				return ResponseHandler.generateResponse(CONSTANTMESSAGE.FILE_UPLOAD_SUCCESSFULLY, HttpStatus.OK,
						result);
			} else
				return ResponseHandler.generateResponse(CONSTANTMESSAGE.FILE_UPLOAD_FAILED, HttpStatus.BAD_REQUEST,
						new EmptyJsonResponse());
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseHandler.generateResponse(CONSTANTMESSAGE.FILE_UPLOAD_FAILED,
					HttpStatus.INTERNAL_SERVER_ERROR, new EmptyJsonResponse());

		}

	}
	@PostMapping(value = "/resetUserPassword")
	public ResponseEntity<Object> resetUserPassword(@RequestBody ResetUserPassword request) {
		try {
			if (request.getId()==null|| request.getId()==0) {

				return ResponseHandler.generateResponse(CONSTANTMESSAGE.ID_REQUIRED, HttpStatus.BAD_REQUEST,
						new EmptyJsonResponse());
			}

			User result = adminService.resetUserPassword(request);

			if (result != null)
				return ResponseHandler.generateResponse(CONSTANTMESSAGE.PASSWORD_UPDATED_SUCCESSFULLY, HttpStatus.OK,
						new EmptyJsonResponse());
			else
				return ResponseHandler.generateResponse(CONSTANTMESSAGE.ID_NOT_EXIST, HttpStatus.BAD_REQUEST,
						new EmptyJsonResponse());
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseHandler.generateResponse(CONSTANTMESSAGE.PASSWORD_UPDATED_UNSUCCESSFULLY,
					HttpStatus.INTERNAL_SERVER_ERROR, new EmptyJsonResponse());

		}
	}
	@PostMapping(value = "/delete")
	public ResponseEntity<Object> deleteExtansion(@RequestBody IDRequest request) {

		try {
			if (request.getId() == null || request.getId() == 0) {

				return ResponseHandler.generateResponse(CONSTANTMESSAGE.ID_REQUIRED, HttpStatus.BAD_REQUEST,
						new EmptyJsonResponse());

			}

			int status = adminService.deleteUser(request.getId());

			if (status == 1) {
				return ResponseHandler.generateResponse("User Deleted Successfully!", HttpStatus.OK,
						new EmptyJsonResponse());

			} else   {
				return ResponseHandler.generateResponse("User Not Found!", HttpStatus.BAD_REQUEST,
						new EmptyJsonResponse());

			} 

		} catch (Exception e) {
			e.printStackTrace();
			return ResponseHandler.generateResponse("User Not Deleted!", HttpStatus.INTERNAL_SERVER_ERROR,
					new EmptyJsonResponse());
		}
	}
	

	
	@PostMapping(value = "/sentPushNotification")
	public ResponseEntity<Object> sentPushNotification(@RequestBody PushNotificationPayload request) {

		String	 result = userService.getAllUsersByIds(request);

		try {
			if (result != null) {
				System.out.println("ios push notification result==>" + result);
				return ResponseHandler.generateResponse(CONSTANTMESSAGE.SUCCESSFULLY, HttpStatus.OK, result);
			} else {
				return ResponseHandler.generateResponse(CONSTANTMESSAGE.FAILED, HttpStatus.BAD_REQUEST,
						new EmptyJsonResponse());
				
			}
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseHandler.generateResponse(CONSTANTMESSAGE.DATA_FETCH_UNSUCCESSFULLY,
					HttpStatus.INTERNAL_SERVER_ERROR, new EmptyJsonResponse());
		}

	}

	@PostMapping(value = "/sentPushNotificationOnEmail")
	public ResponseEntity<Object> sentPushNotificationOnEmail(@RequestBody PushNotificationPayload request) {
		if (request.getDescription()==null||request.getDescription().isEmpty()) {

			return ResponseHandler.generateResponse(CONSTANTMESSAGE.DESCRIPTION_REQUIRED, HttpStatus.BAD_REQUEST,
					new EmptyJsonResponse());

		}
		if (request.getTitle()==null||request.getTitle().isEmpty()) {

			return ResponseHandler.generateResponse(CONSTANTMESSAGE.EMAIL_TITLE_REQUIRED, HttpStatus.BAD_REQUEST,
					new EmptyJsonResponse());

		}	
		String	 result = userService.getAllUsersByIdsEmail(request);

		try {
			if (result != null) {
			
				
						return ResponseHandler.generateResponse(CONSTANTMESSAGE.SUCCESSFULLY, HttpStatus.OK, result);
			} else {
				return ResponseHandler.generateResponse(CONSTANTMESSAGE.FAILED, HttpStatus.BAD_REQUEST,
						new EmptyJsonResponse());
				
			}
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseHandler.generateResponse(CONSTANTMESSAGE.DATA_FETCH_UNSUCCESSFULLY,
					HttpStatus.INTERNAL_SERVER_ERROR, new EmptyJsonResponse());
		}

	}
 	@PostMapping("/addSubscriptionPlan")
	public ResponseEntity<Object> subscriptionPlan(@RequestBody SubscriptionPlanPayload request) {

		
			try {
				
				if (request.getCountry()==null||request.getCountry().isEmpty()) {

					return ResponseHandler.generateResponse(CONSTANTMESSAGE.COUNTRY_REQUIRED, HttpStatus.BAD_REQUEST,
							new EmptyJsonResponse());

				}

				if (request.getPlanValidity()==null||request.getPlanValidity().isEmpty()) {

					return ResponseHandler.generateResponse(CONSTANTMESSAGE.PLAN_VALIDITY_REQUIRED, HttpStatus.BAD_REQUEST,
							new EmptyJsonResponse());

				}
				if (request.getIncomingSms()==null||request.getIncomingSms().isEmpty()) {

					return ResponseHandler.generateResponse(CONSTANTMESSAGE.INCOMING_SMS_REQUIRED, HttpStatus.BAD_REQUEST,
							new EmptyJsonResponse());

				}
				if (request.getIncomingCall()==null||request.getIncomingCall().isEmpty()) {

					return ResponseHandler.generateResponse(CONSTANTMESSAGE.INCOMING_CALL_REQUIRED, HttpStatus.BAD_REQUEST,
							new EmptyJsonResponse());
				}
				
				if (request.getOutgoingSms()==null||request.getOutgoingSms().isEmpty()) {
					return ResponseHandler.generateResponse(CONSTANTMESSAGE.OUTGOING_SMS_REQUIRED, HttpStatus.BAD_REQUEST,
							new EmptyJsonResponse());
				}
				
				if (request.getOutgoingCall()==null||request.getOutgoingCall().isEmpty()) {

					return ResponseHandler.generateResponse(CONSTANTMESSAGE.OUTGOING_CALL_REQUIRED, HttpStatus.BAD_REQUEST,
							new EmptyJsonResponse());

				}
				if (request.getAmount()==null||request.getAmount()==0) {

					return ResponseHandler.generateResponse(CONSTANTMESSAGE.AMOUNT_REQUIRED, HttpStatus.BAD_REQUEST,
							new EmptyJsonResponse());

				}
				if (request.getDiscount()==null) {

					return ResponseHandler.generateResponse(CONSTANTMESSAGE.DISCOUNT_REQUIRED, HttpStatus.BAD_REQUEST,
							new EmptyJsonResponse());

				}
				if (request.getTitle()==null||request.getTitle().isEmpty()) {

					return ResponseHandler.generateResponse(CONSTANTMESSAGE.TITLE, HttpStatus.BAD_REQUEST,
							new EmptyJsonResponse());

				}
				
				String msg = "";
				for (String num : request.getDescription()) {
					if (num == null || num.isEmpty()) {
						msg = CONSTANTMESSAGE.DESCRIPTION_REQUIRED;
						break;
					}
				}
				if (!msg.isEmpty()) {
					return ResponseHandler.generateResponse(msg, HttpStatus.BAD_REQUEST, new EmptyJsonResponse());
				}

				String result = subscriptionPlanService.subscriptionPlan(request);
				if (result != null)
					return ResponseHandler.generateResponse(CONSTANTMESSAGE.RECORD_ADDED_SUCCESSFULLY, HttpStatus.OK,
							new EmptyJsonResponse());
				else
					return ResponseHandler.generateResponse(CONSTANTMESSAGE.RECORD_ADDED_UNSUCCESSFULLY,
							HttpStatus.BAD_REQUEST, new EmptyJsonResponse());

			} catch (Exception e) {
				e.printStackTrace();
				return ResponseHandler.generateResponse(CONSTANTMESSAGE.EXCEPTION_CASE,
						HttpStatus.INTERNAL_SERVER_ERROR, new EmptyJsonResponse());
			}

		}
 	@GetMapping("/getAllSubscriptionPlan")
	public ResponseEntity<Object> getAllSubscriptionPlan() {
		try {

//			List<VirtualNumbers> result = virtualNumbersRepository.findAllByOrderByIdDesc();// findAll();
			List<SubscriptionPlan> result = subscriptionPlanService.getAllSubscriptionPlan();// findAll();

			if (result != null && !result.isEmpty())
				return ResponseHandler.generateResponse(CONSTANTMESSAGE.DATA_FETCH_SUCCESSFULLY, HttpStatus.OK, result);
			else
				return ResponseHandler.generateResponse(CONSTANTMESSAGE.NO_RECORD_FOUND, HttpStatus.BAD_REQUEST,
						new EmptyJsonResponse());
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseHandler.generateResponse(CONSTANTMESSAGE.NO_RECORD_FOUND, HttpStatus.INTERNAL_SERVER_ERROR,
					new EmptyJsonResponse());
		}
	}

	@PostMapping(value = "/getByIdSubscriptionPlan")
	public ResponseEntity<Object> getByIdSubscriptionPlan(@RequestBody IDRequest request) {

		try {
			if (request.getId() == null) {

				return ResponseHandler.generateResponse(CONSTANTMESSAGE.ID_REQUIRED, HttpStatus.BAD_REQUEST,
						new EmptyJsonResponse());
			}

			SubscriptionPlan result = subscriptionPlanService.getByIdSubscriptionPlan(request.getId());// findAll();

			if (result != null) {
						System.out.println("Result found - " + result.getId());
						List<SubscriptionPlanDescription> subscriptionPlanDescription=new ArrayList<SubscriptionPlanDescription>();
						List<SubscriptionPlanDescription> ac = subscriptionPlanDescriptionRepo.findBySubscriptionPlanId(result.getId());
				if (ac != null) {
					for(SubscriptionPlanDescription list:ac) {
						subscriptionPlanDescription.add(list);
					
							}
					if(subscriptionPlanDescription!=null) {
						result.setSubscriptionPlanDescription(subscriptionPlanDescription);
						
					}
					}
				return ResponseHandler.generateResponse(CONSTANTMESSAGE.DATA_FETCH_SUCCESSFULLY, HttpStatus.OK, result);
			} else
				return ResponseHandler.generateResponse(CONSTANTMESSAGE.DATA_NOT_FOUND, HttpStatus.BAD_REQUEST,
						new EmptyJsonResponse());
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseHandler.generateResponse(CONSTANTMESSAGE.EXCEPTION_CASE, HttpStatus.INTERNAL_SERVER_ERROR,
					new EmptyJsonResponse());

		}
	}

	@PostMapping(value = "/updateSubscriptionPlan")
	public ResponseEntity<Object> updateSubscriptionPlan(@RequestBody UpdateSubscriptionPlanPayload request) {

		try {
			if (request.getId() == null||request.getId()==0) {

				return ResponseHandler.generateResponse(CONSTANTMESSAGE.ID_REQUIRED, HttpStatus.BAD_REQUEST,
						new EmptyJsonResponse());
			}

			SubscriptionPlan result = subscriptionPlanService.updateSubscriptionPlan(request);// findAll();

			if (result != null) {
				
		return ResponseHandler.generateResponse(CONSTANTMESSAGE.RECORD_UPDATED_SUCCESSFULLY , HttpStatus.OK, result);
	} else
				return ResponseHandler.generateResponse(CONSTANTMESSAGE.RECORD_NOTUPDATED_SUCCESSFULLY, HttpStatus.BAD_REQUEST,
						new EmptyJsonResponse());
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseHandler.generateResponse(CONSTANTMESSAGE.EXCEPTION_CASE, HttpStatus.INTERNAL_SERVER_ERROR,
					new EmptyJsonResponse());

		}
	}
	@PostMapping(value = "/deleteByIdSubscriptionPlan")
	public ResponseEntity<Object> deleteSubscriptionPlan(@RequestBody IDRequest request) {

		try {
			if (request.getId() == null||request.getId()==0) {

				return ResponseHandler.generateResponse(CONSTANTMESSAGE.ID_REQUIRED, HttpStatus.BAD_REQUEST,
						new EmptyJsonResponse());
			}

			String result = subscriptionPlanService.deleteByIdSubscriptionPlan(request);// findAll();

			if (result != null) {						
					
				return ResponseHandler.generateResponse(CONSTANTMESSAGE.DELETED, HttpStatus.OK, result);
			} else
				return ResponseHandler.generateResponse(CONSTANTMESSAGE.DATA_FETCH_UNSUCCESSFULLY, HttpStatus.BAD_REQUEST,
						new EmptyJsonResponse());
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseHandler.generateResponse(CONSTANTMESSAGE.EXCEPTION_CASE, HttpStatus.INTERNAL_SERVER_ERROR,
					new EmptyJsonResponse());

		}
	}
	@PostMapping(value = "/getSubscriptionPlanByStatus")
	public ResponseEntity<Object> getBySubscriptionPlanByStatus(@RequestBody GetByStatusPayload request) {

		 List<SubscriptionPlan> result = subscriptionPlanService.getSubscriptionPlanByStatus(request);// findAll();

			if (result != null) {
				
		return ResponseHandler.generateResponse(CONSTANTMESSAGE.RECORD_ADDED_SUCCESSFULLY, HttpStatus.OK, result);
	} else
				return ResponseHandler.generateResponse(CONSTANTMESSAGE.EXCEPTION_CASE, HttpStatus.BAD_REQUEST,
						new EmptyJsonResponse());
		
			
		}
	@GetMapping("/changeStatusById")
	public ResponseEntity<Object> changeStatusById(@RequestParam Long id,
			@RequestParam(required = true) Boolean status) {
		try {

			SubscriptionPlan response = subscriptionPlanService.changeStatusById(id, status);

			if (response != null)

				return ResponseHandler.generateResponse(CONSTANTMESSAGE.RECORD_UPDATED_SUCCESSFULLY, HttpStatus.OK, response);
			else
				return ResponseHandler.generateResponse(CONSTANTMESSAGE.RECORD_ADDED_UNSUCCESSFULLY, HttpStatus.BAD_REQUEST,
						new EmptyJsonResponse());
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseHandler.generateResponse(CONSTANTMESSAGE.EXCEPTION_CASE, HttpStatus.INTERNAL_SERVER_ERROR,
					new EmptyJsonResponse());
		}
	}

}
