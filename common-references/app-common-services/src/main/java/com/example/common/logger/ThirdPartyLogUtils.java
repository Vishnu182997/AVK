package com.example.common.logger;

import com.amazon.sqs.javamessaging.AmazonSQSExtendedClient;
import com.amazon.sqs.javamessaging.ExtendedClientConfiguration;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.BucketLifecycleConfiguration;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import com.example.common.util.FastCollabCommonUtil;
import com.example.logger.request.LoggerCategory;
import com.example.logger.request.LoggerMessageDTO;
import com.example.logger.request.ThirdPartyLoggerDTO;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class ThirdPartyLogUtils {

    private static final Logger logger = LogManager.getLogger();
    private static AmazonSQS sqsExtended = null;
    private static AmazonS3 s3 = null;

    public static void exceptionLogger(Throwable throwable) {
        //logger.error("Exception Logger: " + ExceptionUtils.getStackTrace(throwable));
    	LogUtils.exceptionLogger(null, throwable, "Exception in Third Party Api");
    }

    public static void exceptionLogger(String errorMessage) {
        //logger.error("Exception Logger: " + errorMessage);
        LogUtils.printThirdPartyDebuggerLogs(null, errorMessage, "ERROR", null);
    }

    private static void customRequestLogger(ThirdPartyLoggerDTO loggerMessage) {
        logger.info("Input Request: " + FastCollabCommonUtil.getResponseBody(loggerMessage));
    }

    private static void customResponseLogger(ThirdPartyLoggerDTO loggerMessage) {
        logger.info("OutPut Response: " + FastCollabCommonUtil.getResponseBody(loggerMessage));
    }

    private static void exceptionLogger(ThirdPartyLoggerDTO loggerMessage) {
        //logger.info("Exception: " + FastCollabCommonUtil.getResponseBody(loggerMessage));
        LogUtils.printThirdPartyDebuggerLogs(null, FastCollabCommonUtil.getResponseBody(loggerMessage), "ERROR", null);
    }

    public static void log(ThirdPartyLoggerDTO loggerMessage) {
        if ("true".equalsIgnoreCase(System.getenv("ENABLE_THIRD_PARTY_LOGGERS"))) {
            switch (loggerMessage.getCategory()) {
                case API_INPUT_REQUEST:
                case API_OUTPUT_RESPONSE:
                	boolean printLog = thirdPartyReqRspLogger(loggerMessage);
                    CompletableFuture.runAsync(() -> {
                        sendLogToSQS(loggerMessage, printLog);
                    });
                    break;
                case INPUT_REQUEST:
                    customRequestLogger(loggerMessage);
                    break;
                case OUTPUT_RESPONSE:
                    customResponseLogger(loggerMessage);
                    break;
                default:
                    exceptionLogger(loggerMessage);
            }
        } else if (loggerMessage.getCategory() == LoggerCategory.API_INPUT_REQUEST || loggerMessage.getCategory() == LoggerCategory.API_OUTPUT_RESPONSE) {
            CompletableFuture.runAsync(() -> {
                sendLogToSQS(loggerMessage, false);
            });
        }
    }

	
	@SuppressWarnings("unchecked")
	public static void customIPLogger(Map<String, Object> requestContext) {
		try {
			Map<String, Object> identity = FastCollabCommonUtil.hasData(requestContext)
					? (FastCollabCommonUtil.hasData(requestContext.get("identity"))
							? (Map<String, Object>) requestContext.get("identity")
							: null)
					: null;
			Object ipAddress = FastCollabCommonUtil.hasData(identity) ? identity.get("sourceIp") : "";
			Object userAgent = FastCollabCommonUtil.hasData(identity) ? identity.get("userAgent") : "";
			LogUtils.printlogger(null, "clientIpAddress:: " + ipAddress + " ---- " + "userAgent:: " + userAgent);
			}catch (Exception e) {
				LogUtils.printlogger(null, "requestContext:: "+requestContext);
				e.printStackTrace();
			}
	}
    private static boolean thirdPartyReqRspLogger(ThirdPartyLoggerDTO loggerMessage) {
    	if ("true".equalsIgnoreCase(System.getenv("PRINT_LOG")) || "true".equalsIgnoreCase(System.getenv("PRINT_SUPPLIER_REQ_RES"))) {
    		//logger.info("Third Party Api Logger: " + FastCollabCommonUtil.getResponseBody(loggerMessage));
    		LogUtils.printThirdPartyDebuggerLogs(null, FastCollabCommonUtil.getResponseBody(loggerMessage), null, null);
    		return true;
    	} else {//TODO: please modify "printDetailsWithoutOriginalMessage" method for change any modification to print details
    		//logger.info("Third Party Api Logger: " + loggerMessage.printDetailsWithoutOriginalMessage());
    		LogUtils.printThirdPartyDebuggerLogs(null, loggerMessage.printDetailsWithoutOriginalMessage(), null, null);
    		return false;
    	}
    }

    public static void debuggerLog(String value) {
        logger.debug(value);
    }

    public static void printLog(Object value) {
    	if ("true".equalsIgnoreCase(System.getenv("PRINT_LOG"))) {
	        if (value instanceof String) {
	            logger.info(value);
	        } else {
	            logger.info(FastCollabCommonUtil.getResponseBody(value));
	        }
    	}
    }

    private static void sendLogToSQS(ThirdPartyLoggerDTO thirdPartyLoggerDTO, boolean printLog) {
        try {
        	printLog("<<<<<<<<<<<<<<< START SETTING LOGS TO SQS >>>>>>>>>>>>>>>>>>>>>>>>>");
            String third_party_loggers_sqs_queue_name = System.getenv("THIRD_PARTY_LOGGERS_SQS_QUEUE_NAME");
            String sqs_log_s3_bucket_name = System.getenv("SQS_LOG_S3_BUCKET_NAME");
            if (FastCollabCommonUtil.isStringNotEmpty(third_party_loggers_sqs_queue_name) && FastCollabCommonUtil.isStringNotEmpty(sqs_log_s3_bucket_name)) {
                /*
                 * Set the Amazon S3 bucket name, and then set a lifecycle rule on the
                 * bucket to permanently delete objects 14 days after each object's
                 * creation date.
                 */
                if (s3 == null) {
                    s3 = AmazonS3ClientBuilder.defaultClient();
                    //Handle the exception while applying policy to the Amazon s3
                    try {
                    	BucketLifecycleConfiguration eva_s3_bucket_conf = s3.getBucketLifecycleConfiguration(sqs_log_s3_bucket_name);
                    	if (eva_s3_bucket_conf == null || eva_s3_bucket_conf.getRules() == null || eva_s3_bucket_conf.getRules().isEmpty()) {
                            BucketLifecycleConfiguration.Rule expirationRule = new BucketLifecycleConfiguration.Rule();
                            expirationRule.withExpirationInDays(14).withStatus("Enabled");
                            BucketLifecycleConfiguration lifecycleConfig = new BucketLifecycleConfiguration().withRules(expirationRule);
                            s3.setBucketLifecycleConfiguration(sqs_log_s3_bucket_name, lifecycleConfig);
                        }
                    } catch (Exception e) {
                    	ThirdPartyLogUtils.exceptionLogger(e);
					}
                }

                /*
                 * Set the Amazon SQS extended client configuration with large payload
                 * support enabled.
                 */
                if (sqsExtended == null) {
                    ExtendedClientConfiguration extendedClientConfig = new ExtendedClientConfiguration().withLargePayloadSupportEnabled(s3, sqs_log_s3_bucket_name);
                    sqsExtended = new AmazonSQSExtendedClient(AmazonSQSClientBuilder.defaultClient(), extendedClientConfig);
                }

                String queueUrl = sqsExtended.getQueueUrl(third_party_loggers_sqs_queue_name).getQueueUrl();
                SendMessageRequest send_msg_request = new SendMessageRequest()
                        .withQueueUrl(queueUrl)
                        .withMessageBody(FastCollabCommonUtil.getResponseBody(thirdPartyLoggerDTO));
                sqsExtended.sendMessage(send_msg_request);
                printLog("<<<<<<<<<<<<<<< END SETTING LOGS TO SQS >>>>>>>>>>>>>>>>>>>>>>>>>"+ sqsExtended);
            } else {
                printLog("The SQS-Queue is not created for the Third party API-Loggers");
            }
        } catch (Exception e) {
        	if(!printLog) {
        		logger.info("Third Party Api Logger: " + FastCollabCommonUtil.getResponseBody(thirdPartyLoggerDTO));
        	}
            exceptionLogger(e);
        }
    }
}
