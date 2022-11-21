package com.cooee.payload;

import java.sql.Timestamp;

public class OtpVerificationRequest {

	private String email;
	private Integer otp;

	private String deviceId;
	private String deviceType;
	private String deviceToken;
	private String deviceIp;
	private String callToken;
	private String device;
	private String isDeviceActive;
	


	public Integer getOtp() {
		return otp;
	}

	public void setOtp(Integer otp) {
		this.otp = otp;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public String getDeviceType() { 
		return deviceType;
	}

	public void setDeviceType(String deviceType) {
		this.deviceType = deviceType;
	}

	public String getDeviceToken() {
		return deviceToken;
	}

	public void setDeviceToken(String deviceToken) {
		this.deviceToken = deviceToken;
	}

	public String getDeviceIp() {
		return deviceIp;
	}

	public void setDeviceIp(String deviceIp) {
		this.deviceIp = deviceIp;
	}

	

	public String getCallToken() {
		return callToken;
	}

	public void setCallToken(String callToken) {
		this.callToken = callToken;
	}

	public String getDevice() {
		return device;
	}

	public void setDevice(String device) {
		this.device = device;
	}

	public String getIsDeviceActive() {
		return isDeviceActive;
	}

	public void setIsDeviceActive(String isDeviceActive) {
		this.isDeviceActive = isDeviceActive;
	}


	

}