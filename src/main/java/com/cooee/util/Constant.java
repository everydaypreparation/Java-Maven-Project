package com.cooee.util;

import org.springframework.beans.factory.annotation.Value;

public class Constant {

//------------------------USERNAME AND PASSWORD FOR MAIL VERFICATION------------------
//public static final String EMAIL_ADDRESS = "onebrush.app@gmail.com";
//public static final String PASSWORD = "Onebrush@123";
//---------------------------------------END------------------------------------------
//
public static final String EMAIL_ADDRESS = "admin@callcooee.com";//"sonam.kushwah@advantal.net";
public static final String PASSWORD = "Ma.ioHp^C4FK";//


	
public static final String AUTHENTICATION_KEY = "a22f96db8bddb95ad0dc60dad56aaed6";//ONEBRUSHSECURITYKEY
// public static final String KEY16BYTE = "advantaladvantal";//AESCipher Key16Byte key for Auth
public static final String KEY16BYTEFRONT = "advantaladvantal2";//AESCipher Key16Byte key for Auth2
public static final String AES_ENCRYPTION_KEY = "advantaladvantal";//AESCipher Key16Byte

// --------------------------------------OTHER CONSTATNT-----------------------------------
public static final Boolean FALSE = false;
public static final Boolean TRUE = true;
public static final String ACTIVATE = "activate";
public static final String TYPE_ACTIVATE = "kHJIqVoRke6QPOT/nSXKrw==";

// for developement profile
// public static final String FORGETPASSWORD_ADMIN_TEMPLATE_VIEW_PAGELINK ="http://36.255.3.15:8086/onebrushalpha/#/admin/reset-password?";
// public static final String FORGETPASSWORD_TEMPLATE_VIEW_PAGELINK ="http://36.255.3.15:8086/onebrushalpha/#/user/reset-password?";
// public static final String FORGETPASSWORD_EXPIREPAGE_TEMPLATE_PAGELINK ="http://36.255.3.15:8086/onebrushalpha/#/expire-page?";
// public static final String VERIFICATION_LINK ="api/customer/user_verification_mail?";
// public static final String REVERIFICATION_LINK ="api/customer/resend_user_verification_mail?";
// public static final String FORGETPASSWORD_OPERATION_LINK ="api/customer/userForgetPasswordOperation?";
// public static final String FORGET_PASSWORD_VERIFICATION_LINK_API ="api/admin/forget_password_verification?";
// public static final String FORGET_PASSWORD_CUSTOMER_VERIFICATION_LINK_API ="api/customer/forget_password_verification?";
// public static final String USER_LOGIN_VERIFICATION_LINK ="api/customer/user_login_verification?";

public static final String FORGETPASSWORD_ADMIN_TEMPLATE_VIEW_PAGELINK ="http://36.255.3.15:8086/ONEBRUSH/#/admin/reset-password?";
public static final String FORGETPASSWORD_TEMPLATE_VIEW_PAGELINK ="http://36.255.3.15:8086/ONEBRUSH/#/user/reset-password?";
public static final String FORGETPASSWORD_EXPIREPAGE_TEMPLATE_PAGELINK ="http://36.255.3.15:8086/ONEBRUSH/#/expire-page?";
public static final String VERIFICATION_LINK ="api/customer/user_verification_mail?";
public static final String REVERIFICATION_LINK ="api/customer/resend_user_verification_mail?";
public static final String FORGETPASSWORD_OPERATION_LINK ="api/customer/userForgetPasswordOperation?";
public static final String FORGET_PASSWORD_VERIFICATION_LINK_API ="api/admin/forget_password_verification?";
public static final String FORGET_PASSWORD_CUSTOMER_VERIFICATION_LINK_API ="api/customer/forget_password_verification?";
public static final String USER_LOGIN_VERIFICATION_LINK ="api/customer/user_login_verification?";
//public static final String FORGETPASSWORD_ADMIN_TEMPLATE_VIEW_PAGELINK ="api/admin/view_forgetPasswordTemplate?";
//public static final String FORGETPASSWORD_TEMPLATE_VIEW_PAGELINK ="api/customer/view_forgetPasswordTemplate?";

// for local profile
// public static final String FORGETPASSWORD_ADMIN_TEMPLATE_VIEW_PAGELINK ="http://localhost:8080/api/admin/view_forgetPasswordTemplate?";
// public static final String FORGETPASSWORD_TEMPLATE_VIEW_PAGELINK ="http://localhost:8080/api/customer/view_forgetPasswordTemplate?";
// public static final String FORGETPASSWORD_OPERATION_LINK ="http://localhost:8080/api/customer/userForgetPasswordOperation";
// public static final String VERIFICATION_LINK ="http://localhost:8080/api/customer/user_verification_mail?";
// public static final String REVERIFICATION_LINK ="http://localhost:8080/api/customer/resend_user_verification_mail?";


public static final String REGISTRATION_VERIFICATION_LINK = "kHJIqVoRke6QPOT/nSXKrw==";
public static final String FORGETPASSWORD_VERIFICATION_LINK = "kHJIqVoRkelskdkejdk6QPOT/nSXKrw==";
public static final String LOGIN_VERIFICATION_LINK = "kHJIqVoRkelskdkejdsffdgdfdfhfdk6QPOT/nSXKrw==";
   
//public static final String VERIFY_ACCOUNT_EMAIL_TEMPLATE_IMAGE = "http://36.255.3.15:8086/onebrushdentinostic-api/resources/verify-account.png";
//public static final String VERIFY_ACCOUNT_EMAIL_TEMPLATE_IMAGE = baseUrl+"resources/verify-account.png";
//public static final String EMAIL_VERIFY_TEMPLATE_IMAGE = "http://36.255.3.15:8086/onebrush-images/emailverify.png";
//public static final String EMAIL_VERIFY_TEMPLATE_IMAGE = imgUrl+"emailverify.png";
//public static final String LOGO_TEMPLATE_IMAGE = "http://36.255.3.15:8086/onebrush-images/logo.png";
//public static final String LOGO_TEMPLATE_IMAGE = imgUrl+"logo.png";
//public static final String RESET_PASS_EMAIL_TEMPLATE_IMAGE = "http://36.255.3.15:8086/onebrushdentinostic-api/resources/reset-password.png";
//public static final String RESET_PASS_EMAIL_TEMPLATE_IMAGE = baseUrl+"resources/reset-password.png";

public static final String IOS_KEY_ID = "WA2US7X5M9";
public static final String IOS_TEAM_ID = "NEZFP8VAHR";
// public static final String BASE_LINK = "http://localhost:4200/#/admin/reset-password?";
// public static final String BASE_LINK2 = "http://localhost:4200/#/admin/verify-user-account?";
//public static final String BASE_LINK = "http://36.255.3.15:8086/ONEBRUSH/#/admin/reset-password?";
public static final String TYPE_VERIFY = "TscjucFm3GPKE9K9G/dfrw==";
public static final String TYPE_VERIFIED = "nt7QAJvvcFsoYFONe964xw==";
public static final String VALID = "ufjSeCyE8rx4b7Adecswqg==";
public static final String INVALID = "A91gv+t+QABPnMhuOli2xQ==";
public static final int ZERO = 0;
public static final int ONE = 1;
public static final int TWO = 2;
public static final int THREE = 3;
//--------------------------------------END------------------------------------------

//------------------------------------STATUS CODE------------------------------------
public static final String CREATE = "201";
public static final String OK = "200";
public static final String BAD_REQUEST = "400";
public static final String NOT_AUTHORIZED = "401";
public static final String FORBIDDEN = "403";
public static final String WRONGEMAILPASSWORD = "402";
public static final String NOT_FOUND = "404";
public static final String SERVER_ERROR = "500";
public static final String DB_CONNECTION_ERROR = "502";
public static final String ENCRYPTION_DECRYPTION_ERROR = "503";
public static final String NOT_EXIST = "405";

public static final String CONFLICT  = "409";
public static final String UNPROCESSABLE_ENTITY = "422";
//--------------------------------------END---------------------------------------

//----------------------------------RESPONSE KEY----------------------------------
public static final String INVALID_REQUEST = "Invalid request!";//
public static final String UNAUTHRISED = "UNAUTHRISED";//
public static final String WRONG_INPUT_DATA = "Wrong input data!";//
public static final String RESPONSE_CODE = "responseCode";
//public static final String STATUS = "status";
public static final String SUCCESS = "SUCCESS";
public static final String ERROR = "ERROR";
public static final String BLOCKED = "Blocked";
public static final String NOT_VERIFIED = "Not verified";
public static final String VERIFY_ACCOUNT = "Verify Account";
public static final String RESET_PASSWORD = "Reset Password";
public static final String AUTH_KEY = "authKey";
public static final String MESSAGE = "message";
public static final String DATA = "data";
public static final String AUTH_ID = "authId";
public static final String TOTAL_USER = "totalUser";
public static final String ISVALID = "isValid";

public static final String URL = "url";
public static final String OTP = "otp";
public static final String INVALID_REQ = "Invalid req.!";
public static final String LIST = "list";

public static final String TOTAL_ADMIN = "totalAdmin";
public static final String VERIFFY = "verify";
public static final String VERIFIED ="verified";
public static final String RESET = "reset";
public static final String EMAIL = "email";
//--------------------------------------END---------------------------------------

//--------------------------------SERVER MESSAGES-----------------------------------
public static final String ERROR_MESSAGE = "Please try again";//
public static final String SERVER_MESSAGE = "Technical issue";//
public static final String DATA_NOT_FOUND_MESSAGE = "No record found!";//
public static final String USERACCOUNT_ALREADY_BLOCK = "user account already block";//
public static final String DATA_FOUND_MESSAGE = "Record found!";
public static final String DATA_ALREADY_DELETED_MESSAGE = "Data already deleted!";//
public static final String DATA_DELETED_MESSAGE = "Data deleted Successfully!";//

public static final String NOT_FOUND_MESSAGE = "That record you want to find for update, which is not found!";//
public static final String BAD_REQUEST_MESSAGE = "Bad request!";//onebrush
public static final String SOME_FIELD_MISSING = "Some Fields are misssing!";//onebrush
public static final String FORBIDDEN_REQUEST = "Access denied";//onebrush
public static final String LINK_EXPIRED = "Link expired,Please try again!";
public static final String PASSWORD_CHANGE_SUCCESS_MESSAGE = "Password changed successfully!.";
public static final String PASSWORD_CHANGE_FAILD_MESSAGE = "Password not updated successfully! you can try again.";
public static final String OLD_PASSWORD_NOT_MATCHED_MESSAGE = "Your old password not matched! please check.";
public static final String ADMIN_NOT_UPDATE_MESSAGE = "Admin not updated successfully!.";
public static final String VALID_EMAIL_ADDRESS_MESSAGE = "Valid email address!";
public static final String INVALID_EMAIL_ADDRESS_MESSAGE = "The email address you entered does not exist on the Internet. Please check and correct it.";
public static final String VERIFICATION_LINK_ALREADY_SENT_MESSAGE = "Verification link already been sent on your email! please verify first.";
//-------------------------------------END------------------------------------


   
//-----------------------------------USER MESSAGES-----------------------------------  
public static final String USER_UPDATED_SUCCESS_MESSAGE = "User updated successfully";//onebrush
public static final String USER_FEEDBACK_SUCCESS_MESSAGE = "Your feedback submitted successfully!";
public static final String USER_FEEDBACK_FAILED_MESSAGE = "Invalid attempte, please try again!";
  public static final String ACCOUNT_BLOCKED = "Account blocked, please contact to Admin ";
  public static final String ACCOUNT_NOT_VERIFIED = "User account not verified, please verify first";
  public static final String ACCOUNT_NOT_UPDATED = "Invalid attempte, please try again to update your profile!.";
  public static final String USER_CREATED_SUCCESS_MESSAGE = "Registered successfully.";
  public static final String RESEND_MAIL_SUCCESS_MESSAGE = "We have sent a link on your mail please verify your email Id.";
  public static final String VERIFY_SUCCESS_MESSAGE = "Congratulation! Your account verified successfully.";
  public static final String EMPTY = "";
  public static final String ACCOUNT_NOT_EXIST_MESSAGE = "Account not exist!";
  public static final String ACCOUNT_ALREADY_EXIST_MESSAGE = "User account already exist in database!";
public static final String EMAIL_ALREADY_EXIST_MESSAGE = "This email-id already exists in the database!";
  public static final String INVALID_CREDENTIAL = "Invalid credential!";
  public static final String PATIENT_UPDATED_SUCCESS_MESSAGE = "patient updated successfully";
    public static final String PATIENT_CREATED_SUCCESS_MESSAGE = "patient  save successfully.";
    public static final String PATIENT_ACCOUNT_NOT_UPDATED = "Invalid attempte, please try again to update your patient profile.";
    public static final String FORGOT_PASSWORD_FAILD_MESSAGE = "We could not verify given email,Please check email and try again!";
    public static final String ACCOUNT_DISABLE_MESSAGE = "Account is temporary disabled.!";
    public static final String FORGOT_LINK_SENT_SUCCESS_MESSAGE = "We have sent reset password link on your registered email,Link will be expire in 24 hours";
    public static final String ACCOUNT_DISABLED_SUCCESS_MESSAGE = "Account disabled successfully.";
public static final String USER_VERIFY_MESSAGE = "We have sent a link on your mail please verify your email Id.";
public static final String USER_DELETED_SUCCESS_MESSAGE = "User deleted successfully";
public static final String PATIENT_DELETED_SUCCESS_MESSAGE = "patient deleted successfully";
public static final String USER_ALREADY_DELETED_MESSAGE = "User already deleted!";
public static final String ACCOUNT_ENBALED_SUCCESS_MESSAGE = "Account enabled successfully.";
public static final String CASES_ADDED = "Cases Added Successfully!";
public static final String USER_ALREADY_VERIFIED_MESSAGE = "Email already verified!";
public static final String USER_NOT_VERIFIED_MESSAGE = "Email not verified!";
public static final String UUID_NOT_EXIST = "UUId not exist";
public static final String LOGIN_MAIL_SUCCESS_MESSAGE = "Your email-id has not been verified! We have sent a link on your mail please verify your email-id first.";
  public static final String CASE_REGISTERED_SUCCESS_MESSAGE = "Case registered successfully.";
//----------------------------------------END------------------------------------------

//------------------------------------ADMIN MESSAGES------------------------------------
public static final String LOGIN_FAILED = "Invalid email or password provided.";
public static final String LOGIN_SUCCESS = "Login successfully!.";
public static final String LOGOUT_SUCCESS = "Logout successfully!.";
public static final String LOGOUT_FAILED = "Logout failed!.";
public static final String USERNAME_EXIST_MESSAGE = "Username already in used.";
public static final String EMAIL_EXISTS_MESSAGE = "Please provide another Email, as this Email already exist!";
public static final String MOBILE_NO_EXIST_MESSAGE = "Mobile no already exist.";
public static final String ADMIN_ADDED_SUCCESS_MESSAGE = "Admin registered successfully!.";
public static final String ADMIN_ADDED_FAILED_MESSAGE = "Admin not registered successfully!.";
public static final String ADMIN_UPDATED_SUCCESS_MESSAGE = "Admin updated successfully!";
public static final String ADMIN_DELETED_SUCCESS_MESSAGE = "Admin deleted successfully.";
// public static final String ADMIN_DELETED_SUCCESS_MESSAGE = "Admin deleted successfully";

public static final String UPDATED_SUCCESS_MESSAGE = "Update successfully.";
//-----------------------------------------END----------------------------------------

//--------------------------------------------Welcome Carousel------------------------

public static final String WELCOME_CAROUSEL_ADDED_SUCCESS_MESSAGE = "Welcome Carousel Added successfully!!";

public static final String WELCOME_CAROUSEL_UPDATED_SUCCESS_MESSAGE = "Welcome Carousel updated successfully!!";

public static final String WELCOME_CAROUSEL_DELETED_SUCCESS_MESSAGE = "Welcome Carousel deleted successfully!!";

public static final String WELCOME_CAROUSEL_FILE_NOT_FOUND_MESSAGE = "Welcome Carousel File not Found!!";
//------------------------------------------END-------------------------------------

//------------------------------------Content-----------------------------------
public static final String CONTENT_ADDED_SUCCESS_MESSAGE = "Content Added successfully!!";
public static final String RECORD_ADDED_SUCCESSFULLY = "Record added successfully!";
public static final String RECORD_UPDATED_SUCCESSFULLY = "Record updated successfully!";
public static final String RECORD_NOT_UPDATED = "Record not updated ";

public static final String RECORD_ALREDY_EXIST = "Record already exist!";
 
public static final String FIREBASE_KEY="AAAAkdVBNNc:APA91bHmaqoo6C8GBfnF3vGwTuv7IPpzRDoMYqBMvTNTzAKWeN9WExIbjlSsLJPuh-eAebHkp7QonXFuqtZi-bLzxI55pHwnmi1jE_BfeIE9xvjNBtW0u6nr7NnDv-3WI5jgyUORqIM1";

public static final String CERT_PATH="/home/cooee_files/AuthKey_WA2US7X5M9.p8";// certificate path
//public static final String CERT_PATH="C:\\CooeeData\\AuthKey_WA2US7X5M9.p8";

public static final String CERT_TEAMID="NEZFP8VAHR";
public static final String CERT_KEYID="WA2US7X5M9";
public static final String CERT_BUNDALIDENTIFIRE="net.advantal.cooeeapp";
public static final String CERT_BUNDALIDENTIFIRE_VOIP="net.advantal.cooeeapp.voip";

public static final String ANDROID_DEVICE_TYPE = "android";
public static final String IOS_DEVICE_TYPE = "ios";
public static final String KYC_APPROVED_TITLE = "cooee";
public static final String KYC_REJECT_TITLE = "cooee";
public static final String NOTI_KYC_APPROVED = "NOTI_KYC_APPROVED";
public static final String NOTI_KYC_REJECTED = "NOTI_KYC_REJECTED";
public static final String KYC_APPROVED_MESSAGE = "Your kyc approved";
public static final String KYC_REJECT_MESSAGE = "Your kyc rejected";
public static final String STRIPE_API_KEY = null;
}