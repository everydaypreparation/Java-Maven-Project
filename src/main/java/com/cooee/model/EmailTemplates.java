package com.cooee.model;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;


	@Entity
	@Table(name = "email_templates")
	public class EmailTemplates {

		@Id
		@GeneratedValue(strategy = GenerationType.IDENTITY)
		@Column(name = "id")
		private Long id;

		@Column(name = "email_body", columnDefinition = "varchar(245) default ''")
		private String emailBody;

		@Column(name = "emailType",unique = true, columnDefinition = "varchar(245) default ''")
		private String emailType;
		
//		@Column(name = "emailTitle", columnDefinition = "varchar(245) default ''")
//		private String emailTitle;
		
		@Column(name = "creation_date", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
		private Timestamp creationDate;

		@Column(name = "updation_date", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
		private Timestamp updationDate;

		public Long getId() {
			return id;
		}

		public void setId(Long id) {
			this.id = id;
		}

		public String getEmailBody() {
			return emailBody;
		}

		public void setEmailBody(String emailBody) {
			this.emailBody = emailBody;
		}

		public String getEmailType() {
			return emailType;
		}

		public void setEmailType(String emailType) {
			this.emailType = emailType;
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

//		public String getEmailTitle() {
//			return emailTitle;
//		}
//
//		public void setEmailTitle(String emailTitle) {
//			this.emailTitle = emailTitle;
//		}

		
		
		
}
