package com.cooee.model;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "user_subscriptionlan")
public class UserSubscriptionPlan {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;

	@Column(name = "planValidity", columnDefinition = "varchar(245) default ''")
	private String planValidity;

	@Column(name = "incomingSms", columnDefinition = "varchar(245) default ''")
	private String incomingSms;

	@Column(name = "incomingCall", columnDefinition = "varchar(245) default ''")
	private String incomingCall;

	@Column(name = "outgoingSms", columnDefinition = "varchar(245) default ''")
	private String outgoingSms;

	@Column(name = "outgoingCall", columnDefinition = "varchar(245) default ''")
	private String outgoingCall;

	@Column(name = "country", columnDefinition = "varchar(245) default ''")
	private String country;

	@Column(name = "amount", columnDefinition = "integer")
	private Integer amount;

	@Column(name = "discount", columnDefinition = "integer")
	private Integer discount;

	@Column(name = "totalAmount", columnDefinition = "integer")
	private Integer totalAmount;

	@Column(name = "plan_id", unique = true)
	private String plan_Id;

	@Column(name = "title", columnDefinition = "varchar(245) default ''")
	private String title;

	@Column(name = "status", columnDefinition = "boolean default true")
	private Boolean status;

	@Column(name = "subscriptionPlanActiveDate", columnDefinition = "varchar(245) default ''")
	private String subscriptionPlanActiveDate;

	@Column(name = "subscriptionPlanRenewalDate", columnDefinition = "varchar(245) default ''")
	private String subscriptionPlanRenewalDate;

	@Column(name = "planStatus", columnDefinition = "boolean default true")
	private Boolean planStatus;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "subscriptionPlanId")
	private SubscriptionPlan subscriptionPlan;

	@Column(name = "payment_method_type")
	private String paymentMethod;

	@Column(name = "intent_id")
	private String intentId;

	@Column(name = "subscription_stripe_id")
	private String subscriptionStripeId;

	@Column(name = "creation_date", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
	private Timestamp creationDate;

	@Column(name = "updation_date", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
	private Timestamp updationDate;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private User user;

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

	public String getIntentId() {
		return intentId;
	}

	public String getSubscriptionStripeId() {
		return subscriptionStripeId;
	}

	public void setSubscriptionStripeId(String subscriptionStripeId) {
		this.subscriptionStripeId = subscriptionStripeId;
	}

	public void setIntentId(String intentId) {
		this.intentId = intentId;
	}

	public String getPaymentMethod() {
		return paymentMethod;
	}

	public void setPaymentMethod(String paymentMethod) {
		this.paymentMethod = paymentMethod;
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

	public Integer getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(Integer totalAmount) {
		this.totalAmount = totalAmount;
	}

	public String getPlan_Id() {
		return plan_Id;
	}

	public void setPlan_Id(String plan_Id) {
		this.plan_Id = plan_Id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Boolean getStatus() {
		return status;
	}

	public void setStatus(Boolean status) {
		this.status = status;
	}

	public SubscriptionPlan getSubscriptionPlanId() {
		return subscriptionPlan;
	}

	public void setSubscriptionPlan(SubscriptionPlan subscriptionPlan) {
		this.subscriptionPlan = subscriptionPlan;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
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

	public String getSubscriptionPlanActiveDate() {
		return subscriptionPlanActiveDate;
	}

	public void setSubscriptionPlanActiveDate(String subscriptionPlanActiveDate) {
		this.subscriptionPlanActiveDate = subscriptionPlanActiveDate;
	}

	public String getSubscriptionPlanRenewalDate() {
		return subscriptionPlanRenewalDate;
	}

	public void setSubscriptionPlanRenewalDate(String subscriptionPlanRenewalDate) {
		this.subscriptionPlanRenewalDate = subscriptionPlanRenewalDate;
	}

	public Boolean getPlanStatus() {
		return planStatus;
	}

	public void setPlanStatus(Boolean planStatus) {
		this.planStatus = planStatus;
	}

//	public List<SubscriptionPlan> getSubscriptionPlan() {
//		return subscriptionPlan;
//	}
//
//	public void setSubscriptionPlan(List<SubscriptionPlan> subscriptionPlan) {
//		this.subscriptionPlan = subscriptionPlan;
//	}
//
//	
//
//	public List<User> getUser() {
//		return user;
//	}
//
//	public void setUser(List<User> user) {
//		this.user = user;
//	}

//	public SubscriptionPlan getSubscriptionPlan() {
//		return subscriptionPlan;
//	}
	

	

}
