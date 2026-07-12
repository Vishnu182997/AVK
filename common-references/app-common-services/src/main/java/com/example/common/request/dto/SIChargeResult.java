package com.example.common.request.dto;

import lombok.Data;

@Data
public class SIChargeResult {
	public String si_charge_status;
	public String si_mer_charge_ref_no;
	public String si_charge_txn_status;
	public String si_sub_ref_no;
	public String si_error_desc;
	public String reference_no;
	public String error_code;
	public String si_mer_ref_no1;
	public String si_mer_ref_no2;
	public String order_tid;
}
