package com.cooee.payload;

import java.sql.Timestamp;

import com.fasterxml.jackson.annotation.JsonFormat;

public class CallLogsByExtensionResponse {

//	@JsonFormat(pattern="MM-dd-yyyy HH:mm:ss")
	@JsonFormat(shape = JsonFormat.Shape.NUMBER)
	// private String callDate;
	private Timestamp callDate;
	private String src;
	private String dst;
	private String callDuration;
	private String callType;

	private String srcDisplayName;
	private String dstDisplayName;

	public Timestamp getCallDate() {
		return callDate;
	}

	public void setCallDate(Timestamp callDate) {
		this.callDate = callDate;
	}

	public String getSrc() {
		return src;
	}

	public void setSrc(String src) {
		this.src = src;
	}

	public String getDst() {
		return dst;
	}

	public void setDst(String dst) {
		this.dst = dst;
	}

	public String getCallDuration() {
		return callDuration;
	}

	public void setCallDuration(String callDuration) {
		this.callDuration = callDuration;
	}

	public String getCallType() {
		return callType;
	}

	public void setCallType(String callType) {
		this.callType = callType;
	}

	public String getSrcDisplayName() {
		return srcDisplayName;
	}

	public void setSrcDisplayName(String srcDisplayName) {
		this.srcDisplayName = srcDisplayName;
	}

	public String getDstDisplayName() {
		return dstDisplayName;
	}

	public void setDstDisplayName(String dstDisplayName) {
		this.dstDisplayName = dstDisplayName;
	}

}
