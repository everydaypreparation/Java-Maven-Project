package com.cooee.payload;

import java.util.List;

public class BulkUploadRequest {
	private  List<String> didNumber;
	private String country;

	

	public List<String> getDidNumber() {
		return didNumber;
	}

	public void setDidNumber(List<String> didNumber) {
		this.didNumber = didNumber;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	}
