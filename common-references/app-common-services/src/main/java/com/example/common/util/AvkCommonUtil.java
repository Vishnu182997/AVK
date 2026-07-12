package com.example.common.util;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.UncheckedIOException;
import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.TimeZone;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.zip.GZIPInputStream;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.rpc.soap.SOAPFaultException;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.util.DigestUtils;
import org.w3c.dom.Document;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.lambda.AWSLambda;
import com.amazonaws.services.lambda.AWSLambdaClientBuilder;
import com.amazonaws.services.lambda.model.InvocationType;
import com.amazonaws.services.lambda.model.InvokeRequest;
import com.amazonaws.services.lambda.model.InvokeResult;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.example.common.constant.AvkConstants;
import com.example.common.enums.FCHttpStatus;
import com.example.common.enums.Gender;
import com.example.common.exceptions.CustomException;
import com.example.common.logger.LogUtils;
import com.example.common.request.dto.AWSProxyRequest;
import com.example.common.request.dto.CommonStatusCode;
import com.example.common.request.dto.ExceptionEmailRequest;
import com.example.flight.common.dto.ApiCredential;
import com.example.s3.S3PreSignedUrlService;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class AvkCommonUtil {

	private static final Logger LOG = LogManager.getLogger(AvkCommonUtil.class);

	public static final String EXCEPTION_MESSAGE = "Oops!! something has went wrong, please contact admininstrator or try again";

	private static String algorithm = "AES";
	private static byte[] keyValue = new String("y6Lk1+noLp3bT*%W").getBytes(Charset.forName("UTF-8"));

	private AvkCommonUtil() {
	}

	public static final String API_DATE = "ddMMyy";// response date format from soap end point
	public static final String API_FULL_DATE = "ddMMyyyy";
	public static final String API_TIME = "HHmm";// response time format from soap end point
	public static final String REQUEST_DATE = "dd-MM-yyyy";// request date format from client
	public static final String DATE_TIME_FORMAT = "dd-MM-yyyy HH:mm:ss";
	public static final String END_POINT_API_DATE_TIME = "ddMMyy HHmm";// combining above two fields
	public static final String API_DATE_TIME = "yyyy-MM-dd HH:mm";// required format for client
	public static final String END_POINT_DATE_TIME = "dd/MM/yyyy-HH:mm"; // Required for Travelfusion
	public static final String FLIGHT_DATE_FORMAT = "yyyy-MM-dd";// request date format from client
	public static final ObjectMapper mapper = new ObjectMapper();
	public static final ObjectMapper mapperThirdParty = new ObjectMapper();
	public static final String FARE_MASKING_KEY = "FARE_MASKING";

	static {
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		mapper.configure(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES, false);
		mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
	}
	
	static {
		mapperThirdParty.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		mapperThirdParty.configure(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES, false);
		mapperThirdParty.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
		mapperThirdParty.configure(DeserializationFeature.FAIL_ON_MISSING_CREATOR_PROPERTIES, true);
	} 	

	/**
	 * @param <T>
	 * @param body
	 * @param clazz
	 * @return (Request Body < T >) clazz
	 * @throws JsonMappingException
	 * @throws JsonParseException
	 * @throws IOException{\"isGSTAllowed\":true,\"isPackageFare\":false,\"isPriceChanged\":false,\"hotelName\":\"Avasa Hotel\",\"isPackageDetailsMandatory\":false,\"isCancellationPolicyChanged\":false,\"uniqueId\":null,\"selectedRoomDetails\":[{\"roomKey\":\"0b3121b89dda3fe99d2b8f344687869e\",\"requireAllPaxDetails\":true,\"roomIndex\":1,\"roomCode\":\"qoysxksvqyrcpwy\",\"roomName\":\"Room
	 *                                                                                                                  STANDARD\",\"rateCode\":\"4phfnnbw4avc3fstusmwohou4ll6hjxe6gzo3ht23fdb37pdl2yqb6hpibuwlg4vtistz56qzadrqlshxlhuia47t3wglyobspswlohv4wne5tcozb6ggnczeiz6cokowhdwnkdfdabhoogf7g33ymmi5dc4ilphaseefp2q7bfihwhyaetcpaa4s2grdgtnaw4gijymajicmpw5xoaxnqrhv3he5zorbwgtlcmpd2l5q7wehx5aikbb\",\"smokingPreference\":null,\"bedTypes\":[],\"hotelSupplements\":[],\"roomAmenities\":[],\"cancellationPolicy\":[],\"price\":{\"currencyCode\":\"INR\",\"roomPrice\":17106.69,\"tax\":0.0,\"extraGuestCharge\":0.0,\"childCharge\":0.0,\"otherCharges\":0.0,\"discount\":0.0,\"publishedPrice\":17510.4,\"offeredPrice\":17510.4,\"agentCommission\":0.0,\"agentMarkUp\":0.0,\"serviceTax\":0.0,\"TDS\":0.0,\"serviceCharge\":342.13,\"totalGSTAmount\":61.58,\"GST\":{\"CGSTAmount\":61.58,\"CGSTRate\":0.0,\"CessAmount\":0.0,\"CessRate\":0.0,\"IGSTAmount\":0.0,\"IGSTRate\":0.0,\"SGSTRate\":0.0,\"SGSTAmount\":0.0,\"taxableAmount\":0.0},\"publishedPriceRoundedOff\":17510,\"offeredPriceRoundedOff\":17510},\"lastCancellationDate\":null,\"guaranteeInfo\":null,\"passengers\":[]},{\"roomKey\":\"0b3121b89dda3fe99d2b8f344687869e\",\"requireAllPaxDetails\":true,\"roomIndex\":1,\"roomCode\":\"qoysxksvqyrcpwy\",\"roomName\":\"Room
	 *                                                                                                                  STANDARD\",\"rateCode\":\"4phfnnbw4avc3fstusmwohou4ll6hjxe6gzo3ht23fdb37pdl2yqb6hpibuwlg4vtistz56qzadrqlshxlhuia47t3wglyobspswlohv4wne5tcozb6ggnczeiz6cokowhdwnkdfdabhoogf7g33ymmi5dc4ilphaseefp2q7bfihwhyaetcpaa4s2grdgtnaw4gijymajicmpw5xoaxnqrhv3he5zorbwgtlcmpd2l5q7wehx5aikbb\",\"smokingPreference\":null,\"bedTypes\":[],\"hotelSupplements\":[],\"roomAmenities\":[],\"cancellationPolicy\":[],\"price\":{\"currencyCode\":\"INR\",\"roomPrice\":16569.37,\"tax\":0.0,\"extraGuestCharge\":0.0,\"childCharge\":0.0,\"otherCharges\":0.0,\"discount\":0.0,\"publishedPrice\":16960.41,\"offeredPrice\":16960.41,\"agentCommission\":0.0,\"agentMarkUp\":0.0,\"serviceTax\":0.0,\"TDS\":0.0,\"serviceCharge\":331.39,\"totalGSTAmount\":59.65,\"GST\":{\"CGSTAmount\":59.65,\"CGSTRate\":0.0,\"CessAmount\":0.0,\"CessRate\":0.0,\"IGSTAmount\":0.0,\"IGSTRate\":0.0,\"SGSTRate\":0.0,\"SGSTAmount\":0.0,\"taxableAmount\":0.0},\"publishedPriceRoundedOff\":16960,\"offeredPriceRoundedOff\":16960},\"lastCancellationDate\":null,\"guaranteeInfo\":null,\"passengers\":[]}]}
	 */
	public static <T> T getRequestBody(String body, Class<T> clazz) throws IOException {
		return mapper.readValue(body, clazz);
	}
	
	public static long atStartOfDay(Date date) {
		LocalDateTime localDateTime = LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
		LocalDateTime startOfDay = localDateTime.with(LocalTime.MIN);
		return startOfDay.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
	}

	public static long atEndOfDay(Date date) {
		LocalDateTime localDateTime = LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
		LocalDateTime endOfDay = localDateTime.with(LocalTime.MAX);
		return endOfDay.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
	}
	
	/**
	 * @param <T>
	 * @param url
	 * @param clazz
	 * @return (Request Body < T >) clazz
	 * @throws JsonMappingException
	 * @throws JsonParseException
	 * @throws IOException{\"isGSTAllowed\":true,\"isPackageFare\":false,\"isPriceChanged\":false,\"hotelName\":\"Avasa Hotel\",\"isPackageDetailsMandatory\":false,\"isCancellationPolicyChanged\":false,\"uniqueId\":null,\"selectedRoomDetails\":[{\"roomKey\":\"0b3121b89dda3fe99d2b8f344687869e\",\"requireAllPaxDetails\":true,\"roomIndex\":1,\"roomCode\":\"qoysxksvqyrcpwy\",\"roomName\":\"Room
	 *                           
	 */
	public static <T> T getRequestBodyFromUrl(URL url, Class<T> clazz) throws IOException {
		return mapper.readValue(url, clazz);
	}
	
	public static <T> T getThirdPartyRequestBody(String body, Class<T> clazz) throws IOException {
		return mapperThirdParty.readValue(body, clazz);
	}

	// converting number to words
	private static final String[] specialNames = { "", " thousand", " million", " billion", " trillion", " quadrillion",
			" quintillion" };

	private static final String[] tensNames = { "", " ten", " twenty", " thirty", " forty", " fifty", " sixty",
			" seventy", " eighty", " ninety" };

	private static final String[] numNames = { "", " one", " two", " three", " four", " five", " six", " seven",
			" eight", " nine", " ten", " eleven", " twelve", " thirteen", " fourteen", " fifteen", " sixteen",
			" seventeen", " eighteen", " nineteen" };

	public static String convertLessThanOneThousand(int number) {
		String current;
		
		if(number<0)
			number = number * -1;

		if (number % 100 < 20) {
			current = numNames[number % 100];
			number /= 100;
		} else {
			current = numNames[number % 10];
			number /= 10;

			current = tensNames[number % 10] + current;
			number /= 10;
		}
		if (number == 0)
			return current;
		return numNames[number] + " hundred" + current;
	}

	public static String convert(int number) {
		if(number<0)
			number = number * -1;

		if (number == 0) {
			return "zero";
		}

		String prefix = "";

		if (number < 0) {
			number = -number;
			prefix = "negative";
		}

		String current = "";
		int place = 0;

		do {
			int n = number % 1000;
			if (n != 0) {
				String s = convertLessThanOneThousand(n);
				current = s + specialNames[place] + current;
			}
			place++;
			number /= 1000;
		} while (number > 0);

		return (prefix + current).trim();
	}

	/*
	 * public static String convertDateToString(Date date) { String pattern =
	 * "dd/MM/yyyy HH:mm:ss"; DateFormat df = new SimpleDateFormat(pattern); String
	 * dateString = df.format(date); return dateString; }
	 */

	public static String convertDateToString1(Date date) {
		DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
		df.setTimeZone(TimeZone.getTimeZone("IST"));
		String dateString = df.format(date);

		return dateString;
	}

	public static boolean isBlank(Object o) {
		if (o == null)
			return true;
		else if (o instanceof String) {
			if (((String) o).trim().equals(""))
				return true;
		} else if (o instanceof Collection<?>) {
			if (((Collection<?>) o).isEmpty())
				return true;
		} else if (o instanceof Integer) {
			if (((Integer) o) <= 0)
				return true;
		} else if (o instanceof Long) {
			if (((Long) o) <= 0)
				return true;
		} else if (o instanceof Short) {
			if (((Short) o) <= 0)
				return true;
		} else if (o instanceof Byte) {
			if (((Byte) o) <= 0)
				return true;
		} else if (o instanceof Double) {
			if (((Double) o) == 0)
				return true;
		} else if (o instanceof Float) {
			if (((Float) o) <= 0)
				return true;
		} else if (o instanceof Map<?, ?>) {
			if (((Map<?, ?>) o).isEmpty())
				return true;
		} else if (o.getClass().isArray()) {
			return Array.getLength(o) == 0;
		} else {
			if (o.toString().trim().equals(""))
				return true;
		}
		return false;
	}
/**
 * @MethodName hasData()
 * @param Object
 * @return if data present then true else false
 */
	public static boolean hasData(Object o) {
		return !isBlank(o);
	}

	/**
	 * @param <T>
	 * @param body
	 * @return String
	 */
	public static <T> String getResponseBody(T body) {
		try {
			// mapper.setVisibility(PropertyAccessor.FIELD, Visibility.ANY);
			return mapper.writeValueAsString(body);
		} catch (JsonProcessingException e) {
			LOG.error("Error parsing an object to string :{}", e);
		}
		return null;
	}
	
	/**
	 * @Method removeNullFromList()
	 * @param t
	 * @Functionality remove null and empty values from List
	 */
	public static <T> void removeNullFromList(List<T> t) {
		t.remove(null);
		t.removeAll(Collections.singleton(""));
		t.remove("");
	}
	
	
	/**
	 * @param <T>
	 * @param body
	 * @return String
	 */
	public static <T> String getResponseBodyNullIfEmpty(T body) {
		if(AvkCommonUtil.isBlank(body)) return null;
		try {
			// mapper.setVisibility(PropertyAccessor.FIELD, Visibility.ANY);
			return mapper.writeValueAsString(body);
		} catch (JsonProcessingException e) {
			LOG.error("Error parsing an object to string :{}", e);
		}
		return null;
	}

	/**
	 * @param value
	 * @return true if value is not empty else false
	 */
	public static boolean isStringNotEmpty(String value) {
		return value != null && !value.trim().isEmpty();
	}

	/**
	 * @param value
	 * @return true if value is not empty else false
	 */
	public static boolean isNullOrEmpty(Object value) {
		return value != null && !value.toString().trim().isEmpty();
	}

	/**
	 * @param value
	 * @return true if value is not empty else false
	 */
	public static boolean isNotNullOrEmpty(Object value) {
		return value != null && !value.toString().trim().isEmpty();
	}

	/**
	 * @param strDateTime
	 * @param fromFormat
	 * @param toDateFormat
	 * @return returns the date time by taking the input format with output format
	 */
	public static String changeDateTimeStringFormate(String strDateTime, String fromFormat, String toDateFormat) {
		if (!isStringNotEmpty(strDateTime) || !isStringNotEmpty(fromFormat) || !isStringNotEmpty(toDateFormat)) {
			return null;
		}
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(fromFormat);
		LocalDateTime ldt = LocalDateTime.parse(strDateTime, formatter);
		return DateTimeFormatter.ofPattern(toDateFormat).format(ldt);

	}

	public static long caluclateLayOverDuration(String currentSegmentDepartureDateTime,
			String prevSegmentArrivalDateTime) {
		DateTimeFormatter format = DateTimeFormatter.ofPattern(API_DATE_TIME);
		final LocalDateTime layOverArrivalDateTime = LocalDateTime.parse(prevSegmentArrivalDateTime, format);
		final LocalDateTime layOverDepartureDateTime = LocalDateTime.parse(currentSegmentDepartureDateTime, format);
		return layOverArrivalDateTime.until(layOverDepartureDateTime, ChronoUnit.MINUTES);
	}

	public static String formatLocalDateTime(LocalDateTime ldt, String toDateFormat) {
		if (ldt == null || !isStringNotEmpty(toDateFormat)) {
			return null;
		}
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(toDateFormat);
		return ldt.format(formatter);
	}

	/**@Method changeDateStringFormate()
	 * @param strDateTime
	 * @param fromFormat
	 * @param toDateFormat
	 * @return returns the date by taking the input format with output format<B>[Ex: dd-mm-yyyy to yyyy-mm-dd and vice versa]</B>
	 */
	public static String changeDateStringFormate(String strDateTime, String fromFormat, String toDateFormat) {
		if (!isStringNotEmpty(strDateTime) || !isStringNotEmpty(fromFormat) || !isStringNotEmpty(toDateFormat)) {
			return null;
		}
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(fromFormat);
		LocalDate ldt = LocalDate.parse(strDateTime, formatter);
		return DateTimeFormatter.ofPattern(toDateFormat).format(ldt);

	}
	
	/**
	 * @param strDobDateTime
	 * @param fromFormat
	 * @param toDateFormat
	 * @return returns the date by taking the input format with output format with past year date
	 */
	public static String changeDOBStringFormate(String strDobDateTime, String fromFormat, String toDateFormat) {
		if (!isStringNotEmpty(strDobDateTime) || !isStringNotEmpty(fromFormat) || !isStringNotEmpty(toDateFormat)) {
			return null;
		}
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(fromFormat);
		LocalDate ldt = LocalDate.parse(strDobDateTime, formatter);
		//DOB Year Should not be greater than current year
		if(ldt.getYear() > Calendar.getInstance().get(Calendar.YEAR)) {
			ldt = ldt.minusYears(100);
		}
		return DateTimeFormatter.ofPattern(toDateFormat).format(ldt);

	}

	/**
	 * @param json1Obj
	 * @param json2Obj
	 * @return
	 */
	public static JsonObject merge(JsonObject json1Obj, JsonObject json2Obj) {

		Set<Map.Entry<String, JsonElement>> entrySet1 = json1Obj.entrySet();
		for (Map.Entry<String, JsonElement> entry : entrySet1) {
			String key1 = entry.getKey();
			if (json2Obj.get(key1) != null) {
				JsonElement tempEle2 = json2Obj.get(key1);
				JsonElement tempEle1 = entry.getValue();
				if (tempEle2.isJsonObject() && tempEle1.isJsonObject()) {
					JsonObject mergedObj = merge(tempEle1.getAsJsonObject(), tempEle2.getAsJsonObject());
					entry.setValue(mergedObj);
				}
			}
		}

		Set<Map.Entry<String, JsonElement>> entrySet2 = json2Obj.entrySet();
		for (Map.Entry<String, JsonElement> entry : entrySet2) {
			String key2 = entry.getKey();
			if (json1Obj.get(key2) == null) {
				json1Obj.add(key2, entry.getValue());
			}
		}
		return json1Obj;
	}

	/**
	 * @param stringDate
	 * @return
	 */
	/*
	 * public static Date convertStringToDate(String stringDate) { if (stringDate !=
	 * null && !stringDate.isEmpty()) { String output = stringDate.substring(0, 10);
	 * Date convertedDate = null; try { convertedDate = new
	 * SimpleDateFormat("yyyy-MM-dd").parse(output); } catch (ParseException e) {
	 * LOG.error("ParseException in Date convertion" + e); } return convertedDate; }
	 * else { return null; } }
	 */

	/**
	 * returns the date & time to required format
	 *
	 * @param dateTime
	 * @param format
	 * @return
	 */
	public static LocalDateTime toParseLocalDateTime(String dateTime, String format) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
		return LocalDateTime.parse(dateTime, formatter);
	}

	public static LocalDate toParseLocalDate(String dateTime, String format) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
		return LocalDate.parse(dateTime, formatter);
	}

	/*
	 * returns the date & time to required format
	 *
	 * @param dateTime
	 * 
	 * @param format
	 * 
	 * @return
	 * 
	 * @throws ParseException
	 */
	public static Date toParseDate(String date) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
		sdf.setTimeZone(TimeZone.getTimeZone("IST"));
		return sdf.parse(date);
	}

	public static Date toParseDate(String date, String format) throws Exception {
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		sdf.setTimeZone(TimeZone.getTimeZone("IST"));
		return sdf.parse(date);
	}
	
	public static Date getddMMYYYYformat(String date) throws Exception {
		String[] arr = date.split("-");
		Date getDate = null;
		if(arr[0].length() ==4) {
			getDate = toParseDate(date, "yyyy-MM-dd");
		}else {
			getDate = toParseDate(date, "dd-MM-yyyy");
		}
		return getDate;
	}

	// TODO: Clarify(Need to change timezone from "UTC" to "IST"
	public static String parseTimeStampToString(Date date) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
		dateFormat.setTimeZone(TimeZone.getTimeZone("IST"));
		String formattedDate = dateFormat.format(date);
		return formattedDate;
	}

	// TODO: CC encryption
	public static String encryptCardMasking(String inputValue, String issuing_Brand) {
		int totLenth = inputValue.length();
		String maskValue = null;
		String finalString = null;

		if (issuing_Brand.equals("VISA") || issuing_Brand.equals("MASTERS")) {
			maskValue = "XXXX XXXX XXXX ";
			finalString = maskValue + inputValue.substring(totLenth - 4);
		} else if (issuing_Brand.equals("AMEX")) {

			maskValue = "XXXX XXXXXX ";
			finalString = maskValue + inputValue.substring(totLenth - 5);
		} else if (issuing_Brand.equals("DINERS")) {
			maskValue = "XXXX XXXXXX ";
			finalString = maskValue + inputValue.substring(totLenth - 4);
		} else if (issuing_Brand.equals("BTA")) {
			maskValue = "XXXX XXXXXXX ";
			finalString = maskValue + inputValue.substring(totLenth - 4);
		}

		return finalString;
	}

	/**
	 * returns the dateString to required Date
	 *
	 * @param date
	 * @param format
	 * @return
	 */
	public static Date parseStringtoDate(String date, String format) {
		if (isNullOrEmpty(date)) {
			try {
				SimpleDateFormat sdf = new SimpleDateFormat(format);
				sdf.setTimeZone(TimeZone.getTimeZone("IST"));
				return sdf.parse(date);
			} catch (ParseException e) {
			}
		}
		return null;
	}
	
	
	/**
	 * returns the dateString to required Date
	 *
	 * @param date
	 * @param format
	 * @return
	 */
	public static Date parseStringtoDateOriginal(String date, String format) {
		if (isNullOrEmpty(date)) {
			try {
				SimpleDateFormat sdf = new SimpleDateFormat(format);
				return sdf.parse(date);
			} catch (ParseException e) {
			}
		}
		return null;
	}


	/**
	 * returns the date required format
	 *
	 * @param date
	 * @param format
	 * @return
	 */
	public static String parseDatetoString(Date date, String format) {
		if (date != null) {
			SimpleDateFormat sdf = new SimpleDateFormat(format);
			sdf.setTimeZone(TimeZone.getTimeZone("IST"));
			return sdf.format(date);
		}
		return null;
	}
	
	/**
	 * returns the date & time
	 *
	 * @param dateTime
	 */
	public static LocalDateTime toParseLocalDateTime(String dateTime) {
		return LocalDateTime.parse(dateTime);
	}

	public static LocalDate toParseLocalDate(String date) {
		return LocalDate.parse(date);
	}

	/**
	 * @return generated random UUID with time stamp
	 * @throws Exception
	 */
	public static String generateRandomKey() {
		String ts = String.valueOf(System.currentTimeMillis());
		String rand = UUID.randomUUID().toString();
		return DigestUtils.md5DigestAsHex((ts + rand).getBytes());
	}

	public static String getCacheKeyWithTripIndicator(String cacheKey, Integer tripIndicator) {
		if (tripIndicator != null && tripIndicator > 0)
			return cacheKey + "_" + tripIndicator;
		return cacheKey;
	}

	// convert from utc date to ist date
	public static String convertUtcToIst(String utcDate) {
		SimpleDateFormat df = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.ENGLISH);
		Format f = new SimpleDateFormat("MMM dd, yyyy HH:mm:ss");
		df.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));
		Date date = null;
		try {
			date = df.parse(utcDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		String date1 = f.format(date);
		return date1;

	}
	
	
	// convert from utc date to ist date
	public static String convertUtcToIstNew(String UTCFormat, String utcDate) throws Exception {
		try {
			 // Create formatter for input
	        DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern(UTCFormat);
	        
	        // Parse UTC time
	        LocalDateTime utcTime = LocalDateTime.parse(utcDate, inputFormatter);
	        ZonedDateTime utcZoned = utcTime.atZone(ZoneId.of("UTC"));
	        
	        // Convert to IST
	        ZonedDateTime istZoned = utcZoned.withZoneSameInstant(ZoneId.of("Asia/Kolkata"));
	        
	        // Format output
	        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("MMM dd, yyyy HH:mm:ss");
	        return istZoned.format(outputFormatter);
		} catch (Exception e) {
			System.out.println(e.getMessage());
			throw e;
		}
	}
	 
	public static Document getNewDocument() throws ParserConfigurationException {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setNamespaceAware(true);
		factory.setFeature("http://xml.org/sax/features/external-general-entities", false);
		factory.setFeature("http://xml.org/sax/features/external-parameter-entities", false);
		factory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
		return factory.newDocumentBuilder().newDocument();
	}

	public static DocumentBuilderFactory getDocumentBuilderFactory() {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		try {
			factory.setNamespaceAware(true);
			factory.setFeature("http://xml.org/sax/features/external-general-entities", false);
			factory.setFeature("http://xml.org/sax/features/external-parameter-entities", false);
			factory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
		} catch (ParserConfigurationException e) {
			LOG.error("Cannot able to generate document builder :{}", e);
			throw new CustomException(EXCEPTION_MESSAGE, FCHttpStatus.INTERNAL_SERVER_ERROR,
					CommonStatusCode.UNHANDLED_ERROR_CODE);
		}
		return factory;
	}

	public static long caluclateSetLayOverDuration(String currentSegmentDepartureDateTime,
			String prevSegmentArrivalDateTime) {
		final LocalDateTime layOverArrivalDateTime = AvkCommonUtil
				.toParseLocalDateTime(prevSegmentArrivalDateTime);
		final LocalDateTime layOverDepartureDateTime = AvkCommonUtil
				.toParseLocalDateTime(currentSegmentDepartureDateTime);
		return layOverArrivalDateTime.until(layOverDepartureDateTime, ChronoUnit.MINUTES);
	}

	public static String encodeBase64(String valueToEncode) {
		return Base64.getEncoder().encodeToString(valueToEncode.getBytes());
	}

	public static String decodeBase64(String valueToDecode) {
		return new String(Base64.getDecoder().decode(valueToDecode.getBytes()));
	}

	public static Float distBetweenPointsInKMs(Double lat1, Double lng1, Double lat2, Double lng2) {
		Float dist = 0f;
		if (lat1 != null && lat1 != 0 && lng1 != null && lng1 != 0 && lat2 != null && lat2 != 0 && lng2 != null
				&& lng2 != 0) {
			double earthRadius = 6371000; // meters
			double dLat = Math.toRadians(lat2 - lat1);
			double dLng = Math.toRadians(lng2 - lng1);
			double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) + Math.cos(Math.toRadians(lat1))
					* Math.cos(Math.toRadians(lat2)) * Math.sin(dLng / 2) * Math.sin(dLng / 2);
			double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
			Float distdata = new Float(earthRadius * c);
			return (distdata / 1000);
		}
		return dist;
	}

	/**
	 * @param <T>
	 * @param body
	 * @param clazz
	 * @return (Request Body < T >) clazz
	 * @throws JsonMappingException
	 * @throws JsonParseException
	 * @throws IOException
	 */
	public static <T> T getRequestBodyWithoutCaseSensitive(String body, Class<T> clazz) throws IOException {
		mapper.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		return mapper.readValue(body, clazz);
	}

	public static String invokeLambda(String functionName, String payload, Environment environment) {
		AWSLambda client = AWSLambdaClientBuilder.standard().withRegion(Regions.AP_SOUTH_1).build();
		LogUtils.printlogger(null, functionName + " Result invoking " + functionName + " with Payload : " + payload);
		InvokeRequest request = new InvokeRequest();
		request.withFunctionName(functionName).withPayload(payload);
		InvokeResult invoke = client.invoke(request);
		String response = StandardCharsets.UTF_8.decode(invoke.getPayload()).toString();
		LogUtils.printlogger(null, functionName + " Result invoking " + functionName + " with Payload : " + response);
		return response;
	}
	
	/**
	 * @Method invokeLambdaWithAutorization()
	 * @param functionName
	 * @param payload
	 * @param authorization
	 * @param environment
	 * @return response
	 */
	public static String invokeLambdaWithAutorization(String functionName, String payload, String authorization, Environment environment) {
		AWSLambda client = AWSLambdaClientBuilder.standard().withRegion(Regions.AP_SOUTH_1).build();
		LogUtils.printlogger(null, functionName + " Result invoking " + functionName + " with Payload : " + payload);
		InvokeRequest request = new InvokeRequest();
		request.withFunctionName(functionName).withPayload(payload).putCustomRequestHeader("Authorization", authorization);
		InvokeResult invoke = client.invoke(request);
		String response = StandardCharsets.UTF_8.decode(invoke.getPayload()).toString();
		LogUtils.printlogger(null, functionName + " Result invoking " + functionName + " with Payload : " + response);
		return response;
	}
	
	
	
	

	/**
	 * @param request
	 * @param clazz
	 * @return
	 * @throws JAXBException
	 */
	public static String marshaller(Object request, Class clazz) throws JAXBException {
		// creating the JAXB context
		JAXBContext jContext = JAXBContext.newInstance(clazz);
		// creating the marshaller object
		Marshaller marshallObj = jContext.createMarshaller();
		marshallObj.setProperty(Marshaller.JAXB_FRAGMENT, Boolean.TRUE);
		// setting the property to show xml format output
		// marshallObj.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
		// calling the marshall method
		StringWriter sw = new StringWriter();
		marshallObj.marshal(request, sw);

		return sw.toString();
	}

	/**
	 * @param request
	 * @param clazz
	 * @param <T>
	 * @return
	 * @throws JAXBException
	 */
	public static <T> T unmarshal(String request, Class<T> clazz) throws JAXBException {
		// creating the JAXB context
		JAXBContext jContext = JAXBContext.newInstance(clazz);
		// creating the UnMarshaller object
		Unmarshaller unMarshaller = jContext.createUnmarshaller();
		// calling the unmarshaller method
		return (T) unMarshaller.unmarshal(new StringReader(request.toString()));
	}

	/**
	 * @return String
	 */
	public static String generateGUID() {
		return UUID.randomUUID().toString();
	}

	/**
	 * @param guid1
	 * @param guid2
	 * @return boolean
	 */
	public static boolean compareGUIDs(String guid1, String guid2) {
		try {
			UUID UUID_1 = UUID.fromString(guid1);
			UUID UUID_2 = UUID.fromString(guid2);
			return UUID_1.compareTo(UUID_2) == 0 ? true : false;
		} catch (Exception e) {
		}
		return false;
	}

	/**
	 * @param map
	 * @param delimiter
	 * @return
	 */
	public static String convertMapToString(Map<?, ?> map, String delimiter) {
		return map.keySet().stream().map(key -> key + delimiter + map.get(key)).collect(Collectors.joining(", "));
	}

	/**
	 * @param date
	 * @param format
	 * @return
	 */
	public static String convertDatetoFormattedString(Date date, String format) {
		try {
			DateFormat dateFormat = new SimpleDateFormat(format);
			dateFormat.setTimeZone(TimeZone.getTimeZone("IST"));
			return dateFormat.format(date);
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * This method will append the delimited String to the last.
	 *
	 * @param dataRecord
	 * @param delimiter
	 * @return String
	 */
	public static String appendDelimitedString(String dataRecord, String delimiter) {
		if (isNullOrEmpty(dataRecord) && !dataRecord.equalsIgnoreCase("null"))
			return new StringBuffer(dataRecord).append(delimiter).toString();
		else
			return new StringBuffer(delimiter).toString();
	}

	/**
	 * This method will compare two string content.
	 */

	public static boolean compareString(String value1, String value2) {
		return value1.equals(value2);
	}

	/**
	 * @param bigDecimal
	 * @return
	 */
	public static String convertBigDecimalToString(BigDecimal bigDecimal) {
		if (bigDecimal != null)
			return new DecimalFormat("#0.##").format(bigDecimal);
		else
			return new DecimalFormat("#0.##").format(BigDecimal.ZERO);
	}

	/**
	 * @param compressed allowed object is {@link byte[] }
	 * @return possible object is {@link String }
	 */
	public static String decompress(final byte[] compressed) {
		if (compressed == null || compressed.length == 0) {
			return null;
		}
		try (final GZIPInputStream gzipInput = new GZIPInputStream(new ByteArrayInputStream(compressed));
				final StringWriter stringWriter = new StringWriter()) {
			IOUtils.copy(gzipInput, stringWriter, StandardCharsets.UTF_8);
			return stringWriter.toString();
		} catch (IOException e) {
			throw new UncheckedIOException("Error while decompression!", e);
		}
	}

	/**
	 * this method is to getHashId
	 */
	public static String getSecureHashId(String id) {
		String passwordToHash = "eva-fastcollab" + id;
		String securePassword = "";
		try {
			byte[] salt = getHashIdSalt();

			securePassword = genrateSecureHashId(passwordToHash, salt);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return securePassword;
	}

	public static boolean compareSecureHashId(String Id, String inputHash) {
		String strHashId = getSecureHashId(Id);

		return compareString(strHashId, inputHash);

	}

	// Add salt
	private static byte[] getHashIdSalt() throws NoSuchAlgorithmException, NoSuchProviderException {
		String string = "EVA-PRODUCT@#001542856!";
		byte[] salt = string.getBytes();
		// return salt
		return salt;
	}

	public static String genrateSecureHashId(String passwordToHash, byte[] salt) {
		String generatedPassword = null;
		try {
			// Create MessageDigest instance for MD5
			MessageDigest md = MessageDigest.getInstance("MD5");
			// Get the hash's bytes
			byte[] bytes = md.digest(passwordToHash.getBytes());
			// This bytes[] has bytes in decimal format;
			// Convert it to hexadecimal format
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < bytes.length; i++) {
				sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
			}
			// Get complete hashed password in hex format
			generatedPassword = sb.toString();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return generatedPassword;
	}

	/**
	 * @param map
	 * @return
	 */
	public static String getAuthorizationToken(Map<String, String> map) {
		return map.get(AvkConstants.AUTHORIZATION).replace("Bearer ", "");
	}

	/**
	 * @param inputRequest
	 * @param authorization
	 * @param invokeUrl
	 * @param lambdaLogger
	 * @throws IOException
	 */
	public static void invokeUrlAsyn(Object inputRequest, String authorization, String invokeUrl,
			LambdaLogger lambdaLogger) throws IOException {
		LogUtils.printlogger(lambdaLogger, " <<< :INVOKEURL: >>> " + invokeUrl);
		String requestPayLoad = AvkCommonUtil.getResponseBody(inputRequest);
		requestPayLoad = requestPayLoad.replace("\'", "");
		LogUtils.printlogger(lambdaLogger, " <<< :RequestPayoad: >>> " + requestPayLoad);
		if (AvkCommonUtil.isNotNullOrEmpty(invokeUrl)) {
			try {
				RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(1000).build();
				HttpClient client = HttpClientBuilder.create().setDefaultRequestConfig(requestConfig).build();
				HttpPost requestAmz = new HttpPost(invokeUrl);
				requestAmz.addHeader(AvkConstants.AUTHORIZATION, authorization);
				requestAmz.addHeader("Content-Type", ContentType.APPLICATION_JSON.toString());
				StringEntity strEntity = new StringEntity(requestPayLoad);
				requestAmz.setEntity(strEntity);
				HttpResponse response = client.execute(requestAmz);
				LogUtils.printlogger(lambdaLogger, "<<< Response Payload >>>: " + EntityUtils.toString(response.getEntity()));
			} catch (SocketTimeoutException e) {
				LogUtils.exceptionLogger(lambdaLogger, e);
			} catch (IOException e) {
				LogUtils.exceptionLogger(lambdaLogger, e);
			}
		}
	}


	/**
	 * send printLog = true in this method, if you want to print Step Function ARN in the CloudWatch Logs.
	 *
	 * @param inputRequest
	 * @param authorization
	 * @param invokeUrl
	 * @param printLog
	 * @param lambdaLogger
	 * @throws IOException
	 */
	public static void invokeStepFunction(Object inputRequest, String authorization, String invokeUrl, boolean printLog,
									 LambdaLogger lambdaLogger) throws IOException {
		LogUtils.printlogger(lambdaLogger, " <<< :INVOKEURL: >>> " + invokeUrl);
		String requestPayLoad = AvkCommonUtil.getResponseBody(inputRequest);
		requestPayLoad = requestPayLoad.replace("\'", "");
		LogUtils.printlogger(lambdaLogger, " <<< :RequestPayoad: >>> " + requestPayLoad);
		if (AvkCommonUtil.isNotNullOrEmpty(invokeUrl)) {
			try {
				RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(1000).build();
				HttpClient client = HttpClientBuilder.create().setDefaultRequestConfig(requestConfig).build();
				HttpPost requestAmz = new HttpPost(invokeUrl);
				requestAmz.addHeader(AvkConstants.AUTHORIZATION, authorization);
				requestAmz.addHeader("Content-Type", ContentType.APPLICATION_JSON.toString());
				StringEntity strEntity = new StringEntity(requestPayLoad);
				requestAmz.setEntity(strEntity);
				HttpResponse response = client.execute(requestAmz);
				if(printLog)
					LogUtils.printDebuggerLogs(lambdaLogger, "<<< Step Function ARN >>>: " + EntityUtils.toString(response.getEntity()),"INFO", "STEP_FN_ARN");
					//System.out.println("<<< Step Function ARN >>>: " + EntityUtils.toString(response.getEntity()));
			} catch (SocketTimeoutException e) {
				LogUtils.exceptionLogger(lambdaLogger, e);
			} catch (IOException e) {
				LogUtils.exceptionLogger(lambdaLogger, e);
			}
		}
	}
	
	@SuppressWarnings("serial")
	public static void callIntegrationStepFunction(List<Map<String, Object>> triggers, Integer referenceId) throws Exception {
		ArrayList<Map<String, Object>> jobs = new ArrayList<Map<String, Object>>();
		for (Map<String, Object> job : triggers) {
			Set<String> keySet = job.keySet();
			String id = keySet.toString().replace("[", "").replace("]", "");
			String apiType = job.get(id) + "";
			LogUtils.timelogger("FOR REFRENECE ID:: " + referenceId + " Trigger Created:: " + id);
			jobs.add(new HashMap<String, Object>() {{put("apiType", apiType); put("id", id);}});
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("integrationData", jobs);
		String invokeUrl = System.getenv("INTEGRATION_STEP_FUNCTION_URL");
		if (AvkCommonUtil.isBlank(invokeUrl)) invokeUrl = "https://6agwzn7mxj.execute-api.ap-south-1.amazonaws.com/eva/integration-trigger";
		if(AvkCommonUtil.hasData(jobs)) invokeStepFunction(map, null, invokeUrl, true, null);
	}
	/**
	 * @param inputRequest
	 * @param authorization
	 * @param invokeUrl
	 * @param lambdaLogger
	 * @throws IOException
	 */
	public static String invokeUrl(Object inputRequest, String authorization, String invokeUrl,
			LambdaLogger lambdaLogger) throws IOException {
		LogUtils.printlogger(lambdaLogger, " <<< :INVOKEURL: >>> " + invokeUrl);
		String requestPayLoad = AvkCommonUtil.getResponseBody(inputRequest);
		LogUtils.printlogger(lambdaLogger, " <<< :RequestPayoad: >>> " + requestPayLoad);
		String response = null;
		if (AvkCommonUtil.isNullOrEmpty(invokeUrl)) {
			try {
				RequestConfig requestConfig = RequestConfig.custom().build();
				HttpClient client = HttpClientBuilder.create().setDefaultRequestConfig(requestConfig).build();
				HttpPost requestAmz = new HttpPost(invokeUrl);
				requestAmz.addHeader(AvkConstants.AUTHORIZATION, authorization);
				requestAmz.addHeader("Content-Type", ContentType.APPLICATION_JSON.toString());
				StringEntity strEntity = new StringEntity(requestPayLoad);
				requestAmz.setEntity(strEntity);
				HttpResponse httpresponse = client.execute(requestAmz);
				response = EntityUtils.toString(httpresponse.getEntity());
				LogUtils.printlogger(lambdaLogger, "<<< Response Payload >>>: " + response);
			} catch (SocketTimeoutException e) {
				LogUtils.exceptionLogger(lambdaLogger, e);
			} catch (IOException e) {
				LogUtils.exceptionLogger(lambdaLogger, e);
				throw e;
			}
		}
		return response;
	}
	
	public static String invokeUrlSatGuruTranslation(Object inputRequest, String authorization, String invokeUrl,
			LambdaLogger lambdaLogger) throws IOException {
		LogUtils.printlogger(lambdaLogger, " <<< :INVOKEURL: >>> " + invokeUrl);
		String requestPayLoad = AvkCommonUtil.getResponseBody(inputRequest);
		LogUtils.printlogger(lambdaLogger, " <<< :RequestPayoad: >>> " + requestPayLoad);
		String response = null;
		if (AvkCommonUtil.isNullOrEmpty(invokeUrl)) {
			try {
				RequestConfig requestConfig = RequestConfig.custom().build();
				HttpClient client = HttpClientBuilder.create().setDefaultRequestConfig(requestConfig).build();
				HttpPost requestAmz = new HttpPost(invokeUrl);
				requestAmz.addHeader(AvkConstants.AUTHORIZATION, authorization);
				requestAmz.addHeader("Content-Type", ContentType.APPLICATION_JSON.getMimeType());
                StringEntity strEntity = new StringEntity(requestPayLoad, ContentType.APPLICATION_JSON);
                requestAmz.setEntity(strEntity);
				HttpResponse httpresponse = client.execute(requestAmz);
				response = EntityUtils.toString(httpresponse.getEntity());
				LogUtils.printlogger(lambdaLogger, "<<< Response Payload >>>: " + response);
			} catch (SocketTimeoutException e) {
				LogUtils.exceptionLogger(lambdaLogger, e);
			} catch (IOException e) {
				LogUtils.exceptionLogger(lambdaLogger, e);
				throw e;
			}
		}
		return response;
	}
	
	
	public static String invokeUrlWithJsonObject(JSONObject inputRequest, String authorization, String invokeUrl,
			LambdaLogger lambdaLogger) throws IOException {
		LogUtils.printlogger(lambdaLogger, " <<< :INVOKEURL: >>> " + invokeUrl);
		String requestPayLoad = inputRequest.toString();
		LogUtils.printlogger(lambdaLogger, " <<< :RequestPayoad: >>> " + requestPayLoad);
		String response = null;
		if (AvkCommonUtil.isNullOrEmpty(invokeUrl)) {
			try {
				RequestConfig requestConfig = RequestConfig.custom().build();
				HttpClient client = HttpClientBuilder.create().setDefaultRequestConfig(requestConfig).build();
				HttpPost requestAmz = new HttpPost(invokeUrl);
				requestAmz.addHeader(AvkConstants.AUTHORIZATION, authorization);
				requestAmz.addHeader("Content-Type", ContentType.APPLICATION_JSON.toString());
				StringEntity strEntity = new StringEntity(requestPayLoad);
				requestAmz.setEntity(strEntity);
				HttpResponse httpresponse = client.execute(requestAmz);
				response = EntityUtils.toString(httpresponse.getEntity());
				LogUtils.printlogger(lambdaLogger,"<<< Response Payload >>>: " + response);
			} catch (SocketTimeoutException e) {
				LogUtils.exceptionLogger(lambdaLogger, e);
			} catch (IOException e) {
				LogUtils.exceptionLogger(lambdaLogger, e);
				throw e;
			}
		}
		return response;
	}
	
	public static void invokeUrlAsynWithJsonObject(JSONObject inputRequest, String authorization, String invokeUrl,
			LambdaLogger lambdaLogger) throws IOException {
		LogUtils.printlogger(lambdaLogger, " <<< :INVOKEURL: >>> " + invokeUrl);
		String requestPayLoad = inputRequest.toString();
		LogUtils.printlogger(lambdaLogger, " <<< :RequestPayoad: >>> " + requestPayLoad);
		if (AvkCommonUtil.isNotNullOrEmpty(invokeUrl)) {
			try {
				RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(1000).build();
				HttpClient client = HttpClientBuilder.create().setDefaultRequestConfig(requestConfig).build();
				HttpPost requestAmz = new HttpPost(invokeUrl);
				requestAmz.addHeader(AvkConstants.AUTHORIZATION, authorization);
				requestAmz.addHeader("Content-Type", ContentType.APPLICATION_JSON.toString());
				StringEntity strEntity = new StringEntity(requestPayLoad);
				requestAmz.setEntity(strEntity);
				HttpResponse response = client.execute(requestAmz);
				LogUtils.printlogger(lambdaLogger, "<<< Response Payload >>>: " + EntityUtils.toString(response.getEntity()));
			} catch (SocketTimeoutException e) {
				LogUtils.exceptionLogger(lambdaLogger, e);
			} catch (IOException e) {
				LogUtils.exceptionLogger(lambdaLogger, e);
				throw e;
			}
		}
	} 

	/**
	 * @param departureDateTime
	 * @return
	 */
	public static long convertDateToTime(String departureDateTime) {
		String output = departureDateTime.substring(11, 16);
		return Long.parseLong(output.replace(":", ""));
	}

	/**
	 * @param date1
	 * @param date2
	 * @return
	 */
	/*
	 * public static boolean compareFlightDates(Date date1, Date date2) {
	 * SimpleDateFormat sdfo = new SimpleDateFormat("yyyy-MM-dd"); return
	 * sdfo.format(date1).compareTo(sdfo.format(date2)) == 0; }
	 */

	/**
	 * @param json
	 * @return
	 */
	public static JsonObject getJSONObject(String json) {
		return JsonParser.parseString(json).getAsJsonObject();
	}

	/**
	 * @param body
	 * @return
	 * @throws IOException
	 */
	public static LinkedHashMap getHasMapDTO(Object body) {
		LinkedHashMap linkedHashMap = (LinkedHashMap) body;
		return linkedHashMap;
	}

	/**
	 * @param body
	 * @param clazz
	 * @param <T>
	 * @return Java Object(<B>Ex:</B> User Defined Class or (Key and Value) Contains Object (Ex: Map))
	 * @throws IOException
	 */
	public static <T> T getDTO(Object body, Class<T> clazz) {
		return mapper.convertValue(body, clazz);
	}
	
	public static <T> List<T> getDTOList(Object body, Class<T> clazz) {
	    return mapper.convertValue(body, mapper.getTypeFactory().constructCollectionType(List.class, clazz));
	}

	public static <T> String getSoapXmlFormat(T object) {
		String xmlString = null;
		try {
			if (object != null) {
				JAXBContext jaxbContext = JAXBContext.newInstance(object.getClass());
				Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
				jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
				StringWriter sw = new StringWriter();
				jaxbMarshaller.marshal(object, sw);
				xmlString = sw.toString();
			}
		} catch (JAXBException e) {
			if (object instanceof SOAPFaultException) {
				xmlString = ((SOAPFaultException) object).getMessage();
			}
		}
		return xmlString;
	}

	// Add these as class fields
	private static final Map<String, String> CURRENCY_FORMAT_MAP = new HashMap<>();
	static {
		// Western format (groups of 3)
		CURRENCY_FORMAT_MAP.put("USD", "#,###.00");
		CURRENCY_FORMAT_MAP.put("CAD", "#,###.00");
		CURRENCY_FORMAT_MAP.put("EUR", "# ###,00");
		CURRENCY_FORMAT_MAP.put("GBP", "#,###.00");

		// East Asian format (groups of 4)
		CURRENCY_FORMAT_MAP.put("JPY", "#,####.00");
		CURRENCY_FORMAT_MAP.put("CNY", "#,####.00");
		CURRENCY_FORMAT_MAP.put("KRW", "#,####.00");

		// South Asian format (Indian)
		CURRENCY_FORMAT_MAP.put("INR", "indian");
		CURRENCY_FORMAT_MAP.put("NPR", "indian");
		CURRENCY_FORMAT_MAP.put("LKR", "indian");

		// Others
		CURRENCY_FORMAT_MAP.put("IDR", "#,###.00");
		// Add more currencies as needed
	}

	public static String formatCurrency(Double amount, String currencyCode) {
		try {
			if (currencyCode == null || currencyCode.isEmpty()) {
				return getIndianCurrencyFormat(String.valueOf(amount)); // default to Indian format
			}

			String pattern = CURRENCY_FORMAT_MAP.get(currencyCode.toUpperCase());
			if (pattern == null) {
				return getIndianCurrencyFormat(String.valueOf(amount)); // default to Indian format
			}

			if (pattern.equals("indian")) {
				return getIndianCurrencyFormat(String.valueOf(amount));
			} else {
				DecimalFormat formatter = new DecimalFormat(pattern);
				return formatter.format(Double.parseDouble(String.valueOf(amount)));
			}
		} catch (Exception e) {
			e.printStackTrace();
			return getIndianCurrencyFormat(String.valueOf(amount)); // return original amount if formatting fails
		}
	}


	public static String getIndianCurrencyFormat(String inputPrice) {

		String currencyINR = inputPrice;

		try {

			if (inputPrice == null || inputPrice.isEmpty())
				return "0";

			Double price = Double.parseDouble(inputPrice);
			
			if(price < 0)
				return new BigDecimal(inputPrice).setScale(0, BigDecimal.ROUND_HALF_UP).toPlainString();
			
			String priceStr = new BigDecimal(inputPrice).setScale(0, BigDecimal.ROUND_HALF_UP).toPlainString();

			if (priceStr.length() > 3) {
				String secondStr = priceStr.substring(priceStr.length() - 3);
				String firstStr = priceStr.substring(0, (priceStr.length() - 3));

				DecimalFormat formatter = new DecimalFormat("##,##,##,##,##");
				firstStr = formatter.format(Double.parseDouble(firstStr));
				currencyINR = firstStr + "," + secondStr;
			} else {
				currencyINR = priceStr;
			}

			if (currencyINR.contains(".")) {

				Double price1 = Double.parseDouble(currencyINR.split("\\.")[1]);
				if (price1 == 0) {
					currencyINR = currencyINR.split("\\.")[0];
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return currencyINR;
	}
	public static String getCurrencyFormatInMillions(String inputPrice) {

		String currencyINR = inputPrice;

		try {

			if (inputPrice == null || inputPrice.isEmpty())
				return "0";

			Double price = Double.parseDouble(inputPrice);
			
			if(price < 0)
				return new BigDecimal(inputPrice).setScale(0, BigDecimal.ROUND_HALF_UP).toPlainString();
			
			String priceStr = new BigDecimal(inputPrice).setScale(0, BigDecimal.ROUND_HALF_UP).toPlainString();

			if (priceStr.length() > 3) {
				String secondStr = priceStr.substring(priceStr.length() - 3);
				String firstStr = priceStr.substring(0, (priceStr.length() - 3));

				DecimalFormat formatter = new DecimalFormat("#,###,###,###");
				firstStr = formatter.format(Double.parseDouble(firstStr));
				currencyINR = firstStr + "," + secondStr;
			} else {
				currencyINR = priceStr;
			}

			if (currencyINR.contains(".")) {

				Double price1 = Double.parseDouble(currencyINR.split("\\.")[1]);
				if (price1 == 0) {
					currencyINR = currencyINR.split("\\.")[0];
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return currencyINR;
	}

	public static String getForeignCurrencyFormat(String inputPrice) {

		String currencyINR = inputPrice;

		try {

			if (inputPrice == null || inputPrice.isEmpty())
				return "0";

//			Double price = Double.parseDouble(inputPrice);
			
//			if(price < 0)
//				return new BigDecimal(inputPrice).setScale(0, BigDecimal.ROUND_HALF_UP).toPlainString();
			
//			String priceStr = new BigDecimal(inputPrice).setScale(0, BigDecimal.ROUND_HALF_UP).toPlainString();

			inputPrice = inputPrice.replace(",", "");
			if (inputPrice.length() > 3) {

				DecimalFormat formatter = new DecimalFormat("###,###,###,###,###,###.###");
				currencyINR = formatter.format(Double.parseDouble(inputPrice));
				 
			} else {
				currencyINR = inputPrice;
			}

			if (currencyINR.contains(".")) {

				Double price1 = Double.parseDouble(currencyINR.split("\\.")[1]);
				if (price1 == 0) {
					currencyINR = currencyINR.split("\\.")[0];
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return currencyINR;
	}
	/**
	 * 
	 * @param inputPrice
	 * @param countryId
	 * @return currency Format based on countryId
	 */
	public static String getIndianCurrencyFormat(String inputPrice,BigInteger countryId) {

		String currencyINR = inputPrice;

		try {

			if (inputPrice == null || inputPrice.isEmpty())
				return "0";

			if (AvkCommonUtil.isNullOrEmpty(countryId) && new BigInteger("64").compareTo(countryId)!=0) {
				return getForeignCurrencyFormat( inputPrice);
			} else {
				return getIndianCurrencyFormat(inputPrice);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return currencyINR;
	}
	
	public static String getAmountInWords(String amount) {

		String amountInWords = "";
		try {

			if (amount == null || amount.isEmpty())
				return amountInWords;
			amount = amount.replace("-", "");

			BigDecimal bd = new BigDecimal(amount);
			long number = bd.longValue();
			long no = bd.longValue();
			int decimal = (int) (bd.remainder(BigDecimal.ONE).doubleValue() * 100);
			int digits_length = String.valueOf(no).length();
			int i = 0;
			ArrayList<String> str = new ArrayList<>();
			HashMap<Integer, String> words = new HashMap<>();
			words.put(0, "");
			words.put(1, "One");
			words.put(2, "Two");
			words.put(3, "Three");
			words.put(4, "Four");
			words.put(5, "Five");
			words.put(6, "Six");
			words.put(7, "Seven");
			words.put(8, "Eight");
			words.put(9, "Nine");
			words.put(10, "Ten");
			words.put(11, "Eleven");
			words.put(12, "Twelve");
			words.put(13, "Thirteen");
			words.put(14, "Fourteen");
			words.put(15, "Fifteen");
			words.put(16, "Sixteen");
			words.put(17, "Seventeen");
			words.put(18, "Eighteen");
			words.put(19, "Nineteen");
			words.put(20, "Twenty");
			words.put(30, "Thirty");
			words.put(40, "Forty");
			words.put(50, "Fifty");
			words.put(60, "Sixty");
			words.put(70, "Seventy");
			words.put(80, "Eighty");
			words.put(90, "Ninety");
			String digits[] = { "", "Hundred", "Thousand", "Lakh", "Crore" };
			while (i < digits_length) {
				int divider = (i == 2) ? 10 : 100;
				number = no % divider;
				no = no / divider;
				i += divider == 10 ? 1 : 2;
				if (number > 0) {
					int counter = str.size();
					String plural = (counter > 0 && number > 9) ? "s" : "";
					String tmp = (number < 21)
							? words.get(Integer.valueOf((int) number)) + " " + digits[counter] + plural
							: words.get(Integer.valueOf((int) Math.floor(number / 10) * 10)) + " "
									+ words.get(Integer.valueOf((int) (number % 10))) + " " + digits[counter] + plural;
					str.add(tmp);
				} else {
					str.add("");
				}
			}

			Collections.reverse(str);
			amountInWords = String.join(" ", str).trim();

			amountInWords = amountInWords + " Only";
		} catch (Exception e) {
			e.printStackTrace();
		}
		return amountInWords;
	}

	/**
	 * @param serviceUrl
	 * @param params
	 * @return returns responseBody in the form of string
	 */
	public static String performPostRequest(String serviceUrl, String params) {
		String responseBody = null;
		try {

			HttpClient httpclient = getDefaultHttpClient(null);
			HttpPost httppost = new HttpPost(serviceUrl);
			httppost.setEntity(new StringEntity(params));
			HttpResponse response = httpclient.execute(httppost);
			responseBody = inputStreamToString(response.getEntity().getContent());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return responseBody;
	}

	public static DefaultHttpClient getDefaultHttpClient(HttpParams params) {
		DefaultHttpClient client;
		if (params != null) {
			client = new DefaultHttpClient(params);
		} else {
			client = new DefaultHttpClient();
		}
		client = new DefaultHttpClient();
		return client;
	}

	public static List<NameValuePair> buildBasicNameValuePaires(Map<String, String> params) {
		List<NameValuePair> listNameValuePairs = new ArrayList<NameValuePair>();
		if (params != null) {
			Set<String> keyset = params.keySet();
			for (String key : keyset) {
				NameValuePair nvps = new BasicNameValuePair(key, params.get(key));
				listNameValuePairs.add(nvps);
			}
		}
		return listNameValuePairs;
	}

	public static String inputStreamToString(InputStream stream) throws IOException, ClassNotFoundException {

		BufferedReader br = new BufferedReader(new InputStreamReader(stream));
		StringBuilder sb = new StringBuilder();
		String line = null;
		while ((line = br.readLine()) != null) {
			sb.append(line + "\n");

		}
		br.close();

		return sb.toString();
	}

	/**
	 * @param time
	 * @return
	 */
	public static String convertDateToTime(Date time) {
		SimpleDateFormat localDateFormat = new SimpleDateFormat("HH:mm");
		localDateFormat.setTimeZone(TimeZone.getTimeZone("IST"));
		String output = localDateFormat.format(time);
		return output.replace(":", "");
	}

	public static String appendDatatoCacheKey(String cacheKey, String dataKey) {
		if (isNullOrEmpty(dataKey))
			return cacheKey + ":" + dataKey;
		return cacheKey;
	}

	public static <T> T validateAndGetGenericRequest(AWSProxyRequest awsProxyRequest, Class<T> type) {
		try {
			return AvkCommonUtil.getRequestBody(awsProxyRequest.getBody(), type);
		} catch (IOException ioException) {
			LOG.error("Error parsing the request body {0}", ioException);
			throw new CustomException("Invalid Request, please provide proper request", FCHttpStatus.BAD_REQUEST,
					CommonStatusCode.VALIDATION_ERROR_CODE);
		}
	}

	public static <T> List<T> getListResponse(String response, Class<T> type) throws IOException {
		return mapper.readValue(response, new TypeReference<List<T>>() {
        });
    }
	
	public static <T> List<Map<Object,Object>> getListOfMapResponse(String response, Class<T> type) throws IOException {
		return mapper.readValue(response, new TypeReference<List<Map<Object, Object>>>() {
        });
    }
	
	public static <T> List<Map<String,Object>> getListOfMapStringResponse(String response, Class<T> type) throws IOException {
		return mapper.readValue(response, new TypeReference<List<Map<String, Object>>>() {
        });
    }
	
	public static <T> Map<Object,Object> getMapResponse(String response, Class<T> type) throws IOException {
		return mapper.readValue(response, new TypeReference<Map<Object, Object>>() {
        });
    }
	
	public static <T> List<T> getListResponseCollection(String response, Class<T> type) throws IOException {
        CollectionType listType = mapper.getTypeFactory().constructCollectionType(ArrayList.class, type);
        return mapper.readValue(response, listType);
	}

	public static String encrypt(String plainText) throws Exception {
		String encryptedValue = null;
		try {
			Key key = generateKey();
			Cipher chiper = Cipher.getInstance(algorithm + "/CBC/PKCS5Padding");
			chiper.init(Cipher.ENCRYPT_MODE, key, new IvParameterSpec(new byte[16]));
			byte[] encVal = chiper.doFinal(plainText.getBytes(StandardCharsets.UTF_8));
			encryptedValue = Base64.getEncoder().encodeToString(encVal);
//			encryptedValue = new BASE64Encoder().encode(encVal);
		} catch (Exception ex) {
			LOG.error("Exception raised in the method encrypt:", ex);
		}
		return encryptedValue;
	}

	
	public static String encrypt(String plainText, String secretKey) throws Exception {
		String encryptedValue = null;
		try {
			Key key = generateKey(secretKey);
			Cipher chiper = Cipher.getInstance(algorithm + "/CBC/PKCS5Padding");
			chiper.init(Cipher.ENCRYPT_MODE, key, new IvParameterSpec(new byte[16]));
			byte[] encVal = chiper.doFinal(plainText.getBytes(StandardCharsets.UTF_8));
			encryptedValue = Base64.getEncoder().encodeToString(encVal);
//			encryptedValue = new BASE64Encoder().encode(encVal);
		} catch (Exception ex) {
			LOG.error("Exception raised in the method encrypt:", ex);
		}
		return encryptedValue;
	}
	public static void main(String[] args) throws Exception{
		System.out.println("Password: " + decrypt("UvB2dCD5LnX8cYqMMh8fDg=="));
		System.out.println(getSecureHashId("1738304263908"));
		System.out.println("Random Key: " + generateRandomKey());
		System.out.println("TTL Expiry Date (epoch seconds): " + LocalDateTime.now().plusHours(24).atZone(ZoneId.of("Asia/Kolkata")).toEpochSecond());
		String  fromLocationDetails= "{\"fromState\":\"Telangana\",\"fromCountry\":\"India\",\"fromsubLocation\":\"Telangana\",\"fromSearchLocation\":\"Hyderabad, Telangana, India\",\"fromLocationCountryCode\":\"IN\",\"disPlayFromName\":\"Hyderabad, Telangana, India\",\"fromPlaceId\":\"ChIJx9Lr6tqZyzsRwvu6koO3k64\",\"fromCity\":\"Hyderabad\",\"fromPlaceDetails\":{\"date\":\"\",\"country\":\"\",\"lng\":null,\"city\":\"\",\"description\":\"Hyderabad, Telangana, India\",\"landMark\":\"\",\"pinCode\":\"\",\"location\":\"Hyderabad\",\"state\":\"\",\"time\":\"\",\"subLocation\":\"\",\"place_id\":\"ChIJx9Lr6tqZyzsRwvu6koO3k64\",\"lat\":null}}";
//		String  fromLocationDetails= "{\"fromCity\":\"Mysuru\",\"fromCountry\":\"India\",\"fromLocationCountryCode\":\"IN\",\"fromState\":\"Karnataka\",\"fromsubLocation\":\"Karnataka\",\"disPlayFromName\":\"6, Infosys Road, Meenakunte, Hebbal Industrial Estate, Hebbal Industrial Area, Mysuru, Karnataka, India\",\"fromPlaceId\":\"Emc2LCBJbmZvc3lzIFJvYWQsIE1lZW5ha3VudGUsIEhlYmJhbCBJbmR1c3RyaWFsIEVzdGF0ZSwgSGViYmFsIEluZHVzdHJpYWwgQXJlYSwgTXlzdXJ1LCBLYXJuYXRha2EsIEluZGlhIi4qLAoUChIJAYKtMkZ6rzsRTvzT_iUJzUASFAoSCRVVK69Leq87EQXQsKRw9KiL\",\"fromPlaceDetails\":{\"description\":\"6, Infosys Road, Meenakunte, Hebbal Industrial Estate, Hebbal Industrial Area, Mysuru, Karnataka, India\",\"place_id\":\"Emc2LCBJbmZvc3lzIFJvYWQsIE1lZW5ha3VudGUsIEhlYmJhbCBJbmR1c3RyaWFsIEVzdGF0ZSwgSGViYmFsIEluZHVzdHJpYWwgQXJlYSwgTXlzdXJ1LCBLYXJuYXRha2EsIEluZGlhIi4qLAoUChIJAYKtMkZ6rzsRTvzT_iUJzUASFAoSCRVVK69Leq87EQXQsKRw9KiL\",\"location\":\"6\",\"city\":\"\",\"state\":\"\",\"lat\":null,\"lng\":null,\"date\":\"\",\"time\":\"\",\"subLocation\":\"\",\"landMark\":\"\",\"pinCode\":\"\",\"country\":\"\"},\"fromSearchLocation\":\"6, Infosys Road, Meenakunte, Hebbal Industrial Estate, Hebbal Industrial Area, Mysuru, Karnataka, India\"}";

		JSONObject fromLocation =new JSONObject(fromLocationDetails);
		String locationRequest = makeLocationRequest(fromLocation, "fromPlaceDetails");
		System.out.println(getLocationDetailsByChatGpt(locationRequest));
	
	}
	
	public static String decrypt(String encryptedText) throws Exception {
		String decryptedValue = null;
		try {
			Key key = generateKey();
			Cipher chiper = Cipher.getInstance(algorithm + "/CBC/PKCS5Padding");
			chiper.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(new byte[16]));
			byte[] decordedValue = Base64.getDecoder().decode(encryptedText);
//			byte[] decordedValue = new BASE64Decoder().decodeBuffer(encryptedText);
			byte[] decValue = chiper.doFinal(decordedValue);
			decryptedValue = new String(decValue);

		} catch (Exception ex) {
			LOG.error("Exception raised in the method decrypt:", ex);
		}
		return decryptedValue;
	}

	public static String decrypt(String encryptedText, String secretKey) throws Exception {
		String decryptedValue = null;
		try {
			Key key = generateKey(secretKey);
			Cipher chiper = Cipher.getInstance(algorithm + "/CBC/PKCS5Padding");
			chiper.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(new byte[16]));
			byte[] decordedValue = Base64.getDecoder().decode(encryptedText);
//			byte[] decordedValue = new BASE64Decoder().decodeBuffer(encryptedText);
			byte[] decValue = chiper.doFinal(decordedValue);
			decryptedValue = new String(decValue);

		} catch (Exception ex) {
			LOG.error("Exception raised in the method decrypt:", ex);
		}
		return decryptedValue;
	}
	
	private static Key generateKey() throws Exception {
		Key key = new SecretKeySpec(keyValue, algorithm);
		return key;
	}
	
	private static Key generateKey(String secretKey) throws Exception {
		if(AvkCommonUtil.hasData(secretKey)) {
			byte[] keyValue = new String(secretKey).getBytes(Charset.forName("UTF-8"));
			Key key = new SecretKeySpec(keyValue, algorithm);
			return key;
		}else 
			return generateKey();
	}

	public static String getTripBoxDateTime(String oldDateString, String currentDateString) throws ParseException {
		Calendar oldCalender = Calendar.getInstance();
		Calendar currentCalender = Calendar.getInstance();
		SimpleDateFormat dateFormatter = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy");
		dateFormatter.setTimeZone(TimeZone.getTimeZone("IST"));
		Date oldDate = dateFormatter.parse(oldDateString);
		Date currentDate = dateFormatter.parse(currentDateString);
		oldCalender.setTime(oldDate);
		currentCalender.setTime(currentDate);
		boolean sameDay = isSameDay(oldCalender, currentCalender);
		if (sameDay) {
			dateFormatter = new SimpleDateFormat("HH:mm");
			dateFormatter.setTimeZone(TimeZone.getTimeZone("IST"));
			return dateFormatter.format(oldCalender.getTime()) + " " + "Today";
		} else {
			dateFormatter = new SimpleDateFormat("HH:mm dd MMM yyyy");
			dateFormatter.setTimeZone(TimeZone.getTimeZone("IST"));
			return dateFormatter.format(oldCalender.getTime());
		}
	}

	public static boolean isSameDay(Calendar calendar1, Calendar calendar2) {
		return calendar1.get(Calendar.YEAR) == calendar2.get(Calendar.YEAR)
				&& calendar1.get(Calendar.MONTH) == calendar2.get(Calendar.MONTH)
				&& calendar1.get(Calendar.DAY_OF_MONTH) == calendar2.get(Calendar.DAY_OF_MONTH);
	}

	public static boolean isSameYear(Calendar calendar1, Calendar calendar2) {
		return calendar1.get(Calendar.YEAR) == calendar2.get(Calendar.YEAR);
	}

	public static void invokeLambdaAsyn(Object inputRequest, String authorization, String invokeUrl,
			LambdaLogger lambdaLogger) throws IOException {
		LogUtils.printlogger(lambdaLogger, " <<< :INVOKELAMBDA: >>> " + invokeUrl);
		String requestPayLoad = AvkCommonUtil.getResponseBody(inputRequest);
		LogUtils.printlogger(lambdaLogger, " <<< :RequestPayoad: >>> " + requestPayLoad);
		AWSLambda client = AWSLambdaClientBuilder.standard().withRegion(Regions.AP_SOUTH_1).build();
		InvokeRequest req = new InvokeRequest().withInvocationType(InvocationType.Event).withFunctionName(invokeUrl)
				.withPayload(requestPayLoad);
		client.invoke(req);
	}
	public static void invokeLambdaAsynTboHolidays(Object inputRequest, String authorization, String invokeUrl,
			LambdaLogger lambdaLogger) throws IOException, Exception {
		LogUtils.printlogger(lambdaLogger, " <<< :INVOKELAMBDA: >>> " + invokeUrl);
		String requestPayLoad = AvkCommonUtil.getResponseBody(inputRequest);
		LogUtils.printlogger(lambdaLogger, " <<< :RequestPayoad: >>> " + requestPayLoad);
		AWSLambda client = AWSLambdaClientBuilder.standard().withRegion(Regions.AP_SOUTH_1).build();
		InvokeRequest req = new InvokeRequest().withInvocationType(InvocationType.Event).withFunctionName(invokeUrl)
				.withPayload(requestPayLoad);
		client.invoke(req);
	}
	
	public static void timeLogger(Date startTime, Date endTime, String apiName, LambdaLogger lambdaLogger) {
		SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss:ms zzz");
		LogUtils.printlogger(lambdaLogger, "TimeLogger -" + apiName + " : StartTime:-" + dateFormatter.format(startTime) + ", EndTime -"+ dateFormatter.format(endTime) + ", Response Time:Dif:" + (endTime.getTime() - startTime.getTime()));
	}
	
	public static String oldDecrypt(String encryptedText) throws Exception {
		String decryptedValue = null;
		try {
			Key key = generateKey();
			Cipher chiper = Cipher.getInstance(algorithm + "/CBC/PKCS5Padding");
			chiper.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(new byte[16]));
			byte[] decordedValue = Base64.getDecoder().decode(encryptedText);
//			byte[] decordedValue = new BASE64Decoder().decodeBuffer(encryptedText);
			byte[] decValue = chiper.doFinal(decordedValue);
			decryptedValue = new String(decValue);

		} catch (Exception ex) {
			LOG.error("Exception raised in the method decrypt:", ex);
		}
		return decryptedValue;
	}
	
	public static Map<String, Object> executeSP(SimpleJdbcCall jdbcCall, Map<String, Object> inMap, boolean showInParams, boolean showOutParams, LambdaLogger lambdaLogger) {
		try {
			if(showInParams) {
				LogUtils.printlogger(lambdaLogger, jdbcCall.getProcedureName() + " -IN- " + inMap);
				
				if(inMap instanceof LinkedHashMap<?, ?>){
					StringBuilder sb = new StringBuilder();
					sb.append(" call " + jdbcCall.getProcedureName() + " ( ");
					for(String key : inMap.keySet()){
						if(inMap.get(key) instanceof String){
							sb.append( "'"+inMap.get(key)+"',");
						}else{
						sb.append( inMap.get(key)+",");}
					}
					sb.append("@errorCode,@errorMessage);");
					LogUtils.printSqlLogs(lambdaLogger, sb.toString(), "INFO", "SQL_QUERY");
					//System.out.println(sb.toString());
//				LogUtils.printlogger(lambdaLogger, sb.toString());
				}
			}
			
			SqlParameterSource in = new MapSqlParameterSource(inMap);
			long startTime = System.currentTimeMillis();
			Map<String, Object> out = jdbcCall.execute(in);
			long endTime = System.currentTimeMillis();
			LogUtils.printlogger(lambdaLogger, jdbcCall.getProcedureName() + " - Execution Time: " + ((endTime - startTime)/1000F) + " seconds");
			if(showOutParams && hasData(out)) {
				LogUtils.printlogger(lambdaLogger, jdbcCall.getProcedureName() + " -OUT- " + getResponseBody(out));
			}
			String errorCode = (String) out.get("p_ErrorCode");
			String errorMessage = (String) out.get("p_ErrorMessage");
			if(errorCode != null && !errorCode.equals("0")) {
				LogUtils.printlogger(lambdaLogger, "error In execute sp (" + jdbcCall.getProcedureName() + "): " + errorMessage);
			}
			return out;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			LogUtils.exceptionLogger(lambdaLogger, e);
			throw new CustomException("Unable to process your request. Please contact admin.", FCHttpStatus.OK, CommonStatusCode.UNHANDLED_ERROR_CODE);
		}
	}

	public static boolean isValidDateFormat(String dateStr, String dateFormat) {
		DateFormat sdf = new SimpleDateFormat(dateFormat);
		sdf.setLenient(false);
		try {
			sdf.parse(dateStr);
		} catch (ParseException e) {
			return false;
		}
		return true;
	}
	

	 public static String getDateStringFormate(String strDate, String fromDateFormat, String toDateFormat) {
        String convertedDateToStringFormat = strDate;
        if (strDate == null || fromDateFormat == null || toDateFormat == null) {
            return strDate;
        }
        try {
            DateFormat fromFormat = new SimpleDateFormat(fromDateFormat);
            fromFormat.setLenient(false);
            DateFormat toFormat = new SimpleDateFormat(toDateFormat);
            toFormat.setLenient(false);
            Date date = fromFormat.parse(strDate);
            convertedDateToStringFormat = toFormat.format(date);
        } catch (ParseException e) {
            LOG.error("Cannot parse date :{}", e);
        }
        return convertedDateToStringFormat;
    }

	 public static String splitStringDateOfBirth(String stringDate) {
		 if (stringDate !=null && !stringDate.isEmpty()) { 
			 String output = stringDate.substring(0, 10);	 
		  return output; 
		 }
		 else { 
			 return null; 
		} 
	}

	public static Date getCurrentISTDate() {
		Calendar cal = Calendar.getInstance();
		cal.setTimeZone(TimeZone.getTimeZone("IST"));
		return cal.getTime();
	}
	
	public static ApiCredential copyAPICredentials(ApiCredential source) {
		ApiCredential target = null;
		try {
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
	        ObjectOutputStream out = new ObjectOutputStream(bos);
	        out.writeObject(source);
	        //De-serialization of object
	        ByteArrayInputStream bis = new   ByteArrayInputStream(bos.toByteArray());
	        ObjectInputStream in = new ObjectInputStream(bis);
	        target = (ApiCredential) in.readObject();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return target;
	}

	@SuppressWarnings("unchecked")
	public static String getCustomParam(Map<String, Object> customParam, String paramName) {
		String param = "";
		String paramValue = "";
		String subParamValue = "";
		List<Map<String, Object>> searchCustomParamList = (List<Map<String, Object>>) customParam.get("searchCustomParam");
		if (!(paramName.equalsIgnoreCase("selected_options") || paramName.equalsIgnoreCase("pta_number"))) {
			switch (paramName) {
			case "Project_Id":
			case "Project_Name":
			case "Project_Code":
			case "SOW_Code":
			case "SOW_Name": 
			case "GL_Code": 
			case "Engagement_Code": 
			case "Mphasis_Project": 
				// To get the Project from SearchCustomParamList
				if (AvkCommonUtil.hasData(searchCustomParamList)) {
					for (Map<String, Object> searchCustomParam : searchCustomParamList) {
						if(searchCustomParam.get("key").toString().equalsIgnoreCase("Engagement_Code") && paramName.equalsIgnoreCase("Engagement_Code")) {
								return (String) searchCustomParam.get("value");
						}
						if(searchCustomParam.get("key").toString().equalsIgnoreCase("Mphasis_Project") && paramName.equalsIgnoreCase("Mphasis_Project")) {
							return (String) searchCustomParam.get("value");
						}
						if ("Project".equalsIgnoreCase((String) searchCustomParam.get("key")) 
								|| "Zydus_Project".equalsIgnoreCase((String) searchCustomParam.get("key"))
								|| "Alkem_Project".equalsIgnoreCase((String) searchCustomParam.get("key"))) {
							paramValue = (String) searchCustomParam.get("value");
							switch (paramName) {
							case "Project_Id":
								if (AvkCommonUtil.isNullOrEmpty(paramValue)) {
									param = paramValue.split("~")[0];
								}
								break;
							case "Project_Name":
								if (AvkCommonUtil.isNullOrEmpty(paramValue)) {
									param = paramValue.split("~")[1];
								}
								break;
							case "Project_Code":
								if (AvkCommonUtil.isNullOrEmpty(paramValue)) {
									param = paramValue.split("~")[2];
								}
								break;
							case "SOW_Code":
								if (AvkCommonUtil.isNullOrEmpty(paramValue)) {
									param = paramValue.split("~")[4];
								}
								break;
							case "SOW_Name":
								if (AvkCommonUtil.isNullOrEmpty(paramValue)) {
									param = paramValue.split("~")[3];
								}
								break;
							case "GL_Code":
								if (AvkCommonUtil.isNullOrEmpty(paramValue)) {
									param = paramValue.split("~")[5];
								}
								break; 
							default:
								param = "";
								break;
							}
							return param;
						} 
					}
				}
				break;
				
			case "Cost_Center_Id":
			case "Cost_Center_Name":
			case "Cost_Center_Code":
			case "Fund_Centre":
			case "WBS_Code":
				if (AvkCommonUtil.hasData(searchCustomParamList)) {
					for (Map<String, Object> searchCustomParam : searchCustomParamList) {
						 if ("Cost_Center".equalsIgnoreCase((String) searchCustomParam.get("key")) || "Company_Cost_Center".equalsIgnoreCase((String) searchCustomParam.get("key")) || "Zydus_Cost_Center".equalsIgnoreCase((String) searchCustomParam.get("key")) || "TVS_Cost_Center".equalsIgnoreCase((String) searchCustomParam.get("key"))) {
							paramValue = (String) searchCustomParam.get("value");
							switch (paramName) {
							case "Cost_Center_Id":
								if (AvkCommonUtil.isNullOrEmpty(paramValue)) {
									param = paramValue.split("~")[0];
								}
								break;
							case "Cost_Center_Name":
								if (AvkCommonUtil.isNullOrEmpty(paramValue)) {
									param = paramValue.split("~")[1];
								}
								break;
							case "Cost_Center_Code":
								if (AvkCommonUtil.isNullOrEmpty(paramValue)) {
									param = paramValue.split("~")[2];
								}
								break;
							case "Fund_Centre":
								if (AvkCommonUtil.isNullOrEmpty(paramValue)) {
									param = paramValue.split("~")[3];
								}
								break;
							case "WBS_Code":
								if (AvkCommonUtil.isNullOrEmpty(paramValue)) {
									param = paramValue.split("~")[2];
								}
								break;
							default:
								param = "";
								break;
							}
							return param;
						}
					}
				}
				break;
				
			case "BANDTEXT":
			case "COSTCENTERTEXT":
				if (AvkCommonUtil.hasData(searchCustomParamList)) {
					for (Map<String, Object> searchCustomParam : searchCustomParamList) {
						paramValue = (String) searchCustomParam.get("value");
						if (paramName.equalsIgnoreCase((String) searchCustomParam.get("key"))) {
							param = paramValue;
							return param;
						}
					}
				}
				break;
				
			default:
				param = "";
				break;
			}
			return param;
		} else {
			if (AvkCommonUtil.hasData(searchCustomParamList)) {
				for (Map<String, Object> searchCustomParam : searchCustomParamList) {
					if (paramName.equalsIgnoreCase((String) searchCustomParam.get("key"))) {
						paramValue = (String) searchCustomParam.get("value");
						break;
					} 
				}
			}
			return paramValue;
		}
	}
	
	public static String postFinBookReq(Object inputRequest, String apiKey, String invokeUrl,
			LambdaLogger lambdaLogger) throws IOException {
		
		LogUtils.printlogger(lambdaLogger, " <<< :INVOKEURL: >>> " + invokeUrl);
		String requestPayLoad = AvkCommonUtil.getResponseBody(inputRequest);
		LogUtils.printlogger(lambdaLogger, " <<< :RequestPayoad: >>> " + requestPayLoad);
		String response = null;
		if (AvkCommonUtil.isNullOrEmpty(invokeUrl)) {
		try {
			RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(2*60000)
					  .setConnectionRequestTimeout(2*60000).build();
			HttpClient client = HttpClientBuilder.create().setDefaultRequestConfig(requestConfig).build();
			HttpPost requestAmz = new HttpPost(invokeUrl);
			requestAmz.addHeader("x-api-key", apiKey);
			requestAmz.addHeader("Content-Type", ContentType.APPLICATION_JSON.toString());
			StringEntity strEntity = new StringEntity(requestPayLoad);
			requestAmz.setEntity(strEntity);	
			HttpResponse httpresponse = client.execute(requestAmz);
			LogUtils.printlogger(lambdaLogger, " <<<<<: FINBOOK Invoice Response Status: >>>> " + httpresponse.getStatusLine().getStatusCode());
			response = EntityUtils.toString(httpresponse.getEntity());
			LogUtils.printlogger(lambdaLogger, " <<<<<: FINBOOK Invoice ResponsePayload: >>>> " + response);
		} catch (SocketTimeoutException e) {
			LogUtils.exceptionLogger(lambdaLogger, e);
			LogUtils.printlogger(lambdaLogger, " <<<<<: FINBOOK Invoice TimeOut: >>>> ");
		} catch (IOException e) {
			LogUtils.exceptionLogger(lambdaLogger, e);
		throw e;
		}
		}
	return response;
	}
	
	
	public static String getFinBookReq(String apiKey, String invokeUrl,
			LambdaLogger lambdaLogger) throws IOException {
		
		LogUtils.printlogger(lambdaLogger, " <<< :INVOKEURL: >>> " + invokeUrl);
		String response = null;
		if (AvkCommonUtil.isNullOrEmpty(invokeUrl)) {
		try {
			RequestConfig requestConfig = RequestConfig.custom().setCookieSpec(CookieSpecs.STANDARD).build();
			HttpClient client = HttpClientBuilder.create().setDefaultRequestConfig(requestConfig).build();
			HttpGet requestAmz = new HttpGet(invokeUrl);
			requestAmz.addHeader("x-api-key", apiKey);
			HttpResponse httpresponse = client.execute(requestAmz);
			response = EntityUtils.toString(httpresponse.getEntity());
			LogUtils.printlogger(lambdaLogger, " <<<<<: ResponsePayload: >>>> " + response);
		} catch (SocketTimeoutException e) {
			LogUtils.exceptionLogger(lambdaLogger, e);
		} catch (IOException e) {
			LogUtils.exceptionLogger(lambdaLogger, e);
		throw e;
		}
		}
	return response;
	}
	
	
	
	/**
	 * @Method getFileSignedUrl()
	 * @param rmParamValue
	 * @return Only SignedUrl
	 */
	public static String getSignedUrl(String rmParamValue) {
		String value = "";
		String decodedURL;
		try {
			decodedURL = URLDecoder.decode(rmParamValue, "UTF-8");
			URL url = new URL(decodedURL);
			value =S3PreSignedUrlService.getPreSignedUrlforDownloadingS3(decodedURL, 5);
		} catch (Exception e) {
			value = rmParamValue;
		}
		
		return value;
	}

	public static String formatIssuingBrand(String brand) {
		if ("BTA".equalsIgnoreCase(brand)) {
			brand = "BTA";
		} else if ("CTA VISA".equalsIgnoreCase(brand) || "VISA".equalsIgnoreCase(brand)) {
			brand = "VISA";
		} else if ("CTA Master".equalsIgnoreCase(brand) || "Masters".equalsIgnoreCase(brand)) {
			brand = "MASTERS";
		} else if ("Diners".equalsIgnoreCase(brand)) {
			brand = "DINERS";
		} else if ("Rupay".equalsIgnoreCase(brand)) {
			brand = "RUPAY";
		} else if ("American Express".equalsIgnoreCase(brand)) {
			brand = "AMEX";
		} else {
			brand = "MASTERS";
		}
		return brand;
	}
	
	public static String fetchCardIssuingBrand(String brand) {
		if ("CTA VISA".equalsIgnoreCase(brand) || "VISA".equalsIgnoreCase(brand)) {
			brand = "VI";
		} else if ("CTA Master".equalsIgnoreCase(brand) || "Masters".equalsIgnoreCase(brand)) {
			brand = "CA";
		} else if ("Diners".equalsIgnoreCase(brand)) {
			brand = "DC";
		} else if ("Rupay".equalsIgnoreCase(brand)) {
			brand = "RR";
		} else if ("American Express".equalsIgnoreCase(brand)) {
			brand = "AX";
		} else {
			brand = "DC";
		}
		return brand;
	}
	
	
	// UTILITY METHODS FOR STORE PROCEDURE START ---------------------
	public static String makeLocalVariable(String s) {
		String res = "";
		for(String s1 : s.split(",")) {
			s1 = s1.replace("`", "").trim();
			res = res +","+ "l_"+s1 ;
		}
		return res;
	}
	public static void initializedLocalVariable(String s) {
		String res = "";
		for(String s1 : s.split(",")) {
			s1 = s1.replace("`", "").trim();
			res = res + "DECLARE " +s1 + " BIGINT; \n";
		}
		LogUtils.printlogger(null, res);
	}
	
	public static void makeUpdateQuery(String s) {
		String res = "";
		for(String s1 : s.split(",")) {
			s1 = s1.replace("`", "").trim();
			res += (s1 + " = l_"+s1+", ") ;
		}
		LogUtils.printlogger(null, res);
	}
	
	public static String getCamelCaseQuery(String query) {
		String result = "";
		String [] split = query.split(",");
		for(String s : split) {
			result += s.trim()+" "+getString(s,"vr.")+", ";  
		}
		return result;
	}
	
	public static String getString(String s, String alias) {
		String returnString = s.replace(alias, "").replace("_", "").trim();
		returnString = Character.toLowerCase(returnString.charAt(0)) + (returnString.length() > 1 ? returnString.substring(1) : ""); 
		return returnString;
	}
	
	
	// UTILITY METHODS FOR STORE PROCEDURE ENDS ---------------------
	
	/**
	 * This Method takes 2 Parameter (valueToBeMasked, symbolForMasking) to return Masked value of given Data with provided character 
	 * @param String(Value To Be Masked)
	 * @param String(Special character)
	 * @return maskedValue (String)
	 */
	public static String convertToMaskingNumber(String valueToBeMasked, String character) {
		StringBuffer maskedbuf = new StringBuffer();
		if(AvkCommonUtil.hasData(valueToBeMasked) && character != null && !character.isEmpty()) {
			valueToBeMasked = valueToBeMasked.trim();
			character = character.trim();
			valueToBeMasked=valueToBeMasked.replaceAll("[A-Za-z0-9]", character); //123-1234567890 ASD123
			
//			int length = valueToBeMasked.length();
//			if(length > 0) {
//				for(int i=0; i< length; i++) {
//					maskedbuf.append(character);
//				}
//			}
		}
		return AvkCommonUtil.hasData(valueToBeMasked) ? valueToBeMasked : null;
	}

	
	public static BigInteger getBigInteger(Object value) {
		return AvkCommonUtil.isBlank(value) ? null : new BigInteger(value+"") ;
	}
	
	public static BigDecimal getBigDecimal(Object value) {
		return AvkCommonUtil.isBlank(value) ? null : new BigDecimal(value+"");
	}
	
	public static Integer getInteger(Object value) {
		return AvkCommonUtil.isBlank(value) ? null: Integer.parseInt(value+"");
	}
	
	public static String getString(Object value) {
		return AvkCommonUtil.isBlank(value) ? null: value+"";
	}
	
	public static Boolean getBoolean(Object value) {
		try {
			return AvkCommonUtil.isBlank(value) ? false : getInteger(value) == 1 ;
		}catch(NumberFormatException e ) {
			return new Boolean(value+"");
		}
	}
	
	public static Long getLong(Object value) {
		return AvkCommonUtil.isBlank(value) ? null : new Long(value+"");
	}
	
	public static Double getDouble(Object value) {
		return AvkCommonUtil.isBlank(value) ? null : new Double(value+"");
	}
	
	public static Byte getByte(Object value) {
		return AvkCommonUtil.isBlank(value) ? 0 : new Byte(value+"");
	}
	
	
	
	
	
	public static BigInteger getDefaultBigInteger(Object value) {
		try {
			return AvkCommonUtil.isBlank(value) ? new BigInteger(0+"") : new BigInteger(value+"") ;
		}catch (Exception e) {
			e.printStackTrace();
			return new BigInteger(0 + "");
		}
	}
	
	public static BigDecimal getDefaultBigDecimal(Object value) {
		try {
			return AvkCommonUtil.isBlank(value) ? new BigDecimal(0+"") : new BigDecimal(value+"");
		}catch (Exception e) {
			e.printStackTrace();
			return new BigDecimal(0 + "");
		}
	}
	
	public static Integer getDefaultInteger(Object value) {
		try {
			return AvkCommonUtil.isBlank(value) ? new Integer(0 + "") : Integer.parseInt(value + "");
		}catch (Exception e) {
			e.printStackTrace();
			return new Integer(0 + "");
		}
	}
	
	public static Long getDefaultLong(Object value) {
		try {
			return AvkCommonUtil.isBlank(value) ? new Long(0+"") : new Long(value+"");
		}catch (Exception e) {
			e.printStackTrace();
			return new Long(0 + "");
		}
	}
	
	public static Double getDefaultDouble(Object value) {
		try {
			return AvkCommonUtil.isBlank(value) ? new Double(0+"") : new Double(value+"");
		}catch (Exception e) {
			e.printStackTrace();
			return new Double(0 + "");
		}

	}
	
	public static String getDefaultString(Object value) {
		return AvkCommonUtil.isBlank(value) ? "" : value + "";
	}
	 
	
	public static int getBrandIdByIssingBrandName(String brand) {
		int cardBrandId = 0;
		if ("visa".equalsIgnoreCase(brand)) {
			cardBrandId = 2;
		} else if ("rupay".equalsIgnoreCase(brand)) {
			cardBrandId = 5;
		} else if ("diners".equalsIgnoreCase(brand)) {
			cardBrandId = 4;
		} else if ("masters".equalsIgnoreCase(brand)) {
			cardBrandId = 3;
		} else if ("american express".equalsIgnoreCase(brand) || "AMEX".equalsIgnoreCase(brand)) {
			cardBrandId = 1;
		}
		return cardBrandId;
	}
	
	 public static String generateRandomPassword(int Length)
	    {
		 
		 String result="";
		 	try {
		 		  // ASCII range – alphanumeric (0-9, a-z, A-Z)
		        final String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
		 
		        SecureRandom random = new SecureRandom();
		        StringBuilder sb = new StringBuilder();
		 
		        // each iteration of the loop randomly chooses a character from the given
		        // ASCII range and appends it to the `StringBuilder` instance
		        sb.append("EVA@");
		        for (int i = 0; i < Length; i++)
		        {
		            int randomIndex = random.nextInt(chars.length());
		            sb.append(chars.charAt(randomIndex));
		        }
		        result= encrypt(sb.toString());
			} catch (Exception e) {
				// TODO: handle exception
			}
		 	return result;
	      
	    }
	 
	 
	 public static String generatePlainRandomPassword(int Length)
	    {
		 
		 String result="";
		 	try {
		 		  // ASCII range – alphanumeric (0-9, a-z, A-Z)
		        final String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
		 
		        SecureRandom random = new SecureRandom();
		        StringBuilder sb = new StringBuilder();
		 
		        // each iteration of the loop randomly chooses a character from the given
		        // ASCII range and appends it to the `StringBuilder` instance
//		        sb.append("EVA@");
		        for (int i = 0; i < Length; i++)
		        {
		            int randomIndex = random.nextInt(chars.length());
		            sb.append(chars.charAt(randomIndex));
		        }
//		        result= encrypt(sb.toString());
		        result= sb.toString();
			} catch (Exception e) {
				// TODO: handle exception
			}
		 	return result;
	      
	    }
	 
	public static double formatDecimal(Double value) {
		if (value != null) {
			return new BigDecimal(value).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
		}
		return 0d;
	}
	
	public static boolean expiredTimeWithCurrentTime(Date lastUpdatedDate,String formatLastUpdatedDate, Integer expiryInMinutes, LambdaLogger lambdaLogger) {
		if(lastUpdatedDate != null && AvkCommonUtil.isStringNotEmpty(formatLastUpdatedDate) && AvkCommonUtil.hasData(expiryInMinutes)) {
			
			Instant instant = Instant.ofEpochMilli(lastUpdatedDate.getTime());
			Instant differenceInMinutes = Instant.now().minus(Duration.ofMinutes(expiryInMinutes));
			boolean cond = instant.isBefore(differenceInMinutes);
			LogUtils.printlogger(lambdaLogger, "last Updated Time: " + lastUpdatedDate + " --- currentTime: " + Instant.now() + " --- difference(Min): " + differenceInMinutes + " --- Expiry Time(Configured): " + expiryInMinutes + " --- Expired: " + cond);
			return cond;
		}
		LogUtils.printlogger(lambdaLogger, "last Updated Time: " + lastUpdatedDate + " --- currentTime:" + Instant.now() + " --- difference(Min):NaN --- Expiry Time(Configured): " + expiryInMinutes);
		return false;
	}
	
	public static BigDecimal getExchangeRateFare(BigDecimal fare,BigDecimal  exchangeRate) {
		if(fare == null ||exchangeRate == null) {
			return null;
		}
		if(fare.equals(0) || exchangeRate.equals(1)) {
			return fare;
		}	
		else{
			
			return fare.multiply(exchangeRate).setScale(0,BigDecimal.ROUND_HALF_UP);
		}
	}

	
	public static String getPriority(String travelRequestedDate) {
		
		DateTimeFormatter dateFormate = DateTimeFormatter.ofPattern("dd-MM-yyyy");

		try {
		    LocalDate tripDate = LocalDate.parse(travelRequestedDate, dateFormate);
		    LocalDate currentDate =  LocalDate.now();
		    long daysBetween = ChronoUnit.DAYS.between(currentDate, tripDate);
			LogUtils.printlogger(null, "Days: " + tripDate+"::::"+daysBetween);
		    if (daysBetween < Long.valueOf(1)) {
				return "1";
			} else if (daysBetween >=1 && daysBetween <= 2) {
				return "2";
			} else {
				return "3";
			}
		} catch (Exception e) {
		    e.printStackTrace();
		}
		return "3";
	}
	
	public static String getDuration(String dateOfIssue, String dateOfExpiry) {
			
			DateTimeFormatter dateFormate = DateTimeFormatter.ofPattern("dd-MM-yyyy");
	
			Long daysBetween = null;
			try {
			    LocalDate Issue = LocalDate.parse(dateOfIssue, dateFormate);
			    LocalDate Expiry =  LocalDate.parse(dateOfExpiry, dateFormate);
			    daysBetween = ChronoUnit.DAYS.between(Issue, Expiry);
				LogUtils.printlogger(null, "Days: " + Expiry+"::::"+dateOfIssue);
			   
			} catch (Exception e) {
			    e.printStackTrace();
			}
			
			return AvkCommonUtil.getString(daysBetween);
		}

	public static String executeGetRequest(Map<String, String> headers, String invokeUrl, LambdaLogger lambdaLogger)
			throws IOException {

		LogUtils.printlogger(lambdaLogger, " <<< :INVOKEURL: >>> " + invokeUrl);
		String response = null;
		if (AvkCommonUtil.isNullOrEmpty(invokeUrl)) {
			try {
				RequestConfig requestConfig = RequestConfig.custom().setCookieSpec(CookieSpecs.STANDARD).build();
				HttpClient client = HttpClientBuilder.create().setDefaultRequestConfig(requestConfig).build();
				HttpGet requestAmz = new HttpGet(invokeUrl);
				if (headers != null && !headers.isEmpty()) {
					headers.forEach((key, value) -> {
						requestAmz.addHeader(key, value);
					});
				}
				HttpResponse httpresponse = client.execute(requestAmz);
				response = EntityUtils.toString(httpresponse.getEntity());
				LogUtils.printlogger(lambdaLogger, " <<<<<: ResponsePayload: >>>> " + response);
			} catch (SocketTimeoutException e) {
				LogUtils.exceptionLogger(lambdaLogger, e);
			} catch (IOException e) {
				LogUtils.exceptionLogger(lambdaLogger, e);
				throw e;
			}
		}
		return response;
	}
	
	public static String getInvoicePayloadData(String s3Url, LambdaLogger lambdaLogger) {
		
		LogUtils.printlogger(lambdaLogger, "**** FlightInvoiceServiceImpl getInvoicePayloadData --- Start");
		String invoicePayloadData = "";
		
		try {
			
			Map fileDetails = getS3BucketDetails(s3Url, lambdaLogger);
			String filePath = fileDetails.get("filePath") != null ? fileDetails.get("filePath").toString() : "";
			String fileName = fileDetails.get("fileName") != null ? fileDetails.get("fileName").toString() : "";
			String bucketName = fileDetails.get("bucketName") != null ? fileDetails.get("bucketName").toString() : "";
			String s3Region = fileDetails.get("s3Region") != null ? fileDetails.get("s3Region").toString() : AWSConstant.REGION;
	        
			LogUtils.printlogger(lambdaLogger, "**** filePath: "+filePath);
			LogUtils.printlogger(lambdaLogger, "**** bucketName: "+bucketName);
			LogUtils.printlogger(lambdaLogger, "**** fileName: "+fileName);
			
	        AmazonS3 s3Client = AmazonS3ClientBuilder.standard().withRegion(s3Region).build();
	        S3Object s3Object = s3Client.getObject(bucketName, filePath);
	        
	        S3ObjectInputStream s3is = s3Object.getObjectContent();
	        
	        BufferedReader reader = new BufferedReader(new InputStreamReader(s3is, StandardCharsets.UTF_8));
	        StringBuilder out = new StringBuilder();
			String line;
		
			while ((line = reader.readLine()) != null) {
				out.append(line);
			}
			
			invoicePayloadData = out.toString();
			
			LogUtils.printlogger(lambdaLogger, "**** Invoice payload data: "+invoicePayloadData);
			
			reader.close();
			s3is.close();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		LogUtils.printlogger(lambdaLogger, "**** FlightInvoiceServiceImpl getInvoicePayloadData --- End");
		return invoicePayloadData;
	}
	
	public static Map<String, String> getS3BucketDetails(String s3Url, LambdaLogger lambdaLogger) {

		LogUtils.printlogger(lambdaLogger, "getS3BucketDetails ---- Start ");
		LogUtils.printlogger(lambdaLogger, "S3 Url = "+s3Url);
		Map<String,String> returnMap = new HashMap<String, String>();
		
		Pattern pattern = null;
		Matcher matcher = null;
		
		pattern = Pattern.compile("(\\/\\/)[a-z.-]+(?=[.](s3))");
    	matcher = pattern.matcher(s3Url);
    	
    	String bucketName = null;
    	if(matcher.find()) {
    		bucketName = matcher.group(0);
    	}
    	
    	pattern = Pattern.compile("(?<=s3.)[a-z0-9-]+(?=[.](amazonaws))");
    	matcher = pattern.matcher(s3Url);
    	
    	String s3Region = null;
    	if(matcher.find()) {
    		s3Region = matcher.group(0);
    	}
    	returnMap.put("s3Region", s3Region);
    	
    	bucketName = bucketName.replaceAll("/", "");
    	returnMap.put("bucketName", bucketName);
    		
    	String path = s3Url.replaceAll("(http|https)[:][\\/]+[a-z0-9.-]+", "");
    	path = path.substring(1);
    	returnMap.put("filePath", path);
    	
    	String [] fileNameTemp = path.split("/");
    	returnMap.put("fileName", fileNameTemp[fileNameTemp.length-1]);
		
    	LogUtils.printlogger(lambdaLogger, "getS3BucketDetails return data = "+returnMap);
    	LogUtils.printlogger(lambdaLogger, "getS3BucketDetails ---- End");
		return returnMap;
	}
	public static void printXml(String message, String s) {
        LOG.info(message, s);
    }
	
	/**
	 * @param getGenderNameByTitle
	 * @return Gender
	 */
	public static String getGenderNameByTitle(String title) {
		String gender = null;
		if(AvkCommonUtil.isBlank(title))
			gender = Gender.MALE.toString();
		else {
			switch (title.toLowerCase().replace(" ", "")) {
				case "mr":
				case "mstr":
				case "dr(m)":
				case "prof(m)":
				case "capt(m)":
				case "hon(m)":
				case "gen(m)":
				case "sen(m)":
				case "col(m)":
				case "sgt(m)":
				case "lt(m)":
				case "maj(m)":
				case "comm(m)":
				case "judg(m)":
				case "jstc(m)":
				case "hrh(m)":
				case "lord":
				case "fr":
				case "br":
				case "sr":
				case "sir":
				case "rev":	
					gender = Gender.MALE.toString();
					break;
				case "ms":
				case "mrs":
				case "miss":
				case "dr(f)":
				case "prof(f)":
				case "capt(f)":
				case "hon(f)":
				case "gen(f)":
				case "sen(f)":
				case "col(f)":
				case "sgt(f)":
				case "lt(f)":
				case "maj(f)":
				case "comm(f)":
				case "judg(f)":
				case "jstc(f)":
				case "hrh(f)":
				case "lady":
					gender = Gender.FEMALE.toString();
					break;
				case "mx":
					gender = Gender.TRANSGENDER.toString();
					break;	
				default:
					gender = Gender.MALE.toString();
					break;
			}
		}
		return gender;
	}
	public static void invokeLambdaAsynSupplierHotelStaticCity(Object inputRequest , String invokeUrl,LambdaLogger lambdaLogger) throws Exception {
		LogUtils.printlogger(lambdaLogger, " <<< :INVOKELAMBDA: >>> " + invokeUrl);
		String requestPayLoad = AvkCommonUtil.getResponseBody(inputRequest);
		LogUtils.printlogger(lambdaLogger, " <<< :RequestPayoad: >>> " + requestPayLoad);
		AWSLambda client = AWSLambdaClientBuilder.standard().withRegion(Regions.AP_SOUTH_1).build();
		InvokeRequest req = new InvokeRequest().withInvocationType(InvocationType.Event).withFunctionName(invokeUrl).withPayload(requestPayLoad);
		client.invoke(req);
	}
	
	// get Unique Data from list
	public static <T> java.util.function.Predicate<T> distinctByKey(
			java.util.function.Function<? super T, Object> keyExtractor) {
		java.util.Set<Object> seen = new java.util.HashSet<>();
		return t -> seen.add(keyExtractor.apply(t));
	}
		// trim the string based on the length
	public  static String trimString(String address,int maxLength) {
		if(AvkCommonUtil.hasData(address) && address.trim().length()>maxLength) {
			address = address.trim().substring(0,maxLength);
		}
		return address;
	}
	
	/**
	 * Setting up Proxy Data
	 */
	public static void setProxyData() {
		if(AvkCommonUtil.hasData(System.getenv("APPLY_PROXY")) && Boolean.parseBoolean(System.getenv("APPLY_PROXY"))) {
			if(AvkCommonUtil.hasData(System.getenv("BELAIR_PROXY_HOST")) && AvkCommonUtil.hasData(System.getenv("BELAIR_PROXY_PORT"))) {
				System.setProperty("http.proxyHost", System.getenv("BELAIR_PROXY_HOST"));
				System.setProperty("http.proxyPort", System.getenv("BELAIR_PROXY_PORT"));
			}
        }
	}
	
	public static void invokeExceptionInfoUrl(ExceptionEmailRequest exceptionEmailRequest, String authorization, String invokeUrl,
			LambdaLogger lambdaLogger) throws IOException {
		LogUtils.printlogger(lambdaLogger, " <<< :INVOKEURL: >>> " + invokeUrl);
		String requestPayLoad = AvkCommonUtil.getResponseBody(exceptionEmailRequest);
		LogUtils.printlogger(lambdaLogger, " <<< :RequestPayoad: >>> " + requestPayLoad);
		if (AvkCommonUtil.isNotNullOrEmpty(invokeUrl)) {
			try {
				RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(1000).build();
				HttpClient client = HttpClientBuilder.create().setDefaultRequestConfig(requestConfig).build();
				HttpPost requestAmz = new HttpPost(invokeUrl);
				if(authorization != null) {
					requestAmz.addHeader(AvkConstants.AUTHORIZATION, authorization);
				}
				requestAmz.addHeader("Content-Type", ContentType.APPLICATION_JSON.toString());
				StringEntity strEntity = new StringEntity(requestPayLoad);
				requestAmz.setEntity(strEntity);
				HttpResponse response = client.execute(requestAmz);
				LogUtils.printlogger(lambdaLogger, "<<< Response Payload >>>: " + EntityUtils.toString(response.getEntity()));
			} catch (SocketTimeoutException e) {
				System.out.println("SocketTimeoutException <<< :INVOKEURL: >>> " + invokeUrl);
				System.out.println(" <<< :RequestPayoad: >>> " + requestPayLoad);
				LogUtils.exceptionLogger(lambdaLogger, e);
			} catch (IOException e) {
				System.out.println("IOException <<< :INVOKEURL: >>> " + invokeUrl);
				System.out.println(" <<< :RequestPayoad: >>> " + requestPayLoad);
				LogUtils.exceptionLogger(lambdaLogger, e);
			} catch (Exception e) {
				System.out.println("Exception <<< :INVOKEURL: >>> " + invokeUrl);
				System.out.println(" <<< :RequestPayoad: >>> " + requestPayLoad);
				LogUtils.exceptionLogger(lambdaLogger, e);
			}
		}
	}
	
	/**
     * 
     * @param exception
     * @param exceptionEmailRequest
     * @param invokeUrl - send URL to initiated email (without invoke URL function will not execute)
     * @param awsProxyRequest
     * @param productId
     * 			1 - Flight
     * 			2 - Hotel
     * @param cacheKey
     * @param supplierName
     * 			- Name of the supplier (applicable if unhandled exception from third party suppliers
     * @param lambdaLogger
     * @throws IOException
     */
	public static void sendExceptionDetails(Exception exception, ExceptionEmailRequest exceptionEmailRequest, String invokeUrl, AWSProxyRequest awsProxyRequest, String productId, String cacheKey, String supplierName, LambdaLogger lambdaLogger) {
		try {
	    	if (AvkCommonUtil.hasData(invokeUrl)) {
				if(exceptionEmailRequest == null) { //default params for all third party suppliers
					exceptionEmailRequest = new ExceptionEmailRequest();
					exceptionEmailRequest.setProductId(AvkCommonUtil.getDefaultString(productId));
					exceptionEmailRequest.setExceptionStackTrace(getExceptionStackTrace(exception));
					exceptionEmailRequest.setCacheKey(cacheKey);
					exceptionEmailRequest.setSupplierName(supplierName);
				}
				String authorizationToken = null;
				if(awsProxyRequest != null) {
					exceptionEmailRequest.setApiGatewayUrl(awsProxyRequest.getResource());
					exceptionEmailRequest.setRequestPayload(awsProxyRequest.getBody());
					if(AvkCommonUtil.hasData(awsProxyRequest.getHeaders())) {
						authorizationToken = AvkCommonUtil.getAuthorizationToken(awsProxyRequest.getHeaders());
					}
				}
				AvkCommonUtil.invokeExceptionInfoUrl(exceptionEmailRequest, authorizationToken, invokeUrl, lambdaLogger);
			} else {
				LogUtils.exceptionLogger(lambdaLogger, exception);
				LogUtils.printlogger(lambdaLogger, "Url details are not available to share unhandle exception details.");
			}
		} catch (SocketTimeoutException socketException) {
			LogUtils.printlogger(lambdaLogger, "Ignore this SocketTimeoutException");
		} catch (Exception e) {
			LogUtils.exceptionLogger(lambdaLogger, e);
		}
	}
	
	public static String getExceptionStackTrace(Exception exception) {
		if(exception != null) {
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			exception.printStackTrace(pw);
			LogUtils.printlogger(null, sw.toString());
			return sw.toString();
		}
		return null;
	}
	
	/**
	 * 
	 * @param input
	 * @return Capital Fist Letter and flowed by small letters
	 */
	public static String capitalizeFirstLetter(String input) {
        if (isBlank(input)) {
            return input;
        }

        // Convert the first letter to uppercase
        char firstChar = Character.toUpperCase(input.charAt(0));

        // Convert the remaining letters to lowercase
        String restOfString = input.substring(1).toLowerCase();

        // Combine the first letter and the rest of the string
        return firstChar + restOfString;
    }
	
	@SuppressWarnings("serial")
	public static void callCallBackStepFunction(String s3Url, String client, String funName) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("webHookData", new ArrayList<Map<String,Object>>() {{
			add(new HashMap<String,Object>(){{
				put("apiType", client);
				put("reqData", AvkCommonUtil.getResponseBody(new HashMap<String,Object>(){{put("s3Url", s3Url); put("type", funName);}}));
			}});
		}});
		String invokeUrl =  System.getenv("WEB_HOOKS_STEP_FUNCTION_URL");
		// Invoking Step Function URL.
		invokeUrlAsyn(map, null, invokeUrl, null);
	} 
	
	
	
	/**
	 * 
	 * @param fileName   : Cancel_Request
	 * @param folderName : tripId/requestId 
	 * @param client	 : MMT
	 * @param bucketName : eva-resources
	 * @param data	     : complete request/response  
	 * @param refrenceId : requestId 
	 * @param s3client   : s3Object 
	 */
	public static String uploadCronJobWebHookLogsToS3(String fileName, String folderName, String client, String bucketName, String data, Object refrenceId, AmazonS3 s3client) {
		byte[] bytes = data.toString().getBytes(StandardCharsets.UTF_8);
		InputStream is = new ByteArrayInputStream(bytes);
		ObjectMetadata meta = new ObjectMetadata();
		meta.setContentLength(bytes.length);
		meta.setContentType("text/plain");

		String destinationKeyName = "application-uploads/Cron-Job/"+ client +"/" + getDate() + "/" + folderName + "/" + fileName + ".json";
		s3client.putObject(bucketName, destinationKeyName, is, meta);
		LogUtils.printlogger(null, "Log for reference Id::" + refrenceId + " bucketName  :: " + bucketName + "   destinationKeyName :: " + destinationKeyName);
		return destinationKeyName;
	}
	
	/**
	 * 
	 * @param fileName   : Cancel_Request
	 * @param folderName : tripId/requestId 
	 * @param client	 : MMT
	 * @param bucketName : eva-resources
	 * @param data	     : complete request/response  
	 * @param refrenceId : requestId 
	 * @param s3client   : s3Object 
	 */
	public static String uploadCronJobWebHookLogsToS3WithoutDate(String fileName, String folderName, String client, String bucketName, String data, Object refrenceId, AmazonS3 s3client) {
		byte[] bytes = data.toString().getBytes(StandardCharsets.UTF_8);
		InputStream is = new ByteArrayInputStream(bytes);
		ObjectMetadata meta = new ObjectMetadata();
		meta.setContentLength(bytes.length);
		meta.setContentType("text/plain");

		String destinationKeyName = "application-uploads/Cron-Job/"+ client +"/" + folderName + "/" + fileName + ".json";
		s3client.putObject(bucketName, destinationKeyName, is, meta);
		LogUtils.printlogger(null, "Log for reference Id::" + refrenceId + " bucketName  :: " + bucketName + "   destinationKeyName :: " + destinationKeyName);
		return destinationKeyName;
	}

	private static String getDate() {
		String stringDate = "";
		try {
			Calendar cal = Calendar.getInstance();
			Date date = cal.getTime();
			SimpleDateFormat format1 = new SimpleDateFormat("ddMMMyyyy");
			stringDate = format1.format(date);
			LogUtils.printlogger(null, stringDate);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return stringDate;
	}

	/**
	 * @Method getOnlyNumberFromString()
	 * @param mobileNo
	 * @return Only Numbers from the Numeric String [Ex: Removing Special charaters from the phone number]
	 */
	public static String getOnlyNumberFromString(String mobileNo) {
		if(isNullOrEmpty(mobileNo)){
			return mobileNo.replaceAll("[^0-9]", "");
		}
		return null;
	}
	
	/**
	 * return merge names, remove spaces and lower case
	 * @param firstName
	 * @param lastName
	 * @return
	 */
	public static String mergeName(String firstName, String lastName) {
    	return getDefaultString(firstName).replaceAll("\\s+", "").toLowerCase() + getDefaultString(lastName).replaceAll("\\s+", "").toLowerCase();
	}

	public static void invokeAsynLambdaForDynamoDb(Object requestedObject, String lambdaFunctionName, LambdaLogger lambdaLogger) {
		try {
			AWSProxyRequest awsProxyRequest = new AWSProxyRequest();
			awsProxyRequest.setBody(AvkCommonUtil.getResponseBody(requestedObject));
			AvkCommonUtil.invokeLambdaAsyn(awsProxyRequest, null, lambdaFunctionName, lambdaLogger);
		} catch (Exception e) {
			LogUtils.exceptionLogger(lambdaLogger, e);
		}
	}
	
	 private static final String API_KEY = "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";
	 private static final String API_URL = "https://api.openai.com/v1/chat/completions";

	 public static String getLocationDetailsByChatGpt(String locationRequest) {
	    	String reply = "";
	    	try {
	            // Create the URL and connection
	            URL url = new URL(API_URL);
	            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
	            connection.setRequestMethod("POST");
	            connection.setRequestProperty("Content-Type", "application/json");
	            connection.setRequestProperty("Authorization", "Bearer " + API_KEY);
	            connection.setDoOutput(true);

	            // Create the request payload
	            JSONObject payload = new JSONObject();
	            payload.put("model", "gpt-3.5-turbo");

	            // Add messages (user input to the API)
	            JSONArray messages = new JSONArray();
	            JSONObject message = new JSONObject();
	            message.put("role", "user");
	            message.put("content", locationRequest);
	            messages.put(message);

	            payload.put("messages", messages);

	            // Write the request body
	            try (OutputStream os = connection.getOutputStream()) {
	                byte[] input = payload.toString().getBytes("utf-8");
	                os.write(input, 0, input.length);
	            }

	            // Get the response
	            try (BufferedReader br = new BufferedReader(
	                    new InputStreamReader(connection.getInputStream(), "utf-8"))) {
	                StringBuilder response = new StringBuilder();
	                String responseLine;
	                while ((responseLine = br.readLine()) != null) {
	                    response.append(responseLine.trim());
	                } 

	                // Parse the JSON response if needed
	                JSONObject jsonResponse = new JSONObject(response.toString());
	                reply = jsonResponse.getJSONArray("choices")
	                                           .getJSONObject(0)
	                                           .getJSONObject("message")
	                                           .getString("content");
	             
	            }

	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	    	return reply;
	    }
	  
	public static String makeFlightLocationRequest(JSONObject locationDetails, String key) {
		return "Nearest airport IATA code by proximity to "+locationDetails.getJSONObject(key).getString("description")+". Answer without explanation.";
	}
	
	public static String makeBusLocationRequest(JSONObject locationDetails, String key) {
		return "What is the city for the location "+locationDetails.getJSONObject(key).getString("description")+". Give me the answer without any explanation.";
	}
	
	public static String makeRailLocationRequest(JSONObject locationDetails, String key) {
		return "Nearest major railway station code to "+locationDetails.getJSONObject(key).getString("description")+", for long-distance/express trains. Answer without explanation.";
	} 
	
	public static final String KEY_RAILWAY_STATION_CODE = "nearest_major_railway_station_code";
	public static final String KEY_CITY_NAME = "nearest_major_city_name";
	public static final String KEY_AIRPORT_CODE = "nearest_major_airport_iata_code";
	public static final String KEY_TOWN_NAME = "nearest_major_town_name";
	
	public static String makeLocationRequest(JSONObject locationDetails, String key) {
		String description = AvkCommonUtil.hasData(locationDetails.opt(key))
				? locationDetails.getJSONObject(key).getString("description")
				: "Hyderabad, Telangana";
		String improvedMessage = "Provide the following information in JSON format for the location closest to " + 
				description + ":\n" +
			    "1. Nearest major railway station code for long-distance/express trains\n" +
			    "2. The nearest major metropolitan city for the location, using the English variant of the city name (e.g., 'Bangalore' instead of 'Bengaluru', 'Hyderabad' instead of 'Secunderabad')\n" +
			    "3. The IATA code of the nearest major airport by proximity\n" +
			    "4. The nearest town to the location, using the English variant of the town name\n" +
			    "Use these exact key names in your JSON response: " +
			    KEY_RAILWAY_STATION_CODE + ", " + KEY_CITY_NAME + ", " + KEY_AIRPORT_CODE+ ", " + KEY_TOWN_NAME + 
			    ". \n Ensure that the nearest major railway station and airport are selected for long-distance/express services, " + 
			    " and the airport chosen should be the major one serving international or national flights, " + 
			    " not regional or smaller airports. \n Respond with only the JSON object, no additional text.";
		return improvedMessage;
	}

	public static String invokeStepFunctionSave(Map<String, Object> inputRequest, String authorization,
            String invokeUrl, boolean printLog, LambdaLogger lambdaLogger) {
        LogUtils.printlogger(lambdaLogger, " <<< :INVOKEURL: >>> " + invokeUrl);
        String requestPayLoad = AvkCommonUtil.getResponseBody(inputRequest);
        requestPayLoad = requestPayLoad.replace("\'", "");
        LogUtils.printlogger(lambdaLogger, " <<< :RequestPayoad: >>> " + requestPayLoad);
        String stepFunctionArn = "";

        if (AvkCommonUtil.isNotNullOrEmpty(invokeUrl)) {
            try {
                RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(1000).build();
                HttpClient client = HttpClientBuilder.create().setDefaultRequestConfig(requestConfig).build();
                HttpPost requestAmz = new HttpPost(invokeUrl);
                requestAmz.addHeader(AvkConstants.AUTHORIZATION, authorization);
                requestAmz.addHeader("Content-Type", ContentType.APPLICATION_JSON.toString());
                StringEntity strEntity = new StringEntity(requestPayLoad);
                requestAmz.setEntity(strEntity);
                HttpResponse response = client.execute(requestAmz);

                String responseString = EntityUtils.toString(response.getEntity());
                // Parse the response JSON to extract the executionArn
                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode jsonNode = objectMapper.readTree(responseString);
                stepFunctionArn = jsonNode.get("executionArn").asText();

                if (printLog) {
                    System.out.println("<<< Step Function ARN >>>: " + responseString);
                }

            } catch (SocketTimeoutException e) {
                LogUtils.exceptionLogger(lambdaLogger, e);
            } catch (IOException e) {
                LogUtils.exceptionLogger(lambdaLogger, e);
            }
        }
        return stepFunctionArn;
    }

	public static void storeActualDataToS3(String fileName, String response, String bucket) {
		try {
			AmazonS3 s3Client = AmazonS3ClientBuilder.defaultClient();
		 	byte[] contentBytes = response.getBytes(StandardCharsets.UTF_8);
	        InputStream inputStream = new ByteArrayInputStream(contentBytes);
	        ObjectMetadata metadata = new ObjectMetadata();
	        metadata.setContentType("text/html; charset=utf-8");  // Explicitly set charset
	        metadata.setContentLength(contentBytes.length);
	        metadata.setContentEncoding("UTF-8");
	        s3Client.putObject(bucket, fileName, inputStream, metadata); 
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static boolean isFileExistsInS3(String key, String bucketName, AmazonS3 instance) {
        try {
            return instance.doesObjectExist(bucketName, key);
        } catch (AmazonServiceException e) {
            throw new RuntimeException("Failed to check file existence in S3", e);
        }
    }

    public static String readDataFromS3(String key, String bucketName, AmazonS3 instance) {
        try {
            if (!isFileExistsInS3(key, bucketName, instance)) {
                throw new FileNotFoundException("Template not exist in s3" + key);
            }
            S3Object s3Object = instance.getObject(bucketName, key);
            return IOUtils.toString(s3Object.getObjectContent(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException("Failed to read data from S3", e);
        }
    }
    

    public static String translateHtmlContent(String htmlString, String targetLanguage, LambdaLogger lambdaLogger) throws Exception {
		Map<String, Object> inputRequest = new HashMap<>();
		inputRequest.put("html_template", htmlString);
		inputRequest.put("target_language", targetLanguage);
		String translateUrl = Optional.ofNullable(System.getenv("TRANSLATE_API_URL"))
				.filter(AvkCommonUtil::hasData)
				.orElse("https://dev.api.eva.travel/python/v1/statguru-french-api");
		String response = invokeUrlSatGuruTranslation(inputRequest, null, translateUrl, lambdaLogger);
		if (response.trim().startsWith("{")) {
			JSONObject responseJson = new JSONObject(response);
			String errorMessage = responseJson.optString("error", "Unknown error occurred");
			lambdaLogger.log("Translation error: " + errorMessage);
			throw new RuntimeException("Translation failed: " + errorMessage);
		} else {
			htmlString = response;
		}
		return htmlString;
	}
    
    public static String uploadToS3(String pdfBase64Response, String bucketName, AmazonS3 s3Client) {
        if (pdfBase64Response == null || pdfBase64Response.isEmpty()) {
            return "";
        }
        try {
            byte[] pdfData = Base64.getDecoder().decode(pdfBase64Response);
            String fileName = "pdf_" + UUID.randomUUID().toString() + ".pdf";
            String s3Key = "application-assets/Actual_Email_Template/" + fileName;
            
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentType("application/pdf");
            metadata.setContentLength(pdfData.length);
            metadata.setHeader("Content-Disposition", "attachment; filename=" + fileName);
            try (ByteArrayInputStream inputStream = new ByteArrayInputStream(pdfData)) {
                s3Client.putObject(new PutObjectRequest(bucketName, s3Key, inputStream, metadata)
                        .withCannedAcl(CannedAccessControlList.PublicRead));
            }
            return String.format("https://%s.s3.amazonaws.com/%s", bucketName, s3Key);
        } catch (Exception e) {
            return "";
        }
    }
	
	public static void saveEnglishTranslatedTemplateInS3(String hotelRequestId, String response, String product) {
	    String eva_bucket = Optional.ofNullable(System.getenv("EVA_S3_BUCKET"))
	            .filter(AvkCommonUtil::hasData)
	            .orElse("eva-resources");
	    String folderName = "application-assets/Actual_Email_Template/" + product + "/" + hotelRequestId + ".html";
	    AvkCommonUtil.storeActualDataToS3(folderName, response, eva_bucket);
	}
	
	public static JSONObject getNearestCityByAI(String cityDetails, LambdaLogger lambdaLogger) throws Exception {
		String hotelCitiesbyAIURL = AvkCommonUtil.isBlank(System.getenv("CITIES_BY_AI_URL")) ?"https://uat.api.eva.travel/settings/v1/search/product-location-info" :System.getenv("CITIES_BY_AI_URL");
		Map<String, Object> map = new HashMap<>();
		map.put("cityDetails", cityDetails);
		map.put("actionType", "nearest_city");
		String cityData = AvkCommonUtil.invokeUrl(map, null, hotelCitiesbyAIURL, lambdaLogger);
		LogUtils.printDebuggerLogs(lambdaLogger, "AI city Data::"+cityData, null, null);
		JSONObject cityAPIJson = new JSONObject(cityData);
		JSONObject cityJsonObj = new JSONObject(cityAPIJson.optString("payload"));
		return cityJsonObj;
	}

	public static String sanitizeFileNameForS3(String fileName) {
		// Remove characters that might cause issues with S3 or downstream processing
		String sanitized = fileName.replaceAll("[\\\\/:*?\"<>|^&%$()]", "_");
		//returns only with one underscore even if it has multiple special characters
		sanitized = sanitized.replaceAll("_{2,}", "_");
		return sanitized;
	}
	
	public static String removeSpaces(String value) {
		if (value != null) {
			return value.replaceAll("\\s+", "");
		}
		return null;
	}
	
}
