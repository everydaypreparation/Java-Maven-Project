package com.cooee.model;

import java.sql.Timestamp;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "subscription_plan")

public class SubscriptionPlan {
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
	

		@Column(name = "status", columnDefinition = "boolean default true")
		private Boolean status;
		
		@Column(name = "creation_date", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
		private Timestamp creationDate;

		@Column(name = "updation_date", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
		private Timestamp updationDate;
		
		@Column(name = "plan_id", unique = true)
		private String plan_Id;
		
		@Column(name = "title", columnDefinition = "varchar(245) default ''")
		private String title;
		
		
		@OneToMany(mappedBy = "subscriptionPlan")
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
		
		public Boolean getStatus() {
			return status;
		}

		public void setStatus(Boolean status) {
			this.status = status;
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

		public String getTitle() {
			return title;
		}

		public void setTitle(String title) {
			this.title = title;
		}

	


		
}