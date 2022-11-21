package com.cooee.util;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Random;

public class IUtil {

	public static int getSixDigitRandomNumbers() {
		int response = (int) ((Math.random() * 900000) + 100000);
		return response;
	}

	public static String getRandomNumberString() {
		// It will generate 6 digit random Number.
		// from 0 to 999999
		Random rnd = new Random();
		int number = rnd.nextInt(999999);
		// this will convert any number sequence into 6 character.
		return String.format("%06d", number);
	}

	public static String randomPassword(int len) {
		String Capital_chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
		String Small_chars = "abcdefghijklmnopqrstuvwxyz";
		String numbers = "0123456789";
		String symbols = "@$";//"!@#$%^&*_=+-/.?<>)";

		String values = Capital_chars + Small_chars + numbers + symbols;

		Random rndm_method = new Random();

		char[] password = new char[len];

		for (int i = 0; i < len; i++) {
			// Use of charAt() method : to get character value
			// Use of nextInt() as it is scanning the value as int
			password[i] = values.charAt(rndm_method.nextInt(values.length()));

		}
		return String.valueOf(password);
	}

	public static Boolean checkOtpExpire(Timestamp otpCreationTime) {
//		int addMinuteTime = 10;
//		Date old = Calendar.getInstance().getTime();
//		System.out.println("Before Adding : " + old);
//		Date newTime = DateUtils.addMinutes(old, addMinuteTime); // add minute
//		System.out.println("After adding targetTime : " + newTime);
//		if (newTime.compareTo(old) > 0) {
//
//			return true;
//		}

		// Vishal sir code
		Timestamp currentDT = new Timestamp(System.currentTimeMillis());

		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(currentDT.getTime());

		long milliseconds = currentDT.getTime() - otpCreationTime.getTime();
		int seconds = (int) milliseconds / 1000;

		int minutes = (seconds % 3600) / 60;

		if (minutes < 10) {
			return true;
		}

		return false;

	}

	public static String getTrimParam(String param) {
		if (param != null)
			return param.trim();
		else
			return "";
	}

	public static String convertEmptyParam(String param) {
		if (param != null) {
			if (param.trim().equals(""))
				return "NA";
			else
				return param.trim();
		} else {
			return "NULL";
		}
	}

	public static String convertSrcAndDstEmptyParam(String param) {
		if (param != null) {
			if (param.trim().equals(""))
				return "NA";
			else {
				if (param.length() > 10) {
					String res = (param.substring(9));
					return res;
				} else {
					return param.trim();
				}
			}
		} else {
			return "NULL";
		}
	}

	public static String convertSecToHourMinSec(Integer seconds) {
		if (seconds != null) {
			int p1 = seconds % 60;
			int p2 = seconds / 60;
			int p3 = p2 % 60;
			p2 = p2 / 60;

			String outputString = "";

			if (p2 == 0 && p3 == 0)
				outputString = (p1 + "s");
			else if (p2 == 0 && p3 != 0)
				outputString = (p3 + "m:" + p1 + "s");
			else
				outputString = (p2 + "h:" + p3 + "m:" + p1 + "s");
			return outputString;
		} else
			return "NULL";
	}
}