package com.cerner.devacedemy.backend.exceptions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT)
public class AllergyAlreadyExistsException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	Logger logger = LoggerFactory.getLogger(AllergyAlreadyExistsException.class);
	/**
	 * Exception thrown when a user tries to add an allergy to a patient when the allergy is already recorded for patient
	 * 
	 * @param resourceName
	 * @param allergyName
	 * @param patientId
	 */
	public AllergyAlreadyExistsException(String resourceName, String allergyName, long patientId) {
		super(String.format("%s %s already exists for %d", resourceName, allergyName, patientId));
		logger.warn("Allergy Already Exists for Patient");
	}

}
