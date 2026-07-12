package com.example.s3.response;

import lombok.Data;

@Data
public class S3Response {
	
	private String uploadFlag;
	private String uploadMessage;
	private String uploadUrl;

}
