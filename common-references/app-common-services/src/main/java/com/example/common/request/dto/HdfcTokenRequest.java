package com.example.common.request.dto;

import lombok.Data;

@Data
public class HdfcTokenRequest {
	public String requestHash;
	public String trackingId;
	public String regId;
	public String orderId;
	public String accessCode;
}
