package com.example.common.request.dto;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import com.example.common.enums.FCHttpStatus;
import com.example.common.util.AvkCommonUtil;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class AWSProxyResponse implements Serializable {
	
	private static final long serialVersionUID = -4108012477546036422L;

	private boolean isBase64Encoded;
	private int statusCode;
	private Map<String, Object> headers = new HashMap<>();
	private String body;
	
	private void setHeaders() {
		headers.put("X-Content-Type-Options" , "nosniff");
		headers.put("Content-Type" , "application/json");
		headers.put("X-XSS-Protection" , "1; mode=block");
		headers.put("Access-Control-Allow-Origin" , "*");
		headers.put("Allow" , "OPTIONS, POST");
		headers.put("Access-Control-Allow-Methods" , "OPTIONS, POST");
		headers.put("Access-Control-Allow-Headers" , "*");
	}
	
	public AWSProxyResponse() {
		
	}

	public AWSProxyResponse(Object payload, String message, FCHttpStatus status, int errorCode) {
		setHeaders();
		FastcollabResponse fastcollabResponse = new FastcollabResponse(payload, message, status, errorCode);
		this.body = AvkCommonUtil.getResponseBody(fastcollabResponse);
		this.statusCode = fastcollabResponse.getStatus().getCode();
	}
	
	public AWSProxyResponse(Object payload, String message, FCHttpStatus status){
		setHeaders();
		FastcollabResponse fastcollabResponse = new FastcollabResponse(payload,message,status);
		this.body = AvkCommonUtil.getResponseBody(fastcollabResponse);
		this.statusCode = fastcollabResponse.getStatus().getCode();
	}
	public AWSProxyResponse(Object payload){
		setHeaders();
		this.body = AvkCommonUtil.getResponseBody(payload);
		this.statusCode = 200;
	}
	
	public AWSProxyResponse(Object payload,FCHttpStatus status){
		setHeaders();
		FastcollabResponse fastcollabResponse = new FastcollabResponse(payload,status);
		this.body = AvkCommonUtil.getResponseBody(fastcollabResponse);
		this.statusCode = fastcollabResponse.getStatus().getCode();
	}
	
	public AWSProxyResponse(String message, FCHttpStatus status) {
		setHeaders();
		FastcollabResponse fastcollabResponse = new FastcollabResponse(message, status);
		this.body = AvkCommonUtil.getResponseBody(fastcollabResponse);
		this.statusCode = fastcollabResponse.getStatus().getCode();
	}

	public AWSProxyResponse(String message, FCHttpStatus status, int errorCode) {
		setHeaders();
		FastcollabResponse fastcollabResponse = new FastcollabResponse(message, status, errorCode);
		this.body = AvkCommonUtil.getResponseBody(fastcollabResponse);
		this.statusCode = fastcollabResponse.getStatus().getCode();
	}

	public AWSProxyResponse(Object payload, FCHttpStatus status, int errorCode) {
		setHeaders();
		FastcollabResponse fastcollabResponse = new FastcollabResponse(payload, status, errorCode);
		this.body = AvkCommonUtil.getResponseBody(fastcollabResponse);
		this.statusCode = fastcollabResponse.getStatus().getCode();
	}

	public AWSProxyResponse(Object payload, boolean isBase64Encoded, FCHttpStatus status, int errorCode) {
		setHeaders();
		FastcollabResponse fastcollabResponse = new FastcollabResponse(payload, status, errorCode);
		this.body = AvkCommonUtil.getResponseBody(fastcollabResponse);
		this.statusCode = fastcollabResponse.getStatus().getCode();
		this.isBase64Encoded = isBase64Encoded;
	}

	public AWSProxyResponse(Object payload, boolean isBase64Encoded, String message, FCHttpStatus status,
			int errorCode) {
		setHeaders();
		FastcollabResponse fastcollabResponse = new FastcollabResponse(payload, message, status, errorCode);
		this.body = AvkCommonUtil.getResponseBody(fastcollabResponse);
		this.statusCode = fastcollabResponse.getStatus().getCode();
		this.isBase64Encoded = isBase64Encoded;
	}
	
	public AWSProxyResponse(Object payload, String message, FCHttpStatus status, int errorCode, boolean isError) {
		setHeaders();
		FastcollabResponse fastcollabResponse = new FastcollabResponse(payload, message, status, errorCode, isError);
		this.body = AvkCommonUtil.getResponseBody(fastcollabResponse);
		this.statusCode = fastcollabResponse.getStatus().getCode();
	}
}
