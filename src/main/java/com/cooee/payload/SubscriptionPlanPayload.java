 package com.cooee.payload;

import java.util.List;


public class SubscriptionPlanPayload {
//	private Long id;
	private String planValidity;
	private String incomingSms;
	private String incomingCall;
	private String outgoingSms;
	private String outgoingCall;
	private String country;
	private Integer amount;
	private Integer discount;
	private Boolean status;
	private String title;
	
	private List<String> description;
	//private Long subscriptionPlanId;
//	private List<String> Description;
	
	public String getPlanValidity() {
		return planValidity;
	}
	public void setPlanValidity(String planValidity) {
		this.planValidity = planValidity;
	}
	public String getIncomingSms() {
		return incomingSms;
	}
	public void setIncomingSms(String incomingSms) {
		this.incomingSms = incomingSms;
	}
	public String getIncomingCall() {
		return incomingCall;
	}
	public void setIncomingCall(String incomingCall) {
		this.incomingCall = incomingCall;
	}
	public String getOutgoingSms() {
		return outgoingSms;
	}
	public void setOutgoingSms(String outgoingSms) {
		this.outgoingSms = outgoingSms;
	}
	public String getOutgoingCall() {
		return outgoingCall;
	}
	public void setOutgoingCall(String outgoingCall) {
		this.outgoingCall = outgoingCall;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}

	
	
	public Integer getAmount() {
		return amount;
	}
	public void setAmount(Integer amount) {
		this.amount = amount;
	}
	public Integer getDiscount() {
		return discount;
	}
	public void setDiscount(Integer discount) {
		this.discount = discount;
	}
	public List<String> getDescription() {
		return description;
	}
	public void setDescription(List<String> description) {
		this.description = description;
	}
	public Boolean getStatus() {
		return status;
	}
	public void setStatus(Boolean status) {
		this.status = status;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	
	
//	public Long getId() {
//		return id;
//	}
//	public void setId(Long id) {
//		this.id = id;
//	}
	
//	public Long getSubscriptionPlanId() {
//		return subscriptionPlanId;
//	}
//	public void setSubscriptionPlanId(Long subscriptionPlanId) {
//		this.subscriptionPlanId = subscriptionPlanId;
//	}
	
	
	
}
