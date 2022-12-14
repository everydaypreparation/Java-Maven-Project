package com.cooee.payload;

import org.springframework.web.multipart.MultipartFile;

public class FileUploadRequest {

	private Long id;

	private String firstName;
	private String lastName;
	private String dob;
	private String currentServiceProvider;
	private String optusAccountNumber;

	private String country;
	private String address;
	private String city;
	private String state;
	private String postalCode;

	private String idCardType;
	private String agreedToTermsAndConfition;

	private MultipartFile idCardFront;
	private MultipartFile idCardBack;
	private MultipartFile idCardSelfie;
	
	private String didNumber;
	private Boolean isPorting;
	private String mobileNumber;

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

	public String getDob() {
		return dob;
	}

	public void setDob(String dob) {
		this.dob = dob;
	}

	public String getCurrentServiceProvider() {
		return currentServiceProvider;
	}

	public void setCurrentServiceProvider(String currentServiceProvider) {
		this.currentServiceProvider = currentServiceProvider;
	}

	public String getOptusAccountNumber() {
		return optusAccountNumber;
	}

	public void setOptusAccountNumber(String optusAccountNumber) {
		this.optusAccountNumber = optusAccountNumber;
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

	public String getPostalCode() {
		return postalCode;
	}

	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}

	public String getIdCardType() {
		return idCardType;
	}

	public void setIdCardType(String idCardType) {
		this.idCardType = idCardType;
	}

	public String getAgreedToTermsAndConfition() {
		return agreedToTermsAndConfition;
	}

	public void setAgreedToTermsAndConfition(String agreedToTermsAndConfition) {
		this.agreedToTermsAndConfition = agreedToTermsAndConfition;
	}

	public MultipartFile getIdCardFront() {
		return idCardFront;
	}

	public void setIdCardFront(MultipartFile idCardFront) {
		this.idCardFront = idCardFront;
	}

	public MultipartFile getIdCardBack() {
		return idCardBack;
	}

	public void setIdCardBack(MultipartFile idCardBack) {
		this.idCardBack = idCardBack;
	}

	public MultipartFile getIdCardSelfie() {
		return idCardSelfie;
	}

	public void setIdCardSelfie(MultipartFile idCardSelfie) {
		this.idCardSelfie = idCardSelfie;
	}

	public String getDidNumber() {
		return didNumber;
	}

	public void setDidNumber(String didNumber) {
		this.didNumber = didNumber;
	}

	public Boolean getIsPorting() {
		return isPorting;
	}

	public void setIsPorting(Boolean isPorting) {
		this.isPorting = isPorting;
	}

	public String getMobileNumber() {
		return mobileNumber;
	}

	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}
	

}