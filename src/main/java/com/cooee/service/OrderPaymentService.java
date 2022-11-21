package com.cooee.service;

import java.util.Map;

public interface OrderPaymentService {

	Map<Object, Object> getEphemeralKey(String api_version, Long Id);

	Map<Object, Object> clientSecret(String currencyType, Double amount, Long userId);

//	Map<Object, Object> createProduct(String productName);
//
//	Map<Object, Object> createPlan(Integer amount, String currency, String interval, String product);

	//Map<Object, Object> subscription(String price, String customer);
	
	Map<Object, Object> getIntentId(String intentId);
	
	Map<Object, Object> subscription(String price, String customer, String paymentMethod);




}
