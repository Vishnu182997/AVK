package com.example.handler;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.example.app.config.AppConfiguration;
import com.example.common.constant.FastCollabConstants;
import com.example.common.enums.FCHttpStatus;
import com.example.common.logger.ThirdPartyLogUtils;
import com.example.common.request.dto.AWSProxyRequest;
import com.example.common.request.dto.AWSProxyResponse;
import com.example.handler.service.RequestHandlerService;

/**
 * @author vishnu
 * @version 1.0
 * @since 11/03/2020
 */
public class ThirdPartyRequestDispatcherHandler implements RequestHandler<AWSProxyRequest, AWSProxyResponse> {
    private static AnnotationConfigApplicationContext ctx = null;

    static {
        ctx = new AnnotationConfigApplicationContext(AppConfiguration.class);
    }

    /**
     * @param input request received from AWS Request Handler
     * @return AWSProxyResponse response object which contains the body, status code and headers.
     */
    @Override
    public AWSProxyResponse handleRequest(AWSProxyRequest input, Context context) {
        try {
            if (ctx == null) {
                ctx = new AnnotationConfigApplicationContext(AppConfiguration.class);
            }
            ThirdPartyLogUtils.printLog("REQUEST SERVED");
            RequestHandlerService service = ctx.getBean(RequestHandlerService.class);

            if (FastCollabConstants.WARMUP_MESSAGE.equalsIgnoreCase(input.getBody())) {
                return new AWSProxyResponse("WarmUp Invoke.", "Success", FCHttpStatus.OK);
            } else {
                return (AWSProxyResponse) service.processHandler(input, context);
            }
        }catch (Exception ex) {
            ThirdPartyLogUtils.exceptionLogger(ex);
            return new AWSProxyResponse(ex.getMessage(), FCHttpStatus.OK, FCHttpStatus.UNHANDLED_ERROR_CODE.getCode());
        }

    }
}
