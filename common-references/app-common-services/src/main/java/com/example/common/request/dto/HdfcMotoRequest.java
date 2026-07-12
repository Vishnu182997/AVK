package com.example.common.request.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class HdfcMotoRequest {
	private String tranportalId;
	private String password;
	private String card;
	private String expMonth;
	private String expYear;
	private String cvv2;
	private String currencyCode;
	private String action;
	private String trackId;
	private String amt;
	private String member;
	private String transId;
	private String udf1;
	private String udf2;
	private String udf3;
	private String udf4;
	private String udf5;
	private String motoUrl;
	private String paymentUrl;
}
