package com.example.common.request.dto;

import lombok.Data;

/**
 * @author Bhargava Kurella
 * @version 1.0
 * @since 22/11/2019
 *
 */
@Data
public class ValidateRequestDTO {
	
	private boolean validRequest;
	private String errorMessage;

}
