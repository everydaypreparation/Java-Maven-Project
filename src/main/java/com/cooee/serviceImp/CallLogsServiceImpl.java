package com.cooee.serviceImp;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.cooee.payload.CallLogsApi;
import com.cooee.payload.CallLogsByExtensionResponse;
import com.cooee.projections.CallLogsProjection;
import com.cooee.responsemessgae.CONSTANTMESSAGE;
import com.cooee.service.CallLogsService;
import com.cooee.util.IUtil;

@Service
public class CallLogsServiceImpl implements CallLogsService {

	@Value("${asteriskUrl}")
	private String asteriskUrl;

	@Value("${asteriskApISecretKey}")
	private String asteriskApISecretKey;

	@Override
	public CallLogsApi fetchCallAllLogsByUsername(String username, int pageFrom, int limit) {

		System.out.println(
				"getCallLogsByUsername. username-> " + username + " pageFrom-> " + pageFrom + " limit-> " + limit);

		CallLogsApi response = new CallLogsApi();

		JSONObject json = new JSONObject();
		json.put("username", username);
		json.put("pageFrom", pageFrom);
		json.put("limit", limit);

		final String uri = asteriskUrl + "callLogs/fetchCallAllLogsByUsername";

		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.set("Authorization", asteriskApISecretKey);
		HttpEntity<String> entity = new HttpEntity<String>(json.toString(), headers);

		ResponseEntity<CallLogsApi> result = restTemplate.exchange(uri, HttpMethod.POST, entity, CallLogsApi.class);

		if (result != null) {
			if (result.getStatusCodeValue() == 200) {
				response = result.getBody();
//				for (CallLogsByExtensionResponse res : result.getBody()) {
				// System.out.println("src: " + res.getSrc() + " dst: " + res.getDst() + " date:
				// " + res.getCallDate());
				// }
			} else {
				response.setStatus("0");
			}
		} else {
			response.setStatus("0");
		}

		return response;
	}

	List<CallLogsByExtensionResponse> returnListOfLogsByUsername(String username, List<CallLogsProjection> callLogs) {
		List<CallLogsByExtensionResponse> response = new ArrayList<>();

		for (CallLogsProjection callLog : callLogs) {
			CallLogsByExtensionResponse resp = new CallLogsByExtensionResponse();
			resp.setCallDate(callLog.getCalldate());
			resp.setCallDuration(IUtil.convertSecToHourMinSec(callLog.getBillsec()).trim());

			String src = "";
			String dst = "";
			String srcDisplayName = "";
			String dstDisplayName = "";

			String call_type = "";

			if (callLog.getChannel().contains("PJSIP/" + username)) {
				call_type = CONSTANTMESSAGE.OutgoingCall;// outgoing;

				if (callLog.getDstchannel().contains("outgoingTeli")) {
					dstDisplayName = callLog.getDst();

					srcDisplayName = IUtil.convertSrcAndDstEmptyParam(
							callLog.getChannel().substring(6, callLog.getChannel().indexOf("-")));
				} else {
					int index = 8;

					if (callLog.getDstchannel().contains("@") && callLog.getDstchannel().contains("-")) {
						index = callLog.getDstchannel().indexOf("@");
						dstDisplayName = IUtil.convertSrcAndDstEmptyParam(callLog.getDstchannel().substring(6, index));
					} else if (callLog.getDstchannel().contains("-")) {
						index = callLog.getDstchannel().indexOf("-");
						dstDisplayName = IUtil.convertSrcAndDstEmptyParam(callLog.getDstchannel().substring(6, index));
					} else {
						dstDisplayName = IUtil.convertSrcAndDstEmptyParam("");
					}

					srcDisplayName = callLog.getSrc();
				}

				dst = callLog.getDst();
				src = callLog.getChannel().substring(6, callLog.getChannel().indexOf("-"));

			} else if (callLog.getDstchannel().contains("PJSIP/" + username)
					&& callLog.getDisposition().equalsIgnoreCase("ANSWERED")) {
				call_type = CONSTANTMESSAGE.IncomingCall;// incoming;

				if (callLog.getChannel().contains("IncomingTeli")) {
					srcDisplayName = callLog.getSrc();

					src = callLog.getSrc();
				} else {
					srcDisplayName = IUtil.convertSrcAndDstEmptyParam(
							callLog.getChannel().substring(6, callLog.getChannel().indexOf("-")));

					src = callLog.getChannel().substring(6, callLog.getChannel().indexOf("-"));
				}

				dstDisplayName = IUtil.convertSrcAndDstEmptyParam(callLog.getDst());
				dst = callLog.getDst();

			} else if (callLog.getDstchannel().contains("PJSIP/" + username)
					&& (callLog.getDisposition().equalsIgnoreCase("NO ANSWER")
							|| callLog.getDisposition().equalsIgnoreCase("BUSY"))) {
				call_type = CONSTANTMESSAGE.MissedCall;// misscall;

				if (callLog.getChannel().contains("IncomingTeli") || callLog.getChannel().contains("outgoingTeli")) {
					srcDisplayName = callLog.getSrc();
					src = callLog.getSrc();

					int index = 8;

					if (callLog.getDstchannel().contains("@") && callLog.getDstchannel().contains("-")) {
						index = callLog.getDstchannel().indexOf("@");
						dst = callLog.getDstchannel().substring(6, index);
						dstDisplayName = IUtil.convertSrcAndDstEmptyParam(dst);
					} else if (callLog.getDstchannel().contains("-")) {
						index = callLog.getDstchannel().indexOf("-");
						dst = callLog.getDstchannel().substring(6, index);
						dstDisplayName = IUtil.convertSrcAndDstEmptyParam(dst);
					} else {
						dstDisplayName = IUtil.convertSrcAndDstEmptyParam("");
						dst = "";
					}
				} else {
					srcDisplayName = IUtil.convertSrcAndDstEmptyParam(
							callLog.getChannel().substring(6, callLog.getChannel().indexOf("-")));

					src = callLog.getChannel().substring(6, callLog.getChannel().indexOf("-"));

					dstDisplayName = IUtil.convertSrcAndDstEmptyParam(callLog.getDst());
					dst = callLog.getDst();
				}

			}

			resp.setDst(dst);
			resp.setSrc(src);
			resp.setSrcDisplayName(srcDisplayName);
			resp.setDstDisplayName(dstDisplayName);

			resp.setCallType(IUtil.convertEmptyParam(call_type));

			response.add(resp);
		}
		return response;
	}
}