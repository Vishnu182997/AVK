package com.example.common.request.dto;

public enum HdfcCardType {
	AMERICANEXPRESS("American Express","Amex"), VISA("VISA", "Visa"), MASTERS("Masters", "MasterCard"),
	RUPAY("RUPAY", "RuPay"), Diners("Diners", "Diners Club"), CTAVisa("CTA Visa", "Visa"), CTAMaster("CTA Master", "MasterCard");

	private String evaCardName;
	private String hdfcCardName;

	private HdfcCardType(String evaCardName, String hdfcCardName) {
		this.evaCardName = evaCardName;
		this.hdfcCardName = hdfcCardName;
	}

	public String getEvaCardName() {
		return evaCardName;
	}

	public void setEvaCardName(String evaCardName) {
		this.evaCardName = evaCardName;
	}

	public String getHdfcCardName() {
		return hdfcCardName;
	}

	public void setHdfcCardName(String hdfcCardName) {
		this.hdfcCardName = hdfcCardName;
	}
	
	public static HdfcCardType getCardNameByEvaCardName(String evaCardName) {
		for (HdfcCardType cardType : values()) {
			if (cardType.evaCardName.equalsIgnoreCase(evaCardName)) {
				return cardType;
			}
		}
		return VISA;
	}

}
