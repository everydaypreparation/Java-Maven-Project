package com.cooee.serviceImp;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.cooee.model.Sms;
import com.cooee.model.User;
import com.cooee.payload.ChatPushNotificationRequest;
import com.cooee.payload.PushNotificationRequest;
import com.cooee.payload.UserResponse;
import com.cooee.serviceImpl.firebase.FCMService;
import com.cooee.util.Constant;
import com.eatthepath.pushy.apns.ApnsClient;
import com.eatthepath.pushy.apns.ApnsClientBuilder;
import com.eatthepath.pushy.apns.PushNotificationResponse;
import com.eatthepath.pushy.apns.auth.ApnsSigningKey;
import com.eatthepath.pushy.apns.util.ApnsPayloadBuilder;
import com.eatthepath.pushy.apns.util.SimpleApnsPayloadBuilder;
import com.eatthepath.pushy.apns.util.SimpleApnsPushNotification;
import com.eatthepath.pushy.apns.util.concurrent.PushNotificationFuture;
import com.google.cloud.Date;

@Service
public class PushNotificationService {

	@Value("#{${app.notifications.defaults}}")
	private Map<String, String> defaults;

	private Logger logger = LoggerFactory.getLogger(PushNotificationService.class);

	private FCMService fcmService;

	public PushNotificationService(FCMService fcmService) {
		this.fcmService = fcmService;
	}

	public void sendPushNotification(PushNotificationRequest request) {
		try {
			fcmService.sendMessage(getSamplePayloadData(), request);
		} catch (InterruptedException | ExecutionException e) {
			logger.error(e.getMessage());
		}
	}

	public void sendPushNotificationWithoutData(PushNotificationRequest request) {
		try {
			fcmService.sendMessageWithoutData(request);
		} catch (InterruptedException | ExecutionException e) {
			logger.error(e.getMessage());
		}
	}

	public void sendPushNotificationToToken(PushNotificationRequest request) {
		try {
			fcmService.sendMessageToToken(request);
		} catch (InterruptedException | ExecutionException e) {
			logger.error(e.getMessage());
		}
	}

	public void sendPushNotificationToApp(PushNotificationRequest request, Sms savedSms, long unreadCount) {
		try {
			if (request.getType().equalsIgnoreCase("android"))
				fcmService.sendMessageToToken(getAndroidSmsPayloadData(savedSms, request, unreadCount), request);
			else
				fcmService.sendMessageToToken(getSmsPayloadData(savedSms, unreadCount), request);
		} catch (InterruptedException | ExecutionException e) {
			logger.error(e.getMessage());
		}
	}

	public void sendPushNotificationBackground(PushNotificationRequest request) {
		try {
			if (request.getType().equalsIgnoreCase("android"))
				fcmService.sendMessageToToken(getAndroidBackgroundPayloadData(request), request);
			else
				fcmService.sendMessageToToken(request);
		} catch (InterruptedException | ExecutionException e) {
			logger.error(e.getMessage());
		}
	}

	private Map<String, String> getSamplePayloadData() {
		Map<String, String> pushData = new HashMap<>(); 
		pushData.put("messageId", defaults.get("payloadMessageId"));
		pushData.put("text", defaults.get("payloadData") + " " + LocalDateTime.now());
		return pushData;
	}

	private Map<String, String> getSmsPayloadData(Sms savedSms, long unreadCount) {
		Map<String, String> pushData = new HashMap<>();
		pushData.put("id", savedSms.getId().toString());
		pushData.put("userId", savedSms.getUser().getId().toString());
		pushData.put("message", savedSms.getMessage().toString());
	//	pushData.put("smsDate", String.valueOf(savedSms.getSmsDate().getTime()));
		pushData.put("smsDate", savedSms.getSmsDate());

		pushData.put("number", savedSms.getNumber().toString());
		pushData.put("type", savedSms.getType().toString());
		pushData.put("totalUnReadCount", String.valueOf(unreadCount));
		return pushData;
	}

	private Map<String, String> getAndroidSmsPayloadData(Sms savedSms, PushNotificationRequest request,
			long unreadCount) {
		Map<String, String> pushData = new HashMap<>();
		pushData.put("id", savedSms.getId().toString());
		pushData.put("userId", savedSms.getUser().getId().toString());
		pushData.put("message", savedSms.getMessage().toString());
		// pushData.put("smsDate", savedSms.getSmsDate().toString());
//		pushData.put("smsDate", String.valueOf(savedSms.getSmsDate()));
	//	pushData.put("smsDate", String.valueOf(savedSms.getSmsDate().getTime()));
		pushData.put("smsDate", savedSms.getSmsDate());
		pushData.put("number", savedSms.getNumber().toString());
		pushData.put("type", savedSms.getType().toString());
		pushData.put("title", request.getTitle().toString());
		pushData.put("body", request.getMessage().toString());
		pushData.put("totalUnReadCount", String.valueOf(unreadCount));
		return pushData;
	}

	private Map<String, String> getAndroidBackgroundPayloadData(PushNotificationRequest request) {
		Map<String, String> pushData = new HashMap<>();
		pushData.put("title", request.getTopic().toString());
		pushData.put("body", request.getMessage().toString());
		return pushData;
	}

	public void sendPushNotificationToAppOnStatusDelete(PushNotificationRequest request, long userId, long statusId) {
		try {
			if (request.getType().equalsIgnoreCase("android"))
				fcmService.sendMessageToToken(getAndroidStatusOnDeletePayloadData(userId, statusId, request), request);
			else
				fcmService.sendMessageToToken(getStatusOnDeletePayloadData(userId, statusId), request);
		} catch (InterruptedException | ExecutionException e) {
			logger.error(e.getMessage());
		}
	}

	private Map<String, String> getStatusOnDeletePayloadData(Long userId, Long statusId) {
		Map<String, String> pushData = new HashMap<>();
		pushData.put("id", statusId.toString());
		pushData.put("userId", userId.toString());
		return pushData;
	}

	private Map<String, String> getAndroidStatusOnDeletePayloadData(Long userId, Long statusId,
			PushNotificationRequest request) {
		Map<String, String> pushData = new HashMap<>();
		pushData.put("id", statusId.toString());
		pushData.put("userId", userId.toString());
		pushData.put("title", request.getTitle().toString());
		pushData.put("body", request.getMessage().toString());
		return pushData;
	}

	public void sendPushNotificationToAppOnviewStatus(PushNotificationRequest request, Long statusId,
			String displayName, Long userId, String firstName, String lastName, String username, Timestamp viewTime) {
		try {
			if (request.getType().equalsIgnoreCase("android"))
				fcmService.sendMessageToToken(getAndroidStatusOnViewPayloadData(statusId, userId, displayName,
						firstName, lastName, username, viewTime, request), request);
			else
				fcmService.sendMessageToToken(getStatusOnViewPayloadData(statusId, userId, displayName, firstName,
						lastName, username, viewTime), request);
		} catch (InterruptedException | ExecutionException e) {
			logger.error(e.getMessage());
		}
	}

	private Map<String, String> getStatusOnViewPayloadData(Long statusId, Long userId, String displayName,
			String firstName, String lastName, String username, Timestamp viewTime) {
		Map<String, String> pushData = new HashMap<>();
		pushData.put("id", statusId.toString());
		pushData.put("userId", userId.toString());
		pushData.put("displayName", displayName.toString());
		pushData.put("firstName", firstName.toString());
		pushData.put("lastName", lastName.toString());
		pushData.put("username", username.toString());
		pushData.put("statusViewTime", String.valueOf(viewTime.getTime()));
		return pushData;
	}

	private Map<String, String> getAndroidStatusOnViewPayloadData(Long statusId, Long userId, String displayName,
			String firstName, String lastName, String username, Timestamp viewTime, PushNotificationRequest request) {
		Map<String, String> pushData = new HashMap<>();
		pushData.put("id", statusId.toString());
		pushData.put("userId", userId.toString());
		pushData.put("displayName", displayName.toString());
		pushData.put("firstName", firstName.toString());
		pushData.put("lastName", lastName.toString());
		pushData.put("username", username.toString());
		pushData.put("statusViewTime", String.valueOf(viewTime.getTime()));
		pushData.put("title", request.getTitle().toString());
		pushData.put("body", request.getMessage().toString());
		return pushData;
	}

	// for sticker

	public void sendPushNotificationToAppOnStickerDelete(PushNotificationRequest request, long categoryId,
			long stickerImageId, String code) {
		try {
			if (request.getType().equalsIgnoreCase("android"))
				fcmService.sendMessageToToken(
						getAndroidSickerDeletePayloadData(categoryId, stickerImageId, code, request), request);
			else
				fcmService.sendMessageToToken(getSickerDeletePayloadData(categoryId, stickerImageId, code), request);
		} catch (InterruptedException | ExecutionException e) {
			logger.error(e.getMessage());
		}
	}

	private Map<String, String> getSickerDeletePayloadData(Long categoryId, Long stickerImageId, String code) {
		Map<String, String> pushData = new HashMap<>();
		pushData.put("categoryId", categoryId.toString());
		pushData.put("stickerId", stickerImageId.toString());
		pushData.put("code", code.toString());
		return pushData;
	}

	private Map<String, String> getAndroidSickerDeletePayloadData(Long categoryId, Long stickerImageId, String code,
			PushNotificationRequest request) {
		Map<String, String> pushData = new HashMap<>();
		pushData.put("categoryId", categoryId.toString());
		pushData.put("stickerId", stickerImageId.toString());
		pushData.put("code", code.toString());
		pushData.put("title", request.getTitle().toString());
		pushData.put("body", request.getMessage().toString());
		return pushData;
	}

	public void sendPushNotificationToAppOnStickerSave(PushNotificationRequest request, Long categoryId,
			Long stickerImageId, String code, String imagePath) {
		try {
			if (request.getType().equalsIgnoreCase("android"))
				fcmService.sendMessageToToken(
						getAndroidSickerAddedPayloadData(categoryId, stickerImageId, code, imagePath, request),
						request);
			else
				fcmService.sendMessageToToken(getSickerAddedPayloadData(categoryId, stickerImageId, code, imagePath),
						request);
		} catch (InterruptedException | ExecutionException e) {
			logger.error(e.getMessage());
		}
	}

	private Map<String, String> getSickerAddedPayloadData(Long categoryId, Long stickerImageId, String code,
			String imagePath) {
		Map<String, String> pushData = new HashMap<>();
		pushData.put("categoryId", categoryId.toString());
		pushData.put("stickerId", stickerImageId.toString());
		pushData.put("code", code.toString());
		pushData.put("stickerImagePath", imagePath.toString());
		return pushData;
	}

	private Map<String, String> getAndroidSickerAddedPayloadData(Long categoryId, Long stickerImageId, String code,
			String imagePath, PushNotificationRequest request) {
		Map<String, String> pushData = new HashMap<>();
		pushData.put("categoryId", categoryId.toString());
		pushData.put("stickerId", stickerImageId.toString());
		pushData.put("code", code.toString());
		pushData.put("stickerImagePath", imagePath.toString());
		pushData.put("title", request.getTitle().toString());
		pushData.put("body", request.getMessage().toString());
		return pushData;
	}

	public void sendPushToAppOnAddUpdateUser(PushNotificationRequest request, UserResponse userObj) {
		try {
			if (request.getType().equalsIgnoreCase("android"))
				fcmService.sendMessageToToken(getAndroidAddUpdateUserPayloadData(userObj, request), request);
			else
				fcmService.sendMessageToToken(getAddUpdateUserPayloadData(userObj), request);
		} catch (InterruptedException | ExecutionException e) {
			logger.error(e.getMessage());
		}
	}

	private Map<String, String> getAddUpdateUserPayloadData(UserResponse userObj) {
		Map<String, String> pushData = new HashMap<>();
		pushData.put("userId", userObj.getId().toString());
		pushData.put("userName", userObj.getUsername().toString());
		pushData.put("displayName", userObj.getDisplayName().toString());
		pushData.put("firstName", userObj.getFirstName().toString());
		pushData.put("lastName", userObj.getLastName().toString());
		pushData.put("emailId", userObj.getEmailId().toString());
		pushData.put("mobileNo", userObj.getMobileNumber().toString());
		return pushData;
	}

	private Map<String, String> getAndroidAddUpdateUserPayloadData(UserResponse userObj,
			PushNotificationRequest request) {
		Map<String, String> pushData = new HashMap<>();
		pushData.put("userId", userObj.getId().toString());
		pushData.put("userName", userObj.getUsername().toString());
		pushData.put("displayName", userObj.getDisplayName().toString());
		pushData.put("firstName", userObj.getFirstName().toString());
		pushData.put("lastName", userObj.getLastName().toString());
		pushData.put("emailId", userObj.getEmailId().toString());
		pushData.put("mobileNo", userObj.getMobileNumber().toString());
		pushData.put("title", request.getTitle());
		pushData.put("body", request.getMessage());
		return pushData;
	}

	public void sendPushToAppOnDeleteUser(PushNotificationRequest request, Long userId) {
		try {
			if (request.getType().equalsIgnoreCase("android"))
				fcmService.sendMessageToToken(getAndroidDeleteUserPayloadData(userId, request), request);
			else
				fcmService.sendMessageToToken(getDeleteUserPayloadData(userId), request);
		} catch (InterruptedException | ExecutionException e) {
			logger.error(e.getMessage());
		}
	}

	private Map<String, String> getDeleteUserPayloadData(Long userId) {
		Map<String, String> pushData = new HashMap<>();
		pushData.put("userId", userId.toString());
		return pushData;
	}

	private Map<String, String> getAndroidDeleteUserPayloadData(Long userId, PushNotificationRequest request) {
		Map<String, String> pushData = new HashMap<>();
		pushData.put("userId", userId.toString());
		pushData.put("title", request.getTitle());
		pushData.put("body", request.getMessage());
		return pushData;
	}

	// Call Push New Code
	public void sendCallPushNotificationBackground(PushNotificationRequest request) {
		try {
			if (request.getType().equalsIgnoreCase("android"))
				fcmService.sendCallMessageToToken(getAndroidBackgroundPayloadData(request), request);
			else
				fcmService.sendMessageToToken(request);
		} catch (InterruptedException | ExecutionException e) {
			logger.error(e.getMessage());
		}
	}

	public void sendChatPushNotificationToApp(PushNotificationRequest request, ChatPushNotificationRequest chatReq,
			User user) {
		try {
			if (request.getType().equalsIgnoreCase("android"))
				fcmService.sendMessageToToken(getAndroidChatPushPayloadData(chatReq, request, user), request);
			else
				fcmService.sendMessageToToken(getChatPushPayloadData(chatReq, user), request);
		} catch (InterruptedException | ExecutionException e) {
			logger.error(e.getMessage());
		}
	}

	private Map<String, String> getChatPushPayloadData(ChatPushNotificationRequest chatReq, User user) {
		Map<String, String> pushData = new HashMap<>();
		pushData.put("content", chatReq.getContent().toString());
		pushData.put("contentType", chatReq.getContentType().toString());
		pushData.put("contentTypeText", chatReq.getContentTypeText().toString());
		pushData.put("messageId", chatReq.getMessageId().toString());
//		pushData.put("smsDate", String.valueOf(chatReq.getSmsDate().getTime()));
		pushData.put("messageTimestamp", chatReq.getMessageTimestamp().toString());
		pushData.put("ownerBareJid", chatReq.getOwnerBareJid().toString());
		pushData.put("senderUsername", chatReq.getSenderUsername().toString());
		pushData.put("thumbOfMedia", chatReq.getThumbOfMedia().toString());
		pushData.put("type", chatReq.getType().toString());
		pushData.put("title", chatReq.getTitle().toString());

		pushData.put("firstName", user.getFirstName().toString());
		pushData.put("lastName", user.getLastName().toString());
		pushData.put("email", user.getEmail().toString());

		return pushData;
	}

	private Map<String, String> getAndroidChatPushPayloadData(ChatPushNotificationRequest chatReq,
			PushNotificationRequest request, User user) {
		Map<String, String> pushData = new HashMap<>();
		pushData.put("content", chatReq.getContent().toString());
		pushData.put("contentType", chatReq.getContentType().toString());
		pushData.put("contentTypeText", chatReq.getContentTypeText().toString());
		pushData.put("messageId", chatReq.getMessageId().toString());
//		pushData.put("smsDate", String.valueOf(chatReq.getSmsDate().getTime()));
		pushData.put("messageTimestamp", chatReq.getMessageTimestamp().toString());
		pushData.put("ownerBareJid", chatReq.getOwnerBareJid().toString());
		pushData.put("senderUsername", chatReq.getSenderUsername().toString());
		pushData.put("thumbOfMedia", chatReq.getThumbOfMedia().toString());
		pushData.put("type", chatReq.getType().toString());
		pushData.put("title", chatReq.getTitle().toString());

		pushData.put("firstName", user.getFirstName().toString());
		pushData.put("lastName", user.getLastName().toString());
		pushData.put("email", user.getEmail().toString());

		pushData.put("title", request.getTitle().toString());
		pushData.put("body", request.getMessage().toString());
		return pushData;
	}
	
	public static String sendPushNotificationFromAdndroid(String deviceToken, String timestamp, String Message, String from ,String notificationType) {

		
		String result = "";
		try {
			
		HttpClient client = HttpClientBuilder.create().build();

		HttpPost post = new HttpPost("https://fcm.googleapis.com/fcm/send");

		post.setHeader("Content-type", "application/json");

		post.setHeader("Authorization", "key=" + Constant.FIREBASE_KEY);


		JSONObject message = new JSONObject();

		message.put("to", deviceToken);

		message.put("priority", "high");


		JSONObject data = new JSONObject();

		data.put("title", notificationType);

		data.put("body", Message);

		data.put("sender", from);
		
		data.put("timestamp", timestamp);
		
		message.put("data", data);

		post.setEntity(new StringEntity(message.toString(), "UTF-8"));

		org.apache.http.HttpResponse response = client.execute(post);

		System.out.println("RESPONSE : " + response);

		} catch (Exception e) {

		e.printStackTrace();

		}

		// System.out.println("Notification is sent successfully");

		return result;
		}
	
	
public static String sendSMSPushNotificationFromAdndroid(String deviceToken, String timestamp, String Message, String from ,Long msgId,String notificationType) {

		
		String result = "";
		try {
			
		HttpClient client = HttpClientBuilder.create().build();

		HttpPost post = new HttpPost("https://fcm.googleapis.com/fcm/send");

		post.setHeader("Content-type", "application/json");

		post.setHeader("Authorization", "key=" + Constant.FIREBASE_KEY);


		JSONObject message = new JSONObject();

		message.put("to", deviceToken);

		message.put("priority", "high");


		JSONObject data = new JSONObject();

		data.put("title", notificationType);

		data.put("body", Message);

		data.put("sender", from);
		
		data.put("timestamp", timestamp);
		
		data.put("msgId", msgId);
		
		message.put("data", data);

		post.setEntity(new StringEntity(message.toString(), "UTF-8"));

		org.apache.http.HttpResponse response = client.execute(post);

		System.out.println("RESPONSE : " + response);

		} catch (Exception e) {

		e.printStackTrace();

		}

		// System.out.println("Notification is sent successfully");

		return result;
		}
	
	
	
	
	public static void sendNotificationToAndroidForCalls(String token,String from) {

		try {

		// String token = "e0Rn9rEeT7-9z_dgqfGNFN:APA91bHrj9uN1DObWN-_VA0pphxjJtiKp876wpHwu1evLa_kBsGlGkxecqEU9uMlK4zYaSOFmtEsam4YttES95pB-zmNbXDgfdY_oZhvFDtltPx3meQvnlHKSzm-OHun-L03jclM3577";



		HttpClient client = HttpClientBuilder.create().build();

		HttpPost post = new HttpPost("https://fcm.googleapis.com/fcm/send");

		post.setHeader("Content-type", "application/json");

		post.setHeader("Authorization", "key=" + Constant.FIREBASE_KEY);


		JSONObject message = new JSONObject();

		message.put("to", token);

		message.put("priority", "high");



		JSONObject data = new JSONObject();

		data.put("title", "CALL");

		data.put("body", "You have incoming call from "+from);

		message.put("data", data);

		post.setEntity(new StringEntity(message.toString(), "UTF-8"));

		org.apache.http.HttpResponse response = client.execute(post);

		System.out.println("RESPONSE : " + response);

		} catch (Exception e) {

		e.printStackTrace();

		}

		}
	public static String sendPushNotificationFromIos(String deviceToken, String timestamp, String Message, String sender ,String notificationType) {

		//Key Id : WA2US7X5M9
		//Team ID : NEZFP8VAHR
		String   result=null;
		try {


		final ApnsClient apnsClient = new ApnsClientBuilder()
		       .setApnsServer(ApnsClientBuilder.DEVELOPMENT_APNS_HOST)
		       .setSigningKey(ApnsSigningKey.loadFromPkcs8File(new File(Constant.CERT_PATH),
		        Constant.CERT_TEAMID, Constant.CERT_KEYID)).build();

		// final ApnsClient apnsClient = new ApnsClientBuilder()
//		        .setApnsServer(ApnsClientBuilder.PRODUCTION_APNS_HOST)
//		        .setSigningKey(ApnsSigningKey.loadFromPkcs8File(new File(IConstant.CERT_PATH),
//		         IConstant.CERT_TEAMID, IConstant.CERT_KEYID)).build();

		 final ApnsPayloadBuilder payloadBuilder = new SimpleApnsPayloadBuilder();
		   if (sender != null) {
		if (sender.startsWith("http"))
		payloadBuilder.addCustomProperty("title", sender);
		}
		 payloadBuilder.setAlertBody("You have recevied text message from "+sender);
		 payloadBuilder.setSound("default");
		     payloadBuilder.addCustomProperty("content-available",1);
		     payloadBuilder.addCustomProperty("message", Message);
		     payloadBuilder.addCustomProperty("timestamp", timestamp);
		     payloadBuilder.addCustomProperty("notificationType", notificationType);
		     payloadBuilder.addCustomProperty("sender", sender);
		
		     payloadBuilder.setContentAvailable(true);
		   

		   final String payload = payloadBuilder.build();
//		    final String token = TokenUtil.sanitizeTokenString("1ccfd84fa1d0bbe358e5016372943e3214be31376b074e2b62cb954db56d089e");

//		    SimpleApnsPushNotification    pushNotification = new SimpleApnsPushNotification("1ccfd84fa1d0bbe358e5016372943e3214be31376b074e2b62cb954db56d089e", "com.DarkFiledRadio", payload);
		   SimpleApnsPushNotification    pushNotification = new SimpleApnsPushNotification(deviceToken.trim(), Constant.CERT_BUNDALIDENTIFIRE, payload);
		   final PushNotificationFuture<SimpleApnsPushNotification, PushNotificationResponse<SimpleApnsPushNotification>>
		   sendNotificationFuture = apnsClient.sendNotification(pushNotification);
		       result = "succcess";
		   return result;


		} catch (Exception e) {
		result="failed";
		return result;
		}

		}
	
	public static String sendSMSPushNotificationFromIos(String deviceToken, String timestamp, String Message, String sender,Long msgId ,String notificationType) {

		//Key Id : WA2US7X5M9
		//Team ID : NEZFP8VAHR
		String   result=null;
		try {


		final ApnsClient apnsClient = new ApnsClientBuilder()
		       .setApnsServer(ApnsClientBuilder.DEVELOPMENT_APNS_HOST)
		       .setSigningKey(ApnsSigningKey.loadFromPkcs8File(new File(Constant.CERT_PATH),
		        Constant.CERT_TEAMID, Constant.CERT_KEYID)).build();

		// final ApnsClient apnsClient = new ApnsClientBuilder()
//		        .setApnsServer(ApnsClientBuilder.PRODUCTION_APNS_HOST)
//		        .setSigningKey(ApnsSigningKey.loadFromPkcs8File(new File(IConstant.CERT_PATH),
//		         IConstant.CERT_TEAMID, IConstant.CERT_KEYID)).build();

		 final ApnsPayloadBuilder payloadBuilder = new SimpleApnsPayloadBuilder();
		   if (sender != null) {
		if (sender.startsWith("http"))
		payloadBuilder.addCustomProperty("title", sender);
		}
		 payloadBuilder.setAlertBody("You have recevied text message from "+sender);
		 payloadBuilder.setSound("default");
		     payloadBuilder.addCustomProperty("content-available",1);
		     payloadBuilder.addCustomProperty("message", Message);
		     payloadBuilder.addCustomProperty("timestamp", timestamp);
		     payloadBuilder.addCustomProperty("msgId", msgId);
		     payloadBuilder.addCustomProperty("notificationType", notificationType);
		     payloadBuilder.addCustomProperty("sender", sender);
		
		     payloadBuilder.setContentAvailable(true);
		   

		   final String payload = payloadBuilder.build();
//		    final String token = TokenUtil.sanitizeTokenString("1ccfd84fa1d0bbe358e5016372943e3214be31376b074e2b62cb954db56d089e");

//		    SimpleApnsPushNotification    pushNotification = new SimpleApnsPushNotification("1ccfd84fa1d0bbe358e5016372943e3214be31376b074e2b62cb954db56d089e", "com.DarkFiledRadio", payload);
		   SimpleApnsPushNotification    pushNotification = new SimpleApnsPushNotification(deviceToken.trim(), Constant.CERT_BUNDALIDENTIFIRE, payload);
		   final PushNotificationFuture<SimpleApnsPushNotification, PushNotificationResponse<SimpleApnsPushNotification>>
		   sendNotificationFuture = apnsClient.sendNotification(pushNotification);
		       result = "succcess";
		   return result;


		} catch (Exception e) {
		result="failed";
		return result;
		}

		}


	
	public static String sendPushNotificationFromIosVoip(String deviceToken, String title, String Message, String fromUser ,String notificationType) {

		//Key Id : WA2US7X5M9
		//Team ID : NEZFP8VAHR
		String   result=null;
		try {


		final ApnsClient apnsClient = new ApnsClientBuilder()
		       .setApnsServer(ApnsClientBuilder.DEVELOPMENT_APNS_HOST)
		       .setSigningKey(ApnsSigningKey.loadFromPkcs8File(new File(Constant.CERT_PATH),
		        Constant.CERT_TEAMID, Constant.CERT_KEYID)).build();

		// final ApnsClient apnsClient = new ApnsClientBuilder()
//		        .setApnsServer(ApnsClientBuilder.PRODUCTION_APNS_HOST)
//		        .setSigningKey(ApnsSigningKey.loadFromPkcs8File(new File(IConstant.CERT_PATH),
//		         IConstant.CERT_TEAMID, IConstant.CERT_KEYID)).build();

		 final ApnsPayloadBuilder payloadBuilder = new SimpleApnsPayloadBuilder();
////		   if (image != null) {
////		if (image.startsWith("http"))
//		payloadBuilder.addCustomProperty("image", ");
//		}
		 payloadBuilder.setAlertBody(Message);
		 payloadBuilder.setSound("default");
		 payloadBuilder.addCustomProperty("content-available", 1);

		 payloadBuilder.addCustomProperty("mutable-content", 1);

		 payloadBuilder.addCustomProperty("handle", "You have incoming call from "+fromUser);

		 payloadBuilder.setContentAvailable(true);

		 payloadBuilder.setMutableContent(true);
		   
		 System.out.println("Push Notification Params : deviceToken: " + deviceToken.trim());
//		 System.out.println("Push Notification Params : to: " + to);
		 System.out.println("Push Notification Params : from: " + fromUser);
		   final String payload = payloadBuilder.build();
		   SimpleApnsPushNotification    pushNotification = new SimpleApnsPushNotification(deviceToken.trim(), Constant.CERT_BUNDALIDENTIFIRE_VOIP, payload);
		   final PushNotificationFuture <SimpleApnsPushNotification, PushNotificationResponse<SimpleApnsPushNotification>>
		   sendNotificationFuture = apnsClient.sendNotification(pushNotification);
		       result = "succcess";
		   return result;

		   
		} catch (Exception e) {
		result="failed";
		return result;
		}
		}
//	
//	public static void main(String arg[]) {
//		try {
////			String result = PushNotificationService.sendPushNotificationFromIos("8379769567968C260499560DB07F2CA9EBC69B028F715D8359A3B2F815A5467E", "ios titile", "ios msg", null,
//			//		"type2
////			String result = PushNotificationService.sendPushNotificationFromIosVoip("eea1e72a0d9f16845d51199780baa989ccbfcf765768b7cae728c9840b6de2b0", "ios titile 9322344445", "ios msg 985564985", null,
////					"type2");
////			System.out.println("ios push notification result==>" + result);
//			
//			//    eyl0DtgETNa6MxuL5qTvsn:APA91bF1VK20uQSPFcbYZ9ujQl_rjiKCcPCC0fYd_NjZkcI4gDa9YL6xTlXrtUxnymrelqJcuebTe9Sxva8IgjxvfezFQbymc9vnmUQVF_fjAQSLIfiC5z9DZxXkeJRPKEp45qNWAvZs
//
//			// String deviceToken, String timestamp, String Message, String from ,String notificationType
//			Timestamp currentDateTime = new Timestamp(System.currentTimeMillis());
//			
//			Long timestamp=currentDateTime.getTime();
//	        System.out.println("Current Date Timestamp : " + timestamp);            // 1616574866666
//
//			sendPushNotificationFromIos("7CDB91005F0B67609944650516EFA4A602553A8EA6E4E10AF630789A0B6DB046", String.valueOf(timestamp), "Hello shruti, This is SMS sending from push notification. ","61485833460", "SMS");
//			//sendPushNotificationFromAdndroid("dAx-NYgiSPOA1VuLINILUv:APA91bHODO-1gx1O571lODZKsz-M4ALo3T7W5EqDrz-7uxvqQsxCYb5GP6uwwUctu4H-nrood9kPBqDKLe_wQw8UqftajIRv_VnyDX49iuWGgPhgBMToKELLZwYV720kYLbWgzoCCKgY", String.valueOf(timestamp), "Hello This is SMS sending from push notification. please store me.","919770683567", "SMS");
//			
//			//sendNotificationToAndroidForCalls("fn1lsNxcQTOg53LtFOvofj:APA91bFyPbejQPcKYP6N4Snjn3ULYccDDT10WIsdOcu6aXm6QrNv1BBbPAcJZ5yiowLS29q83X5M5fL0JSm413D_huReHCGn8pVPwhQ4H5Igog8Hl2j97jug7NFV8cygceRBPuSJZBTS", "61485833454");
//		} catch (Exception e) {
//			e.printStackTrace();
//			//return ResponseHandler.generateResponse(CONSTANTMESSAGE.DATA_FETCH_UNSUCCESSFULLY,
//					//HttpStatus.INTERNAL_SERVER_ERROR, new EmptyJsonResponse());
//		}		
//}	
	
}
