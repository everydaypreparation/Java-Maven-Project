package com.cooee.model;

import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "send_by_email")
public class SendByEmail {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
//	@JsonIgnore
	private Long id;

	@Column(name = "type", columnDefinition = "varchar(245) default ''")
	private String type;

	@Column(name = "title", columnDefinition = "varchar(245) default ''")
	private String title;
	
	@Column(name = "userId",  columnDefinition = "varchar(245) default ''")
	private String userId;

	@Column(name = "dsecription",  columnDefinition = "varchar(245) default ''")
	private String dsecription;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getDsecription() {
		return dsecription;
	}

	public void setDsecription(String dsecription) {
		this.dsecription = dsecription;
	}

	

}
