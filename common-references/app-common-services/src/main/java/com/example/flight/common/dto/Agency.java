package com.example.flight.common.dto;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Agency implements Serializable {
	/**
	* 
	*/
	private static final long serialVersionUID = 172201990490265356L;

	private String areaCode;
	private String countryCode;
	private String location;
	private String contactNumber;
	private String name;
	private String type;
	private String id;
	private String gdsCode;
	private String password;
	private String email;
	private String agencyIATANumber;
}
