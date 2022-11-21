package com.cooee.serviceImp;

import java.sql.Timestamp;
import java.util.Date;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.cooee.model.AccountNumberDetails;
import com.cooee.model.User;
import com.cooee.payload.BasicResponsePayload;
import com.cooee.repository.AccountNumberRepository;
import com.cooee.service.AccountNumberDetailsService;

@Service
public class AccountNumberDetailsServiceImpl implements AccountNumberDetailsService {

	@Value("${asteriskUrl}")
	private String asteriskUrl;

	@Value("${asteriskApISecretKey}")
	private String asteriskApISecretKey;

	@Autowired
	private AccountNumberRepository accountNumberRepository;

	@Override
	public AccountNumberDetails findLastEntry() {
		// TODO Auto-generated method stub
		return accountNumberRepository.findFirstByOrderByIdDesc();
	}

	@Override
	public AccountNumberDetails add(String username, String password, String server, String did, User user) {

		AccountNumberDetails account = accountNumberRepository.findByDidNumber(did);
		if (account == null) {
			AccountNumberDetails accountNumber = new AccountNumberDetails();
			accountNumber.setSipUsername(username);
			accountNumber.setSipPassword(password);
			accountNumber.setSipServer(server);
			accountNumber.setDidNumber(did);
			accountNumber.setCreationDate(new Timestamp(new Date().getTime()));
			accountNumber.setUser(user);

			try {
				
				JSONObject jsonObject = new JSONObject();
				jsonObject.put("userUsername", username);
				jsonObject.put("password", password);
				jsonObject.put("displayName", username);
				jsonObject.put("outboundCid", did);

				jsonObject.put("orgUserName", username);
				jsonObject.put("departmentRingGrpNum", username);
				jsonObject.put("newUserDprtmt", username);

				if (user.getEmail().equals("")) {
					
					jsonObject.put("email", user.getCooeeId()+"@callcooee.com"); 
					jsonObject.put("firstName", user.getCooeeId());

				}
				else {
					jsonObject.put("email", user.getEmail()); 
					jsonObject.put("firstName", user.getEmail());

				}

				RestTemplate restTemplate = new RestTemplate();
				HttpHeaders headers = new HttpHeaders();
				headers.setContentType(MediaType.APPLICATION_JSON);
				headers.set("Authorization", asteriskApISecretKey);
				HttpEntity<String> entity = new HttpEntity<String>(jsonObject.toString(), headers);

				final String uri = asteriskUrl + "user/addUser";
				ResponseEntity<BasicResponsePayload> result = restTemplate.exchange(uri, HttpMethod.POST, entity,
						BasicResponsePayload.class);

				BasicResponsePayload response = result.getBody();

				if (response != null) {
					if (response.getStatus().equalsIgnoreCase("1")) {
						return accountNumberRepository.save(accountNumber);
					} else {
						System.out.println("Not able to save extension in asterisk for - " + response.getStatus());
						System.out.println(response.getMessage());
					}
				} else {
					System.out.println("Not able to save extension in asterisk because, response is null");
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	@Override
	public AccountNumberDetails findByUserId(Long id) {
		return accountNumberRepository.findByUserId(id);
	}

}