package com.example.handler.service;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.example.common.enums.FCHttpStatus;
import com.example.common.exceptions.CustomException;
import com.example.common.logger.LogUtils;
import com.example.common.logger.ThirdPartyLogUtils;
import com.example.common.request.dto.AWSProxyRequest;
import com.example.common.request.dto.AWSProxyResponse;
import com.example.logger.request.LoggerCategory;
import com.example.logger.request.ThirdPartyLoggerFactory;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

@SuppressWarnings({"rawtypes","unused"})
public abstract class ThirdPartyRequestHandlerService implements RequestHandlerService {
    
    @Override
    public AWSProxyResponse processHandler(AWSProxyRequest input, Context context) {
        String cacheKey = null,correlationId=null;
        LambdaLogger lambdaLogger =  null;
        if(context != null) {
        	lambdaLogger = context.getLogger();
        }
        
        long startTime = System.currentTimeMillis();
        AWSProxyResponse response = null;
        boolean isEncryptedRequest = false;
        String encryptionReferenceKey = null;
        
        try {
        	//Warm up message
    		if(input.getBody() == null) {
    			return new AWSProxyResponse();
    		}
    		
    		// Check and decrypt request if encrypted (E2E encryption support)
            isEncryptedRequest = E2EEncryptionHandler.isEncryptedRequest(input.getBody(), lambdaLogger);
            if (isEncryptedRequest) {
            	encryptionReferenceKey = E2EEncryptionHandler.decryptRequest(input, lambdaLogger);
            	if (encryptionReferenceKey == null) {
                    LogUtils.debuglogger(lambdaLogger, "Failed to decrypt request body");
                    response = new AWSProxyResponse("Failed to decrypt request.", FCHttpStatus.BAD_REQUEST, FCHttpStatus.VALIDATION_ERROR_CODE.getCode());
                    return encryptAndReturn(input, response, isEncryptedRequest, encryptionReferenceKey, lambdaLogger);
                }
            }
    		
            cacheKey = getCacheKey(input.getBody());
//			ThirdPartyLoggerFactory.createThirdPartyLoggerFactory(context.getFunctionName(), input.getResource());
            correlationId = LogUtils.initializeCorrelationId(context != null ? context.getAwsRequestId() : null);
            if(context != null) {
                LogUtils.setCurrentLambdaName(context.getFunctionName());
            }
            Map<String, Object> requestMap = new HashMap<>();
	        requestMap.put("path", input.getPath());
	        requestMap.put("method", input.getHttpMethod());
	        requestMap.put("queryParams", input.getQueryStringParameters());
	        requestMap.put("body", input.getBody());
	        LogUtils.startRequest(lambdaLogger, input.getResource(), requestMap);

            ThirdPartyLogUtils.log(ThirdPartyLoggerFactory.getThirdPartyLoggerDTO().withCategory(LoggerCategory.INPUT_REQUEST).withOriginalMessage(input.getBody()).withKey(String.valueOf(Calendar.getInstance().getTimeInMillis())).withCacheKey(cacheKey));

            ThirdPartyLogUtils.customIPLogger(input.getRequestContext());

            response = handleRequest(input, context);

            ThirdPartyLogUtils.log(ThirdPartyLoggerFactory.getThirdPartyLoggerDTO().withCategory(LoggerCategory.OUTPUT_RESPONSE).withOriginalMessage(response).withKey(String.valueOf(Calendar.getInstance().getTimeInMillis())).withCacheKey(cacheKey));
            // Encrypt response only if the request was encrypted (E2E encryption support)
 			if (isEncryptedRequest && response != null && response.getBody() != null) {
 				E2EEncryptionHandler.encryptResponse(input, response, encryptionReferenceKey, lambdaLogger);
 			}
        } catch (CustomException ex) {
        	LogUtils.exceptionLogger(lambdaLogger, ex, "Lambda execution failed");
            //ThirdPartyLogUtils.exceptionLogger(ex);
            ThirdPartyLogUtils.log(ThirdPartyLoggerFactory.getThirdPartyLoggerDTO().withLog_message(ex.getMessage()).withCategory(LoggerCategory.EXCEPTION).withOriginalMessage(ex).withKey(String.valueOf(Calendar.getInstance().getTimeInMillis())).withCacheKey(cacheKey));
            response = new AWSProxyResponse("", ex.getMessage(), ex.getStatusCode(), ex.getErrorCode());
            return encryptAndReturn(input, response, isEncryptedRequest, encryptionReferenceKey, lambdaLogger);
        } catch (Exception ex) {
        	LogUtils.exceptionLogger(lambdaLogger, ex, "Lambda execution failed");
            //ThirdPartyLogUtils.exceptionLogger(ex);
            ThirdPartyLogUtils.log(ThirdPartyLoggerFactory.getThirdPartyLoggerDTO().withLog_message(ex.getMessage()).withCategory(LoggerCategory.EXCEPTION).withOriginalMessage(ex).withKey(String.valueOf(Calendar.getInstance().getTimeInMillis())).withCacheKey(cacheKey));
            response = new AWSProxyResponse(ex.getMessage(), FCHttpStatus.OK, FCHttpStatus.UNHANDLED_ERROR_CODE.getCode());
            return encryptAndReturn(input, response, isEncryptedRequest, encryptionReferenceKey, lambdaLogger);
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
            LogUtils.clearLambdaName();
        }
        return response;
    }
    
    /**
     * Helper method to encrypt response and return for early exit scenarios
     */
    private AWSProxyResponse encryptAndReturn(AWSProxyRequest input, AWSProxyResponse response, boolean isEncryptedRequest, String referenceKey, LambdaLogger lambdaLogger) {
        if (isEncryptedRequest && response != null && response.getBody() != null) {
            E2EEncryptionHandler.encryptResponse(input, response, referenceKey, lambdaLogger);
        }
        return response;
    }

    /**
     * @param json
     * @return
     */
    private JsonObject getJSONObject(String json) {
        return new Gson().fromJson(json, JsonObject.class);
    }

    protected abstract AWSProxyResponse handleRequest(AWSProxyRequest input, Context context) throws Exception;

    private String getCacheKey(String body) {
        JsonObject jsonObject = getJSONObject(body);
        if (jsonObject.has("cacheKey") && jsonObject.get("cacheKey") != null && !jsonObject.get("cacheKey").getAsString().equals(""))
            return jsonObject.get("cacheKey").getAsString();
        else
            return null;
    }

}
