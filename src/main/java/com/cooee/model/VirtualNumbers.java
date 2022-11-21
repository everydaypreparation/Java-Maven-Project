package com.cooee.model;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonFormat;

@Entity
@Table(name = "virtual_numbers")
public class VirtualNumbers {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID")
	private Long id;

	@Column(name = "VIRTUAL_NUMBER", unique = true, columnDefinition = "varchar(100) default '' NOT NULL")
	private String virtualNumber;

	@Column(name = "STATE", columnDefinition = "varchar(50) default '' NOT NULL")
	private String state;

	@Column(name = "RATECENTER", columnDefinition = "varchar(50) default '' NOT NULL")
	private String ratecenter;

//	@Column(name = "TYPE", columnDefinition = "varchar(100) default '' NOT NULL")
//	private String type;

	@Column(name = "ACTIVE", columnDefinition = "boolean default true")
	private Boolean active;

	@JsonFormat(pattern = "MM-dd-yyyy HH:mm:ss")
	@Column(name = "CREATION_DATE")
	private Timestamp creationDate;

	@Column(name = "NUMBER_TYPE", columnDefinition = "varchar(100) default '' NOT NULL")
	private String numberType;
	
	@Column(name = "CALL_DESTINATION_ID", columnDefinition = "varchar(255) default ''")
	private String callDestinationId;

     @Column(name = "SMS_DESTINATION_ID", columnDefinition = "varchar(255) default ''")
	private String smsDestinationId;

	@Column(name = "IS_AVAILABLE", columnDefinition = "boolean default true")
	private Boolean isAvailable;

	@Column(name = "COUNTRY", columnDefinition = "varchar(255) default ''")
	private String country;

	@ManyToOne
	@JoinColumn(name = "user_id")
	private User user;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getVirtualNumber() {
		return virtualNumber;
	}

	public void setVirtualNumber(String virtualNumber) {
		this.virtualNumber = virtualNumber;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getRatecenter() {
		return ratecenter;
	}

	public void setRatecenter(String ratecenter) {
		this.ratecenter = ratecenter;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public Timestamp getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Timestamp creationDate) {
		this.creationDate = creationDate;
	}

	/*
	 * public Organization getOrganization() { return organization; }
	 * 
	 * public void setOrganization(Organization organization) { this.organization =
	 * organization; }
	 */
	public String getNumberType() {
		return numberType;
	}

	public void setNumberType(String numberType) {
		this.numberType = numberType;
	}


	public String getCallDestinationId() {
		return callDestinationId;
	}

	public void setCallDestinationId(String callDestinationId) {
		this.callDestinationId = callDestinationId;
	}




	public String getSmsDestinationId() {
		return smsDestinationId;
	}

	public void setSmsDestinationId(String smsDestinationId) {
		this.smsDestinationId = smsDestinationId;
	}

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	public Boolean getIsAvailable() {
		return isAvailable;
	}

	public void setIsAvailable(Boolean isAvailable) {
		this.isAvailable = isAvailable;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

}