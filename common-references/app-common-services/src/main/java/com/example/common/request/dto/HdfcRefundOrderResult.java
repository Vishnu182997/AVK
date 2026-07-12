package com.example.common.request.dto;

import lombok.Data;

@Data
public class HdfcRefundOrderResult {
	public String reason;
	public int refund_status;
}
