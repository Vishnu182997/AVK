package com.example.common.logger;

import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.example.common.util.FastCollabCommonUtil;

import java.util.Map;

/**
 * Utility to publish exception details to an external Python Lambda which
 * persists logs to ClickHouse. This class is intentionally separate from
 * existing logging utilities so that existing behaviour remains untouched.
 */
public final class PythonExceptionLogger {

    /** Name of the Python Lambda that stores exception logs. */
    private static final String PYTHON_EXCEPTION_LAMBDA = "python-aws-logger-analyzer";

    private PythonExceptionLogger() {}

    /**
     * Send the given exception data to the Python Lambda asynchronously. Any
     * errors during invocation are logged but not propagated.
     *
     * @param exceptionData structured exception information
     * @param logger optional LambdaLogger for debug output
     */
    public static void publish(Map<String, Object> exceptionData, LambdaLogger logger) {
        try {
            FastCollabCommonUtil.invokeLambdaAsyn(exceptionData, null, PYTHON_EXCEPTION_LAMBDA, logger);
        } catch (Exception ex) {
            if (logger != null) {
                logger.log("Failed to invoke exception lambda: " + ex.getMessage());
            } else {
                System.out.println("Failed to invoke exception lambda: " + ex.getMessage());
            }
        }
    }
}