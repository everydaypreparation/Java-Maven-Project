package com.cooee.serviceImpl.asterisk;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.cooee.model.Incoming;
import com.cooee.service.asterisk.IncomingService;

@Service
public class IncomingServiceImpl implements IncomingService {

	@Value("${asteriskUrl}")
	private String asteriskUrl;

	@Value("${asteriskApISecretKey}")
	private String asteriskApISecretKey;

	@Override
	public Incoming add(String orgUserName, String virtualNumber, String incoming_desnitaion, String orgName) {
		try {
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("orgName", orgName);
			jsonObject.put("virtualNumber", virtualNumber);
			jsonObject.put("incomingDesnitaion", incoming_desnitaion);
			jsonObject.put("orgUserName", orgUserName);

			RestTemplate restTemplate = new RestTemplate();
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			headers.set("Authorization", asteriskApISecretKey);
			HttpEntity<String> entity = new HttpEntity<String>(jsonObject.toString(), headers);

			final String uri = asteriskUrl + "teliapi/addIncoming";
			ResponseEntity<Incoming> result = restTemplate.exchange(uri, HttpMethod.POST, entity, Incoming.class);
			return result.getBody();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public Incoming update(String virtualNumber, String incoming_desnitaion) {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("virtualNumber", virtualNumber);
		jsonObject.put("incomingDesnitaion", incoming_desnitaion);

		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.set("Authorization", asteriskApISecretKey);
		HttpEntity<String> entity = new HttpEntity<String>(jsonObject.toString(), headers);

		final String uri = asteriskUrl + "teliapi/updateIncoming";
		ResponseEntity<Incoming> result = restTemplate.exchange(uri, HttpMethod.POST, entity, Incoming.class);
		return result.getBody();
	}

	@Override
	public int delete(String virtualNumber) {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("virtualNumber", virtualNumber);

		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.set("Authorization", asteriskApISecretKey);
		HttpEntity<String> entity = new HttpEntity<String>(jsonObject.toString(), headers);

		final String uri = asteriskUrl + "teliapi/deleteIncoming";
		ResponseEntity<Integer> result = restTemplate.exchange(uri, HttpMethod.POST, entity, Integer.class);
		return result.getBody();
	}

}
