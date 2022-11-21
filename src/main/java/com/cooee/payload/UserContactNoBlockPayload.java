package com.cooee.payload;


import java.util.List;


import com.cooee.model.User;


public class UserContactNoBlockPayload {
	


	private List<String> mobileNumber;
	private Long userId;
	
	public List<String> getMobileNumber() {
		return mobileNumber;
	}
	public void setMobileNumber(List<String> mobileNumber) {
		this.mobileNumber = mobileNumber;
	}
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	
	
	
}
