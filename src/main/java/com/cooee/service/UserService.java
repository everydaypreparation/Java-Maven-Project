package com.cooee.service;

import java.io.IOException;
import java.util.List;
import java.util.Set;

import javax.net.ssl.SSLException;

import com.cooee.model.LoginDevice;
import com.cooee.model.Sms;
import com.cooee.model.User;
import com.cooee.model.UserContactNoBlock;
import com.cooee.payload.ChangeMail;
import com.cooee.payload.ChangePassword;
import com.cooee.payload.DashboardResponse;
import com.cooee.payload.EmailAndPasswordRequest;
import com.cooee.payload.FileUploadRequest;
import com.cooee.payload.GetAllResponse;
import com.cooee.payload.IDRequest;
import com.cooee.payload.LoginRequest;
import com.cooee.payload.MultipleResponse;
import com.cooee.payload.OtpVerificationForEmailRequest;
import com.cooee.payload.OtpVerificationRequest;
import com.cooee.payload.PushNotificationPayload;
import com.cooee.payload.ResetPassword;
import com.cooee.payload.SendOtp;
import com.cooee.payload.SignUp;
import com.cooee.payload.SocialSignupRequest;
import com.cooee.payload.UserContactNoBlockPayload;
import com.cooee.payload.UserContactNoUnblockPayload;
import com.cooee.payload.WordpressLoginRequest;


public interface UserService {

	User addUser(SignUp signUp);

	User findByEmailAndPassword(LoginRequest request);

	User resetPassword(ResetPassword pass);

	User sendOtp(SendOtp request);

	MultipleResponse otpVerification(OtpVerificationRequest request);

	User resendOTP(SendOtp request);

	User findByEmail(String email);

	User save(EmailAndPasswordRequest request);

	User resetPasswordUrl(ResetPassword request);

	User updatePassword(String email, String password);

	User submitKYC(FileUploadRequest request);

	GetAllResponse getAllUsers(Integer pageNo, Integer pageSize, String sortBy, Integer approved);

	// List<User> getAllUsers();
	User findById(Long id);

	User saveUser(User user);

	String sendCallPushNotifications(String source, String destination, String type)
			throws SSLException, IOException, InterruptedException;

	MultipleResponse login(LoginRequest request);

	int sendSmsPushNotifications(String userToken, String source, Sms savedSms, long unreadCount, String deviceType);

	// User blockOrUnblock(BlockOrUnblockRequest blockOrUnblockRequest);

	DashboardResponse getDashboardData(IDRequest iDRequest);

	// User socialSignup(SocialSignupRequest request);

	MultipleResponse socialSignUp(SocialSignupRequest request);

	User twoFaEmailOtp(SendOtp request);

	List<LoginDevice> logout(IDRequest request);

	User signupWordpress(WordpressLoginRequest request);

	List<User> getAllUsersByIds(Set<Long> ids);

	User changePassword(ChangePassword request);

	User changeMail(ChangeMail request);

	MultipleResponse otpVerificationForEmail(OtpVerificationForEmailRequest request);

	//User sipCall(SipCallPayload request);

	String sipCall(String to, String from);

	 List<UserContactNoBlock> userContactNoBlock(UserContactNoBlockPayload request);

	List<UserContactNoBlock> getUserContactNoBlock(IDRequest request);

	List<String> checkExistsContact(UserContactNoBlockPayload request);

	List<String> deleteUserContactNoBlock(UserContactNoBlockPayload request);

	String getAllUsersByIds(PushNotificationPayload request);

	String getAllUsersByIdsEmail(PushNotificationPayload request);

	MultipleResponse verifyOtpforWordPress(OtpVerificationForEmailRequest request);

	List<User> encryptAllUsersPassword();

	List<User> getAllUsers();

	MultipleResponse wordpressLogin(String email, String password);





	

}