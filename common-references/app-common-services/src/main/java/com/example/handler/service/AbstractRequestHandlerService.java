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
import com.example.common.util.FastCollabCommonUtil;
import com.example.dto.UserInfo;
import com.example.logger.request.LoggerCategory;
import com.example.service.cognito.CognitoValidator;

/**
 * @author Bhargava Kurella
 * @version 1.0
 * @since 07/01/2020
 *
 */
@SuppressWarnings("unused")
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
		boolean isEncryptedRequest = false;
		String encryptionReferenceKey = null;
		
		try {
			String idToken = null;
			// Initialize correlation ID at the start
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
	    
			
			if(input!=null && input.getHeaders()!=null && !input.getHeaders().isEmpty()) {
				idToken = FastCollabCommonUtil.getAuthorizationToken(input.getHeaders());
				if(!FastCollabCommonUtil.isNullOrEmpty(idToken)) {
					response = new AWSProxyResponse("Authorization token is not valid.", FCHttpStatus.UNAUTHORIZED, FCHttpStatus.VALIDATION_ERROR_CODE.getCode());
				}
			}else {
				response = new AWSProxyResponse("Authorization token not found in the Header.", FCHttpStatus.UNAUTHORIZED, FCHttpStatus.VALIDATION_ERROR_CODE.getCode());
			}
			
			LogUtils.printlogger(lambdaLogger, "Token:>>> "+idToken);
			
                        UserInfo userInfo = cognitoValidator.decodeJWTToken(idToken);

                        if(userInfo!=null) {
                                LogUtils.setCurrentUserId(String.valueOf(userInfo.getUser_Id()));
				
//				LoggerFactory.createLoggerFactory(context.getFunctionName(), input.getResource(), String.valueOf(userInfo.getUser_Id()), getLegalEntityId(input.getBody(), lambdaLogger), getCompanyId(input.getBody(), lambdaLogger));
				
				LogUtils.printDebuggerLogs(lambdaLogger, FastCollabCommonUtil.getResponseBody(userInfo), null, "AUTHENTICATED_USER_INFO");
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
            LogUtils.endRequest(lambdaLogger, input.getResource(), response!=null?response.getBody():"",System.currentTimeMillis() - startTime);
            // Clear correlation ID at the end
            LogUtils.clearCorrelationId();
            LogUtils.clearLambdaName();
            LogUtils.clearUserId();
        }
		
		return response;
	}

	public abstract AWSProxyResponse handleRequest(AWSProxyRequest input, Context context, UserInfo userInfo) throws Exception;

}
