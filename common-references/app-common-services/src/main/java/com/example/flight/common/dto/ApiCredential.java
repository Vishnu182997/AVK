package com.example.flight.common.dto;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.Data;

@Data
public class ApiCredential implements Serializable {
	private static final long serialVersionUID = -6470897585542433619L;

	private String requestUrl;
	private String userId;// tbo Hotels
	private String apiType;
	private String userName;
	private String password;
	private String apiKey; // Give pseudoCityCode here
	private String userIp;

	private PromoCode promoCode;// Ignore tbo for hotels

	private Agency agency;// Ignore for tbo hotels

	private String provider; // 1G, ACH, Indigo

	private String extraParam;

	private String endPointUrl;

	private int sendCreditCard;

	private String tourCode;

	private List<AirlineTourCode> airlineTourCodes;
	
	private List<RenamedFareTypeObject> fareTypes;

	private String supplierId;

	private String includeFareTypes;

	private String includeBookingClasses;

	private Map<String, Object> evaReferences = new HashMap<>();

	private String supplierName; // ET-276

	private Long productId; // ET-302

	private BigInteger credentialId;

	private String credentialName;

	private String authToken;// Sabre

	private String ticketingApiKey;

	private String baseCurrency;

	private String baseCurrencyIcon;

	private double baseCurrencyExchangeRate;

	private BigInteger baseCurrencyId;

	private String supplierCurrency;

	private String destinationCurrency;

	private String destinationCurrencyIcon;

	private double destinationCurrencyExchangeRate;

	private BigInteger destinationCurrencyId;
	
	private String includeAirlines;
	
	private String ignoredAirlines;
	
	//sabre enhanced airbook 
	private String conversationId;
	
	private List<RbdsAirline> includeAirlinesList;
	
	private List<RbdsAirline> excludeAirlinesList;
	
	private boolean enableCache;
	
	private BigInteger companyId;
	
	private Integer cacheExpiryTime;
	
	private boolean dobMandatory; //For Some suppliers DOB is mandatory  (like Amadeus USA)
	
	private String supplierApiUrl; //For Amadeus supplier API Url
	private Integer dedupeFlight;
	
	private String agentName;
}
