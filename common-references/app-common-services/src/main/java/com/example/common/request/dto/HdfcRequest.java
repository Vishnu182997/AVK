package com.example.common.request.dto;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class HdfcRequest {
	@JsonProperty("merchant_id")
	private String merchant_id;
	@JsonProperty("order_id")
	private String order_id;
	@JsonProperty("amount")
	private BigDecimal amount;
	@JsonProperty("currency")
	private String currency;
	@JsonProperty("redirect_url")
	private String redirect_url;
	@JsonProperty("cancel_url")
	private String cancel_url;
	@JsonProperty("billing_name")
	private String billing_name;
	@JsonProperty("billing_address")
	private String billing_address;
	@JsonProperty("billing_city")
	private String billing_city;
	@JsonProperty("billing_state")
	private String billing_state;
	@JsonProperty("billing_zip")
	private String billing_zip;
	@JsonProperty("billing_country")
	private String billing_country;
	@JsonProperty("billing_tel")
	private String billing_tel;
	@JsonProperty("billing_email")
	private String billing_email;
	@JsonProperty("delivery_name")
	private String delivery_name;
	@JsonProperty("delivery_address")
	private String delivery_address;
	@JsonProperty("delivery_city")
	private String delivery_city;
	@JsonProperty("delivery_state")
	private String delivery_state;
	@JsonProperty("delivery_zip")
	private String delivery_zip;
	@JsonProperty("delivery_country")
	private String delivery_country;
	@JsonProperty("delivery_tel")
	private String delivery_tel;
	@JsonProperty("merchant_param1")
	private String merchant_param1;
	@JsonProperty("merchant_param2")
	private String merchant_param2;
	@JsonProperty("merchant_param3")
	private String merchant_param3;
	@JsonProperty("merchant_param4")
	private String merchant_param4;
	@JsonProperty("merchant_param5")
	private String merchant_param5;
	@JsonProperty("payment_option")
	private String payment_option;
	@JsonProperty("card_type")
	private String card_type;
	@JsonProperty("card_name")
	private String card_name;
	@JsonProperty("data_accept")
	private String data_accept;
	@JsonProperty("card_number")
	private String card_number;
	@JsonProperty("expiry_month")
	private String expiry_month;
	@JsonProperty("expiry_year")
	private String expiry_year;
	@JsonProperty("cvv_number")
	private String cvv_number;
	@JsonProperty("issuing_bank")
	private String issuing_bank;
	@JsonProperty("mobile_number")
	private String mobile_number;
	@JsonProperty("UpiPaymentflag")
	private String upi_Payment_flag;
	@Override
	public String toString() {
		return "merchant_id=" + merchant_id + "&order_id=" + order_id + "&amount=" + amount
				+ "&currency=" + currency + "&redirect_url=" + redirect_url + "&cancel_url=" + cancel_url
				+ "&billing_name=" + billing_name + "&billing_address=" + billing_address + "&billing_city="
				+ billing_city + "&billing_state=" + billing_state + "&billing_zip=" + billing_zip
				+ "&billing_country=" + billing_country + "&billing_tel=" + billing_tel + "&billing_email="
				+ billing_email + "&delivery_name=" + delivery_name + "&delivery_address=" + delivery_address
				+ "&delivery_city=" + delivery_city + "&delivery_state=" + delivery_state + "&delivery_zip="
				+ delivery_zip + "&delivery_country=" + delivery_country + "&delivery_tel=" + delivery_tel
				+ "&merchant_param1=" + merchant_param1 + "&merchant_param2=" + merchant_param2 + "&merchant_param3="
				+ merchant_param3 + "&merchant_param4=" + merchant_param4 + "&merchant_param5=" + merchant_param5
				+ "&payment_option=" + payment_option + "&card_type=" + card_type + "&card_name=" + card_name
				+ "&data_accept=" + data_accept + "&card_number=" + card_number + "&expiry_month=" + expiry_month
				+ "&expiry_year=" + expiry_year + "&cvv_number=" + cvv_number + "&issuing_bank=" + issuing_bank
				+ "&mobile_number=" + mobile_number + "&UpiPaymentflag=" + upi_Payment_flag;
	}
	

}
