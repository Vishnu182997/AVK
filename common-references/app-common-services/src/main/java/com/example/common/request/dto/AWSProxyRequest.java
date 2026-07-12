package com.example.common.request.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class AWSProxyRequest {

	private String resource;
	private String path;
	private String methodArn;
	private String httpMethod;
	private Map<String, String> headers;
	private Map<String, Object> requestContext;
	private Map<String, String> queryStringParameters;
	private Map<String, String> pathParameters;
	private boolean isBase64Encoded;
	private String body;
}