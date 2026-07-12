package com.example.dto;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import com.example.flight.common.dto.Agency;
import com.example.flight.common.dto.PromoCode;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class HotelApiCredential implements Serializable {
	/**
	*
	*/
	private static final long serialVersionUID = -6470897585542433619L;
	private String requestUrl;
	private String apiType;
	private String userName;
	private String password;
	private String apiKey; // Give pseudoCityCode here
	private String userIp;
	private String extraParam;// added for Amadeus Hotels
	private String agencyName;
	private String userId;
	private Integer productId; // ET-305
	private BigInteger supplierId;
	private String supplierName;
	private BigInteger credentialId;
	private String transactionUserName;
	private String transactionPassword;
	private String organizationId;
	private String includeFareTypes;
	private BigInteger legalEntityId;
	private BigInteger hotelId;
	private PromoCode promoCode;// added for deal codes
	private List<PromoCode> promoCodes = new ArrayList<PromoCode>();

	private String baseCurrency;// added for multi currency.

	private String baseCurrencyIcon;

	private double baseCurrencyExchangeRate;

	private BigInteger baseCurrencyId;

	private String supplierCurrency;

	private String destinationCurrency;

	private String destinationCurrencyIcon;

	private double destinationCurrencyExchangeRate;

	private BigInteger destinationCurrencyId;
	private Agency agency;

	private String addressLine1;
	private String addressLine2;
	private String cityName;
	private String stateName;
	private String postalCode;
	private String countryCode;
	private String panNumber;
	private String companyName;
	private List<HotelNameCodes> domIncExcHotels;
//	private List<HotelNameCodes> IntIncExcHotels;
	private int includeType;
	private Boolean isHotelCacheEnabled = false;
	private String paymentTypes;
	private String postPayExchangeRate;
	private Integer showOfferedFare;
	private Integer supplierAggregatorType;

	private String agentName;

	private BigInteger extTMCCompanyId;

	private String extTMCCompanyCode;

	private Integer disableThirdpartyInvoice;
	private String collectAllPolicyViolations;
	
}
