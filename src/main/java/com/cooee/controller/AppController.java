package com.cooee.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class AppController {

	@GetMapping(value = "/resetPasswordForm/{email}")
	public String resetPasswordurl(@PathVariable("email") String email) {
		try {

			if (email == null || email.isEmpty()) {

//				String deCodedString = AESCipher.aesDecryptString(email, CONSTANTMESSAGE.key16Byte);

				return "falseCasePage";
			}

			return "successPage";
		} catch (Exception e) {
			return "falseCasePage";

		}
	}

}