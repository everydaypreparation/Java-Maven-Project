package com.cooee.projections;

import java.sql.Timestamp;

import com.fasterxml.jackson.annotation.JsonFormat;

public interface CallLogsProjection {

	@JsonFormat(pattern = "MM-dd-yyyy HH:mm:ss")
	public Timestamp getCalldate();

	public String getSrc();

	public String getDst();

	public int getBillsec();

	public String getUniqueid();

	public String getDisposition();

	public String getChannel();

	public String getDstchannel();

}