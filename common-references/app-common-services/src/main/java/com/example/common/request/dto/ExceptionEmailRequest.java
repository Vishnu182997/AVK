package com.example.common.request.dto;

import lombok.Data;

@Data
public class ExceptionEmailRequest {
	
	private String tripId;
	
	private String requestId; 
	
	private String amendmentId;
	
	private String cacheKey;

	private String productId;
	
	private String apiGatewayUrl;
	
	private String emailId;
	
	private String emailTitle;
	
	private String exceptionStackTrace;
	
	private String requestPayload;
	
	private String supplierName;
	
	
}
