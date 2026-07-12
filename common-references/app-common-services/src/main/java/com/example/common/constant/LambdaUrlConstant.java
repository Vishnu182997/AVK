
package com.example.common.constant;

public interface LambdaUrlConstant {
	
	
	// URL CONSTANT FOR FLIGHT STARTS -----------------------------------------
	
	public static String FLIGHT = "/flight";
	public static final String VERSION = "/v1";
	
	public static String PARENT_SEARCH = FLIGHT+"/parent-flight-search";
	public static String SEARCH_RESULT = FLIGHT+"/search-result";
	public static String SEARCH_FILTER = FLIGHT+"/search-filters";
	public static String RESOURCE_FARE_RULES = FLIGHT+"/fare-rules";
	public static String RESOURCE_MINI_FARE_RULES = "/v1/mini-fare-rules";
	public static String FARE_CHECK = FLIGHT+"/fare-check";
	public static String BOOK = FLIGHT+"/book";
	public static String VOICE_TO_TEXT = "/v1/voice-to-text";
	public static String RETRY_BOOK = "/v1/retry-book";
	
//	public static String PARENT_SEARCH = "/v1/parent-flight-search-dummy";
//	public static String SEARCH_RESULT = "/v1/search-result-dummy";
//	public static String SEARCH_FILTER = "/v1/search-filters-dummy";

	// url for External api"
	public static String EXTERNAL_SEARCH_API = "/v1/flight/search";
	
	public static String EXTERNAL_FARE_CHECK_API = VERSION+FLIGHT+"/farecheck";
	
	
	// URL CONSTANT FOR FLIGHT ENDS -----------------------------------------
	
	
	
	// URL CONSTANT FOR SETTING STARTS -----------------------------------------
	
	public static String SETTING = "/settings";
	
	public static String USERLIST = SETTING+"/users/list";
	public static String USERDETAILS = SETTING+"/users/get";
	public static String GET_USER_ORGANIZATION_PROFILE = SETTING+"/user-organization-profile/get";
	public static String UPDATE_USER_ORGANIZATION_PROFILE = SETTING+"/user-organization-profile/update";
	public static String USER_ADVANCE_SEARCH = "/v1/advance-search";
	public static String UPDATE_USER = SETTING+"/users/update";
	
	
	// LEGAL ENTITY IATA COMMISSIONS
	public static String GET_LEGAL_ENTITY_IATA_COMMISSION_LIST = SETTING+"/legal-entity-iata-commission/list";
	public static String CREATE_LEGAL_ENTITY_IATA_COMMISSION = SETTING+"/legal-entity-iata-commission/create";
	public static String UPDATE_LEGAL_ENTITY_IATA_COMMISSION = SETTING+"/legal-entity-iata-commission/update";
	public static String ENABLE_DISABLE_LEGAL_ENTITY_IATA_COMMISSION = SETTING+"/settings/legal-entity-iata-commission/enable";
	public static String GET_LEGAL_ENTITY_IATA_COMMISSION_AIRLINES = SETTING+"/legal-entity-iata-commission/airlines";
	public static String DELETE_LEGAL_ENTITY_IATA_COMMISSION = SETTING+"/legal-entity-iata-commission/delete";
	
	
	//IATA COMMISSIONS
	public static String GET_IATA_GROUP_COMMISSIONS_LIST = SETTING+"/iata-commissions/list";
	public static String UPDATE_IATA_GROUP_COMMISSIONS = SETTING+"/iata-commissions/add";
	public static String GET_IATA_GROUP_COMMISSIONS = SETTING+"/iata-commissions/get";
	public static String ENABLE_IATA_GROUP_COMMISSIONS = SETTING+"/iata-commissions/enable";
	public static String DISABLE_IATA_GROUP_COMMISSIONS = SETTING+"/iata-commissions/disable";
	
	
	// URL CONSTANT FOR SETTING ENDS -----------------------------------------
 
	//URL SUPPLIERS FOR LEGAL ENTITY SUPPLIERS
	public static String SETTINGS="/settings";
	public static String LEGAL_ENTITY_SUPPLIERS_LIST = SETTINGS+"/legal-entity-supplier/list";
	public static String LEGAL_ENTITY_SUPPLIER_DISABLE_ENABLE = SETTINGS+"/legal-entity-supplier/enable-disable";
	public static String LEGAL_ENTITY_SUPPILER_CREDENTIAL_LIST=SETTINGS+"/legal-entity-supplier/credential-list";
	public static String LEGAL_ENTITY_SUPPLIER_DEALCODE_LIST=SETTINGS+"/legal-entity-supplier/deal-code-list";
	public static String LEGAL_ENTITY_SUPPLIER_ADVANCE_PRICING_LIST=SETTINGS+"/v1/legal-entity-supplier-advance-pricing/list";

	
	//URL SUPPLIERS FOR COMPANY SUPPLIERS
	
	
	public static String COMPANY_SUPPLIERS_LIST=SETTINGS+"/suppliers/list";
	public static String UPDATE_COMPANY_SUPPLIER=SETTINGS+"/suppliers/update";
	public static String GET_COMPANY_SUPPLIER=SETTINGS+"/suppliers/get";
	public static String ACTIVE_COMPANY_SUPPLIER=SETTINGS+"/suppliers/enable";
	public static String INACTIVE_COMPANY_SUPPLIER=SETTINGS+"/suppliers/disable";
	
	//URL FOR GDS PROFILE
	public static String GET_GDS_PROFILE=SETTINGS+"/gds-profile/get";
	public static String GET_GDS_PROFILE_TYPE=SETTINGS+"/gds_profile_type";
	public static String UPDATE_GDS_PROFILE=SETTINGS+"/gds-profile/update";
	
	// URL FOR VISA-REQUIRED-DOCUMENTS
	public static String SAVEORUPDATE = "/v1/visa-required-documents/update";
	public static String GETVISADOCUMENTS = "/v1/visa-required-documents/get";
	public static String VISADOCUMENTSLIST = "/v1/visa-required-documents/list";
	public static String ENABLE = "/v1/visa-required-documents/enable";
	public static String DISABLE = "/v1/visa-required-documents/disable";
	
	// Url for Xray View Invoices
	public static String INVOICES_LIST = "/v1/invoices/list";
	public static String DOWNLOAD_ZIP_INVOICES = "/v1/invoices/downloadZipInvoice";
	public static String INVOICE_HISTORY = "/v1/invoices/history";
	
	
	public static String SEARCH_RESULT_EXTERNAL_API = "/v1"+FLIGHT+"/results";
	public static String SSR = "/v1/flight/ssr";
	public static String BOOK_TICKET_EXTERNAL_API = "/v1/flight/book-ticket";
	public static String ACTION_POOL_EXTERNAL_API = "/v1" + FLIGHT + "/action-poll";
	
	public static String EXTERNAL_FARE_RULES_API = VERSION+FLIGHT+"/farerules";
	public static String EXTERNAL_CANCELLATION_CHARGES_API = VERSION+FLIGHT+"/cancellation-charges";
	public static String EXTERNAL_CANCELLATION_API = VERSION+FLIGHT+"/cancel-ticket";
}

