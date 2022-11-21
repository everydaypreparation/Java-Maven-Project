package com.cooee.model;

import java.sql.Date;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "user")
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
//	@JsonIgnore
	private Long id;

	@Column(name = "firstName", columnDefinition = "varchar(100) default ''")
	private String firstName;

	@Column(name = "lastName", columnDefinition = "varchar(100) default ''")
	private String lastName;

	@Column(name = "otp", columnDefinition = "integer")
	private Integer otp;

	@Column(name = "email", unique = true, columnDefinition = "varchar(100) default ''")
	private String email;

	@JsonIgnore
	
	@Column(name = "password", nullable = false, columnDefinition = "varchar(100)")
	private String password;

	@Column(name = "dob")
	private Date dob;

	@Column(name = "currentServiceProvider", columnDefinition = "varchar(100) default ''")
	private String currentServiceProvider;

	@Column(name = "optusAccountNumber", columnDefinition = "varchar(100) default ''")
	private String optusAccountNumber;

	@Column(name = "country", columnDefinition = "varchar(100) default ''")
	private String country;

	@Column(name = "address", columnDefinition = "varchar(100) default ''")
	private String address;

	@Column(name = "city", columnDefinition = "varchar(100) default ''")
	private String city;

	@Column(name = "state", columnDefinition = "varchar(100) default ''")
	private String state;

	@Column(name = "mobileNumber", columnDefinition = "varchar(100) default ''")
	private String mobileNumber;

	@Column(name = "postalCode", columnDefinition = "varchar(100) default ''")
	private String postalCode;

	@Column(name = "creation_date", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
	private Timestamp creationDate;

	@Column(name = "updation_date", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
	private Timestamp updationDate;

	@Column(name = "active", columnDefinition = "boolean default true")
	private Boolean active;

	@Column(name = "is_verified", columnDefinition = "boolean default false")
	private Boolean isVerified;

	@Column(name = "isBlocked", columnDefinition = "boolean default false")
	private Boolean isBlocked;

	@Column(name = "isDocumentVerified", columnDefinition = "boolean default false")
	private Boolean isDocumentVerified;

	@Column(name = "idCardType", columnDefinition = "varchar(100) default ''")
	private String idCardType;

	@Column(name = "idCardFront", columnDefinition = "varchar(255) default ''")
	private String idCardFront;

	@Column(name = "idCardBack", columnDefinition = "varchar(255) default ''")
	private String idCardBack;

	@Column(name = "idCardSelfie", columnDefinition = "varchar(255) default ''")
	private String idCardSelfie;

	@Column(name = "agreedToTermsAndConfition", columnDefinition = "varchar(100) default")
	private String agreedToTermsAndConfition;

	@Column(name = "approved", columnDefinition = "integer")
	private Integer approved;

	@Column(name = "isPorting", columnDefinition = "boolean default false")
	private Boolean isPorting;

	@Column(name = "did", columnDefinition = "varchar(100) default ''")
	private String did;

	@Column(name = "balance", columnDefinition = "varchar(100) default ''")
	private String balance;

	@Column(name = "loginType", columnDefinition = "varchar(100) default ''")
	private String loginType;

	@Transient
	private AccountNumberDetails accountNumberDetails;

	@Column(name = "socialId", columnDefinition = "varchar(100) default ''")
	private String socialId;

	@Column(name = "otp_creation_time")
	private Timestamp otpCreationTime;
	
	@Column(name = "temp_email", columnDefinition = "varchar(100) default ''")
	private String tempEmail;
	
	@Column(name = "cooee_id", unique = true)
	private String cooeeId;
	
	@Column(name = "stipeCustomerId", columnDefinition = "varchar(100) default ''")
	private String stipeCustomerId;

	public Integer getApproved() {
		return approved;
	}

	public void setApproved(Integer approved) {
		this.approved = approved;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public Date getDob() {
		return dob;
	}

	public void setDob(Date dob) {
		this.dob = dob;
	}

	public String getCurrentServiceProvider() {
		return currentServiceProvider;
	}

	public void setCurrentServiceProvider(String currentServiceProvider) {
		this.currentServiceProvider = currentServiceProvider;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getMobileNumber() {
		return mobileNumber;
	}

	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

	public String getPostalCode() {
		return postalCode;
	}

	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
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

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	public Integer getOtp() {
		return otp;
	}

	public void setOtp(Integer otp) {
		this.otp = otp;
	}

	public Boolean getIsVerified() {
		return isVerified;
	}

	public void setIsVerified(Boolean isVerified) {
		this.isVerified = isVerified;
	}

	public String getOptusAccountNumber() {
		return optusAccountNumber;
	}

	public void setOptusAccountNumber(String optusAccountNumber) {
		this.optusAccountNumber = optusAccountNumber;
	}

	public Boolean getIsBlocked() {
		return isBlocked;
	}

	public void setIsBlocked(Boolean isBlocked) {
		this.isBlocked = isBlocked;
	}

	public Boolean getIsDocumentVerified() {
		return isDocumentVerified;
	}

	public void setIsDocumentVerified(Boolean isDocumentVerified) {
		this.isDocumentVerified = isDocumentVerified;
	}

	public String getIdCardFront() {
		return idCardFront;
	}

	public void setIdCardFront(String idCardFront) {
		this.idCardFront = idCardFront;
	}

	public String getIdCardBack() {
		return idCardBack;
	}

	public void setIdCardBack(String idCardBack) {
		this.idCardBack = idCardBack;
	}

	public String getIdCardSelfie() {
		return idCardSelfie;
	}

	public void setIdCardSelfie(String idCardSelfie) {
		this.idCardSelfie = idCardSelfie;
	}

	
	public String getAgreedToTermsAndConfition() {
		return agreedToTermsAndConfition;
	}

	public void setAgreedToTermsAndConfition(String agreedToTermsAndConfition) {
		this.agreedToTermsAndConfition = agreedToTermsAndConfition;
	}

	public String getIdCardType() {
		return idCardType;
	}

	public void setIdCardType(String idCardType) {
		this.idCardType = idCardType;
	}

	public Boolean getIsPorting() {
		return isPorting;
	}

	public void setIsPorting(Boolean isPorting) {
		this.isPorting = isPorting;
	}

	public String getDid() {
		return did;
	}

	public void setDid(String did) {
		this.did = did;
	}

	public AccountNumberDetails getAccountNumberDetails() {
		return accountNumberDetails;
	}

	public void setAccountNumberDetails(AccountNumberDetails accountNumberDetails) {
		this.accountNumberDetails = accountNumberDetails;
	}

	public String getBalance() {
		return balance;
	}

	public void setBalance(String balance) {
		this.balance = balance;
	}

	public String getLoginType() {
		return loginType;
	}

	public void setLoginType(String loginType) {
		this.loginType = loginType;
	}

	public String getSocialId() {
		return socialId;
	}

	public void setSocialId(String socialId) {
		this.socialId = socialId;
	}

	public Timestamp getOtpCreationTime() {
		return otpCreationTime;
	}

	public void setOtpCreationTime(Timestamp otpCreationTime) {
		this.otpCreationTime = otpCreationTime;
	}

	public String getTempEmail() {
		return tempEmail;
	}

	public void setTempEmail(String tempEmail) {
		this.tempEmail = tempEmail;
	}

	public String getCooeeId() {
		return cooeeId;
	}

	public void setCooeeId(String cooeeId) {
		this.cooeeId = cooeeId;
	}

	public String getStipeCustomerId() {
		return stipeCustomerId;
	}

	public void setStipeCustomerId(String stipeCustomerId) {
		this.stipeCustomerId = stipeCustomerId;
	}
	
	

	/*
	 * public String getSocialId() { return socialId; }
	 * 
	 * public void setSocialId(String socialId) { this.socialId = socialId; }
	 */

}