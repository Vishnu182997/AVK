package com.example.handler.service;

import com.amazonaws.services.lambda.runtime.Context;
import com.example.common.request.dto.AWSProxyRequest;

/**
 * 
 * @author Bhagrava Kurella
 * 
 * This Interface class is used as a RequestServiceHandler which process the specific request. 
 * It need to be implemented for performing the task.
 * 
 *
 */
public interface RequestHandlerService <T> {

	/**
	 * @param awsProxyRequest
	 * @param context
	 * @return
	 */
	public T processHandler(AWSProxyRequest awsProxyRequest, Context context) throws Exception;

}
