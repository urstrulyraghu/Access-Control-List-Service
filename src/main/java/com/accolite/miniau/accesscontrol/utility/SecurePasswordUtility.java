package com.accolite.miniau.accesscontrol.utility;

import java.security.MessageDigest;
import org.apache.log4j.Logger;

public class SecurePasswordUtility {

	private static Logger logger = Logger.getLogger(SecurePasswordUtility.class);

	private SecurePasswordUtility() {

	}

	/* IMPORTANT* DONT CHANGE THE SALT */
	static final String SALT = "dvnewofq0i2i03202u4t34jgowdsmv";
	/* IMPORTANT* DONT CHANGE THE SALT */

	public static String securePassword(String passwordToHash) {
		String generatedPassword = null;
		// TODO change the Exception type
		try {
			MessageDigest md = MessageDigest.getInstance("SHA-512");
			md.update(SALT.getBytes("UTF-8"));
			byte[] bytes = md.digest(passwordToHash.getBytes("UTF-8"));
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < bytes.length; i++) {
				sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
			}
			generatedPassword = sb.toString();
		} catch (Exception e) {
			logger.error("Problem with Password Hashing function.");
		}

		return generatedPassword;
	}
}