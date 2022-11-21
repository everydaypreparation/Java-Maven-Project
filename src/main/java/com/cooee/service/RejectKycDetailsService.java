package com.cooee.service;

import java.util.List;

import com.cooee.model.RejectKycDetails;
import com.cooee.model.User;
import com.cooee.payload.IDRequest;

public interface RejectKycDetailsService {

	List<RejectKycDetails> rejectKycDetails(List<String> descriptions, User user);

	List<RejectKycDetails> getRejectKycDescriptions(IDRequest request);



}
