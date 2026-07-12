package com.example.common.util;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.core.env.Environment;

import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.example.common.enums.FCHttpStatus;
import com.example.common.exceptions.CustomException;
import com.example.common.logger.LogUtils;
import com.example.common.logger.ThirdPartyLogUtils;
import com.example.common.request.dto.CommonStatusCode;

public class HttpPostCommonRequest implements Serializable {

    private static final String CONTENT_TYPE = "Content-Type";

    /**
     *
     */
    private static final long serialVersionUID = 575118383195194253L;

    private static final Logger LOG = LogManager.getLogger(HttpPostCommonRequest.class);

    public String httpPost(String endPointUrl, String authorizationToken, String input) throws IOException {

        URL url = new URL(endPointUrl);

        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setDoOutput(true);
        conn.setRequestMethod("POST");
        String content = "application/json";
        conn.setRequestProperty(CONTENT_TYPE, content);

        conn.setRequestProperty("Accept", content);
        conn.setRequestProperty("TokenId", authorizationToken);

        if (input != null && !input.trim().isEmpty()) {
            byte[] postData;
            postData = input.getBytes();
            OutputStream os = conn.getOutputStream();
            os.write(postData);
            os.flush();
        }

        if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
            throw new IOException("Failed : HTTP error code : " + conn.getResponseCode());
        }
        	
        	byte[] responseByte = readByte(conn.getInputStream());
        	
        	String response= new String(responseByte);

        conn.disconnect();

        return response;

    }

    private static byte[] readByte(InputStream is) throws IOException {
    	if(is == null) {
    		return null;
    	}
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len;

        while ((len = is.read(buffer)) != -1) {
            os.write(buffer, 0, len);
        }
        return os.toByteArray();
    }

    public String httpPost(String endPoint, String request) throws IOException {
        URL url = new URL(endPoint);
        LOG.info("http endPoint>>>>>>>>>{}", endPoint);
        LOG.info("http request>>>>>>>>>> {}", request);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setDoOutput(true);
        conn.setRequestMethod("POST");
        conn.setRequestProperty(CONTENT_TYPE, "application/json");
        byte[] payload = null;

        payload = request.getBytes();

        OutputStream os = conn.getOutputStream();
        os.write(payload);
        os.flush();
        os.close();
        if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
            throw new CustomException("Failed : HTTP error code : " + conn.getResponseCode(), FCHttpStatus.BAD_REQUEST, CommonStatusCode.FAILED);
        }

        byte[] responseByte = readByte(conn.getInputStream());

        String response = new String(responseByte);
        LOG.info("http response>>>>>>>>>>{}", response);
        conn.disconnect();

        return response;

    }
    public static String httpGet(String endPoint) throws IOException {
        URL url = new URL(endPoint);
        LOG.info("http endPoint>>>>>>>>>{}", endPoint);

        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setDoOutput(true);
        conn.setRequestMethod("GET");
        conn.setRequestProperty(CONTENT_TYPE, "application/json");
        conn.setRequestProperty("Authorization", "");


        if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
            throw new CustomException("Failed : HTTP error code : " + conn.getResponseCode(), FCHttpStatus.BAD_REQUEST, CommonStatusCode.FAILED);
        }

        byte[] responseByte = readByte(conn.getInputStream());

        String response = new String(responseByte);
        LOG.info("http response>>>>>>>>>>%S", response);
        conn.disconnect();

        return response;

    }

    public String xmlhttpPost(String endPoint, String request) throws IOException {
    	ThirdPartyLogUtils.printLog(">>>>>>>>>>start api call<<<<<<<<<<<<<<<");
        URL url = new URL(endPoint);
        LOG.info("http endPoint>>>>>>>>>{}", endPoint);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setDoOutput(true);
        conn.setRequestMethod("POST");
        conn.setRequestProperty(CONTENT_TYPE, "text/xml");
        conn.setRequestProperty("Authorization", "");
        byte[] payload = null;

        payload = request.getBytes();

        OutputStream os = conn.getOutputStream();
        os.write(payload);
        os.flush();
        os.close();
        if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
            throw new CustomException("Failed : HTTP error code : " + conn.getResponseCode(), FCHttpStatus.BAD_REQUEST, CommonStatusCode.FAILED);
        }

        byte[] responseByte = readByte(conn.getInputStream());

        String response = new String(responseByte);
        conn.disconnect();
        ThirdPartyLogUtils.printLog(">>>>>>>>>>end api call<<<<<<<<<<<<<<<");
        return response;

    }
    
    public static String httpRequest(String endPointUrl, Map<String, String> headers, String input, String requestMethodType, LambdaLogger lambdaLogger, boolean handleErrorResponse) throws IOException {
		
    	FastCollabCommonUtil.setProxyData();
    	
    	String response = null;
		URL url = new URL(endPointUrl.trim());
		LogUtils.printlogger(lambdaLogger,">>>>>>>>>>start api call<<<<<<<<<<<<<<<");
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		
		try {
			//Set Headers
			if(headers != null) {
				headers.entrySet().stream().forEach(entry -> {
					conn.setRequestProperty(entry.getKey(), entry.getValue());
				});
			}
			
			if(Objects.isNull(input) && "POST".equalsIgnoreCase(requestMethodType))
				conn.setRequestProperty("Content-Length", "0");
			
			conn.setRequestMethod(requestMethodType);
			
			LogUtils.printlogger(lambdaLogger, "Request Url: " + endPointUrl);
			LogUtils.printlogger(lambdaLogger, "Request Headers: " + conn.getRequestProperties());
			LogUtils.printlogger(lambdaLogger, "Request Method Type: " + conn.getRequestMethod());
			
			//Request Type "GET"
			if("GET".equalsIgnoreCase(requestMethodType)) {
				conn.connect();
			} else {
				conn.setDoOutput(true);
				if (input != null && !input.trim().isEmpty()) {
					LogUtils.printlogger(lambdaLogger, "Request input: " + input);
					byte[] postData = input.getBytes();
					OutputStream os = conn.getOutputStream();
					os.write(postData);
					os.flush();
				}
			}

			int responseCode = conn.getResponseCode();
			byte[] responseByte = null;

			if (responseCode >= 200 && responseCode < 300) {
				responseByte = readByte(conn.getInputStream());
				if(responseByte != null) {
					response = new String(responseByte);
				}
			} else {
				responseByte = readByte(conn.getErrorStream());
				if(responseByte != null && handleErrorResponse) {
					response = new String(responseByte);
				} else {
					LogUtils.printlogger(lambdaLogger, "error code:" + responseCode + " error response: " + (responseByte != null ? new String(responseByte) : null));
					throw new CustomException("Failed : HTTP error code : " + responseCode + " Error Response: " + responseByte, FCHttpStatus.BAD_REQUEST, CommonStatusCode.FAILED);
				}
			}
			LogUtils.printlogger(lambdaLogger, "response:" + response);
			conn.disconnect();
			LogUtils.printlogger(lambdaLogger, "Response Code: " + responseCode + " Request Url: " + endPointUrl + " Response: " + response);
		} catch (Exception e) {
			LogUtils.exceptionLogger(lambdaLogger, e);
			if(conn != null) {
				int errorCode = conn.getResponseCode();
				byte[] responseByte = readByte(conn.getErrorStream());
				if(responseByte != null && handleErrorResponse) {
					response = new String(responseByte);
				} else {
					String errorResponse = responseByte != null ? new String(responseByte) : null;
					LogUtils.printlogger(lambdaLogger, "error code:" + errorCode + " error response: " + errorResponse);
					throw new CustomException("Failed : HTTP error code : " + errorCode + " Error Response: " + errorResponse, FCHttpStatus.BAD_REQUEST, CommonStatusCode.FAILED);
				}
			} else {
				throw e;
			}
		}
		LogUtils.printlogger(lambdaLogger, ">>>>>>>>>>end api call<<<<<<<<<<<<<<<");
		return response;

	}
    
    public static String httpRequest(String endPointUrl, Map<String, String> headers, String input, String requestMethodType, LambdaLogger lambdaLogger, boolean handleErrorResponse, Environment environment) throws IOException {
		int timeOut ;
		if(FastCollabCommonUtil.hasData(environment.getProperty("DOTREZ_TIME_OUT"))){
			timeOut =Integer.parseInt(environment.getProperty("DOTREZ_TIME_OUT"));
		}else{
			timeOut=60000;
		}
		ThirdPartyLogUtils.printLog(">>>>>>>>>>start api call<<<<<<<<<<<<<<<");
		
		FastCollabCommonUtil.setProxyData();
		
    	String response = null;
		URL url = new URL(endPointUrl.trim());
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		LogUtils.printlogger(lambdaLogger, "timeOut: " + timeOut);
		conn.setConnectTimeout(timeOut);
		try {
			//Set Headers
			if(headers != null) {
				headers.entrySet().stream().forEach(entry -> {
					conn.setRequestProperty(entry.getKey(), entry.getValue());
				});
			}
			
			if(Objects.isNull(input) && "POST".equalsIgnoreCase(requestMethodType))
				conn.setRequestProperty("Content-Length", "0");
			
			conn.setRequestMethod(requestMethodType);
			
			LogUtils.printlogger(lambdaLogger, "Request Url: " + endPointUrl);
			LogUtils.printlogger(lambdaLogger, "Request Headers: " + conn.getRequestProperties());
			LogUtils.printlogger(lambdaLogger, "Request Method Type: " + conn.getRequestMethod());
			
			//Request Type "GET"
			if("GET".equalsIgnoreCase(requestMethodType)) {
				conn.connect();
			} else {
				conn.setDoOutput(true);
				if (input != null && !input.trim().isEmpty()) {
					LogUtils.printlogger(lambdaLogger, "Request input: " + input);
					byte[] postData = input.getBytes();
					OutputStream os = conn.getOutputStream();
					os.write(postData);
					os.flush();
				}
			}

			int responseCode = conn.getResponseCode();
			byte[] responseByte = null;

			if (responseCode >= 200 && responseCode < 300) {
				responseByte = readByte(conn.getInputStream());
				if(responseByte != null) {
					response = new String(responseByte);
				}
			} else {
				responseByte = readByte(conn.getErrorStream());
				if(responseByte != null && handleErrorResponse) {
					response = new String(responseByte);
				} else {
					LogUtils.printlogger(lambdaLogger, "error code:" + responseCode + " error response: " + (responseByte != null ? new String(responseByte) : null));
					throw new CustomException("Failed : HTTP error code : " + responseCode + " Error Response: " + responseByte, FCHttpStatus.BAD_REQUEST, CommonStatusCode.FAILED);
				}
			}
			LogUtils.printlogger(lambdaLogger, "response:" + response);
			conn.disconnect();
			LogUtils.printlogger(lambdaLogger, "Response Code: " + responseCode + " Request Url: " + endPointUrl + " Response: " + response);
		} catch (Exception e) {
			LogUtils.exceptionLogger(lambdaLogger, e);
			if(conn != null) {
				int errorCode = conn.getResponseCode();
				byte[] responseByte = readByte(conn.getErrorStream());
				if(responseByte != null && handleErrorResponse) {
					response = new String(responseByte);
				} else {
					String errorResponse = responseByte != null ? new String(responseByte) : null;
					LogUtils.printlogger(lambdaLogger, "error code:" + errorCode + " error response: " + errorResponse);
					throw new CustomException("Failed : HTTP error code : " + errorCode + " Error Response: " + errorResponse, FCHttpStatus.BAD_REQUEST, CommonStatusCode.FAILED);
				}
			} else {
				throw e;
			}
		}
		ThirdPartyLogUtils.printLog(">>>>>>>>>>end api call<<<<<<<<<<<<<<<");
		return response;

	}

    public static String formEncodedHttpPost(String endPoint, List<NameValuePair> request) throws IOException {
        LOG.info("http endPoint>>>>>>>>>{}", endPoint);
        LOG.info("http request>>>>>>>>>> {}", request);
        HttpPost post = new HttpPost(endPoint);
        post.setEntity(new UrlEncodedFormEntity(request));
        try (CloseableHttpClient httpClient = HttpClients.createDefault();
             CloseableHttpResponse response = httpClient.execute(post)) {
			return EntityUtils.toString(response.getEntity());
        }
    }

    public String httpPostWithAuth(String endPointUrl, String authorizationToken, String input) throws IOException {

        URL url = new URL(endPointUrl);

        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setDoOutput(true);
        conn.setRequestMethod("POST");
        String content = "application/json";
        conn.setRequestProperty(CONTENT_TYPE, content);

        conn.setRequestProperty("Accept", content);
        conn.setRequestProperty("Authorization", authorizationToken);

        if (input != null && !input.trim().isEmpty()) {
            byte[] postData;
            postData = input.getBytes();
            OutputStream os = conn.getOutputStream();
            os.write(postData);
            os.flush();
        }

        if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
            throw new IOException("Failed : HTTP error code : " + conn.getResponseCode());
        }

        byte[] responseByte = readByte(conn.getInputStream());

        String response = new String(responseByte);

        conn.disconnect();

        return response;

    }

    public static String httpRequestBelairProxy(String endPointUrl, Map<String, String> headers, String requestBody, String requestMethodType, LambdaLogger lambdaLogger, boolean handleErrorResponse, Environment environment) throws IOException {
    	ThirdPartyLogUtils.printLog(">>>>>>>>>>Start api call(BELAIR_PROXY)<<<<<<<<<<<<<<<");
    	String response = "";
		try {
            // Specify the proxy server and port
			String proxyHost = System.getenv("BELAIR_PROXY_HOST");
			int proxyPort = Integer.parseInt(System.getenv("BELAIR_PROXY_PORT")); // Change this to your proxy server's port

            // Create a Proxy instance with the proxy server information
            Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(proxyHost, proxyPort));

            // Create a URL for the endpoint where you want to send the POST request
            URL url = new URL(endPointUrl);

            // Open a connection to the URL using the proxy
            HttpURLConnection connection = (HttpURLConnection) url.openConnection(proxy);

            // Set the request method to POST
            connection.setRequestMethod(requestMethodType);
            
            // Set the Content-Type header
            if(headers != null) {
				headers.entrySet().stream().forEach(entry -> {
					connection.setRequestProperty(entry.getKey(), entry.getValue());
				});
			}
            
            //Request Type "GET"
            if("GET".equalsIgnoreCase(requestMethodType)) {
            	// Connect to the URL
            	connection.connect();
			} else {
				// Write the POST data to the output stream
				connection.setDoOutput(true);
				if (requestBody != null && !requestBody.trim().isEmpty()) {
					LogUtils.printlogger(lambdaLogger, "Request input: " + requestBody);
					byte[] postDataBytes = requestBody.getBytes(StandardCharsets.UTF_8);
					OutputStream os = connection.getOutputStream();
					os.write(postDataBytes);
					os.flush();
				}
			}
            
            if(Objects.isNull(requestBody) && "POST".equalsIgnoreCase(requestMethodType))
            	connection.setRequestProperty("Content-Length", "0");

            // Now you can read from the connection's input stream or perform other operations as needed.
            int responseCode = connection.getResponseCode();
            byte[] responseByte = null;

            if (responseCode >= 200 && responseCode < 300) {
				responseByte = readByte(connection.getInputStream());
				if(responseByte != null) {
					response = new String(responseByte);
				}
			} else {
            	responseByte = readByte(connection.getErrorStream());
				if(responseByte != null && handleErrorResponse) {
					response = new String(responseByte);
				} else {
					LogUtils.printlogger(lambdaLogger, "error code:" + responseCode + " error response: " + (responseByte != null ? new String(responseByte) : null));
					throw new CustomException("Failed : HTTP error code : " + responseCode + " Error Response: " + responseByte, FCHttpStatus.BAD_REQUEST, CommonStatusCode.FAILED);
				}
			}
			LogUtils.printlogger(lambdaLogger, "response:" + response);

            // Don't forget to close the connection when you're done.
            connection.disconnect();
        } catch (Exception e) {
            LogUtils.exceptionLogger(lambdaLogger, e);
        }
		ThirdPartyLogUtils.printLog(">>>>>>>>>>end api call(BELAIR_PROXY)<<<<<<<<<<<<<<<");
		return response;
    }
}

