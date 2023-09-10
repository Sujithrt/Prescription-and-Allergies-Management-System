package com.cerner.devacedemy.backend.exceptions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT)
public class MedicineAlreadyPrescribedException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	Logger logger = LoggerFactory.getLogger(MedicineAlreadyPrescribedException.class);

	/**
	 * Exception thrown when user tries to prescribe the same medicine more than once in a single encounter
	 * 
	 * @param resourceName
	 * @param medicine
	 * @param encounterId
	 */
	public MedicineAlreadyPrescribedException(String resourceName, String medicine, long encounterId) {
		super(String.format("%s %s already exists in encounter %d", resourceName, medicine, encounterId));
		logger.warn("Medicine Already prescribed for Patient in current Encounter");
	}
}
