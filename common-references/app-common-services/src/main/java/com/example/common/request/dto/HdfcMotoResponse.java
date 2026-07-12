package com.example.common.request.dto;

import java.math.BigDecimal;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import lombok.Data;

@XmlRootElement(name = "response")
@Data
@XmlAccessorType(XmlAccessType.FIELD)
public class HdfcMotoResponse {

	private String result;

	private String error_code_tag;

	private String error_service_tag;

	public Long auth;

	public Long ref;

	public Long postdate;

	public Long tranid;

	public Long trackid;

	public Long payid;

	public String udf1;

	public String udf2;

	public String udf3;

	public String udf4;

	public String udf5;

	public BigDecimal amt;

	public Long authRespCode;
}
