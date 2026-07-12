package com.example.handler.service;

import java.util.HashMap;
import java.util.Map;

import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.example.common.logger.LogUtils;
import com.example.common.request.dto.AWSProxyRequest;
import com.example.common.request.dto.AWSProxyResponse;
import com.example.common.util.EncryptionUtil;
import com.example.common.util.FastCollabCommonUtil;

/**
 * Utility class for handling End-to-End encryption of request and response payloads.
 * 
 * @author EVA Team
 * @version 1.0
 */
@SuppressWarnings("unused")
public class E2EEncryptionHandler {

   
	private static final String ENCRYPTION_SECRET_KEY_ENV = "E2E_ENCRYPTION_SECRET_KEY";
    private static final String DEFAULT_ENCRYPTION_SECRET_KEY = "Usdb5aPIHV0TL8Dv";

    /**
     * Check if request contains encrypted data (hashRequest field)
     */
    public static boolean isEncryptedRequest(String requestBody, LambdaLogger logger) {
        if (FastCollabCommonUtil.isBlank(requestBody)) {
            return false;
        }
        try {
            Map<Object, Object> requestMap = FastCollabCommonUtil.getMapResponse(requestBody, Object.class);
            if (requestMap != null && requestMap.containsKey("hashRequest")) {
                Object hashRequest = requestMap.get("hashRequest");
                return hashRequest != null && !FastCollabCommonUtil.isBlank(hashRequest.toString());
            }
        } catch (Exception e) {
            // Not a valid JSON or doesn't have hashRequest - treat as non-encrypted
            return false;
        }
        return false;
    }

    /**
     * Decrypt the encrypted request body and update AWSProxyRequest.
     * Returns the timestamp/referenceKey from the request for use in response.
     */
    public static String decryptRequest(AWSProxyRequest input, LambdaLogger logger) {
        if (input == null || FastCollabCommonUtil.isBlank(input.getBody())) {
            return null;
        }
        try {
        	String idToken = null;
        	if(input!=null && input.getHeaders()!=null && !input.getHeaders().isEmpty()) {
				idToken = FastCollabCommonUtil.getAuthorizationToken(input.getHeaders());
				if(!FastCollabCommonUtil.isNullOrEmpty(idToken)) {
					return null;
				}
			}
            Map<Object, Object> requestMap = FastCollabCommonUtil.getMapResponse(input.getBody(), Object.class);
            if (requestMap == null || !requestMap.containsKey("hashRequest")) {
                LogUtils.debuglogger(logger, "Invalid encrypted request format.");
                return null;
            }
            
            Object hashRequestObj = requestMap.get("hashRequest");
            if (hashRequestObj == null || FastCollabCommonUtil.isBlank(hashRequestObj.toString())) {
                LogUtils.debuglogger(logger, "hashRequest is empty.");
                return null;
            }
            
            String hashRequest = hashRequestObj.toString();
            Object timestampObj = requestMap.get("referenceKey");
            String referenceKey = (timestampObj != null) ? timestampObj.toString() : String.valueOf(System.currentTimeMillis());
             
            String secretKey = getSecretKey(idToken);
            
            LogUtils.debuglogger(logger, "Decrypting request with referenceKey: " + referenceKey);
            LogUtils.debuglogger(logger, "Decrypting request with secretKey: " + secretKey);
            String decryptedBody = EncryptionUtil.createTripDecryptionLogic(hashRequest, secretKey);
            LogUtils.debuglogger(logger, "decryptedBody:: " + decryptedBody);
            input.setBody(decryptedBody);
            LogUtils.debuglogger(logger, "Request decrypted successfully");
            return referenceKey;
        } catch (Exception e) {
            LogUtils.debuglogger(logger, "Exception during decryption: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Encrypt the response body and update AWSProxyResponse
     */
    public static boolean encryptResponse(AWSProxyRequest input, AWSProxyResponse response, String referenceKey, LambdaLogger logger) {
        if (response == null || FastCollabCommonUtil.isBlank(response.getBody())) {
            return false;
        }
        try {
        	String idToken = null;
        	if(input!=null && input.getHeaders()!=null && !input.getHeaders().isEmpty()) {
				idToken = FastCollabCommonUtil.getAuthorizationToken(input.getHeaders());
			}
            String secretKey = getSecretKey(idToken);
            String encryptedBody = EncryptionUtil.createTripEncryptionLogic(response.getBody(), secretKey);
            
            Map<String, String> encryptedMap = new HashMap<>();
            encryptedMap.put("referenceKey", referenceKey != null ? referenceKey : String.valueOf(System.currentTimeMillis()));
            encryptedMap.put("hashResponse", encryptedBody);
//            encryptedMap.put("hashRequest", encryptedBody);
            
            response.setBody(FastCollabCommonUtil.getResponseBody(encryptedMap));
            LogUtils.debuglogger(logger, "Response encrypted successfully");
            return true;
        } catch (Exception e) {
            LogUtils.debuglogger(logger, "Exception during encryption: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Get secret key from env variable or use default
     */
    private static String getSecretKey(String idToken) {
    	idToken = idToken.substring(idToken.length()-16, idToken.length());
    	return idToken;
//        String key = System.getenv(ENCRYPTION_SECRET_KEY_ENV);
//        return FastCollabCommonUtil.isBlank(key) ? DEFAULT_ENCRYPTION_SECRET_KEY : key;
    }
    
    
    public static void main(String[] args) {
    	String s = "eyJraWQiOiJZZmZVTUpXTkJacWJKeEhNNWI4R214OHk1Q2VyWnNqaGQwS1EyTVF6cmFnPSIsImFsZyI6IlJTMjU2In0.eyJjdXN0b206Q29ycG9yYXRlX05hbWUiOiJMTlQgR2hvc3QiLCJjdXN0b206Y291bnRyeSI6IklORElBIiwic3ViIjoiNzA2M2Y5NjYtNGMwNC00MTU0LTlkMGMtYWVjODFmMGE3ZTQ3IiwiY29nbml0bzpncm91cHMiOlsiZGV2ZWxvcGVycyJdLCJlbWFpbF92ZXJpZmllZCI6dHJ1ZSwiZ2VuZGVyIjoiTUFMRSIsImlzcyI6Imh0dHBzOlwvXC9jb2duaXRvLWlkcC5hcC1zb3V0aC0xLmFtYXpvbmF3cy5jb21cL2FwLXNvdXRoLTFfOE9YeEF6dlhlIiwiY29nbml0bzp1c2VybmFtZSI6IjcwNjNmOTY2LTRjMDQtNDE1NC05ZDBjLWFlYzgxZjBhN2U0NyIsImN1c3RvbTpVc2VyX0lkIjoiMTE1MjAzOCIsImNvZ25pdG86cm9sZXMiOlsiYXJuOmF3czppYW06OjAwOTI2NTI0NzA5NTpyb2xlXC9hd3Mtc2VydmljZS1yb2xlXC9zdXBwb3J0LmFtYXpvbmF3cy5jb21cL0FXU1NlcnZpY2VSb2xlRm9yU3VwcG9ydCJdLCJhdWQiOiI1YXM4a2gwN2tnOTExb2ptcmo0MTcyaHQ4ayIsImV2ZW50X2lkIjoiZjBkZGYwMzgtZDlmZi00MTc2LThiYmEtNGZjYzBlNWYwY2U2IiwidG9rZW5fdXNlIjoiaWQiLCJhdXRoX3RpbWUiOjE3Nzg1NzQ2MjksIm5hbWUiOiJHaG9zdCIsInBob25lX251bWJlciI6Iis5MTEyMzQ1Njc4OTAiLCJleHAiOjE3Nzg2NjcwMzMsImlhdCI6MTc3ODU4MDYzMywiZW1haWwiOiJmY3N1cHBvcnQubHR0c0BmYXN0Y29sbGFiLmNvbSJ9.hWAesq_0JSYZRv_2xQBgNIXfHSw_dgSooVETjIiqgJEmCr1YL6XhFC6ytstT5jV5mNLD6a-0TUYJYU3KebZwcpZT6BfnRF-VCCTfOSaE8H8UWaWoc9cLMn5LlqfAySPxLag27_9Jv-4wYwPr8XRb44lE0n2uYagbEt5nZIOSocOjEpF90eLTdMAsvUsssQq-s5V2RNyjzRsqQ2EdQ_naYgn-dIZr6pFCXwn4lSvKlGMLUW3FDf3cJd5qYpjOmU1HpvXQRpRuZwMsUv8rDoVsuZHQTo_q_1OAP3xqMN4EvG3fxtJKIQoGkimBUJvZkxK50XAiCiilN9IUwf5hk0A5Iw";
    	s = s.substring(s.length()-16, s.length());
    	System.out.println(s);
    	AWSProxyRequest input = new AWSProxyRequest();
    	input.setBody("{\"legalEntitys\":\"26009,26010,26011,26012,26013,26014,26015,26016,26017,26018,26019,26020,26021,26022,26023,26024,26025,26026,26117,26118,26124,26125,26126,26127,26128,26129,26130,26131,26132,26133,26134,26135,25989\",\"fromDate\":\"12-04-2026\",\"toDate\":\"12-05-2026\",\"statusIds\":\"\",\"consultantId\":[],\"departmentName\":[],\"searchText\":\"\",\"subFilterId\":\"\",\"actionFilter\":1,\"userId\":1152038,\"userGuid\":\"b991daa143e85d754358d196065ffbad\",\"isAgentView\":1,\"pageIndex\":1,\"pageSize\":20,\"labelId\":1,\"viewType\":1,\"actionType\":null,\"userType\":1,\"afterKey\":null,\"tripId\":null,\"tripGuid\":\"\",\"isCart\":false,\"extTMCCompanyId\":null,\"extTMCCompanyGuid\":null,\"timeZoneId\":\"Asia/Kolkata\"}");
    	Map<String,String> headers = new HashMap<String, String>();
    	headers.put("Authorization", "Bearer eyJraWQiOiJQcGxoVDg1QjVMeUx0elNmeG11MHBYeXNBTit2NUZxR1NsOVhqeFBFcHdVPSIsImFsZyI6IlJTMjU2In0.eyJjdXN0b206Q29ycG9yYXRlX05hbWUiOiJMJlQgVGVjaG5vbG9neSBTZXJ2aWNlcyIsImN1c3RvbTpjb3VudHJ5IjoiSU5ESUEiLCJzdWIiOiI5NDI4MzUwNi01ZmI5LTQ0OWYtOWI4OS1iOGZiMDI2ZWVjMmEiLCJjb2duaXRvOmdyb3VwcyI6WyJkZXZlbG9wZXJzIl0sImVtYWlsX3ZlcmlmaWVkIjp0cnVlLCJnZW5kZXIiOiJNQUxFIiwiaXNzIjoiaHR0cHM6XC9cL2NvZ25pdG8taWRwLmFwLXNvdXRoLTEuYW1hem9uYXdzLmNvbVwvYXAtc291dGgtMV9Ua2xuOUtueVYiLCJjb2duaXRvOnVzZXJuYW1lIjoiOTQyODM1MDYtNWZiOS00NDlmLTliODktYjhmYjAyNmVlYzJhIiwiY3VzdG9tOlVzZXJfSWQiOiIxMzg2OTQiLCJjb2duaXRvOnJvbGVzIjpbImFybjphd3M6aWFtOjo0MTc3NDQ0NDM2MDI6cm9sZVwvYXdzLXNlcnZpY2Utcm9sZVwvc3VwcG9ydC5hbWF6b25hd3MuY29tXC9BV1NTZXJ2aWNlUm9sZUZvclN1cHBvcnQiXSwiYXVkIjoiN2l2MTN2bGY2a2VrNHRnaDNjcWllYW1uNjEiLCJldmVudF9pZCI6ImRhNTg3YjAwLWE4ZTAtNGFkYi05OWY5LTk5ZTU4MDZmMmUwOCIsInRva2VuX3VzZSI6ImlkIiwiYXV0aF90aW1lIjoxNzc4NjU4NDk2LCJuYW1lIjoiUkFKRVNIUkkiLCJwaG9uZV9udW1iZXIiOiIrOTExMjM0NTY3ODkwIiwiZXhwIjoxNzc4NzQ0ODk2LCJpYXQiOjE3Nzg2NTg0OTYsImVtYWlsIjoiUkFKRVNIUkkuTUFOT0hBUkBMVFRTLkNPTSJ9.VPZuNYHdr4bpVPP2QcRtjf46eV2EtkPb29wg90mHu7HAtALa7Ewjme-Ejxv6Y7TscgGIwVyyEZp8rZZFQVFxgLzq3Xbc9qzC6knWpgXeY912BM766bo5pyFVpBn_XHIi0uYr2-CT2lECu3-64LMl7z10bNt-xVBtzHbRKUNItoduYX4x0azIjgWsib9lDaeSsMggvWTMY9Odjml6cn7c-KbDd0eRIzmhoWBlNiLW1f0KkJBd0VhTZw8cz-cGohybfJQvw7J20RSBFeSD1yOiKsHdoPyC7jV7b074BJDvTLBh7_nnXduIUzaAwAeR0GfwCt002aoD2VsRl18m7Z2HAQ");
    	input.setHeaders(headers);
    	AWSProxyResponse response = new AWSProxyResponse();
    	response.setBody("{\"referenceKey\":\"138694992814601778659137713\",\"hashRequest\":\"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\"}");
//     	encryptResponse(input, response, DEFAULT_ENCRYPTION_SECRET_KEY, null);
//     	ObjectMapper mapper = new ObjectMapper();
//     	Map<String, String> data = mapper.convertValue(
//     	    response.getBody(),
//     	    new TypeReference<Map<String, String>>() {}
//     	);
//     	String hashResponse = data.get("hashResponse");
     	input.setBody(response.getBody());
     	decryptRequest(input, null);
     	//System.out.println(response.getBody());
	}
}
