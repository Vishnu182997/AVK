package com.example.logger.request;

import java.math.BigInteger;

import lombok.Builder;
import lombok.Data;

/**
 * @author Bhargava Kurella
 * @version 1.0
 * @since 27/12/2019
 *
 */
@Data
@Builder
public class LoggerMessageDTO {
	
	private Object originalMessage;
	private String key;
	private String user_Id;
	private BigInteger company_Id;
	private BigInteger legal_Entity_Id;
	private LoggerCategory category;
	private String log_message;
	private String lambda_Name;
	private String resource_Path;
	
	
	public LoggerMessageDTO withOriginalMessage(Object originalMessage) {
		setOriginalMessage(originalMessage);
		return this;
	}
	
	public LoggerMessageDTO withKey(String key) {
		setKey(key);
		return this;
	}
	
	public LoggerMessageDTO withUser_Id(String user_Id) {
		setUser_Id(user_Id);
		return this;
	}
	
	public LoggerMessageDTO withCompany_Id(BigInteger company_Id) {
		setCompany_Id(company_Id);
		return this;
	}
	
	public LoggerMessageDTO withLegal_Entity_Id(BigInteger legal_Entity_Id) {
		setLegal_Entity_Id(legal_Entity_Id);
		return this;
	}
	
	public LoggerMessageDTO withCategory(LoggerCategory category) {
		setCategory(category);
		return this;
	}
	
	public LoggerMessageDTO withLog_message(String log_message) {
		setLog_message(log_message);
		return this;
	}
	
	public LoggerMessageDTO withLambda_Name(String lambda_Name) {
		setLambda_Name(lambda_Name);
		return this;
	}
	
	public LoggerMessageDTO withResource_Path(String resource_Path) {
		setResource_Path(resource_Path);
		return this;
	}
	
	
}
