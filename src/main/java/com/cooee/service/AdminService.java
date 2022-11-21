package com.cooee.service;

import org.springframework.web.multipart.MultipartFile;

import com.cooee.model.Admin;
import com.cooee.model.User;
import com.cooee.payload.BlockOrUnblockRequest;
import com.cooee.payload.ChangeMail;
import com.cooee.payload.ChangePassword;
import com.cooee.payload.EmailDataPayload;
import com.cooee.payload.IDRequest;
import com.cooee.payload.MultipleResponse;
import com.cooee.payload.RejectKycRequest;
import com.cooee.payload.ResetPassword;
import com.cooee.payload.ResetUserPassword;
import com.cooee.payload.UsernameAndPasswordRequest;

public interface AdminService {

	Admin findByUsernameAndPassword(UsernameAndPasswordRequest request);

	MultipleResponse approveKyc(IDRequest iDRequest);

	MultipleResponse rejectKyc(RejectKycRequest rejectKycRequest);

	User updatePassword(String email, String password);

	User resetPassword(ResetPassword request);
	
	User blockOrUnblock(BlockOrUnblockRequest blockOrUnblockRequest);

	String uploadPdfWithName(MultipartFile multipartFile, String filename);

	User resetUserPassword(ResetUserPassword request);

	int deleteUser(Long id);
	
	



}