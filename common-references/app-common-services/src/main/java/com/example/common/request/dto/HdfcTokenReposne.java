package com.example.common.request.dto;

import lombok.Data;

@Data
public class HdfcTokenReposne {
	public String amount;
	public String bankRefNumber;
	public String cardName;
	public String currency;
	public String issuingBank;
	public String maskedCardNumber;
	public Object merchantParam1;
	public String merchantParam2;
	public String merchantParam3;
	public String merchantParam4;
	public String merchantParam5;
	public String orderId;
	public String paymentMode;
	public String regId;
	public String statusCode;
	public String status;
	public String statusMessage;
	public String trackingId;
	public String tokenRefNumber;
	public String tokenNumber;
	public String tokenExpiry;
	public String responseHash;
}
