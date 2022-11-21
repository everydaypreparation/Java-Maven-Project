package com.cooee.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.CreationTimestamp;


/**
 * BaseEntity which should extended by all sub entities
 * 
 **/


@MappedSuperclass
public class BaseEntity implements Serializable {

	private static final long serialVersionUID = -7664704413532546100L;
	private Long id;
	private Date recordCreationDate;
	private Date recordUpdationDate;
	private boolean isDeleted = false;
	private String recordCreationFunction;
	private String recordCreationProfile;
	private String recordUpdateFunction;
	private String recordUpdateProfile;
	
	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id", precision = 19, scale = 0)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@CreationTimestamp
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "record_creation_date", precision = 38, scale = 0, updatable = false, nullable = true)
	public Date getRecordCreationDate() {
		return recordCreationDate;
	}

	public void setRecordCreationDate(Date recordCreationDate) {
		this.recordCreationDate = recordCreationDate;
	}

	@CreationTimestamp
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "record_updation_date", precision = 38, scale = 0, nullable = true)
	public Date getRecordUpdationDate() {
		return recordUpdationDate;
	}

	public void setRecordUpdationDate(Date recordUpdationDate) {
		this.recordUpdationDate = recordUpdationDate;
	}

	@Column(name = "is_deleted", nullable = false)
	public boolean isDeleted() {
		return isDeleted;
	}

	public void setDeleted(boolean isDeleted) {
		this.isDeleted = isDeleted;
	}
	
	public String getRecordCreationFunction() {
		return recordCreationFunction;
	}

	public void setRecordCreationFunction(String recordCreationFunction) {
		this.recordCreationFunction = recordCreationFunction;
	}

	public String getRecordCreationProfile() {
		return recordCreationProfile;
	}

	public void setRecordCreationProfile(String recordCreationProfile) {
		this.recordCreationProfile = recordCreationProfile;
	}

	public String getRecordUpdateFunction() {
		return recordUpdateFunction;
	}

	public void setRecordUpdateFunction(String recordUpdateFunction) {
		this.recordUpdateFunction = recordUpdateFunction;
	}

	public String getRecordUpdateProfile() {
		return recordUpdateProfile;
	}

	public void setRecordUpdateProfile(String recordUpdateProfile) {
		this.recordUpdateProfile = recordUpdateProfile;
	}
	
}
