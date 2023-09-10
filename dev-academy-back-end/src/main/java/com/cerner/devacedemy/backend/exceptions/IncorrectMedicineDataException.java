package com.cerner.devacedemy.backend.exceptions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class IncorrectMedicineDataException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	Logger logger = LoggerFactory.getLogger(IncorrectMedicineDataException.class);
	/**
	 * Exception thrown when medicine data passed in Request body is inaccurate with respect to data stored in database
	 * 
	 * @param resourceName
	 * @param medicineName
	 * @param medicineId
	 */
	public IncorrectMedicineDataException(String resourceName, String medicineName, long medicineId) {
		super(String.format("%s %s does not have id %d", resourceName, medicineName, medicineId));
		logger.warn("Incorrect Medicine data");
	}

}
