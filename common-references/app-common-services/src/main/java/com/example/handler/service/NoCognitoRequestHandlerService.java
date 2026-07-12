package com.example.handler.service;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.example.common.enums.FCHttpStatus;
import com.example.common.logger.LogUtils;
import com.example.common.logger.LoggerFactory;
import com.example.common.request.dto.AWSProxyRequest;
import com.example.common.request.dto.AWSProxyResponse;
import com.example.logger.request.LoggerCategory;

/**
 * @author Bhargava Kurella
 * @version 1.0
 * @since 07/01/2020
 *
 */
@SuppressWarnings("unused")
public abstract class NoCognitoRequestHandlerService implements RequestHandlerService<AWSProxyResponse> {
	
	/**
	 * @param awsProxyRequest
	 * @param context
	 * @return
	 */
	@Override
	public AWSProxyResponse processHandler(AWSProxyRequest input, Context context) throws Exception {
		LambdaLogger lambdaLogger =  context.getLogger();
		AWSProxyResponse response = null;
		long startTime = System.currentTimeMillis();
		boolean isEncryptedRequest = false;
		String encryptionReferenceKey = null;
		
		try {
                String correlationId = LogUtils.initializeCorrelationId(context.getAwsRequestId());
                LogUtils.setCurrentLambdaName(context.getFunctionName());
            // Check and decrypt request if encrypted (E2E encryption support)
            isEncryptedRequest = E2EEncryptionHandler.isEncryptedRequest(input.getBody(), lambdaLogger);
            if (isEncryptedRequest) {
            	encryptionReferenceKey = E2EEncryptionHandler.decryptRequest(input, lambdaLogger);
            	if (encryptionReferenceKey == null) {
                    LogUtils.debuglogger(lambdaLogger, "Failed to decrypt request body");
                    response = new AWSProxyResponse("Failed to decrypt request.", FCHttpStatus.BAD_REQUEST, FCHttpStatus.VALIDATION_ERROR_CODE.getCode());
                    return response;
                }
            }
            
	        Map<String, Object> requestMap = new HashMap<>();
	        requestMap.put("path", input.getPath());
	        requestMap.put("method", input.getHttpMethod());
	        requestMap.put("queryParams", input.getQueryStringParameters());
	        requestMap.put("body", input.getBody());
	        LogUtils.startRequest(lambdaLogger, input.getResource(), requestMap);
	        
			//Warm up message
    		if(input.getResource() == null && input.getBody() == null) {
    			return new AWSProxyResponse();
    		}
    		
			LoggerFactory.createLoggerFactory(context.getFunctionName(), input.getResource(), null, null, null);
			
			//LogUtils.customRequestLogger(lambdaLogger, input.getResource(), LoggerFactory.getLoggerMessageDTO().withCategory(LoggerCategory.INPUT_REQUEST).withOriginalMessage(input.getBody()).withKey(String.valueOf(Calendar.getInstance().getTimeInMillis())));
			
			LogUtils.customIPLogger(input.getRequestContext());
			
			response = handleRequest(input, context);
			
			LogUtils.customResponseLogger(lambdaLogger, LoggerFactory.getLoggerMessageDTO().withCategory(LoggerCategory.OUTPUT_RESPONSE).withOriginalMessage(response).withKey(String.valueOf(Calendar.getInstance().getTimeInMillis())));
			
			//return response;
		}catch (Exception e) {
			LogUtils.exceptionLogger(lambdaLogger, e, "Lambda execution failed");
			throw e;
		}
		finally {
			// Encrypt response only if the request was encrypted (E2E encryption support)
			if (isEncryptedRequest && response != null && response.getBody() != null) {
				E2EEncryptionHandler.encryptResponse(input, response, encryptionReferenceKey, lambdaLogger);
			}
			
			if (response != null) {
                if (response.getHeaders() == null) {
                    response.setHeaders(new HashMap<>());
                }
                response.getHeaders().put("X-Correlation-ID", LogUtils.getCurrentCorrelationId());
            }
            LogUtils.endRequest(lambdaLogger, input.getResource(), response!=null?response.getBody():"",
                    System.currentTimeMillis() - startTime);
            // Clear correlation ID at the end
            LogUtils.clearCorrelationId();
            LogUtils.clearLambdaName();
        }
		return response;
	}
	
	public abstract AWSProxyResponse handleRequest(AWSProxyRequest input, Context context) throws Exception;

}
