package com.example.common.request.dto;
/**
 * 
 * @author Vamshi Krishna
 * @version 1.0
 * @since 30/11/2022
 */

public enum HdfcPaymentMode {

	CREDITCARD(2, "OPTCRDC", "CRDC"),DEBITCARD(7, "OPTDBCRD", "DBCRD"),NETBANKING(6, "OPTNBK", "NBK"),UPI(5, "OPTUPI", "UPI"),WALLET(8, "OPTWLT", "WLT");
	
	private int paymentMode;
	private String paymentOption;
	private String cardType;
	
	private HdfcPaymentMode(int paymentMode, String paymentOption, String cardType) {
		this.paymentMode = paymentMode;
		this.paymentOption = paymentOption;
		this.cardType = cardType;
	}


	public int getPaymentMode() {
		return paymentMode;
	}


	public void setPaymentMode(int paymentMode) {
		this.paymentMode = paymentMode;
	}


	public String getPaymentOption() {
		return paymentOption;
	}


	public void setPaymentOption(String paymentOption) {
		this.paymentOption = paymentOption;
	}


	public String getCardType() {
		return cardType;
	}


	public void setCardType(String cardType) {
		this.cardType = cardType;
	}


	public static HdfcPaymentMode getPaymentCodeByPaymentMode(int paymentModeId) {
		for (HdfcPaymentMode paymentMode : values()) {
			if (paymentMode.paymentMode == paymentModeId) {
				return paymentMode;
			}
		}
		return CREDITCARD;
	}
	
}
