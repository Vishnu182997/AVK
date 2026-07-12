package com.example.flight.common.dto;

import java.io.Serializable;
import java.math.BigInteger;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
@Data
public class PromoCodeGst implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 202396986514569407L;
	private String companyName;
	private String gstNumber;
	private String contactNumber;
	private String companyAddress1;
	private String companyAddress2;
	private String emailId;
	private String city;
	private String stateName;
	private String stateCode;
	private String postCode;
	private String countryId;
	private String countryCode;
	private String panNumber;
	@JsonProperty("is_SEZ")
	private Boolean isSEZ;
	@JsonProperty("SEZ_Type")
	private Integer sezType;
	private Boolean isGSTApplicableOnManagementFee;
	private Boolean passGSTDetailsToAirline;
	private BigInteger locationId;
	private Boolean isGSTEnable;
	private Integer gstType;
}
