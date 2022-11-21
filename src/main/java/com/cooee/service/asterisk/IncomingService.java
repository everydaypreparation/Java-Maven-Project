package com.cooee.service.asterisk;

import com.cooee.model.Incoming;

public interface IncomingService {

	public Incoming add(String orgUserName, String virtualNumber, String incoming_desnitaion, String orgName);

	public Incoming update(String virtualNumber, String incoming_desnitaion);

	public int delete(String virtualNumber);
}