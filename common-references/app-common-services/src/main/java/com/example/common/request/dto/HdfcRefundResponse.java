package com.example.common.request.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class HdfcRefundResponse {
	@JsonProperty("Refund_Order_Result") 
    public HdfcRefundOrderResult refund_Order_Result;
}
