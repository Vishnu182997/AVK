package com.example.common.request.dto;

import com.example.common.enums.FCHttpStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class FastcollabResponse {
	
	private boolean IsError;
	
	private String errorMessage;
	
	private int errorCode;

	@JsonIgnore
	private FCHttpStatus status;
	@JsonIgnore
	private String message;

	private Object payload;
	@JsonIgnore
	private int statusCode;

	private FastcollabResponse() {
	}

	public FastcollabResponse(Object payload, String message, FCHttpStatus status, int errorCode) {
		this.payload = payload;
		this.message = message;
		this.status = status;
		this.statusCode = status.getCode();
		this.errorCode = errorCode;
		this.errorMessage = errorCode != 0 ? message : null;
		this.IsError = errorCode != 0 ? true : false;
	}


	public FastcollabResponse(String message, FCHttpStatus status, int errorCode) {
		this.message = message;
		this.status = status;
		this.statusCode = status.getCode();
		this.errorCode = errorCode;
		this.errorMessage = errorCode != 0 ? message : null;
		this.IsError = errorCode != 0 ? true : false;
	}



	public FastcollabResponse(String message, FCHttpStatus status) {
		this.message = message;
		this.status = status;
		this.statusCode = status.getCode();
	}

	public FastcollabResponse(Object payload,FCHttpStatus status){
		this.payload =  payload;
		this.status = status;
		this.statusCode = status.getCode();
	}

	public FastcollabResponse(Object payload,String message,FCHttpStatus status){
		this.payload =  payload;
		this.message = message;
		this.status = status;
		this.statusCode = status.getCode();
	}

	public FastcollabResponse(Object payload, FCHttpStatus status, int errorCode) {
		this.payload = payload;
		this.status = status;
		this.statusCode = status.getCode();
		this.errorCode = errorCode;
		this.errorMessage = errorCode != 0 ? message : null;
		this.IsError = errorCode != 0 ? true : false;
	}
	
	public FastcollabResponse(Object payload, String message, FCHttpStatus status, int errorCode, boolean isError) {
		this.payload = payload;
		this.message = message;
		this.status = status;
		this.statusCode = status.getCode();
		this.errorCode = errorCode;
		this.errorMessage = errorCode != 0 ? message : null;
		this.IsError = isError;
	}

	
}
