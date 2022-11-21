package com.cooee.payload;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;

public class PushNotificationPayload {
	private String title;
	private String description;
	private List<Long> userId;
	public List<Long> getUserId() {
		return userId;
	}
	public void setUserId(List<Long> userId) {
		this.userId = userId;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	}
