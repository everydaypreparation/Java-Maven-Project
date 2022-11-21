package com.cooee.payload;

import java.util.List;

public class RejectKycRequest {
	
	private Long userId;
	private List<String> descriptions;

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public List<String> getDescriptions() {
		return descriptions;
	}

	public void setDescriptions(List<String> descriptions) {
		this.descriptions = descriptions;
	}

	
}
