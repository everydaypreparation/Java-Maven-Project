package com.cooee.serviceImpl.firebase;

import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.cooee.payload.PushNotificationRequest;
import com.google.firebase.messaging.AndroidConfig;
import com.google.firebase.messaging.ApnsConfig;
import com.google.firebase.messaging.Aps;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;

@Service
public class FCMService {

	private Logger logger = LoggerFactory.getLogger(FCMService.class);

	public void sendMessage(Map<String, String> data, PushNotificationRequest request)
			throws InterruptedException, ExecutionException {
		Message message = getPreconfiguredMessageWithData(data, request);
		String response = sendAndGetResponse(message);
		logger.info("Sent message with data. Topic: " + request.getTopic() + ", " + response);
	}

	public void sendMessageWithoutData(PushNotificationRequest request)
			throws InterruptedException, ExecutionException {
		Message message = getPreconfiguredMessageWithoutData(request);
		String response = sendAndGetResponse(message);
		logger.info("Sent message without data. Topic: " + request.getTopic() + ", " + response);
	}

	public void sendMessageToToken(PushNotificationRequest request) throws InterruptedException, ExecutionException {
		Message message = getPreconfiguredMessageToToken(request);
		String response = sendAndGetResponse(message);
		logger.info("Push Response : " + response);
	}

	private String sendAndGetResponse(Message message) throws InterruptedException, ExecutionException {
		// return FirebaseMessaging.getInstance().sendAsync(message).get();
		try {
			return new String(FirebaseMessaging.getInstance().send(message).getBytes());
		} catch (FirebaseMessagingException e) {
			System.out.println(e.getMessage());
//			e.printStackTrace();
		}
		return "Notification Sent Failed";
	}

	/*
	 * private AndroidConfig getAndroidConfig(String topic) { return
	 * AndroidConfig.builder()
	 * .setTtl(Duration.ofDays(10).toMillis()).setCollapseKey(topic)
	 * .setPriority(AndroidConfig.Priority.HIGH)
	 * .setNotification(AndroidNotification.builder().setSound(NotificationParameter
	 * .SOUND.getValue())
	 * .setColor(NotificationParameter.COLOR.getValue()).setTag(topic).build()).
	 * build(); }
	 */

	private AndroidConfig getAndroidConfig(String topic) {
		return AndroidConfig.builder().setTtl(Duration.ofDays(10).toMillis()).setCollapseKey(topic)
				.setPriority(AndroidConfig.Priority.NORMAL).build();
	}

	private ApnsConfig getApnsConfig(String topic) {
		if (topic.equalsIgnoreCase("call"))
			return ApnsConfig.builder()
					.setAps(Aps.builder().setCategory(topic).setThreadId(topic).setSound("ringtone.mp3").build())
					.build();
//			return ApnsConfig.builder().setAps(Aps.builder().setContentAvailable(true).build()).build();
		else
			return ApnsConfig.builder()
					.setAps(Aps.builder().setCategory(topic).setThreadId(topic).setSound("default").build()).build();
	}

	private Message getPreconfiguredMessageToToken(Map<String, String> data, PushNotificationRequest request) {
		return getPreconfiguredMessageBuilder(request).setToken(request.getToken()).putAllData(data).build();
	}

	private Message getPreconfiguredMessageToToken(PushNotificationRequest request) {
		return getPreconfiguredMessageBuilder(request).setToken(request.getToken()).build();
	}

	private Message getPreconfiguredMessageWithoutData(PushNotificationRequest request) {
		return getPreconfiguredMessageBuilder(request).setTopic(request.getTopic()).build();
	}

	private Message getPreconfiguredMessageWithData(Map<String, String> data, PushNotificationRequest request) {
		return getPreconfiguredMessageBuilder(request).putAllData(data).setTopic(request.getTopic()).build();
	}

	private Message.Builder getPreconfiguredMessageBuilder(PushNotificationRequest request) {
		AndroidConfig androidConfig = getAndroidConfig(request.getTopic());
		ApnsConfig apnsConfig = getApnsConfig(request.getTopic());

//		if (request.getType().equalsIgnoreCase("ios")) {
//			return Message.builder().setApnsConfig(apnsConfig).setAndroidConfig(androidConfig)
//					.setNotification(new Notification(request.getTitle(), request.getMessage()));
//		} else 
		if (request.getType().equalsIgnoreCase("android"))
			return Message.builder().setApnsConfig(apnsConfig).setAndroidConfig(androidConfig);
		else
			return Message.builder().setApnsConfig(apnsConfig).setAndroidConfig(androidConfig)
					.setNotification(new Notification(request.getTitle(), request.getMessage()));

	}

	public void sendMessageToToken(Map<String, String> smsPayloadData, PushNotificationRequest request)
			throws InterruptedException, ExecutionException {

		Message message = getPreconfiguredMessageToToken(smsPayloadData, request);
//		JSONObject object = new JSONObject(message);
//		System.out.println("JSON OBJECT -> " + object.toString());

		String response = sendAndGetResponse(message);

		logger.info("Push Response : " + response);
	}

	public void sendCallMessageToToken(Map<String, String> smsPayloadData, PushNotificationRequest request)
			throws InterruptedException, ExecutionException {

		Message message = getPreconfiguredCallMessageToToken(smsPayloadData, request);

		String response = sendAndGetResponse(message);

		logger.info("Push Response : " + response);
	}

	private Message getPreconfiguredCallMessageToToken(Map<String, String> data, PushNotificationRequest request) {
		return getPreconfiguredCallMessageBuilder(request).setToken(request.getToken()).putAllData(data).build();
	}

	private Message.Builder getPreconfiguredCallMessageBuilder(PushNotificationRequest request) {
		AndroidConfig androidConfig = getAndroidConfigForCall(request.getTopic());
		ApnsConfig apnsConfig = getApnsConfig(request.getTopic());

		if (request.getType().equalsIgnoreCase("android"))
			return Message.builder().setApnsConfig(apnsConfig).setAndroidConfig(androidConfig);
		else
			return Message.builder().setApnsConfig(apnsConfig).setAndroidConfig(androidConfig)
					.setNotification(new Notification(request.getTitle(), request.getMessage()));
	}

	private AndroidConfig getAndroidConfigForCall(String topic) {
		return AndroidConfig.builder().setTtl(Duration.ofDays(10).toMillis()).setCollapseKey(topic)
				.setPriority(AndroidConfig.Priority.HIGH).build();
	}
}
