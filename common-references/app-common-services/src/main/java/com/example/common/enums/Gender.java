package com.example.common.enums;

import org.apache.commons.lang3.math.NumberUtils;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum Gender {

	MALE(1,"MALE"), FEMALE(2,"FEMALE"), TRANSGENDER(3,"TRANSGENDER");

	private int code;
	private String desc;

	Gender(int code, String desc) {
		this.code = code;
		this.desc = desc;
	}

	Gender(String desc) {
		this.desc = desc;
	}

	Gender(int code) {
		this.code = code;
	}

	public int getCode() {
		return code;
	}

	public String getDesc() {
		return desc;
	}

	public static Gender getGender(String gender) {
		switch (gender) {
			case "1":
			case "M":
			case "MI":
			case "MALE":
				return MALE;
			case "2":
			case "F":
			case "FI":
			case "FEMALE":
				return FEMALE;
			case "3":
				return TRANSGENDER;
			default:
				return MALE;
		}

	}

	public static Gender getGender(int gender) {
		switch (gender) {
			case 1:
				return MALE;
			case 2:
				return FEMALE;
			case 3:
				return TRANSGENDER;
			default:
				return MALE;
		}

	}

	public static Long getGenderLong(String gender) {
		switch (gender) {
			case "MALE":
				return 1L;
			case "FEMALE":
				return 2L;
			default:
				return 3L;
		}
	}

	@JsonCreator
	public static Gender forValue(Object v) {
		if(NumberUtils.isParsable(v.toString()))
			return Gender.getGender(Integer.valueOf(v.toString()));
		else
			return Gender.getGender(v.toString());
	}



}
