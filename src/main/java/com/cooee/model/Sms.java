package com.cooee.model;




import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import com.fasterxml.jackson.annotation.JsonFormat;

@Entity
@Table(name = "sms")

public class Sms {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID")
	private Long id;

	@NotFound(action = NotFoundAction.IGNORE)
	@ManyToOne(fetch = FetchType.EAGER, optional = false)
	@JoinColumn(name = "USER_ID", nullable = false)
	private User user;

	@Column(name = "NUMBER", columnDefinition = "varchar(20) default '' NOT NULL")
	private String number;

	@Column(name = "MESSAGE", columnDefinition = "varchar(255) default '' NOT NULL")
	private String message;

//	@JsonFormat(pattern = "MM-dd-yyyy HH:mm:ss")
	@JsonFormat(shape = JsonFormat.Shape.NUMBER)
	@Column(name = "SMS_DATE")
	private String smsDate;

	@Column(name = "TYPE", columnDefinition = "varchar(20) default '' NOT NULL")
	private String type;

	@Column(name = "IS_READ")
	private boolean read;

	@Column(name = "ACTIVE", columnDefinition = "boolean default true")
	private boolean active;

	@Column(name = "AMOUNT", columnDefinition = "DECIMAL(4, 2) default '0.00'")
	private double amount;
	
//	@Column(name = "TO", columnDefinition = "varchar(100) default ''")
//	private String to;
//	
//	@Column(name = "FROM", columnDefinition = "varchar(100) default ''")
//	private String from;
	
	@Column(name = "SOURCE", columnDefinition = "varchar(100) default ''")
	private String source;
	
	@Column(name = "DESTINATION", columnDefinition = "varchar(100) default ''")
	private String destination;

	private String messageId; 
	
	public String getMessageId() {
		return messageId;
	}

	public void setMessageId(String messageId) {
		this.messageId = messageId;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	
	public String getSmsDate() {
		return smsDate;
	}

	public void setSmsDate(String smsDate) {
		this.smsDate = smsDate;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public boolean isRead() {
		return read;
	}

	public void setRead(boolean read) {
		this.read = read;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}
	

//	public String getTo() {
//		return to;
//	}
//
//	public void setTo(String to) {
//		this.to = to;
//	}
//
//	public String getFrom() {
//		return from;
//	}
//
//	public void setFrom(String from) {
//		this.from = from;
//	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getDestination() {
		return destination;
	}

	public void setDestination(String destination) {
		this.destination = destination;
	}

	@Override
	public String toString() {
		return "Sms [id=" + id + ", user=" + user + ", number=" + number + ", message=" + message + ", smsDate="
				+ smsDate + ", type=" + type + ", read=" + read + ", active=" + active + ", amount=" + amount + ", source=" + source + ", destination=" + destination + "]";
	}

}
