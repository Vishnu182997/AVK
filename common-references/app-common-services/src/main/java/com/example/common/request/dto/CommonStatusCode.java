package com.example.common.request.dto;

public class CommonStatusCode {

	private CommonStatusCode() {
	}

	public static final int SUCCESS_CODE = 0;
	public static final int UNHANDLED_ERROR_CODE = 1;
	public static final int VALIDATION_ERROR_CODE = 2;
	public static final int INVALID_SESSION = 3;
	public static final int SESSION_EXPIRED = 4;
	public static final int FAILED = 5;
	public static final int UN_AUTHORIZED = 6;
	public static final int WARNING_MESSAGE = 7;
	public static final int COGNITO_UN_AUTHORIZED = 8;
	public static final int COGNITO_FORBIDDEN = 9;
	public static final int REQUEST_TIMED_OUT = 10;
	public static final int INFO_MESSAGE = 11;
	public static final int INVALID_DATA_REQUEST = 12;
	public static final int CALL_BACK_REQUIRED = 13;
	public static final int DATA_NOT_FOUND = 14;
	public static final int INVALID_TRANSACTION = 15;
	public static final int EXCEED_BUFFER_AMOUNT = 16;
	public static final int PNR_COMPLETED_BOOK_FAILED = 17;
	public static final int BOOK_FAILED = 18;
	public static final int NEGATIVE_AMOUNT = 19;
	public static final int Retry = 20;
	public static final int IsWebPluginError = 21;
	public static final int REPUSHED_SUCCESS_CODE = 22;
}
