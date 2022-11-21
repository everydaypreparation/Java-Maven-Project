package com.cooee.serviceImp;

import java.sql.Timestamp;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.cooee.model.EmailTemplates;
import com.cooee.model.User;
import com.cooee.payload.EmailTemplatesPayload;
import com.cooee.payload.GetAllResponse;
import com.cooee.repository.EmailTemplatesRepository;
import com.cooee.service.EmailTemplatesService;

@Service
public class EmailTemplatesServiceImp  implements EmailTemplatesService{
	
	
	@Autowired
	private EmailTemplatesRepository emailTemplatesRepository;

	@Override
	public EmailTemplates save(EmailTemplatesPayload request) {
		try {//"Your new password :"
		Timestamp currentDateTime = new Timestamp(System.currentTimeMillis());
		
		EmailTemplates response=new EmailTemplates();
		response.setEmailBody(request.getEmailBody());
		response.setEmailType(request.getEmailType());
		response.setCreationDate(currentDateTime);
		response.setUpdationDate(currentDateTime);
		
		return emailTemplatesRepository.save(response);
		}catch(Exception e) {
			e.printStackTrace();
			return null;
		}
		
	}

	
	@Override
	public String getEmailByType(String type ) {
		
		
         try {
        	 EmailTemplates emailTemplates= emailTemplatesRepository.findByEmailType(type);
        	 if(emailTemplates!=null) {
        		
 				return	emailTemplates.getEmailBody();
 				
 				} 			
			} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
         return null;
	
	}


	@Override
	public EmailTemplates update(EmailTemplatesPayload request) {
		
        try {        	
        	
       	 EmailTemplates emailTemplates= emailTemplatesRepository.findByEmailType(request.getEmailType());
       	 if(emailTemplates!=null) {
       		Timestamp currentDateTime = new Timestamp(System.currentTimeMillis());
       		 
       		emailTemplates.setEmailBody(request.getEmailBody());
       		emailTemplates.setCreationDate(currentDateTime);
       		emailTemplates.setUpdationDate(currentDateTime);
    		
       		return emailTemplatesRepository.save(emailTemplates);
 		}}catch(Exception e) {
 			e.printStackTrace();
 			return null;
 		}
 		return null;
        }

	
	
	
	
	}

