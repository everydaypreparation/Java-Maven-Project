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

@Entity
@Table(name = "login_device")
public class LoginDevice {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;

	@Column(name = "device_id", columnDefinition = "varchar(100) default ''")
	private String deviceID;

	@Column(name = "device_type", columnDefinition = "varchar(100) default ''")
	private String deviceType;

	@Column(name = "device_token", columnDefinition = "TEXT")
	private String deviceToken;

	@Column(name = "device_ip", columnDefinition = "varchar(100) default ''")
	private String deviceIp;
	
	@Column(name = "last_login", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
	private Timestamp lastLogin;
	
	@Column(name = "call_Token", columnDefinition = "varchar(100) default ''")
	private String callToken;

	@Column(name = "device", columnDefinition = "varchar(100) default ''")
	private String device;

	@Column(name = "is_device_active", columnDefinition = "boolean default true")
	private Boolean isDeviceActive;
	

	@ManyToOne
	@JoinColumn(name = "user_id", nullable = false)
	private User user;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDeviceID() {
		return deviceID;
	}

	public void setDeviceID(String deviceID) {
		this.deviceID = deviceID;
	}

	public String getDeviceType() {
		return deviceType;
	}

	public void setDeviceType(String deviceType) {
		this.deviceType = deviceType;
	}

	public String getDeviceToken() {
		return deviceToken;
	}

	public void setDeviceToken(String deviceToken) {
		this.deviceToken = deviceToken;
	}

	public String getDeviceIp() {
		return deviceIp;
	}

	public void setDeviceIp(String deviceIp) {
		this.deviceIp = deviceIp;
	}

	public Timestamp getLastLogin() {
		return lastLogin;
	}

	public void setLastLogin(Timestamp lastLogin) {
		this.lastLogin = lastLogin;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}



	public String getCallToken() {
		return callToken;
	}

	public void setCallToken(String callToken) {
		this.callToken = callToken;
	}

	public String getDevice() {
		return device;
	}

	public void setDevice(String device) {
		this.device = device;
	}

	public Boolean getIsDeviceActive() {
		return isDeviceActive;
	}

	public void setIsDeviceActive(Boolean isDeviceActive) {
		this.isDeviceActive = isDeviceActive;
	}

	

}
