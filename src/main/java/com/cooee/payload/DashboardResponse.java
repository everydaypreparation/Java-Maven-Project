package com.cooee.payload;

import java.util.List;

import com.cooee.model.VirtualNumbers;

public class DashboardResponse {

	private List<VirtualNumbers> virtualNumbers;
	private Integer smsCount;
	private String balance;
	private Integer type;

	public List<VirtualNumbers> getVirtualNumbers() {
		return virtualNumbers;
	}

	public void setVirtualNumbers(List<VirtualNumbers> virtualNumbers) {
		this.virtualNumbers = virtualNumbers;
	}

	public Integer getSmsCount() {
		return smsCount;
	}

	public void setSmsCount(Integer smsCount) {
		this.smsCount = smsCount;
	}

	public String getBalance() {
		return balance;
	}

	public void setBalance(String balance) {
		this.balance = balance;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

}
