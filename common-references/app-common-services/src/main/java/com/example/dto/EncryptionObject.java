package com.example.dto;

import java.math.BigInteger;

import lombok.Data;

@Data
public class EncryptionObject {

	private BigInteger keyId;

	private String encryptedValue;

	private String encryptedCardRefValue;
	
	private String maskedValue;

}
