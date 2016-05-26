package net.raysforge.commons;

import java.security.MessageDigest;

public class MD5Utils {
	public static MessageDigest getMD5MessageDigest() {
		return HashUtils.getMessageDigest("MD5");
	}

	public static String getMD5Hash(String s) {
		return HashUtils.getHash("MD5", s);
	}

	public static String getMD5Hash(byte b[]) {
		return HashUtils.getHash("MD5", b);
	}
}
