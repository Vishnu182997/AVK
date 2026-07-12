package com.example.flight.common.dto;

import java.io.Serializable;

import lombok.Data;

@Data
public class RenamedFareTypeObject implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1017148070190602054L;

	private Integer fareTypeId;
	
	private String fareTypeDesc;
	
	private String fareTypeName;
	
	private String passGstType;
}
