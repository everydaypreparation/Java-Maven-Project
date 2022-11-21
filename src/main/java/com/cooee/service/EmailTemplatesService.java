package com.cooee.service;

import com.cooee.model.EmailTemplates;
import com.cooee.payload.EmailTemplatesPayload;

public interface EmailTemplatesService  {

	 EmailTemplates save(EmailTemplatesPayload request) ;

	 String getEmailByType(String type);

	EmailTemplates update(EmailTemplatesPayload request);

	
		
	

}
