package com.example.common.exceptions;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.example.common.enums.FCHttpStatus;
import com.example.common.request.dto.CommonStatusCode;
import com.example.common.util.AvkCommonUtil;

import java.util.List;

public class GlobalValidation {

	private static final Logger LOG = LogManager.getLogger(GlobalValidation.class);

	private GlobalValidation() {

	}
	
	public static void isStringEmpty(String value, String message) {
		if (!AvkCommonUtil.isStringNotEmpty(value)) {
			throw new CustomException(message, FCHttpStatus.BAD_REQUEST,CommonStatusCode.VALIDATION_ERROR_CODE);
		}
	}

	public static void checkCustomPropertiesAreMissing(String value, String message) {
		if (!AvkCommonUtil.isStringNotEmpty(value)) {
			LOG.error("Required environment variables are missing in current service");
			throw new CustomException(message, FCHttpStatus.INTERNAL_SERVER_ERROR,CommonStatusCode.UNHANDLED_ERROR_CODE);
		}
	}

	public static void isTrue(boolean value, String message) {
		if (!value) {
			throw new CustomException(message, FCHttpStatus.BAD_REQUEST,CommonStatusCode.VALIDATION_ERROR_CODE);
		}
	}

	public static void isObjectEmpty(Object value, String message) {
		if (value == null) {
			throw new CustomException(message, FCHttpStatus.BAD_REQUEST,CommonStatusCode.VALIDATION_ERROR_CODE);
		}
	}

	public static <T> void isListEmpty(List<T> list, String message) {
		if (list == null || list.isEmpty()) {
			throw new CustomException(message, FCHttpStatus.BAD_REQUEST,CommonStatusCode.VALIDATION_ERROR_CODE);
		}
	}

    public static void isStringNullOrEmpty(String value, String message) {
        if (value == null || value.isEmpty()) {
            throw new CustomException(message, FCHttpStatus.BAD_REQUEST,CommonStatusCode.VALIDATION_ERROR_CODE);
        }
    }
	public static void isDoubleNullOrEmpty(Double value, String message) {
		if (value == null || value==0) {
			throw new CustomException(message, FCHttpStatus.BAD_REQUEST,CommonStatusCode.VALIDATION_ERROR_CODE);
		}
	}
	public static void isLongNullOrEmpty(Long value, String message) {
		if (value == null || value==0) {
			throw new CustomException(message, FCHttpStatus.BAD_REQUEST,CommonStatusCode.VALIDATION_ERROR_CODE);
		}
	}

    public static void isNumberNullOrLessthanOrEqualsToZero(Integer value, String message) {
        if (value == null || value <= 0) {
            throw new CustomException(message, FCHttpStatus.BAD_REQUEST,CommonStatusCode.VALIDATION_ERROR_CODE);
        }
    }
	public static void isNumberNullOrLessthanZero(Integer value, String message) {
		if (value < 0) {
			throw new CustomException(message, FCHttpStatus.BAD_REQUEST,CommonStatusCode.VALIDATION_ERROR_CODE);
		}
	}
	public static void isNumberNullOrLessthanZeroChild(Integer value, String message) {
		if (value == null || value < 0) {
			throw new CustomException(message, FCHttpStatus.BAD_REQUEST,CommonStatusCode.VALIDATION_ERROR_CODE);
		}
	}

	public static void isNumberInBetween(Integer valueToValidate , Integer minValue , Integer maxValue , String message){
		if(minValue>valueToValidate && valueToValidate>maxValue){
			throw new CustomException(message,FCHttpStatus.BAD_REQUEST,CommonStatusCode.VALIDATION_ERROR_CODE);
		}

	}

	public static void isTrue(boolean value, String message, int commonStatusCode) {
		if (!value) {
			throw new CustomException(message, FCHttpStatus.BAD_REQUEST,commonStatusCode);
		}
	}
}
