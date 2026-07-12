package com.example.common.logger;

import java.math.BigInteger;

import com.example.logger.request.LoggerMessageDTO;

/**
 * @author Bhargava Kurella
 * @version 1.0
 * @since 16/01/2020
 *
 */
public class LoggerFactory {

	private static String lambda_Name;
	private static String resource_Path;
	private static String user_Id;
	private static BigInteger legalEntityId;
	private static BigInteger companyId;


	private LoggerFactory() {}

	public static void createLoggerFactory(String lambda_Name, String resource_Path, String user_Id, BigInteger legalEntityId, BigInteger companyId) {
		LoggerFactory.lambda_Name = lambda_Name;
		LoggerFactory.resource_Path = resource_Path;
		LoggerFactory.user_Id = user_Id;
		LoggerFactory.legalEntityId = legalEntityId;
		LoggerFactory.companyId = companyId;
	}

	/**
	 * @return the LoggerMessageDTO
	 */
	public static LoggerMessageDTO getLoggerMessageDTO() {
		return LoggerMessageDTO.builder()
								.lambda_Name(LoggerFactory.lambda_Name)
								.resource_Path(LoggerFactory.resource_Path)
								.user_Id(user_Id)
								.legal_Entity_Id(legalEntityId)
								.company_Id(companyId)
								.build();
	}

	/**
	 * @return the lambda_Name
	 */
	public String getLambda_Name() {
		return lambda_Name;
	}
	/**
	 * @param lambda_Name the lambda_Name to set
	 */
	public void setLambda_Name(String lambda_Name) {
		this.lambda_Name = lambda_Name;
	}
	/**
	 * @return the resource_Path
	 */
	public String getResource_Path() {
		return resource_Path;
	}
	/**
	 * @param resource_Path the resource_Path to set
	 */
	public void setResource_Path(String resource_Path) {
		this.resource_Path = resource_Path;
	}
	/**
	 * @return the user_Id
	 */
	public String getUser_Id() {
		return user_Id;
	}
	/**
	 * @param user_Id the user_Id to set
	 */
	public void setUser_Id(String user_Id) {
		this.user_Id = user_Id;
	}

	/**
	 * @return the legalEntityId
	 */
	public BigInteger getLegalEntityId() {
		return legalEntityId;
	}

	/**
	 * @param legalEntityId the legalEntityId to set
	 */
	public void setLegalEntityId(BigInteger legalEntityId) {
		this.legalEntityId = legalEntityId;
	}

	/**
	 * @return the companyId
	 */
	public BigInteger getCompanyId() {
		return companyId;
	}

	/**
	 * @param companyId the companyId to set
	 */
	public void setCompanyId(BigInteger companyId) {
		this.companyId = companyId;
	}

}
