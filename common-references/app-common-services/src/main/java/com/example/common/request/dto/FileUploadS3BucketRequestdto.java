package com.example.common.request.dto;

import lombok.Data;

@Data
public class FileUploadS3BucketRequestdto {
	
	private String id;
	private String base64Image;
	private String fileUploadFor;
	private String fileName;
	private String pdfUrl;
	private boolean overRideFile;
	
}
