package com.example.common.logger;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import org.apache.commons.lang3.exception.ExceptionUtils;

import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.example.common.request.dto.AWSProxyResponse;
import com.example.common.util.AvkCommonUtil;
import com.example.logger.request.LoggerMessageDTO;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 
 * @author Bhagrava Kurella
 * 
 * This is a custom logger Util class which writes the logs to Cloud Watch.
 *
 */
public class LogUtils {
	private static final int MAX_LOG_SIZE = 1024;
	private static final String INFO_LOG = "INFO";
	private static final String SQL_QUERY = "SQL_QUERY";
	private static final String THIRD_PARTY_API = "THIRD_PARTY_API";
	private static final String API_LOGS = "API_LOGS";
	private static final ThreadLocal<String> currentCorrelationId = new ThreadLocal<>();
	private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss:SSS");
	
	/**
	 * This static method is used to print messages to logger.
	 * 
	 * @param lambdaLogger contains the LambdaLogger logger object to write the logs to cloud watch.
	 * @param value prints the string values.
	 * 
	 */
	public static void printlogger(LambdaLogger lambdaLogger, String value) {
		if ("true".equalsIgnoreCase(System.getenv("PRINT_LOG"))) {
			printDebuggerLogs(lambdaLogger, value, INFO_LOG, API_LOGS);
//		if(lambdaLogger!=null)
//			lambdaLogger.log(value);
//		else
//			System.out.println(value);
		}
	}
	
	
	public static void timelogger(String text) {
		printDebuggerLogs(null, LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss:SSS"))+" --- "+ text, INFO_LOG, "TIME_CALCULATION");
//		if ("true".equalsIgnoreCase(System.getenv("TIME_LOG"))) {
			//System.out.println(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss:SSS"))+" --- "+ text);
//		}
	}
	
	
	/**
	 * This static method is used to log complete Exception stackTrace.
	 * 
	 * @param lambdaLogger contains the LambdaLogger logger object to write the logs to cloud watch.
	 * @param throwable it is the Super Throwable class, which will print the complete exception stackTrace.
	 * 
	 */
	public static void exceptionLogger(LambdaLogger lambdaLogger, Throwable throwable) {		
		exceptionLogger(lambdaLogger, throwable, "EXCEPTION_LOGGER");
//		if(lambdaLogger!=null)
//			lambdaLogger.log("Exception Logger: "+ExceptionUtils.getStackTrace(throwable));
//		else
//			((Exception)throwable).printStackTrace();
	}
	
	/**
	 * This static method is used to log complete Exception stackTrace.
	 * 
	 * @param lambdaLogger contains the LambdaLogger logger object to write the logs to cloud watch.
	 * @param throwable it is the Super Throwable class, which will print the complete exception stackTrace.
	 * 
	 */
	public static void exceptionLogger(LambdaLogger lambdaLogger, Exception exception) {
		exceptionLogger(lambdaLogger, exception, "EXCEPTION_LOGGER");
		//exception.printStackTrace();
	}
	
	/**
	 * This static method is used to write the info logger.
	 * 
	 * @param lambdaLogger contains the LambdaLogger logger object to write the logs to cloud watch.
	 * @param LoggerMessageDTO custom logger bean object.
	 * 
	 */
	public static void infoLogger(LambdaLogger lambdaLogger, LoggerMessageDTO loggerMessage) {
		if ("true".equalsIgnoreCase(System.getenv("PRINT_LOG"))) {
			printDebuggerLogs(lambdaLogger, AvkCommonUtil.getResponseBody(loggerMessage), INFO_LOG, "API_LOGS");
//		if(lambdaLogger!=null)
//			lambdaLogger.log("Info Logger: "+FastCollabCommonUtil.getResponseBody(loggerMessage));
//		else
//			System.out.println("Info Logger: "+FastCollabCommonUtil.getResponseBody(loggerMessage));
		}
	}
	
	/**
	 * This static method is used for the logging the debug messages.
	 * 
	 * @param lambdaLogger contains the LambdaLogger logger object to write the logs to cloud watch.
	 * @param LoggerMessageDTO custom logger bean object.
	 * 
	 */
	public static void debuglogger(LambdaLogger lambdaLogger, LoggerMessageDTO loggerMessage) {
		if ("true".equalsIgnoreCase(System.getenv("PRINT_LOG"))) {
			printDebuggerLogs(lambdaLogger, AvkCommonUtil.getResponseBody(loggerMessage), INFO_LOG, API_LOGS);
//		if(lambdaLogger!=null)
//			lambdaLogger.log("Debug Logger: "+FastCollabCommonUtil.getResponseBody(loggerMessage));
//		else
//			System.out.println("Debug Logger: "+FastCollabCommonUtil.getResponseBody(loggerMessage));
		}
	}
	
	/**
	 * This static method is used to write the Input Request to logger.
	 * 
	 * @param lambdaLogger contains the LambdaLogger logger object to write the logs to cloud watch.
	 * @param inputResource 
	 * @param LoggerMessageDTO custom logger bean object.
	 * 
	 */
	public static void customRequestLogger(LambdaLogger lambdaLogger, String inputResource, LoggerMessageDTO loggerMessage) {
		String inputRequestTitle = AvkCommonUtil.hasData(inputResource) ? "Input Request for " + inputResource +": " : "Input Request: ";		
		printDebuggerLogs(lambdaLogger, (inputRequestTitle + AvkCommonUtil.getResponseBody(loggerMessage)), INFO_LOG, API_LOGS);
		
//		if(lambdaLogger!=null)
//			lambdaLogger.log(inputRequestTitle + FastCollabCommonUtil.getResponseBody(loggerMessage));
//		else
//			System.out.println(inputRequestTitle + FastCollabCommonUtil.getResponseBody(loggerMessage));
	}
	
	@SuppressWarnings("unchecked")
	public static void customIPLogger(Map<String, Object> requestContext) {
		try {
			Map<String, Object> identity = AvkCommonUtil.hasData(requestContext)
					? (AvkCommonUtil.hasData(requestContext.get("identity"))
							? (Map<String, Object>) requestContext.get("identity")
							: null)
					: null;
			Object ipAddress = AvkCommonUtil.hasData(identity) ? identity.get("sourceIp") : "";
			Object userAgent = AvkCommonUtil.hasData(identity) ? identity.get("userAgent") : "";
			LogUtils.printDebuggerLogs(null, "clientIpAddress:: " + ipAddress + " ---- " + "userAgent:: " + userAgent, INFO_LOG, "AUTHENTICATED_USER_IP_INFO");
			//LogUtils.debuglogger(null, "clientIpAddress:: " + ipAddress + " ---- " + "userAgent:: " + userAgent);
			}catch (Exception e) {
				LogUtils.debuglogger(null, "requestContext:: "+requestContext);
				e.printStackTrace();
			}
	}
	
	/**
	 * This static method is used to write the Output Response to logger.
	 * 
	 * @param lambdaLogger contains the LambdaLogger logger object to write the logs to cloud watch.
	 * @param LoggerMessageDTO custom logger bean object.
	 * 
	 */
	public static void customResponseLogger(LambdaLogger lambdaLogger, LoggerMessageDTO loggerMessage) {
		if ("true".equalsIgnoreCase(System.getenv("PRINT_LOG"))) {
			printDebuggerLogs(lambdaLogger, (AvkCommonUtil.getResponseBody(loggerMessage)), INFO_LOG, "OUTPUT RESPONSE");
			
//		if(lambdaLogger!=null)
//			lambdaLogger.log("OutPut Response: "+FastCollabCommonUtil.getResponseBody(loggerMessage));
//		else
//			System.out.println("OutPut Response: "+FastCollabCommonUtil.getResponseBody(loggerMessage));
		}
	}
	
	/**
	 * This static method is used to write the DB Queries to logger. CURD operation queries, Store Procedures calling etc...
	 * 
	 * @param lambdaLogger contains the LambdaLogger logger object to write the logs to cloud watch.
	 * @param LoggerMessageDTO custom logger bean object.
	 * 
	 */
	public static void customSQLQueryLogger(LambdaLogger lambdaLogger, LoggerMessageDTO loggerMessage) {
		if ("true".equalsIgnoreCase(System.getenv("PRINT_LOG"))) {
			printSqlLogs(lambdaLogger, (AvkCommonUtil.getResponseBody(loggerMessage)), INFO_LOG, SQL_QUERY);
//		if(lambdaLogger!=null)
//			lambdaLogger.log("SQL Query Logger: "+FastCollabCommonUtil.getResponseBody(loggerMessage));
//		else
//			System.out.println("SQL Query Logger: "+FastCollabCommonUtil.getResponseBody(loggerMessage));
		}
	}
	
	/**
	 * This static method is used to write the Third Party API's Request/Response to the loggers.
	 * 
	 * @param lambdaLogger contains the LambdaLogger logger object to write the logs to cloud watch.
	 * @param LoggerMessageDTO custom logger bean object.
	 * 
	 */
	public static void thirdPartyApiLogger(LambdaLogger lambdaLogger, LoggerMessageDTO loggerMessage) {
		if ("true".equalsIgnoreCase(System.getenv("PRINT_LOG"))) {
			printDebuggerLogs(lambdaLogger, (AvkCommonUtil.getResponseBody(loggerMessage)), INFO_LOG, THIRD_PARTY_API);
//		if(lambdaLogger!=null)
//			lambdaLogger.log("Third Party Api Logger: "+FastCollabCommonUtil.getResponseBody(loggerMessage));
//		else
//			System.out.println("Third Party Api Logger: "+FastCollabCommonUtil.getResponseBody(loggerMessage));
		}
	}
	
	/**
	 * This static method is used to write the logger.
	 * 
	 * @param lambdaLogger contains the LambdaLogger logger object to write the logs to cloud watch.
	 * @param LoggerMessageDTO custom logger bean object.
	 * 
	 */
	public static void customLogger(LambdaLogger lambdaLogger, LoggerMessageDTO loggerMessage) {
		if ("true".equalsIgnoreCase(System.getenv("PRINT_LOG"))) {
			printDebuggerLogs(lambdaLogger, (AvkCommonUtil.getResponseBody(loggerMessage)), INFO_LOG, API_LOGS);
//		if(lambdaLogger!=null)
//			lambdaLogger.log("Custom Logger: "+FastCollabCommonUtil.getResponseBody(loggerMessage));
//		else
//			System.out.println("Custom Logger: "+FastCollabCommonUtil.getResponseBody(loggerMessage));
		}
	}
	
	public static void debuglogger(LambdaLogger lambdaLogger, String value) {
		
		if (value.length() > MAX_LOG_SIZE) {
        	value = value.substring(0, MAX_LOG_SIZE - 50);
        }
        
        // Batch multiple lines into single log entry
        if (value.contains("\n")) {
        	value = value.replace("\n", " | ");
        }
				
		if(lambdaLogger!=null)
			lambdaLogger.log(value);
		else
			System.out.println(value);
		
	}
	
	/**
     * Initialize correlation ID for the request
     */
    public static String initializeCorrelationId(String requestId) {
        String correlationId = requestId != null ? requestId : UUID.randomUUID().toString();
        currentCorrelationId.set(correlationId);
        return correlationId;
    }
    
    /**
     * Get current correlation ID
     */
    public static String getCurrentCorrelationId() {
        String correlationId = currentCorrelationId.get();
        return correlationId != null ? correlationId : initializeCorrelationId(null);
    }
    
    /**
     * Clear correlation ID after request completion
     */
    public static void clearCorrelationId() {
        currentCorrelationId.remove();
    }
    
    
    /**
     * Truncates and formats large objects for logging
     */
    private static String truncateMessage(Object message, int maxSize) {
        if (message == null) return "null";
        
        String stringMessage;
        try {
            if (message instanceof String) {
                stringMessage = (String) message;
            } else if (message instanceof Map) {
                // For Maps, only log keys and value lengths or types
                Map<String, Object> summarizedMap = summarizeMap((Map<?, ?>) message);
                stringMessage = objectMapper.writeValueAsString(summarizedMap);
            } else {
                stringMessage = objectMapper.writeValueAsString(summarizeObject(message));
            }
            
            if (stringMessage.length() > maxSize) {
                return stringMessage.substring(0, maxSize - 50) + "...[Truncated]";
            }
            return stringMessage;
        } catch (Exception e) {
            return "Error converting message to string: " + e.getMessage();
        }
    }
    
    /**
     * Summarizes large objects by including only essential fields
     */
    private static Object summarizeObject(Object obj) {
        if (obj == null) return null;
        
        if (obj instanceof Collection<?>) {
            Collection<?> collection = (Collection<?>) obj;
            Map<String, Object> summary = new HashMap<>();
            summary.put("size", collection.size());
            if (!collection.isEmpty()) {
                summary.put("type", collection.iterator().next().getClass().getSimpleName());
                summary.put("sample", collection.stream()
                    .limit(2)
                    .map(item -> summarizeValue(item))
                    .collect(Collectors.toList()));
            }
            return summary;
        }
        
        if (obj instanceof AWSProxyResponse) {
            AWSProxyResponse response = (AWSProxyResponse) obj;
            Map<String, Object> summary = new HashMap<>();
            summary.put("statusCode", response.getStatusCode());
            // Truncate response body if present
            if (response.getBody() != null) {
                summary.put("bodyLength", response.getBody().length());
                summary.put("bodySample", truncateMessage(response.getBody(), 100));
            }
            return summary;
        }
        
        // For other objects, return a simple string representation
        return obj.toString();
    }
    
    /**
     * Summarizes map values to prevent large data logging
     */
    private static Map<String, Object> summarizeMap(Map<?, ?> map) {
        Map<String, Object> summarized = new HashMap<>();
        for (Map.Entry<?, ?> entry : map.entrySet()) {
            String key = String.valueOf(entry.getKey());
            summarized.put(key, summarizeValue(entry.getValue()));
        }
        return summarized;
    }

    /**
     * Summarizes individual values based on their type
     */
    private static Object summarizeValue(Object value) {
        if (value == null) return null;
        
        if (value instanceof String) {
            String str = (String) value;
            if (str.length() > 100) {
                return str.substring(0, 97) + "...";
            }
            return str;
        }
        
        if (value instanceof Collection<?>) {
            Collection<?> collection = (Collection<?>) value;
            return String.format("[Collection of %d items]", collection.size());
        }
        
        if (value instanceof Map) {
            Map<?, ?> map = (Map<?, ?>) value;
            return String.format("[Map with %d entries]", map.size());
        }
        
        return value;
    }
    
    /**
     * Creates a structured log entry with consistent format
     */
    private static String createStructuredLog(String level, String action, Object message) {
        try {
            Map<String, Object> logEntry = new HashMap<>();
            String correlationId = getCurrentCorrelationId();
            
            // Basic log information
            logEntry.put("timestamp", LocalDateTime.now().format(DATE_FORMATTER));
            logEntry.put("correlationId", correlationId);
            logEntry.put("level", level!=null ? level : INFO_LOG);
            logEntry.put("action", action!=null ? action : API_LOGS);
            
//         // Truncate and summarize the message
//            Object processedMessage = message instanceof Map ? 
//                summarizeMap((Map<?, ?>) message) : 
//                summarizeObject(message);
//            logEntry.put("data", processedMessage);
//            
//            return truncateMessage(logEntry, MAX_LOG_SIZE);
            
            // Add message data
            if (message instanceof Map) {
                logEntry.put("data", message);
            } else if (message instanceof LoggerMessageDTO) {
                logEntry.put("data", objectMapper.convertValue(message, Map.class));
            } else {
                logEntry.put("message", message);
            }
            return objectMapper.writeValueAsString(logEntry);
        } catch (Exception e) {
            return String.format("Log creation failed: %s", e.getMessage());
        }
    }
    
    
    /**
     * Start request logging
     */
    public static void printDebuggerLogs(LambdaLogger lambdaLogger, String message, String logLevel,String action) {
    	Map<String, Object> startData = new HashMap<>();
    	
    	if ("true".equalsIgnoreCase(System.getenv("PRINT_LOG"))) {
    		startData.put("info", message);
    	}else {
    		startData.put("info", truncateMessage(message,MAX_LOG_SIZE));
    	}                
        String logEntry = createStructuredLog(logLevel, action, startData);
        writeLog(lambdaLogger, logEntry);
    }
    
    /**
     * Start request logging
     */
    public static void printThirdPartyDebuggerLogs(LambdaLogger lambdaLogger, String message, String logLevel,String action) {
    	Map<String, Object> startData = new HashMap<>();
    	
    	if ("true".equalsIgnoreCase(System.getenv("PRINT_LOG")) || "true".equalsIgnoreCase(System.getenv("PRINT_SUPPLIER_REQ_RES"))) {
    		startData.put("info", message);
    	}else {
    		startData.put("info", truncateMessage(message,MAX_LOG_SIZE));
    	}                
        String logEntry = createStructuredLog(logLevel, THIRD_PARTY_API, startData);
        writeLog(lambdaLogger, logEntry);
    }
    
    /**
     * Start request logging
     */
    public static void printSqlLogs(LambdaLogger lambdaLogger, String message, String logLevel,String action) {
        Map<String, Object> startData = new HashMap<>();
        startData.put("query", message);        
        String logEntry = createStructuredLog(logLevel, action, startData);
        writeLog(lambdaLogger, logEntry);
    }
    
    /**
     * Start request logging
     */
    public static void startRequest(LambdaLogger lambdaLogger, String resource, Map<String, Object> requestData) {
        Map<String, Object> startData = new HashMap<>();
        startData.put("resource", resource);
        startData.put("requestData", requestData);
        
        String logEntry = createStructuredLog(INFO_LOG, "REQUEST_START", startData);
        writeLog(lambdaLogger, logEntry);
    }
    
    /**
     * End request logging
     */
    public static void endRequest(LambdaLogger lambdaLogger, String resource, Object response, long executionTime) {
        Map<String, Object> endData = new HashMap<>();
        endData.put("resource", resource);
        endData.put("response", truncateMessage(response,MAX_LOG_SIZE));
        endData.put("executionTimeMs", executionTime);
        
        String logEntry = createStructuredLog(INFO_LOG, "REQUEST_END", endData);
        writeLog(lambdaLogger, logEntry);
    }
    
    private static void writeLog(LambdaLogger lambdaLogger, String logEntry) {
        if (lambdaLogger != null) {
            lambdaLogger.log(logEntry + "\n");  // Add newline for better CloudWatch formatting
        } else {
            System.out.println(logEntry+ "\n");
        }
    }
    
    /**
     * Log exceptions with full context
     */
    public static void exceptionLogger(LambdaLogger lambdaLogger, Throwable throwable, String context) {
        Map<String, Object> errorData = new HashMap<>();
        errorData.put("context", context);
        errorData.put("message", throwable.getMessage());
        errorData.put("type", throwable.getClass().getName());
        errorData.put("stackTrace", ExceptionUtils.getStackTrace(throwable));
        
        String logEntry = createStructuredLog("ERROR", "EXCEPTION", errorData);
        writeLog(lambdaLogger, logEntry);
    }

}
