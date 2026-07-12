package com.example.handler;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.example.common.constant.FastCollabConstants;
import com.example.common.enums.FCHttpStatus;
import com.example.common.exceptions.CustomException;
import com.example.common.logger.LogUtils;
import com.example.common.logger.LoggerFactory;
import com.example.common.request.dto.AWSProxyRequest;
import com.example.common.request.dto.AWSProxyResponse;
import com.example.handler.service.E2EEncryptionHandler;
import com.example.handler.service.RequestHandlerService;
import com.example.jdbc.config.JdbcConfig;
import com.example.logger.request.LoggerCategory;

public class JdbcRequestHandler implements RequestHandler<AWSProxyRequest, AWSProxyResponse> {

	private static AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext(JdbcConfig.class);

	/**
	 * @param input request received from AWS Request Handler
	 * @return AWSProxyResponse response object which contains the body, status code
	 *         and headers.
	 */
	@SuppressWarnings("unchecked")
	@Override
	public AWSProxyResponse handleRequest(AWSProxyRequest input, Context context) {
		try {
			if (ctx == null) {
				ctx = new AnnotationConfigApplicationContext(JdbcConfig.class);
			}
			LambdaLogger lambdaLogger =  context.getLogger();
			long startTime = System.currentTimeMillis();
			RequestHandlerService<AWSProxyResponse> service = ctx.getBean(RequestHandlerService.class);
			boolean isEncryptedRequest = false;
			String encryptionReferenceKey = null;
			if (FastCollabConstants.WARMUP_MESSAGE.equalsIgnoreCase(input.getBody())) {
				return new AWSProxyResponse("WarmUp Invoke.", "Success", FCHttpStatus.OK);
			} else {
				// Check and decrypt request if encrypted (E2E encryption support)
				isEncryptedRequest = E2EEncryptionHandler.isEncryptedRequest(input.getBody(), lambdaLogger);
				if (isEncryptedRequest) {
					encryptionReferenceKey = E2EEncryptionHandler.decryptRequest(input, lambdaLogger);
					if (encryptionReferenceKey == null) {
						LogUtils.debuglogger(lambdaLogger, "Failed to decrypt request body");
						return new AWSProxyResponse("Failed to decrypt request.", FCHttpStatus.BAD_REQUEST,
								FCHttpStatus.VALIDATION_ERROR_CODE.getCode());
					}
				}

				Map<String, Object> requestMap = new HashMap<>();
				requestMap.put("path", input.getPath());
				requestMap.put("method", input.getHttpMethod());
				requestMap.put("queryParams", input.getQueryStringParameters());
				requestMap.put("body", input.getBody());
//				LogUtils.startRequest(lambdaLogger, input.getResource(), requestMap);

				AWSProxyResponse processHandler = service.processHandler(input, context);

				LogUtils.customResponseLogger(lambdaLogger,
						LoggerFactory.getLoggerMessageDTO().withCategory(LoggerCategory.OUTPUT_RESPONSE)
								.withOriginalMessage(processHandler)
								.withKey(String.valueOf(Calendar.getInstance().getTimeInMillis())));
//				LogUtils.endRequest(lambdaLogger, input.getResource(),
//						processHandler != null ? processHandler.getBody() : "", System.currentTimeMillis() - startTime);
				// Encrypt response only if the request was encrypted (E2E encryption support)
				if (isEncryptedRequest && processHandler != null && processHandler.getBody() != null) {
					E2EEncryptionHandler.encryptResponse(input, processHandler, encryptionReferenceKey, lambdaLogger);
				}
				return processHandler;
			}
		} catch (CustomException ex) {
			LogUtils.exceptionLogger(context.getLogger(), ex);
			LogUtils.customResponseLogger(context.getLogger(),
					LoggerFactory.getLoggerMessageDTO().withCategory(LoggerCategory.OUTPUT_RESPONSE)
							.withOriginalMessage(
									new AWSProxyResponse(ex.getMessage(), ex.getStatusCode(), ex.getErrorCode()))
							.withKey(String.valueOf(Calendar.getInstance().getTimeInMillis())));
			return new AWSProxyResponse("", ex.getMessage(), ex.getStatusCode(), ex.getErrorCode());
		} catch (Exception ex) {
			LogUtils.exceptionLogger(context.getLogger(), ex);
			return new AWSProxyResponse(ex.getMessage(), FCHttpStatus.OK, FCHttpStatus.UNHANDLED_ERROR_CODE.getCode());
		}

	}

}
