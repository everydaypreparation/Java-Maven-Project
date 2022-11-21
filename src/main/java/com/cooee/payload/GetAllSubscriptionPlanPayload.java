package com.cooee.payload;

import java.sql.Timestamp;
import java.util.List;

import javax.persistence.Column;

import com.cooee.model.SubscriptionPlan;
import com.cooee.model.SubscriptionPlanDescription;

public class GetAllSubscriptionPlanPayload {
	private Long id;
	private String planValidity;
	private String incomingSms;
	private String incomingCall;
	private String outgoingSms;
	private String outgoingCall;
	private String country;
	private Integer amount;
	private Integer discount;
	private Boolean status;
	private Integer totalAmount;
	private Timestamp creationDate;
    private Timestamp updationDate;
	private String plan_Id;
	
	private List<SubscriptionPlanDescription> subscriptionPlanDescription;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

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

	public Boolean getStatus() {
		return status;
	}

	public void setStatus(Boolean status) {
		this.status = status;
	}

	public Integer getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(Integer totalAmount) {
		this.totalAmount = totalAmount;
	}

	public Timestamp getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Timestamp creationDate) {
		this.creationDate = creationDate;
	}

	public Timestamp getUpdationDate() {
		return updationDate;
	}

	public void setUpdationDate(Timestamp updationDate) {
		this.updationDate = updationDate;
	}

	public String getPlan_Id() {
		return plan_Id;
	}

	public void setPlan_Id(String plan_Id) {
		this.plan_Id = plan_Id;
	}

	public List<SubscriptionPlanDescription> getSubscriptionPlanDescription() {
		return subscriptionPlanDescription;
	}

	public void setSubscriptionPlanDescription(List<SubscriptionPlanDescription> subscriptionPlanDescription) {
		this.subscriptionPlanDescription = subscriptionPlanDescription;
	}


	
	}
	

