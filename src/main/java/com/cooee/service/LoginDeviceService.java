package com.cooee.service;

import com.cooee.model.LoginDevice;
import com.cooee.model.User;

public interface LoginDeviceService {

	LoginDevice saveLoginDevice(String param1, String param2, String param3, String param4,String callToken,String device,String isDeviceActive, User result);

}
