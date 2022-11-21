package com.cooee.payload;

import com.cooee.model.User;
import com.fasterxml.jackson.annotation.JsonIgnore;


public class MultipleResponse {
	//@JsonIgnore
	private User user;
	private Integer type;
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	

}
