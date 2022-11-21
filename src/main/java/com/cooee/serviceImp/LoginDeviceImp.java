package com.cooee.serviceImp;

import java.sql.Timestamp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cooee.model.LoginDevice;
import com.cooee.model.User;
import com.cooee.repository.LoginDeviceRepository;
import com.cooee.service.LoginDeviceService;

@Service
public class LoginDeviceImp implements LoginDeviceService {

	@Autowired
	private LoginDeviceRepository loginDeviceRepository;

	@Override
	public LoginDevice saveLoginDevice(String deviceID, String deviceIP, String deviceToken, String deviceType,String callToken,String device,String isDeviceActive,
			User user) {
		try {
			LoginDevice loginDevice = new LoginDevice();

			loginDevice.setDeviceID(deviceID);
			loginDevice.setDeviceIp(deviceIP);
			loginDevice.setDeviceToken(deviceToken);
			loginDevice.setDeviceType(deviceType);
			loginDevice.setCallToken(callToken);
			loginDevice.setDevice(device);
			loginDevice.setIsDeviceActive(true);

			loginDevice.setUser(user);

			loginDevice.setLastLogin(new Timestamp(System.currentTimeMillis()));

			return loginDeviceRepository.save(loginDevice);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
