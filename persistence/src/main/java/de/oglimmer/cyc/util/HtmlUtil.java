package de.oglimmer.cyc.util;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import lombok.SneakyThrows;

public class HtmlUtil {

	@SneakyThrows(value = NoSuchAlgorithmException.class)
	public static String calcHtmlSafeRepresentation(String str) {
		MessageDigest md = MessageDigest.getInstance("MD5");
		md.reset();
		md.update(str.getBytes());
		byte[] digest = md.digest();
		BigInteger bigInt = new BigInteger(1, digest);
		String hashtext = bigInt.toString(16);
		return hashtext;
	}

}
