package com.example.common.request.dto;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

public class HdfcEncryption {

	public static void main(String[] args) {
		String hashString = "AVPP39JJ67AW61PPWA|MGS07DEC22A0135-Mu4XdGGcVJ|1471272|311009005057|985FC94BFD1E6B52E0B8B94580FB6BFE";
		String hashString1 = "333a2b8b7eceb3d3ec9e0482083ca59ee3af67b298a672b04dd74fbb3d4cec3b";
		String workingKey = "985FC94BFD1E6B52E0B8B94580FB6BFE";
		try {
			System.out.println("Hash value " + generateHash(hashString, workingKey));
		} catch (Exception e) {
			System.out.println("Error");
		}
	}
	public static String generateHash(String originalString, String workingKey) throws Exception {
		String hash = "";
		byte[] encodedhash = null;
		MessageDigest digest = MessageDigest.getInstance("SHA-256");
		digest.update(workingKey.getBytes());
		encodedhash = digest.digest(originalString.getBytes(StandardCharsets.UTF_8));
		hash = bytesToHex(encodedhash);
		return hash;
	}

	private static String bytesToHex(byte[] hash) throws Exception {
		StringBuilder hexString = new StringBuilder(2 * hash.length);
		for (int i = 0; i < hash.length; i++) {
			String hex = Integer.toHexString(0xff & hash[i]);
			if (hex.length() == 1) {
				hexString.append('0');
			}
			hexString.append(hex);
		}
		return hexString.toString();
	}
}
