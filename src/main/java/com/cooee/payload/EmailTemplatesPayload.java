package com.cooee.payload;

import javax.persistence.Column;

public class EmailTemplatesPayload {
	
	private String emailBody;

	private String emailType;
	

	public String getEmailBody() {
		return emailBody;
	}

	public void setEmailBody(String emailBody) {
		this.emailBody = emailBody;
	}

	public String getEmailType() {
		return emailType;
	}

	public void setEmailType(String emailType) {
		this.emailType = emailType;
	}

	
	
	
	
}
