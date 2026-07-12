package com.example.common.request.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CredentialRequestDTO {
	private Boolean isPersonalBooking;
	private String countryShortCode;
}
