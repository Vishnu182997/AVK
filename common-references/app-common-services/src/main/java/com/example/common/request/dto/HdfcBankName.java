package com.example.common.request.dto;

public enum HdfcBankName {
	
	ANDHRABANK(1,"Andhra Bank"),ICICI(2,"ICICI"),AXIS(3,"Axis"),SYNDICATE(4,"Syndicate"),AMERICANEXPRESS(5,"American Express");

	private int bankId;
	private String bankName;
	
	private HdfcBankName(int bankId, String bankName) {
		this.bankId = bankId;
		this.bankName = bankName;
	}

	public int getBankId() {
		return bankId;
	}

	public void setBankId(int bankId) {
		this.bankId = bankId;
	}

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	
	public static HdfcBankName getBankNameByBankId(int bankId) {
		for (HdfcBankName bank : values()) {
			if (bank.bankId == bankId) {
				return bank;
			}
		}
		return ANDHRABANK;
	}
	
	
}
