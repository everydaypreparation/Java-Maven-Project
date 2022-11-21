package com.cooee.service;

import com.cooee.payload.CallLogsApi;

public interface CallLogsService {
	CallLogsApi fetchCallAllLogsByUsername(String username, int pageFrom, int limit);

}
