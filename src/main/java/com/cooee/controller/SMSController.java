package com.cooee.controller;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cooee.config.ResponseHandler;
import com.cooee.model.Sms;
import com.cooee.payload.BasicResponsePayload;
import com.cooee.payload.DeletedSmsFromDid;
import com.cooee.payload.EmptyJsonResponse;
import com.cooee.payload.IDRequest;
import com.cooee.payload.SMSRequest;
import com.cooee.payload.SendSmsRequest;
import com.cooee.payload.SmsDeleteById;
import com.cooee.payload.StatusResponse;
import com.cooee.payload.UserContactNoUnblockPayload;
import com.cooee.repository.DeletedSmsRepository;
import com.cooee.responsemessgae.CONSTANTMESSAGE;
import com.cooee.service.SmsService;
import com.cooee.util.IUtil;

@CrossOrigin
@RestController
@RequestMapping("/sms")
public class SMSController {
 
	@Autowired
	private SmsService smsService;

	@PostMapping("/receive")
	public ResponseEntity<StatusResponse> receiveSms(@RequestBody SMSRequest request) {

		StatusResponse response = new StatusResponse();
		BasicResponsePayload responseTwo = new BasicResponsePayload();

		try {
			String messageId=request.getId();
			String date=request.getDate();
			
			String sourceParam = IUtil.getTrimParam(request.getFrom()).trim();
			String destinationParam = IUtil.getTrimParam(request.getTo()).trim();
			String messageParam = IUtil.getTrimParam(request.getMessage()).trim();
		
			Timestamp currentDateTime = new Timestamp(System.currentTimeMillis());
			System.out.println(
					"sms receive api called ! " + "source : " + sourceParam + " destination : " + destinationParam+"  message  : "+messageParam+"  currentDateTime  : "+currentDateTime+"messageId : "+messageId);

			if (!sourceParam.equalsIgnoreCase("") && !destinationParam.equalsIgnoreCase("")
					&& !messageParam.equalsIgnoreCase("")) {

//				String lastTenDigits = "";
//				if (sourceParam.length() >= 10) {
//					lastTenDigits = sourceParam.substring(sourceParam.length() - 10);
//				} else {
//					lastTenDigits = sourceParam;
//				}
				responseTwo = smsService.receiveSms(messageId,sourceParam, destinationParam, messageParam,date);
			} else {
				response.setStatus(400);
				return ResponseEntity.ok(response);
			}

			if (responseTwo != null) {
				if (responseTwo.getStatus().equals("1")) {
					response.setStatus(200);
				} else if (responseTwo.getStatus().equals("2")) {
					response.setStatus(400);
				} else if (responseTwo.getStatus().equals("4")) {
					response.setStatus(400);
				} else {
					response.setStatus(400);
				}
			} else {
				response.setStatus(400);
			}
		} catch (Exception e) {
			e.printStackTrace();
			response.setStatus(500);
			System.out.println("Sms Received Failed With Error!");
		}
		return ResponseEntity.ok(response);
	}

	@PostMapping("/send")
	public ResponseEntity<BasicResponsePayload> sendSms(@RequestBody SendSmsRequest requestData) {

		BasicResponsePayload response = new BasicResponsePayload();
		BasicResponsePayload responseTwo = new BasicResponsePayload(); 

		try {
			String sourceParam = IUtil.getTrimParam(requestData.getUserId()).trim();
			String destinationParam = IUtil.getTrimParam(requestData.getDestination()).trim();
			String messageParam = IUtil.getTrimParam(requestData.getMessage()).trim();

			if (!sourceParam.equalsIgnoreCase("") && !destinationParam.equalsIgnoreCase("")
					&& !messageParam.equalsIgnoreCase("")) {
				responseTwo = smsService.sendSms(sourceParam, destinationParam, messageParam);
			} else {
				response.setStatus("0");
				response.setMessage("Please Provide All Details Correctly!");
				return ResponseEntity.ok(response);
			}

			if (responseTwo != null) {
				if (responseTwo.getStatus().equals("1")) {


					return ResponseEntity.ok(responseTwo);
				
				} else if (responseTwo.getStatus().equals("2")) 
					response.setStatus("0");
					response.setMessage("User Not Found!");
					response.setData(new EmptyJsonResponse());
				} else {
					response.setStatus("0");
					response.setMessage(responseTwo.getMessage());
					response.setData(new EmptyJsonResponse());
				}
//			 else {
//				response.setStatus("0");
//				response.setMessage("Sms Sent Failed With Error!");
//				response.setData(new EmptyJsonResponse());
//			}
		} catch (Exception e) {
			e.printStackTrace();
			e.getMessage();  
			response.setStatus("0");
			response.setMessage("Sms Sent Failed With Error!");
			response.setData(new EmptyJsonResponse());
		}
   		return ResponseEntity.ok(response);
	}

	@PostMapping(value = "/smsDeleteById")
	public ResponseEntity<Object> smsDeleteById(@RequestBody SmsDeleteById request) {

		try {

			if (request.getId() == null || request.getId()==0) {

				return ResponseHandler.generateResponse(CONSTANTMESSAGE.USER_ID_REQUIRED, HttpStatus.BAD_REQUEST,
						new EmptyJsonResponse());

			}
		if (request.getUserId() == null || request.getUserId() == 0) {

				return ResponseHandler.generateResponse(CONSTANTMESSAGE.ID_REQUIRED, HttpStatus.BAD_REQUEST,
						new EmptyJsonResponse());

			}
			Sms result = smsService.smsDeleteById(request);

			if (result != null)
				return ResponseHandler.generateResponse("Sms Deleted", HttpStatus.OK, new EmptyJsonResponse());

			else
				return ResponseHandler.generateResponse("Sms Not Found!", HttpStatus.BAD_REQUEST,
						new EmptyJsonResponse());

		} catch (Exception e) {
			e.printStackTrace();
			return ResponseHandler.generateResponse("Sms Not Deleted!", HttpStatus.INTERNAL_SERVER_ERROR,
					new EmptyJsonResponse());
		}
	}

	@PostMapping(value = "/smsDeleteByUserIdAndDid")
	public ResponseEntity<Object> smsDeleteByUserId(@RequestBody DeletedSmsFromDid request) {

		try {

			if (request.getUser_id() == null || request.getUser_id()==0) {

				return ResponseHandler.generateResponse(CONSTANTMESSAGE.USER_ID_REQUIRED, HttpStatus.BAD_REQUEST,
						new EmptyJsonResponse());

			}
			if (request.getDid()== null || request.getDid().isEmpty()) {

				return ResponseHandler.generateResponse(CONSTANTMESSAGE.DID_NUMBER_REQUIRED, HttpStatus.BAD_REQUEST,
						new EmptyJsonResponse());

			}
		
			List<Sms> result = smsService.smsDeleteByUserId(request);

			if (result != null)
				return ResponseHandler.generateResponse("Sms Deleted", HttpStatus.OK, new EmptyJsonResponse());

			else
				return ResponseHandler.generateResponse("Sms Not Found!", HttpStatus.BAD_REQUEST,
						new EmptyJsonResponse());

		} catch (Exception e) {
			e.printStackTrace();
			return ResponseHandler.generateResponse("Sms Not Deleted!", HttpStatus.INTERNAL_SERVER_ERROR,
					new EmptyJsonResponse());
		}
	}
	
	@PostMapping(value = "/getSmsById")
	public ResponseEntity<Object> getSmsById(@RequestParam Long msgId) {
Long id=msgId;
		try {

			if (id == null || id==0) {

				return ResponseHandler.generateResponse(CONSTANTMESSAGE.ID_REQUIRED, HttpStatus.BAD_REQUEST,
						new EmptyJsonResponse());

			}
			
			List<Sms> result = smsService.getSmsById(id);

			if (result != null)
				return ResponseHandler.generateResponse(CONSTANTMESSAGE.SUCCESSFULLY, HttpStatus.OK, result);

			else
				return ResponseHandler.generateResponse(CONSTANTMESSAGE.ID_NOT_FOUND, HttpStatus.BAD_REQUEST,
						new EmptyJsonResponse());

		} catch (Exception e) {
			e.printStackTrace();
			return ResponseHandler.generateResponse(CONSTANTMESSAGE.EXCEPTION_CASE, HttpStatus.INTERNAL_SERVER_ERROR,
					new EmptyJsonResponse());
		}
	}

} 