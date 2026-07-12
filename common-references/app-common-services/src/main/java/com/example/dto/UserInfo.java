package com.example.dto;

import java.math.BigInteger;

import lombok.Data;

/**
 * @author Bhargava Kurella
 * @version 1.0
 * @since 07/01/2020
 *
 */

@Data
public class UserInfo {
	
	private String sub;
	private String groups;
	private String gender;
	private String iss;
	private String roles;
	private String auth_time;
	private String exp;
	private String iat;
	private String email;
	private String corporate_Name;
	private String country;
	private String email_verified;
	private String username;
	private String aud;
	private String event_id;
	private String token_use;
	private String name;
	private String phone_number;
	private BigInteger user_Id;
}
