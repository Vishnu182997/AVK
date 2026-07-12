package com.example.logger.request;

import org.springframework.core.env.Environment;

import com.example.flight.common.dto.ApiCredential;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ThirdPartyLoggerDTO {
    private Object originalMessage;//Actual request/response
    private String key;//time in millis
    private String cacheKey;
    private String serviceType;//Terminology of wrt third-party
    private LoggerCategory category;
    private String log_message;
    private String lambda_Name;
    private String resource_Path;
    private Object apiCredential;
    private Environment environment;

    public ThirdPartyLoggerDTO withCacheKey(String cacheKey) {
        setCacheKey(cacheKey);
        return this;
    }

    public ThirdPartyLoggerDTO withKey(String key) {
        setKey(key);
        return this;
    }

    public ThirdPartyLoggerDTO withOriginalMessage(Object originalMessage) {
        setOriginalMessage(originalMessage);
        return this;
    }

    public ThirdPartyLoggerDTO withServiceType(String apiServiceType) {
        setServiceType(apiServiceType);
        return this;
    }

    public ThirdPartyLoggerDTO withCategory(LoggerCategory category) {
        setCategory(category);
        return this;
    }

    public ThirdPartyLoggerDTO withLog_message(String log_message) {
        setLog_message(log_message);
        return this;
    }

    public ThirdPartyLoggerDTO withLambda_Name(String lambda_Name) {
        setLambda_Name(lambda_Name);
        return this;
    }

    public ThirdPartyLoggerDTO withResource_Path(String resource_Path) {
        setResource_Path(resource_Path);
        return this;
    }

    public ThirdPartyLoggerDTO withApiCredential(Object apiCredential) {
        setApiCredential(apiCredential);
        return this;
    }
    
    public ThirdPartyLoggerDTO withEnvironement(Environment environment) {
//        setEnvironment(environment);
        return this;
    }
    
    public String printDetailsWithoutOriginalMessage() {
    	return "cacheKey:" + cacheKey + " >>>>fileName:" + serviceType +  " >>>>category:" +  category + " >>>>message:" + log_message;
    }
}
