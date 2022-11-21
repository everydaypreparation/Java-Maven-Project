package com.cooee.service;

import java.util.List;

import com.cooee.model.Sms;
import com.cooee.model.User;
import com.cooee.payload.BasicResponsePayload;
import com.cooee.payload.DeletedSmsFromDid;
import com.cooee.payload.IDRequest;
import com.cooee.payload.SmsDeleteById;

public interface SmsService {
	Integer countByUserId(Long id);

	public BasicResponsePayload receiveSms(String id,String source, String destination, String message,String date)
			throws Exception;

	public BasicResponsePayload sendSms(String source, String destination, String message) throws Exception;

//	Sms save(User user, String sourceNum, String id,String message,String destination,String date);

	List<Sms> findByUserId(Long id);

	void deleteAll(List<Sms> sms);

	Sms smsDeleteById(SmsDeleteById request);

	List<Sms> smsDeleteByUserId(DeletedSmsFromDid request);

	Sms save(User user, String sourceNum, String message, String destination, String messageId, String date);

	List<Sms> getSmsById(Long id);
}
