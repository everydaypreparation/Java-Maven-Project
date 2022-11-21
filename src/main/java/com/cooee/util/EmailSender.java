package com.cooee.util;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

@Component
public class EmailSender {

	private JavaMailSender javaMailSender;

//	@Autowired
//	public EmailSender(JavaMailSender javaMailSender) {
//		//this.javaMailSender = javaMailSender;
//	}
//	
	@Autowired
	public EmailSender() {
		//this.javaMailSender = javaMailSender;
	}
//	public void sendSimpleMessage(String to, String subject, String text) {
//
//		SimpleMailMessage message = new SimpleMailMessage();
//		message.setFrom("noreply@baeldung.com");
//		message.setTo(to);
//		message.setSubject(subject);
//		message.setText(text);
//
//		javaMailSender.send(message);
//
//		System.out.println("Email Sent Successfully to - " + to);
//
//	}
//
//	public void sendHTMLMessage(String to, String subject, String text) {
//
//		try { 
//			MimeMessage message = javaMailSender.createMimeMessage();
//			MimeMessageHelper helper = new MimeMessageHelper(message);
//
//			helper.setSubject(subject);
//			helper.setFrom("noreply@cooee.com");
//			helper.setTo(to);
//
//			boolean html = true;
//			helper.setText(text, html); 
//
//			javaMailSender.send(message);
//
//			System.out.println("Email Sent Successfully to - " + to);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
	
	public  boolean sendHTMLMessage(String email, String subject, String messageText) {
		try {
		Properties props = new Properties();
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
//		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.host", "mail.callcooee.com");

		props.put("mail.smtp.port", "465");
		props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
		props.put("mail.debug", "false");

		Session session = Session.getInstance(props, new javax.mail.Authenticator() {
		@Override
		protected PasswordAuthentication getPasswordAuthentication() {
		return new PasswordAuthentication(Constant.EMAIL_ADDRESS, Constant.PASSWORD);
		}
		});
		Message message = new MimeMessage(session);
		message.setFrom(new InternetAddress(Constant.EMAIL_ADDRESS));
		message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email));
		message.setSubject(subject);
		message.setContent(messageText, "text/html");
		Transport.send(message);
		} catch (Exception e) {
			e.printStackTrace();
		return false;
		}
		return true;
		}
	
}
//
//	public  boolean sendHTMLMessage(String email, String subject, String messageText) {
//		try {
//		Properties props = new Properties();
//		props.put("mail.smtp.auth", "true");
//		props.put("mail.smtp.starttls.enable", "true");
//   	props.put("mail.smtp.host", "smtp.gmail.com");
//		//props.put("mail.smtp.host", "mail.callcooee.com");
//
//		props.put("mail.smtp.port", "465");
//		props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
//		props.put("mail.debug", "false");
//
//		Session session = Session.getInstance(props, new javax.mail.Authenticator() {
//		@Override
//		protected PasswordAuthentication getPasswordAuthentication() {
//		return new PasswordAuthentication(Constant.EMAIL_ADDRESS, Constant.PASSWORD);
//		}
//		});
//		Message message = new MimeMessage(session);
//		message.setFrom(new InternetAddress(Constant.EMAIL_ADDRESS));
//		message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email));
//		message.setSubject(subject);
//		message.setContent(messageText, "text/html");
//		Transport.send(message);
//		} catch (Exception e) {
//			e.printStackTrace();
//		return false;
//		}
//		return true;
//		}
//	



