package com.cooee.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cooee.config.ResponseHandler;
import com.cooee.model.EmailTemplates;
import com.cooee.model.User;
import com.cooee.payload.EmailAndPasswordRequest;
import com.cooee.payload.EmailDataPayload;
import com.cooee.payload.EmailTemplatesPayload;
import com.cooee.payload.EmptyJsonResponse;
import com.cooee.payload.GetAllResponse;
import com.cooee.repository.EmailTemplatesRepository;
import com.cooee.responsemessgae.CONSTANTMESSAGE;
import com.cooee.service.EmailTemplatesService;





	@CrossOrigin
	@RestController
	@RequestMapping("/EmailTemplates")
	public class EmailTemplatesController {

		@Autowired
		private EmailTemplatesService emailTemplatesService;
		
		@Autowired
		private EmailTemplatesRepository emailTemplatesRepository;
		
		@PostMapping(value = "/save")
		public ResponseEntity<Object> add(@RequestBody EmailTemplatesPayload request) {
			try {

				if (request.getEmailBody()== null || request.getEmailBody().isEmpty()) {

					return ResponseHandler.generateResponse(CONSTANTMESSAGE.EMAIL_BODY_REQUIRED, HttpStatus.BAD_REQUEST,
							new EmptyJsonResponse());

				}

				if (request.getEmailType()== null || request.getEmailType().isEmpty()) {

					return ResponseHandler.generateResponse(CONSTANTMESSAGE.EMAIL_TYPE_REQUIRED, HttpStatus.BAD_REQUEST,
							new EmptyJsonResponse());

				}
				
				
				if (emailTemplatesRepository.findByEmailType( request.getEmailType()) != null) {
					return ResponseHandler.generateResponse(CONSTANTMESSAGE.EMAIL_TYPE_ALREADY_EXIST, HttpStatus.BAD_REQUEST,
							new EmptyJsonResponse());
				}

				EmailTemplates result=emailTemplatesService.save(request);
				if (result != null)
					return ResponseHandler.generateResponse(CONSTANTMESSAGE.SUCCESSFULLY, HttpStatus.OK,
							result);
				else
					return ResponseHandler.generateResponse(CONSTANTMESSAGE.DATA_NOT_FOUND,
							HttpStatus.BAD_REQUEST, new EmptyJsonResponse());

			} catch (Exception e) {
				e.printStackTrace();
				return ResponseHandler.generateResponse(CONSTANTMESSAGE.EXCEPTION_CASE,
						HttpStatus.INTERNAL_SERVER_ERROR, new EmptyJsonResponse());
			}

		}
		
		@GetMapping("/getEmailByType")
		public ResponseEntity<Object> getEmailByType(@RequestParam String type ) {
			try {

				String response = emailTemplatesService.getEmailByType(type);

				if (response != null)
					return ResponseHandler.generateResponse(CONSTANTMESSAGE.SUCCESSFULLY, HttpStatus.OK, response);
				else
					return ResponseHandler.generateResponse(CONSTANTMESSAGE.FAILED, HttpStatus.BAD_REQUEST,
							new EmptyJsonResponse());
			} catch (Exception e) {
				e.printStackTrace();
				return ResponseHandler.generateResponse(CONSTANTMESSAGE.EXCEPTION_CASE, HttpStatus.INTERNAL_SERVER_ERROR,
						new EmptyJsonResponse());
			}
		}
		
		@PostMapping(value = "/update")
		public ResponseEntity<Object> update(@RequestBody EmailTemplatesPayload request) {

				try {

					if (request.getEmailBody()== null || request.getEmailBody().isEmpty()) {

						return ResponseHandler.generateResponse(CONSTANTMESSAGE.EMAIL_BODY_REQUIRED, HttpStatus.BAD_REQUEST,
								new EmptyJsonResponse());

					}

					if (request.getEmailType()== null || request.getEmailType().isEmpty()) {
						

						return ResponseHandler.generateResponse(CONSTANTMESSAGE.EMAIL_TYPE_REQUIRED, HttpStatus.BAD_REQUEST,
								new EmptyJsonResponse());

					}					
					EmailTemplates result=emailTemplatesService.update(request);
					if (result != null)
						return ResponseHandler.generateResponse(CONSTANTMESSAGE.SUCCESSFULLY, HttpStatus.OK,
								result);
					else
						return ResponseHandler.generateResponse(CONSTANTMESSAGE.DATA_NOT_FOUND,
								HttpStatus.BAD_REQUEST, new EmptyJsonResponse());

				} catch (Exception e) {
					e.printStackTrace();
					return ResponseHandler.generateResponse(CONSTANTMESSAGE.EXCEPTION_CASE,
							HttpStatus.INTERNAL_SERVER_ERROR, new EmptyJsonResponse());
				}

			}
				

			
		
			
}