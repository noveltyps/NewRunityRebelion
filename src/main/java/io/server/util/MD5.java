package io.server.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5 {

	private static MessageDigest digest;

	static {
		try {
			digest = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
	}

	public static String hash(String text) {

		byte[] hash;

		try {

			synchronized (digest) {
				digest.reset();
				digest.update(text.getBytes("iso-8859-1"), 0, text.length());
				hash = digest.digest();
			}

		} catch (UnsupportedEncodingException e) {
			return "";
		}

		StringBuilder buf = new StringBuilder();
		for (byte hashByte : hash) {
			int halfbyte = (hashByte >>> 4) & 0x0F;
			for (int i = 0; i < 2; i++) {
				if (0 <= halfbyte && halfbyte <= 9) {
					buf.append((char) ('0' + halfbyte));
				} else {
					buf.append((char) ('a' + (halfbyte - 10)));
				}
				halfbyte = hashByte & 0x0F;
			}
		}
		return buf.toString();
	}

}
