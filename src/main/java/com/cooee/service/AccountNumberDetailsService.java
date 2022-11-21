package com.cooee.service;

import com.cooee.model.AccountNumberDetails;
import com.cooee.model.User;

public interface AccountNumberDetailsService {

	AccountNumberDetails findLastEntry();
	
	AccountNumberDetails add(String username, String password, String server, String did, User user);
	
	AccountNumberDetails findByUserId(Long id);
	

}