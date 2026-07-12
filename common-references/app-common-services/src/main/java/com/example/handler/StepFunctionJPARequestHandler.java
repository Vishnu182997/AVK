package com.example.handler;

import java.util.Calendar;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.example.common.constant.FastCollabConstants;
import com.example.common.enums.FCHttpStatus;
import com.example.common.exceptions.CustomException;
import com.example.common.logger.LogUtils;
import com.example.common.logger.LoggerFactory;
import com.example.common.request.dto.AWSProxyRequest;
import com.example.common.request.dto.AWSProxyResponse;
import com.example.handler.service.RequestHandlerService;
import com.example.jpa.config.JPAConfiguration;
import com.example.logger.request.LoggerCategory;

/**
 * @author Bhargava Kurella
 * @version 1.0
 * @since 27/02/2020
 *
 */
public class StepFunctionJPARequestHandler implements RequestHandler<AWSProxyRequest, Object> {
	private static AnnotationConfigApplicationContext ctx =null;
	
	static {
		ctx = new AnnotationConfigApplicationContext(JPAConfiguration.class);
	}

	/**
	 * @param input request received from AWS Request Handler
	 * @return AWSProxyResponse response object which contains the body, status code and headers.
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Object handleRequest(AWSProxyRequest input, Context context) {
		try {
			if(ctx==null) {
				ctx = new AnnotationConfigApplicationContext(JPAConfiguration.class);
			}
			
			RequestHandlerService<Object> service = ctx.getBean(RequestHandlerService.class);
			
			if(FastCollabConstants.WARMUP_MESSAGE.equalsIgnoreCase(input.getBody())) {
				return new AWSProxyResponse("WarmUp Invoke.", "Success", FCHttpStatus.OK); 
			}else {
				return service.processHandler(input, context);
			}
		} catch (CustomException ex) {
			LogUtils.exceptionLogger(context.getLogger(), ex);
			LogUtils.customResponseLogger(context.getLogger(), LoggerFactory.getLoggerMessageDTO().withCategory(LoggerCategory.OUTPUT_RESPONSE).withOriginalMessage(new AWSProxyResponse(ex.getMessage(), ex.getStatusCode(), ex.getErrorCode())).withKey(String.valueOf(Calendar.getInstance().getTimeInMillis())));
			return "";
		} catch (Exception ex) {
			LogUtils.exceptionLogger(context.getLogger(), ex);
			return "";
		}
		
	}
}
