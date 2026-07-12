package com.example.handler.service;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.example.common.enums.FCHttpStatus;
import com.example.common.logger.LogUtils;
import com.example.common.logger.LoggerFactory;
import com.example.common.request.dto.AWSProxyRequest;
import com.example.common.request.dto.AWSProxyResponse;
import com.example.common.util.AvkCommonUtil;
import com.example.dto.UserInfo;
import com.example.logger.request.LoggerCategory;
import com.example.service.cognito.CognitoValidator;

/**
 * @author Bhargava Kurella
 * @version 1.0
 * @since 07/01/2020
 *
 */
public abstract class AbstractRequestHandlerService implements RequestHandlerService<AWSProxyResponse> {
	
	@Autowired
	private CognitoValidator cognitoValidator;

	/**
	 * @param awsProxyRequest
	 * @param context
	 * @return
	 */
	@Override
	public AWSProxyResponse processHandler(AWSProxyRequest input, Context context) throws Exception{
		LambdaLogger lambdaLogger =  context.getLogger();
		long startTime = System.currentTimeMillis();
		AWSProxyResponse response = null;
		try {
			String idToken = null;
			// Initialize correlation ID at the start
	        String correlationId = LogUtils.initializeCorrelationId(context.getAwsRequestId());	     
	        Map<String, Object> requestMap = new HashMap<>();
	        requestMap.put("path", input.getPath());
	        requestMap.put("method", input.getHttpMethod());
	        requestMap.put("queryParams", input.getQueryStringParameters());
	        requestMap.put("body", input.getBody());
	        LogUtils.startRequest(lambdaLogger, input.getResource(), requestMap);
	    
			
			if(input!=null && input.getHeaders()!=null && !input.getHeaders().isEmpty()) {
				idToken = AvkCommonUtil.getAuthorizationToken(input.getHeaders());
				if(!AvkCommonUtil.isNullOrEmpty(idToken)) {
					response = new AWSProxyResponse("Authorization token is not valid.", FCHttpStatus.UNAUTHORIZED, FCHttpStatus.VALIDATION_ERROR_CODE.getCode());
				}
			}else {
				response = new AWSProxyResponse("Authorization token not found in the Header.", FCHttpStatus.UNAUTHORIZED, FCHttpStatus.VALIDATION_ERROR_CODE.getCode());
			}
			
			LogUtils.printlogger(lambdaLogger, "Token:>>> "+idToken);
			
			UserInfo userInfo = cognitoValidator.decodeJWTToken(idToken);
			
			if(userInfo!=null) {
				
//				LoggerFactory.createLoggerFactory(context.getFunctionName(), input.getResource(), String.valueOf(userInfo.getUser_Id()), getLegalEntityId(input.getBody(), lambdaLogger), getCompanyId(input.getBody(), lambdaLogger));
				
				LogUtils.printDebuggerLogs(lambdaLogger, AvkCommonUtil.getResponseBody(userInfo), null, "AUTHENTICATED_USER_INFO");
				//LogUtils.debuglogger(lambdaLogger, FastCollabCommonUtil.getResponseBody(userInfo));
				
				//LogUtils.customRequestLogger(lambdaLogger, input.getResource(), LoggerFactory.getLoggerMessageDTO().withCategory(LoggerCategory.INPUT_REQUEST).withOriginalMessage(input.getBody()).withKey(String.valueOf(Calendar.getInstance().getTimeInMillis())));

				LogUtils.customIPLogger(input.getRequestContext());
				
				response = handleRequest(input, context, userInfo);
				
				LogUtils.customResponseLogger(lambdaLogger, LoggerFactory.getLoggerMessageDTO().withCategory(LoggerCategory.OUTPUT_RESPONSE).withOriginalMessage(response).withKey(String.valueOf(Calendar.getInstance().getTimeInMillis())));
				
				
			}else {
				response =  new AWSProxyResponse("Please send a valid authorized token.", FCHttpStatus.UNAUTHORIZED, FCHttpStatus.VALIDATION_ERROR_CODE.getCode());
			}
			
		}catch (Exception e) {
			LogUtils.exceptionLogger(lambdaLogger, e, "Lambda execution failed");
			throw e;
		}
		finally {
			if (response != null) {
                if (response.getHeaders() == null) {
                    response.setHeaders(new HashMap<>());
                }
                response.getHeaders().put("X-Correlation-ID", LogUtils.getCurrentCorrelationId());
            }
			LogUtils.endRequest(lambdaLogger, input.getResource(), response!=null?response.getBody():"",System.currentTimeMillis() - startTime);
            // Clear correlation ID at the end
            LogUtils.clearCorrelationId();
        }
		
		return response;
	}
	
	
	/**
	 * This method will get the LegalEntityId.
	 * 
	 * @param body
	 * @param lambdaLogger
	 * @return
	 */
//	private BigInteger getLegalEntityId(String body, LambdaLogger lambdaLogger) {
//		try {
//			JsonObject jsonObject = getJSONObject(body);
//			if(jsonObject.get("legalEntityId")!=null)
//				return jsonObject.get("legalEntityId").getAsBigInteger();
//			else if(jsonObject.get("legal_Entity_Id")!=null)
//				return jsonObject.get("legal_Entity_Id").getAsBigInteger();
//			else if(jsonObject.get("legalentityid")!=null)
//				return jsonObject.get("legalentityid").getAsBigInteger();
//		}catch (Exception e) {
//			LogUtils.exceptionLogger(lambdaLogger, e);
//		}
//		return null;
//	}
//	
//	
//	/**
//	 * This method will get the CompanyId.
//	 * 
//	 * @param body
//	 * @param lambdaLogger
//	 * @return
//	 */
//	private BigInteger getCompanyId(String body, LambdaLogger lambdaLogger) {
//		try {
//			JsonObject jsonObject = getJSONObject(body);
//			if(jsonObject.get("companyId")!=null)
//				return jsonObject.get("companyId").getAsBigInteger();
//			else if(jsonObject.get("company_Id")!=null)
//				return jsonObject.get("company_Id").getAsBigInteger();
//			else if(jsonObject.get("companyid")!=null)
//				return jsonObject.get("companyid").getAsBigInteger();
//		}catch (Exception e) {
//			LogUtils.exceptionLogger(lambdaLogger, e);
//		}
//		return null;
//	}
//	
//	
//	/**
//	 * @param json
//	 * @return
//	 */
//	private JsonObject getJSONObject(String json) {
//		return new Gson().fromJson(json, JsonObject.class);
//	}

	public abstract AWSProxyResponse handleRequest(AWSProxyRequest input, Context context, UserInfo userInfo) throws Exception;

}
