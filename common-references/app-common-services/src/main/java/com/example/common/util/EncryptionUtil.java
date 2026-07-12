package com.example.common.util;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class EncryptionUtil {

	private final static String characterEncoding = "UTF-8";
	private final static String cipherTransformation = "AES/CBC/PKCS5Padding";
	private final static String aesEncryptionAlgorithm = "AES";

	public static String encrypt(String plainText,String key) throws UnsupportedEncodingException, InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException{
//		String key = "$5502a01b1d573d27c9d5d1467dcdc59bceb69c286d3%&^%$#@!*";
		byte [] plainTextbytes = plainText.getBytes(characterEncoding);
	    byte [] keyBytes = getKeyBytes(key);
	    return Base64.getEncoder().encodeToString(encrypt(plainTextbytes,keyBytes, keyBytes));
	}

	public static String decrypt(String encryptedText,String key) throws KeyException, GeneralSecurityException, GeneralSecurityException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException, IOException{
//		String key = "$5502a01b1d573d27c9d5d1467dcdc59bceb69c286d3%&^%$#@!*";
		byte [] cipheredBytes = Base64.getDecoder().decode(encryptedText);
	    byte[] keyBytes = getKeyBytes(key);
	    return new String (decrypt(cipheredBytes, keyBytes, keyBytes), characterEncoding);
	}

	public static byte [] decrypt( byte[] cipherText, byte[] key, byte [] initialVector) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException
	{
	    Cipher cipher = Cipher.getInstance(cipherTransformation);
	    SecretKeySpec secretKeySpecy = new SecretKeySpec(key, aesEncryptionAlgorithm);
	    IvParameterSpec ivParameterSpec = new IvParameterSpec(initialVector);
	    cipher.init(Cipher .DECRYPT_MODE, secretKeySpecy, ivParameterSpec);
	    cipherText = cipher.doFinal(cipherText);
	    return cipherText;
	}

	public static byte[] encrypt(byte[] plainText, byte[] key, byte [] initialVector) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException
	{
	    Cipher cipher = Cipher.getInstance(cipherTransformation);
	    SecretKeySpec secretKeySpec = new SecretKeySpec(key, aesEncryptionAlgorithm);
	    IvParameterSpec ivParameterSpec = new IvParameterSpec(initialVector);
	    cipher.init(Cipher .ENCRYPT_MODE, secretKeySpec, ivParameterSpec);
	    plainText = cipher.doFinal(plainText);
	    return plainText;
	}

	public static byte[] getKeyBytes(String key) throws UnsupportedEncodingException{
	    byte [] keyBytes= new byte[16];
	    byte [] parameterKeyBytes= key.getBytes(characterEncoding);
	    System .arraycopy(parameterKeyBytes, 0 , keyBytes, 0, Math.min(parameterKeyBytes.length, keyBytes.length));
	    return keyBytes;
	}
	
	public static void main(String[] args) throws KeyException, GeneralSecurityException, IOException {
		/* 
		String secretKey = "$5502a01b1d573d27c9d5d1467dcdc59bceb69c286d3%&^%$#@!*";
		String plainText = "4111111111111111|ICICI Bank|Pankaj Kumar Pradhan|07|2023|VISA|123";
		EncryptionUtil aes = new EncryptionUtil();
		String encryptStr = aes.encrypt(plainText,secretKey);
		String decryptStr = aes.decrypt(encryptStr,secretKey);
		System.out.println("plainText : "+plainText);
		System.out.println("encryptStr : "+encryptStr);
		System.out.println("decryptStr : "+decryptStr);
		*/
		
		String secretKey = "DQaC2jN-MiuN1v3Q";
		String plainText = "zuBexaF9DkFQz1cY06TbUyxW6UgFB2oZSoxwYeLtxyiGvsZg68GTHbDkyqQFt4yM";
		String tripDecryptionLogic = createTripDecryptionLogic(plainText,secretKey);
		//System.out.println("tripDecryptionLogic : "+tripDecryptionLogic);
	}
	
	public static String createTripEncryptionLogic(String plainText, String secretKey) {
		try {
			// 1) Build key (raw 16-byte key from UTF-8 string)
			byte[] keyBytes = secretKey.getBytes(StandardCharsets.UTF_8);
			if (keyBytes.length != 16) {
				throw new IllegalArgumentException("AES-128 key must be 16 bytes");
			}
			Key key = new SecretKeySpec(keyBytes, aesEncryptionAlgorithm);

			// 2) Generate random 16-byte IV
			byte[] iv = new byte[16];
			SecureRandom secureRandom = new SecureRandom();
			secureRandom.nextBytes(iv);

			// 3) Encrypt plaintext with the generated IV
			Cipher cipher = Cipher.getInstance(cipherTransformation);
			cipher.init(Cipher.ENCRYPT_MODE, key, new IvParameterSpec(iv));
			byte[] ciphertext = cipher.doFinal(plainText.getBytes(StandardCharsets.UTF_8));

			// 4) Concatenate IV + ciphertext
			byte[] all = new byte[iv.length + ciphertext.length];
			System.arraycopy(iv, 0, all, 0, iv.length);
			System.arraycopy(ciphertext, 0, all, iv.length, ciphertext.length);

			// 5) Encode to Base64 and return
			return Base64.getEncoder().encodeToString(all);

		} catch (Exception ex) {
			// log and rethrow or return null as per your needs
			throw new RuntimeException("Encrypt failed", ex);
		}
	}

	public static String createTripDecryptionLogic(String encryptedBase64, String secretKey) {
		try {
			byte[] all = Base64.getDecoder().decode(encryptedBase64);

			// 1) Extract IV (first 16 bytes)
			byte[] iv = Arrays.copyOfRange(all, 0, 16);

			// 2) Extract ciphertext (rest)
			byte[] ciphertext = Arrays.copyOfRange(all, 16, all.length);

			// 3) Build key (raw 16-byte key from UTF-8 string)
			byte[] keyBytes = secretKey.getBytes(StandardCharsets.UTF_8);
			if (keyBytes.length != 16) {
				throw new IllegalArgumentException("AES-128 key must be 16 bytes");
			}
			Key key = new SecretKeySpec(keyBytes, aesEncryptionAlgorithm);

			// 4) Decrypt with the extracted IV
			Cipher cipher = Cipher.getInstance(cipherTransformation);
			cipher.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(iv));
			byte[] plain = cipher.doFinal(ciphertext);

			// 5) Convert to UTF-8 string
			return new String(plain, StandardCharsets.UTF_8);

		} catch (Exception ex) {
			// log and rethrow or return null as per your needs
			throw new RuntimeException("Decrypt failed", ex);
		}
	}


}
