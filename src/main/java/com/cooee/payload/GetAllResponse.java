package com.cooee.payload;

import java.util.List;

import com.cooee.model.User;

public class GetAllResponse {

	// private User user;
	private List<User> user;
	private Long totalRecord;

	public List<User> getUser() {
		return user;
	}

	public void setUser(List<User> user) {
		this.user = user;
	}

	public Long getTotalRecord() {
		return totalRecord;
	}

	public void setTotalRecord(Long totalRecord) {
		this.totalRecord = totalRecord;
	}

}