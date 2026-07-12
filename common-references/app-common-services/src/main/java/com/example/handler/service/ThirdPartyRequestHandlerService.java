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

@SuppressWarnings("rawtypes")
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
        try {
        	//Warm up message
    		if(input.getBody() == null) {
    			return new AWSProxyResponse();
    		}
            cacheKey = getCacheKey(input.getBody());
//			ThirdPartyLoggerFactory.createThirdPartyLoggerFactory(context.getFunctionName(), input.getResource());
            correlationId = LogUtils.initializeCorrelationId(context != null ? context.getAwsRequestId() : null);	 
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
            
            
//            return response;
        } catch (CustomException ex) {
        	LogUtils.exceptionLogger(lambdaLogger, ex, "Lambda execution failed");
            //ThirdPartyLogUtils.exceptionLogger(ex);
            ThirdPartyLogUtils.log(ThirdPartyLoggerFactory.getThirdPartyLoggerDTO().withLog_message(ex.getMessage()).withCategory(LoggerCategory.EXCEPTION).withOriginalMessage(ex).withKey(String.valueOf(Calendar.getInstance().getTimeInMillis())).withCacheKey(cacheKey));
            return new AWSProxyResponse("", ex.getMessage(), ex.getStatusCode(), ex.getErrorCode());
        } catch (Exception ex) {
        	LogUtils.exceptionLogger(lambdaLogger, ex, "Lambda execution failed");
            //ThirdPartyLogUtils.exceptionLogger(ex);
            ThirdPartyLogUtils.log(ThirdPartyLoggerFactory.getThirdPartyLoggerDTO().withLog_message(ex.getMessage()).withCategory(LoggerCategory.EXCEPTION).withOriginalMessage(ex).withKey(String.valueOf(Calendar.getInstance().getTimeInMillis())).withCacheKey(cacheKey));
            return new AWSProxyResponse(ex.getMessage(), FCHttpStatus.OK, FCHttpStatus.UNHANDLED_ERROR_CODE.getCode());
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
