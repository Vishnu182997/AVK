package com.example.common.request.dto;

import lombok.Data;

@Data
public class HdfcChargeSiRequest {
	public String si_sub_ref_no;
	public String si_mer_charge_ref_no;
	public String si_amount;
	public String si_currency;
//	public String order_tid;
//	public String si_mer_ref_no1;
//	public String si_mer_ref_no2;
	
//	@Override
//	public String toString() {
//		return "si_sub_ref_no=" + si_sub_ref_no + "&si_mer_charge_ref_no=" + si_mer_charge_ref_no
//				+ "&si_amount=" + si_amount + "&si_currency=" + si_currency + "&order_tid=" + order_tid
//				+ "&si_mer_ref_no1=" + si_mer_ref_no1 + "&si_mer_ref_no2=" + si_mer_ref_no2;
//	}
	
}
