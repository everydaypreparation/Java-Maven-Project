package com.cooee.payload;

import java.sql.Timestamp;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

public class UserResponse {

	private Long id;

	private String username;

	private String mobileNumber;

	private String emailId;

	private String displayName;

	private String loginUsername;

	private boolean otpEnable;

	private int otpType;

	private String firstName;

	private String lastName;

	private int passwordStatus;

//	@JsonFormat(pattern = "MM-dd-yyyy HH:mm:ss")
	@JsonFormat(shape = JsonFormat.Shape.NUMBER)
	private Timestamp creationDate;

//	@JsonFormat(pattern = "MM-dd-yyyy HH:mm:ss")
	@JsonFormat(shape = JsonFormat.Shape.NUMBER)
	private Timestamp updationDate;

	private boolean active;

	private boolean dndStatus;

	private boolean verificationStatus;

	private boolean callForwardingStatus;

	private boolean callRecordingStatus;

	private String virtualNumber;

	private String outboundCid;

	private String facebookId;

	private String googleId;

	private String profilePhoto;

	private String customValue;

	private String sendEnable;

	private Long orgId;

	private List<Long> macInventory;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getMobileNumber() {
		return mobileNumber;
	}

	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public String getLoginUsername() {
		return loginUsername;
	}

	public void setLoginUsername(String loginUsername) {
		this.loginUsername = loginUsername;
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

	public int getPasswordStatus() {
		return passwordStatus;
	}

	public void setPasswordStatus(int passwordStatus) {
		this.passwordStatus = passwordStatus;
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

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public boolean isDndStatus() {
		return dndStatus;
	}

	public void setDndStatus(boolean dndStatus) {
		this.dndStatus = dndStatus;
	}

	public boolean isVerificationStatus() {
		return verificationStatus;
	}

	public void setVerificationStatus(boolean verificationStatus) {
		this.verificationStatus = verificationStatus;
	}

	public boolean isCallForwardingStatus() {
		return callForwardingStatus;
	}

	public void setCallForwardingStatus(boolean callForwardingStatus) {
		this.callForwardingStatus = callForwardingStatus;
	}

	public boolean isCallRecordingStatus() {
		return callRecordingStatus;
	}

	public void setCallRecordingStatus(boolean callRecordingStatus) {
		this.callRecordingStatus = callRecordingStatus;
	}

	public String getVirtualNumber() {
		return virtualNumber;
	}

	public void setVirtualNumber(String virtualNumber) {
		this.virtualNumber = virtualNumber;
	}

	public String getOutboundCid() {
		return outboundCid;
	}

	public void setOutboundCid(String outboundCid) {
		this.outboundCid = outboundCid;
	}

	public String getFacebookId() {
		return facebookId;
	}

	public void setFacebookId(String facebookId) {
		this.facebookId = facebookId;
	}

	public String getGoogleId() {
		return googleId;
	}

	public void setGoogleId(String googleId) {
		this.googleId = googleId;
	}

	public String getCustomValue() {
		return customValue;
	}

	public void setCustomValue(String customValue) {
		this.customValue = customValue;
	}

	public String getSendEnable() {
		return sendEnable;
	}

	public void setSendEnable(String sendEnable) {
		this.sendEnable = sendEnable;
	}

	public List<Long> getMacInventory() {
		return macInventory;
	}

	public void setMacInventory(List<Long> macInventory) {
		this.macInventory = macInventory;
	}

	public boolean isOtpEnable() {
		return otpEnable;
	}

	public void setOtpEnable(boolean otpEnable) {
		this.otpEnable = otpEnable;
	}

	public int getOtpType() {
		return otpType;
	}

	public void setOtpType(int otpType) {
		this.otpType = otpType;
	}

	public String getProfilePhoto() {
		return profilePhoto;
	}

	public void setProfilePhoto(String profilePhoto) {
		this.profilePhoto = profilePhoto;
	}

	public Long getOrgId() {
		return orgId;
	}

	public void setOrgId(Long orgId) {
		this.orgId = orgId;
	}

}