package com.example.common.exceptions;

import com.example.common.enums.FCHttpStatus;

public class CustomException extends RuntimeException {
	/**
	 *
	 */
	private static final long serialVersionUID = -7812346617021350144L;

	private final FCHttpStatus statusCode;
	private boolean IsError;
	private final int errorCode;
	private Object payLoad;

	public CustomException(String exceptionMessage, FCHttpStatus statusCode,int errorCode) {
		super(exceptionMessage);
		this.statusCode = statusCode;
		this.errorCode = errorCode;
		this.IsError = true;
	}
	public CustomException(String exceptionMessage, FCHttpStatus statusCode,int errorCode ,Object payLoad) {
		super(exceptionMessage);
		this.statusCode = statusCode;
		this.errorCode = errorCode;
		this.payLoad = payLoad;
		this.IsError = true;
	}
	public CustomException(String exceptionMessage, FCHttpStatus statusCode,int errorCode ,Object payLoad, boolean isError) {
		super(exceptionMessage);
		this.statusCode = statusCode;
		this.errorCode = errorCode;
		this.payLoad = payLoad;
		this.IsError = isError;
	}
	public FCHttpStatus getStatusCode() {
		return statusCode;
	}

	public int getErrorCode() {
		return errorCode;
	}

	public Object getPayLoad() {
		return payLoad;
	}
	public boolean isIsError() {
		return IsError;
	}
	public void setIsError(boolean isError) {
		IsError = isError;
	}
}
