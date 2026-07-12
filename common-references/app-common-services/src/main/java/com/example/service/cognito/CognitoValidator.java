package com.example.service.cognito;

import java.math.BigInteger;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.dto.UserInfo;

/**
 * @author Bhargava Kurella
 * @version 1.0
 * @since 07/01/2020
 *
 */
@Component
public class CognitoValidator {

	private String extractAndDecodeJwt(String token) {
		String tokenResult = token;

		if (token != null && token.startsWith("Bearer ")) {
			tokenResult = token.substring("Bearer ".length());
		}
		return tokenResult;
	}

	public UserInfo decodeJWTToken(String idToken) {
		idToken = extractAndDecodeJwt(idToken);
		DecodedJWT jwt = JWT.decode(idToken);
		return mapsToUserInfo(jwt.getClaims());
	}
	
	public UserInfo mapsToUserInfo(Map<String, Claim> map) {
		UserInfo userInfo = new UserInfo();
		try {
		userInfo.setUser_Id(map.get("custom:User_Id")!=null?new BigInteger(map.get("custom:User_Id").asString()):null);
		userInfo.setSub(map.get("sub").asString());
		userInfo.setGroups(map.get("cognito:groups").asString());
		userInfo.setGender(map.get("gender").asString());
		userInfo.setIss(map.get("iss").asString());
		userInfo.setRoles(map.get("cognito:roles").asString());
		userInfo.setAuth_time(map.get("auth_time").asString());
		userInfo.setExp(map.get("exp").asString());
		userInfo.setIat(map.get("iat").asString());
		userInfo.setEmail(map.get("email").asString());
		userInfo.setCorporate_Name(map.get("custom:Corporate_Name").asString());
		//userInfo.setCountry(map.get("custom:country").asString());
		userInfo.setEmail_verified(map.get("email_verified").asString());
		userInfo.setUsername(map.get("cognito:username").asString());
		userInfo.setAud(map.get("aud").asString());
		userInfo.setEvent_id(map.get("event_id").asString());
		userInfo.setToken_use(map.get("token_use").asString());
		userInfo.setName(map.get("name").asString());
		userInfo.setPhone_number(map.get("phone_number").asString());
		}catch (Exception e) {
			e.printStackTrace();
		}
		return userInfo;
	}
}
