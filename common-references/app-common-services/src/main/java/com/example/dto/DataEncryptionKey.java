package com.example.dto;

import java.math.BigInteger;

import lombok.Data;

@Data
public class DataEncryptionKey {
	
	private BigInteger keyId;
	
	private String encryptedKey;
	
	private boolean status;
	
	private String plainKey;
	
	

}
