package com.example.handler.service;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.example.common.logger.LogUtils;
import com.example.common.logger.LoggerFactory;
import com.example.common.request.dto.AWSProxyRequest;
import com.example.common.request.dto.AWSProxyResponse;
import com.example.logger.request.LoggerCategory;

/**
 * @author Bhargava Kurella
 * @version 1.0
 * @since 27/02/2020
 *
 */
public abstract class StepFunctionRequestHandlerService implements RequestHandlerService<Object> {

	/**
	 * @param awsProxyRequest
	 * @param context
	 * @return
	 */
	@Override
	public Object processHandler(AWSProxyRequest input, Context context) throws Exception {
		LambdaLogger lambdaLogger =  context.getLogger();
		long startTime = System.currentTimeMillis();
		Object response = null;
		try {
			// Initialize correlation ID at the start
                String correlationId = LogUtils.initializeCorrelationId(context.getAwsRequestId());
                LogUtils.setCurrentLambdaName(context.getFunctionName());
	        Map<String, Object> requestMap = new HashMap<>();
	        requestMap.put("path", input.getPath());
	        requestMap.put("method", input.getHttpMethod());
	        requestMap.put("queryParams", input.getQueryStringParameters());
	        requestMap.put("body", input.getBody());
	        LogUtils.startRequest(lambdaLogger, input.getResource(), requestMap);
	        
			LoggerFactory.createLoggerFactory(context.getFunctionName(), input.getResource(), null, null, null);
			
			//LogUtils.customRequestLogger(lambdaLogger, input.getResource(), LoggerFactory.getLoggerMessageDTO().withCategory(LoggerCategory.INPUT_REQUEST).withOriginalMessage(input.getBody()).withKey(String.valueOf(Calendar.getInstance().getTimeInMillis())));
			
			LogUtils.customIPLogger(input.getRequestContext());
			
			response = handleRequest(input, context);
			
			LogUtils.customResponseLogger(lambdaLogger, LoggerFactory.getLoggerMessageDTO().withCategory(LoggerCategory.OUTPUT_RESPONSE).withOriginalMessage(response).withKey(String.valueOf(Calendar.getInstance().getTimeInMillis())));
			
			
		}catch (Exception e) {
			LogUtils.exceptionLogger(lambdaLogger, e, "Lambda execution failed");
			throw e;
		}
		finally {
			
            // Clear correlation ID at the end
            LogUtils.clearCorrelationId();
            LogUtils.clearLambdaName();
        }
		return response;
	}
	
	public abstract Object handleRequest(AWSProxyRequest input, Context context) throws Exception;
}
