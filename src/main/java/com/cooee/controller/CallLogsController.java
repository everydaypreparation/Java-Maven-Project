package com.cooee.controller;

import java.util.ArrayList;
import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.cooee.payload.CallLogsApi;
import com.cooee.payload.CallLogsByExtensionResponse;
import com.cooee.payload.UsernameAndPageFromPayload;
import com.cooee.payloads.CallLogsPagingRequest;
import com.cooee.service.CallLogsService;
import com.cooee.util.IUtil;

@RestController
@RequestMapping("/callLogs")
public class CallLogsController {

	@Value("${asteriskUrl}")
	private String asteriskUrl;

	@Value("${asteriskApISecretKey}")
	private String asteriskApISecretKey;

	@Autowired
	private CallLogsService callLogsService;
	
	@PostMapping("/getAllCallLogs") 
	public ResponseEntity<CallLogsApi> getAllLogs(@RequestBody CallLogsPagingRequest requestData) {

		CallLogsApi response = new CallLogsApi();
		response.setStatus("0");
		response.setData(new ArrayList<>(0));
		response.setTotalCount(0);
		
		try {
			Integer pageFrom = requestData.getStart();
			Integer limit = requestData.getLength();
			
			CallLogsPagingRequest pagingRequest = new CallLogsPagingRequest();
			pagingRequest.setStart(pageFrom);
			pagingRequest.setLength(limit);
			
			final String uri = asteriskUrl + "callLogs/getAllCallLogs";

			RestTemplate restTemplate = new RestTemplate();
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
			headers.set("Authorization", asteriskApISecretKey);
			HttpEntity<CallLogsPagingRequest> entity = new HttpEntity<CallLogsPagingRequest>(pagingRequest, headers);

			ResponseEntity<Page<CallLogsByExtensionResponse>> result = restTemplate.exchange(uri, HttpMethod.POST,
					entity, new ParameterizedTypeReference<Page<CallLogsByExtensionResponse>>() {
					});
			
			if (result != null && result.getBody() != null) {

				response.setStatus("1");
				response.setMessage("Call Logs Fetched Successfully!");
				response.setData(result.getBody().getContent());
				response.setTotalCount(result.getBody().getContent().size());

				return ResponseEntity.ok(response);
			} else {
				response.setMessage("Call Logs Not Fetched!");
			}
		} catch (Exception e) {
			e.printStackTrace();
			response.setMessage("Call Logs Not Fetched With Exception Occured!");
		}

		return ResponseEntity.ok(response);
	}

	@PostMapping("/getCallLogsByUsername")
	public ResponseEntity<CallLogsApi> callLogsByUsername(@RequestBody UsernameAndPageFromPayload requestData) {

		CallLogsApi response = new CallLogsApi();
		response.setStatus("0");
		response.setData(new ArrayList<>(0));
		response.setTotalCount(0);
		
		try {
			String username = IUtil.getTrimParam(requestData.getUsername());
			String pageFrom = IUtil.getTrimParam(requestData.getPageFrom());
			String limit = requestData.getLimit();

			CallLogsApi resp = new CallLogsApi();
			
			if (!username.equalsIgnoreCase("")) {
				if (!pageFrom.equalsIgnoreCase("")) {
					resp = callLogsService.fetchCallAllLogsByUsername(username, Integer.parseInt(pageFrom),
							Integer.parseInt(limit));
				} else {
					resp = callLogsService.fetchCallAllLogsByUsername(username, 0, Integer.parseInt(limit));
				}
			} else {
				response.setMessage("Please Provide Valid Username!");
				ResponseEntity.ok(response);
			}

			if (resp != null && resp.getStatus() != null && !resp.getStatus().isEmpty()) {
				if (resp.getStatus().equalsIgnoreCase("1")) {
					response.setStatus("1");
					response.setMessage("Call Logs Fetched Successfully!");
					response.setData(resp.getData());
					response.setTotalCount(resp.getTotalCount());

				} else {
					response.setStatus("1");
					response.setMessage(resp.getMessage());
					response.setData(resp.getData());
					response.setTotalCount(resp.getTotalCount());
				}

				return ResponseEntity.ok(response);
			} else {
				response.setMessage("Call Logs Not Fetched!");
			}
		} catch (Exception e) {
			e.printStackTrace();
			response.setMessage("Call Logs Not Fetched With Exception Occured!");
		}

		return ResponseEntity.ok(response);
	}
}
