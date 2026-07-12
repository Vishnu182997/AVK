package com.example.entity.login;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;


/**
 * @author Santram
 * @version 1.0
 * @since 29/06/2020
 *
 */

@Entity
@Getter
@Setter
@Table(name = "Validate_Tiny_URL")
public class ValidateLoginURLEntity {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "Tiny_URL_Id")
	private Long tinyURLId;
	@Column(name = "Actual_URL")
	private String actualURL;
	@Column(name = "Tiny_URL")
	private String tinyURL;
	@Column(name = "User_Id")
	private String userId;
	
}
