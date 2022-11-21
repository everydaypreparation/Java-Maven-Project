package com.cooee.payload;

public class OtpVerificationForEmailRequest {

	private String email;
	
	private Integer otp;


	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public Integer getOtp() {
		return otp;
	}
	public void setOtp(Integer otp) {
		this.otp = otp;
	}




}
