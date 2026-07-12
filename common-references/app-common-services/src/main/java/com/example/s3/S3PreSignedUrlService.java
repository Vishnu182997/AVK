package com.example.s3;

import java.net.URL;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.HttpMethod;
import com.amazonaws.SdkClientException;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.AmazonS3URI;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.amazonaws.services.s3.model.ResponseHeaderOverrides;
import com.example.common.logger.LogUtils;
import com.example.common.util.FastCollabCommonUtil;

public class S3PreSignedUrlService {
	
	private static final Logger logger = LogManager.getLogger();
	
	private static AmazonS3 s3Client = AmazonS3ClientBuilder.standard().withRegion(Regions.AP_SOUTH_1).build();

	public static String putServicePreSignedUrlforUploadingS3(String bucketName, String fileName) {
		try {
			Date expiration = Calendar.getInstance().getTime();

			long expTimeMillis = expiration.getTime();
			expTimeMillis += 1000 * 60 * 60;
			expiration.setTime(expTimeMillis);

			GeneratePresignedUrlRequest generatePresignedUrlRequest = new GeneratePresignedUrlRequest(bucketName, fileName)
																			.withMethod(HttpMethod.PUT)
																			.withExpiration(expiration);

			URL url = s3Client.generatePresignedUrl(generatePresignedUrlRequest);

			return url.toString();

		} catch (AmazonServiceException e) {
			logger.error("Exception Logger: " + ExceptionUtils.getStackTrace(e));
		} catch (SdkClientException e) {
			logger.error("Exception Logger: " + ExceptionUtils.getStackTrace(e));
		}
		return null;
	}
	
	public static String getPreSignedUrlforDownloadingS3(String bucketName, String fileName) {
		try {
			
			Date expiration = Calendar.getInstance().getTime();
			
			long expTimeMillis = expiration.getTime();
			expTimeMillis += 1000 * 60 * 60;
			expiration.setTime(expTimeMillis);
            expiration.setTime(expTimeMillis);
            
            ResponseHeaderOverrides overRide=new ResponseHeaderOverrides().withContentType("Application/octet-stream");
            GeneratePresignedUrlRequest generatePresignedUrlRequest = new GeneratePresignedUrlRequest(bucketName, fileName)
																					                .withMethod(HttpMethod.GET)
																					                .withExpiration(expiration)
																					                .withResponseHeaders(overRide);
            
            URL url = s3Client.generatePresignedUrl(generatePresignedUrlRequest);
            
            return url.toString();
			
		} catch (AmazonServiceException e) {
			logger.error("Exception Logger: " + ExceptionUtils.getStackTrace(e));
		} catch (SdkClientException e) {
			logger.error("Exception Logger: " + ExceptionUtils.getStackTrace(e));
		}
		return null;
	}
	
	public static String getPreSignedUrlforDownloadingS3(String s3UrlPath, int expirayTimeInMinutes) {
		try {
			String filePath = "";
			String bucketName = "";
			Date expiration = Calendar.getInstance().getTime();
			long expTimeMillis = expiration.getTime();
			
            
            if(FastCollabCommonUtil.hasData(s3UrlPath) && expirayTimeInMinutes != 0) {
            	
            	//expTimeMillis += 6000 * expirayTimeInMinutes;
				 expTimeMillis+= expirayTimeInMinutes * 300 * 1000;
                expiration.setTime(expTimeMillis);
            	
            	AmazonS3URI s3URI = new AmazonS3URI(s3UrlPath);
 	            bucketName = s3URI.getBucket();
 	            filePath = s3URI.getKey();
				
				ResponseHeaderOverrides overRide=new ResponseHeaderOverrides().withContentType("Application/octet-stream");
	            GeneratePresignedUrlRequest generatePresignedUrlRequest = new GeneratePresignedUrlRequest(bucketName, filePath)
																						                .withMethod(HttpMethod.GET)
																						                .withExpiration(expiration)
																						                .withResponseHeaders(overRide);
	            
	            URL url = s3Client.generatePresignedUrl(generatePresignedUrlRequest);
	            
	            return url.toString();
            }
            
            
			
		} catch (AmazonServiceException e) {
			logger.error("Exception Logger: " + ExceptionUtils.getStackTrace(e));
		} catch (SdkClientException e) {
			logger.error("Exception Logger: " + ExceptionUtils.getStackTrace(e));
		} catch (Exception e) {
			logger.error("Exception Logger: " + ExceptionUtils.getStackTrace(e));
		}
		return null;
	}
	
	
	public static String getPreSignedUrlforDownloadingS3(String bucketName, String fileName, int expiryTimeInMinutes) {
		try {
			
			Date expiration = Calendar.getInstance().getTime();
			
			long expTimeMillis = expiration.getTime();
			
			if(expiryTimeInMinutes != 0) {
			expTimeMillis += 1000 * 60 * expiryTimeInMinutes;
			expiration.setTime(expTimeMillis);
			}
            
            ResponseHeaderOverrides overRide=new ResponseHeaderOverrides().withContentType("Application/octet-stream");
            GeneratePresignedUrlRequest generatePresignedUrlRequest = new GeneratePresignedUrlRequest(bucketName, fileName)
																					                .withMethod(HttpMethod.GET)
																					                .withExpiration(expiration)
																					                .withResponseHeaders(overRide);
            
            URL url = s3Client.generatePresignedUrl(generatePresignedUrlRequest);
            
            return url.toString();
			
		} catch (AmazonServiceException e) {
			logger.error("Exception Logger: " + ExceptionUtils.getStackTrace(e));
		} catch (SdkClientException e) {
			logger.error("Exception Logger: " + ExceptionUtils.getStackTrace(e));
		}
		return null;
	}

}
