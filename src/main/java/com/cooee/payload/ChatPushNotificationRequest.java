package com.cooee.payload;

public class ChatPushNotificationRequest {

	private String title;
	private String message;
	private String topic;
	private String token;
	private String type;

	private String messageId;
	private String ownerBareJid;
	private String senderUsername;
	private String content;// msg
	private String contentType;// msg type
	private String contentTypeText;// msg type text
	private String messageTimestamp;
//    private String thumbOfMedia;
	private byte[] thumbOfMedia;

	public ChatPushNotificationRequest() {
	}

	public ChatPushNotificationRequest(String title, String messageBody, String topicName) {
		this.title = title;
		this.message = messageBody;
		this.topic = topicName;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getTopic() {
		return topic;
	}

	public void setTopic(String topic) {
		this.topic = topic;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getMessageId() {
		return messageId;
	}

	public void setMessageId(String messageId) {
		this.messageId = messageId;
	}

	public String getOwnerBareJid() {
		return ownerBareJid;
	}

	public void setOwnerBareJid(String ownerBareJid) {
		this.ownerBareJid = ownerBareJid;
	}

	public String getSenderUsername() {
		return senderUsername;
	}

	public void setSenderUsername(String senderUsername) {
		this.senderUsername = senderUsername;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public String getMessageTimestamp() {
		return messageTimestamp;
	}

	public void setMessageTimestamp(String messageTimestamp) {
		this.messageTimestamp = messageTimestamp;
	}

	public byte[] getThumbOfMedia() {
		return thumbOfMedia;
	}

	public void setThumbOfMedia(byte[] thumbOfMedia) {
		this.thumbOfMedia = thumbOfMedia;
	}

	public String getContentTypeText() {
		return contentTypeText;
	}

	public void setContentTypeText(String contentTypeText) {
		this.contentTypeText = contentTypeText;
	}

}
