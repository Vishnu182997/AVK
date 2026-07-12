package com.example.flight.common.dto;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AirlineTourCode implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -5123244925286298287L;
	
	
	private String airlineCode;
	private String tourCode;
	
}
