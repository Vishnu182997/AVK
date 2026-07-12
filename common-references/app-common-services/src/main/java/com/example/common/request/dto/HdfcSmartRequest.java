package com.example.common.request.dto;

import java.math.BigDecimal;

import com.example.common.util.AvkCommonUtil;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class HdfcSmartRequest {
	
	@JsonProperty("order.order_id")
	private String order_id;

	@JsonProperty("order.amount")
	private BigDecimal amount;
	
	@JsonProperty("order.currency")
	private String currency;

	@JsonProperty("order.customer_id")
	private String customer_id;

	@JsonProperty("order.return_url")
	private String return_url;

	@JsonProperty("merchant_id")
	private String merchant_id;
	
	@JsonProperty("payment_method_type")
	private String payment_method_type;

	@JsonProperty("payment_method")
	private String payment_method;
	
	@JsonProperty("card_number")
	private String card_number;
	
	@JsonProperty("card_exp_month")
	private String card_exp_month;

	@JsonProperty("card_exp_year")
	private String card_exp_year;
	
	@JsonProperty("name_on_card")
	private String name_on_card;

	@JsonProperty("card_security_code")
	private String card_security_code;

	@JsonProperty("save_to_locker")
	private String save_to_locker;

	@JsonProperty("redirect_after_payment")
	private String redirect_after_payment; 
	
	@JsonProperty("order.customer_email")
	private String customer_email; 

	@JsonProperty("order.customer_phone")
	private String customer_phone; 

	@JsonProperty("order.product_id")
	private String product_id; 
	
	@JsonProperty("order.description")
	private String description; 

	@JsonProperty("order.billing_address_first_name")
	private String billing_address_first_name; 
	
	@JsonProperty("order.billing_address_last_name")
	private String billing_address_last_name;
	
	@JsonProperty("order.billing_address_line1")
	private String billing_address_line1;
	
	@JsonProperty("order.billing_address_line2")
	private String billing_address_line2;
	
	@JsonProperty("order.billing_address_line3")
	private String billing_address_line3;
	
	@JsonProperty("order.billing_address_city")
	private String billing_address_city;
	
	@JsonProperty("order.billing_address_state")
	private String billing_address_state;
	
	@JsonProperty("order.billing_address_country")
	private String billing_address_country;
	
	@JsonProperty("order.billing_address_postal_code")
	private String billing_address_postal_code;
	
	@JsonProperty("order.billing_address_phone")
	private String billing_address_phone;
	
	@JsonProperty("order.billing_address_country_code_iso")
	private String billing_address_country_code_iso;
	
	@JsonProperty("order.shipping_address_first_name")
	private String shipping_address_first_name;
	
	@JsonProperty("order.shipping_address_last_name")
	private String shipping_address_last_name;
	
	@JsonProperty("order.shipping_address_line1")
	private String shipping_address_line1;
	
	@JsonProperty("order.shipping_address_line2")
	private String shipping_address_line2; 

	@JsonProperty("order.shipping_address_line3")
	private String shipping_address_line3; 

	@JsonProperty("order.shipping_address_city")
	private String shipping_address_city; 
	
	@JsonProperty("order.shipping_address_state")
	private String shipping_address_state; 
	
	@JsonProperty("order.shipping_address_phone")
	private String shipping_address_phone; 

	@JsonProperty("order.shipping_address_postal_code")
	private String shipping_address_postal_code; 
	
	@JsonProperty("order.shipping_address_country_code_iso")
	private String shipping_address_country_code_iso; 
	
	@JsonProperty("order.shipping_address_country")
	private String shipping_address_country;
	
	@JsonProperty("tokenize")
	private String tokenize;

	@JsonProperty("format")
	private String format;

	@JsonProperty("upi_vpa")
	private String upi_vpa;
	
	@JsonProperty("txn_type")
	private String txn_type;	
	
	@Override
	public String toString() {
		return "order.order_id=" + order_id 
				+ "&order.amount=" + amount 
				+ "&order.currency=" + currency 
				+ "&order.customer_id=" + customer_id 
				+ "&order.return_url=" + return_url 
				+ "&merchant_id=" + merchant_id  
				+ "&payment_method_type=" + payment_method_type 
				+ "&payment_method=" + payment_method 
				+ (AvkCommonUtil.hasData(card_number)
						? "&card_number=" + card_number 
						+ "&card_exp_month=" + card_exp_month 
						+ "&card_exp_year=" + card_exp_year 
						+ "&name_on_card=" + name_on_card
						+ "&tokenize=" + tokenize 
						+ "&card_security_code=" + card_security_code 
						+ "&save_to_locker=" + save_to_locker
						:"")
				+ (AvkCommonUtil.hasData(upi_vpa)
						? "&upi_vpa=" + upi_vpa
						+ "&txn_type=" + txn_type
						:"")
				+ "&redirect_after_payment=" + redirect_after_payment 
				+ "&order.customer_email=" + customer_email 
				+ "&order.customer_phone=" + customer_phone 
				+ "&order.product_id=" + product_id 
				+ "&order.description=" + description 
				+ "&order.billing_address_first_name=" + billing_address_first_name 
				+ "&order.billing_address_last_name=" + billing_address_last_name 
				+ "&order.billing_address_line1=" + billing_address_line1 
				+ "&order.billing_address_line2=" + billing_address_line2 
				+ "&order.billing_address_line3=" + billing_address_line3
				+ "&order.billing_address_city=" + billing_address_city 
				+ "&order.billing_address_state=" + billing_address_state 
				+ "&order.billing_address_country=" + billing_address_country 
				+ "&order.billing_address_postal_code=" + billing_address_postal_code 
				+ "&order.billing_address_phone=" + billing_address_phone
				+ "&order.billing_address_country_code_iso=" + billing_address_country_code_iso
				+ "&order.shipping_address_first_name=" + shipping_address_first_name
				+ "&order.shipping_address_last_name=" + shipping_address_last_name
				+ "&order.shipping_address_line1=" + shipping_address_line1
				+ "&order.shipping_address_line2=" + shipping_address_line2
				+ "&order.shipping_address_line3=" + shipping_address_line3
				+ "&order.shipping_address_city=" + shipping_address_city
				+ "&order.shipping_address_state=" + shipping_address_state
				+ "&order.shipping_address_postal_code=" + shipping_address_postal_code
				+ "&order.shipping_address_phone=" + shipping_address_phone
				+ "&order.shipping_address_country_code_iso=" + shipping_address_country_code_iso
				+ "&order.shipping_address_country=" + shipping_address_country
				+ "&format=" + format;
	}
	

}
