package com.cerner.devacedemy.backend.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class InvalidEmailOrPasswordException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	/**
	 * Exception thrown when invalid email or password is entered while registering a user
	 * 
	 * @param fieldName
	 * @param fieldValue
	 */
	public InvalidEmailOrPasswordException(String fieldName, String fieldValue) {
		super(String.format("%s %s is Invalid, Please ensure you enter a valid Email ID and "
				+ "that your password is at least 8 characters in length", fieldName, fieldValue));
	}

}
