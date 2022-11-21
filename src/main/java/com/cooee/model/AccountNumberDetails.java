package com.cooee.model;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "account_number_details")
public class AccountNumberDetails {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;

	@Column(name = "sip_username", columnDefinition = "varchar(100) default ''")
	private String sipUsername;

	@Column(name = "sip_password", columnDefinition = "varchar(150) default ''")
	private String sipPassword;

	@Column(name = "sip_server", columnDefinition = "varchar(100) default ''")
	private String sipServer;

	@Column(name = "did_number", columnDefinition = "varchar(100) default ''")
	private String didNumber;

	@Column(name = "creation_date", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
	private Timestamp creationDate;

	@Column(name = "is_active_device", columnDefinition = "boolean default true")
	private Boolean isActiveDevice;

	@JsonIgnore
	@OneToOne
	@JoinColumn(name = "user_id", nullable = false)
	private User user;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getSipUsername() {
		return sipUsername;
	}

	public void setSipUsername(String sipUsername) {
		this.sipUsername = sipUsername;
	}

	public String getSipPassword() {
		return sipPassword;
	}

	public void setSipPassword(String sipPassword) {
		this.sipPassword = sipPassword;
	}

	public String getSipServer() {
		return sipServer;
	}

	public void setSipServer(String sipServer) {
		this.sipServer = sipServer;
	}

	public String getDidNumber() {
		return didNumber;
	}

	public void setDidNumber(String didNumber) {
		this.didNumber = didNumber;
	}

	public Timestamp getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Timestamp creationDate) {
		this.creationDate = creationDate;
	}

	public Boolean getIsActiveDevice() {
		return isActiveDevice;
	}

	public void setIsActiveDevice(Boolean isActiveDevice) {
		this.isActiveDevice = isActiveDevice;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

}
