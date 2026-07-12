package com.example.dto;

import java.math.BigInteger;
import java.util.Date;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TripMaster {
	private Long tripId;
	private String guid;
	private String tripName;
	private BigInteger userId;
	private Double tripCost;
	private String status;
	private boolean isEmailRequest;
	private BigInteger createdBy;
	private Date createdDate;
	private BigInteger updatedBy;
	private Date updatedDate;
	private Long assignedAgentId;	
}
