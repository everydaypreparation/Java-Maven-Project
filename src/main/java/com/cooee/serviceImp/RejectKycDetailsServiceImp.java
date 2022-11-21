package com.cooee.serviceImp;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cooee.model.RejectKycDetails;
import com.cooee.model.User;
import com.cooee.payload.IDRequest;
import com.cooee.repository.RejectKycDetailsRepo;
import com.cooee.service.RejectKycDetailsService;

@Service
public class RejectKycDetailsServiceImp implements RejectKycDetailsService {

	@Autowired
	private RejectKycDetailsRepo rejectKycSetailsRepo;

	@Override
	public List<RejectKycDetails> rejectKycDetails(List<String> descriptions, User user) {

		List<RejectKycDetails> oldEntries = rejectKycSetailsRepo.findByUserId(user.getId());
		if (oldEntries != null && !oldEntries.isEmpty()) {
			for (RejectKycDetails kyc : oldEntries) {
				kyc.setActive(false);
			}

			rejectKycSetailsRepo.saveAll(oldEntries);
		} 

		List<RejectKycDetails> newEntries = new ArrayList<>();

		for (String description : descriptions) {
			RejectKycDetails newKyc = new RejectKycDetails();
			Timestamp currentDateTime = new Timestamp(System.currentTimeMillis());
			newKyc.setActive(true);
			newKyc.setDescription(description);
			newKyc.setCreationDate(currentDateTime);
			newKyc.setUser(user);

			newEntries.add(newKyc);
		}

		if (!newEntries.isEmpty())
			return rejectKycSetailsRepo.saveAll(newEntries);

		return null;

	}

	@Override
	public List<RejectKycDetails> getRejectKycDescriptions(IDRequest request) {
    return rejectKycSetailsRepo.findByUserId(request.getId());
	}
	
}
