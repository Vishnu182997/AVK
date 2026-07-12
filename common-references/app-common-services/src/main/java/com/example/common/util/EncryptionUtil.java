package com.example.common.util;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyException;
import java.security.NoSuchAlgorithmException;
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
		
		String secretKey = "$5502a01b1d573d27c9d5d1467dcdc59bceb69c286d3%&^%$#@!*";
		String plainText = "4111111111111111|ICICI Bank|Pankaj Kumar Pradhan|07|2023|VISA|123";
		
		EncryptionUtil aes = new EncryptionUtil();
		
		String encryptStr = aes.encrypt(plainText,secretKey);
		
		String decryptStr = aes.decrypt(encryptStr,secretKey);
		
		
		System.out.println("plainText : "+plainText);
		System.out.println("encryptStr : "+encryptStr);
		System.out.println("decryptStr : "+decryptStr);
	}
	



/////////C# .net#########################
//using System;
//using System.Collections.Generic;
//using System.Linq;
//using System.Security.Cryptography;
//using System.Text;
//using System.Threading.Tasks;
//
//
//    public class Program
//    {
//       public static void Main(string[] args)
//        {
//            Program p = new Program();
//            string SecretKey = "$5502a01b1d573d27c9d5d1467dcdc59bceb69c286d3%&^%$#@!*"; // this is common key in java and .net
//            string encryptedString = p.Encrypt("Data to encrypt", SecretKey);
//            string decryptedString = p.Decrypt(encryptedString, SecretKey);
//            Console.WriteLine("Encrypted String: " + encryptedString);
//            Console.WriteLine("Decrypted string: " + decryptedString);
//        }
//
//        // Encrypts plaintext using AES 128bit key and a Chain Block Cipher and returns a base64 encoded string
//        public string Encrypt(String plainText, String key)
//        {
//            var plainBytes = Encoding.UTF8.GetBytes(plainText);
//            return Convert .ToBase64String(Encrypt(plainBytes, GetRijndaelManaged(key)));
//        }
//        public string Decrypt(String encryptedText, String key)
//        {
//            var encryptedBytes = Convert.FromBase64String(encryptedText);
//            return Encoding .UTF8.GetString(Decrypt(encryptedBytes, GetRijndaelManaged(key)));
//        }
//        public byte[] Encrypt(byte[] plainBytes, RijndaelManaged rijndaelManaged)
//        {
//            return rijndaelManaged.CreateEncryptor()
//                .TransformFinalBlock(plainBytes, 0, plainBytes.Length);
//        }
//        public byte[] Decrypt(byte[] encryptedData, RijndaelManaged rijndaelManaged)
//        {
//            return rijndaelManaged.CreateDecryptor()
//                .TransformFinalBlock(encryptedData, 0, encryptedData.Length);
//        }
//        public RijndaelManaged GetRijndaelManaged(String secretKey)
//        {
//            var keyBytes = new byte[16];
//            var secretKeyBytes = Encoding.UTF8.GetBytes(secretKey);
//            Array .Copy(secretKeyBytes, keyBytes, Math .Min(keyBytes.Length, secretKeyBytes.Length));
//            return new RijndaelManaged
//            {
//                Mode = CipherMode.CBC,
//                Padding = PaddingMode.PKCS7,
//                KeySize = 128,
//                BlockSize = 128,
//                Key = keyBytes,
//                IV = keyBytes
//            };
//        }

}
