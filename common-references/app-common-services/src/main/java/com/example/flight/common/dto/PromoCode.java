package com.example.flight.common.dto;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class PromoCode implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3297465978181748323L;

	private String code;
	private String netRemitCode;
	private String tourCode;
	private String airline; //supplier when promo code exist
	private String ignoredAirlines;//Prohibited carrier
	private String fareType;
	private String includeAirlines;
	private String companyDealCodeId;
	@JsonProperty(value = "isGstMandatory")
	private boolean isGstMandatory;
	private String pcc;
	private PromoCodeGst promoCodeGst;
	private List<PromoCodeGst> listPromoCodeGst;
}
