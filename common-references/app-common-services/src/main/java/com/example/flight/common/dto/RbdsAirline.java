package com.example.flight.common.dto;

import java.io.Serializable;
import java.util.List;

import lombok.Data;

@Data
public class RbdsAirline implements Serializable {

	private String airlineCode;
	private List<String> rbds;
	
}
