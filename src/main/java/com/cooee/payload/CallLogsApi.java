package com.cooee.payload;

import java.util.List;

public class CallLogsApi {
	private String message;
	private String status;
	private List<CallLogsByExtensionResponse> data;
	private long totalCount;

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public List<CallLogsByExtensionResponse> getData() {
		return data;
	}

	public void setData(List<CallLogsByExtensionResponse> data) {
		this.data = data;
	}

	public long getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(long totalCount) {
		this.totalCount = totalCount;
	}

}