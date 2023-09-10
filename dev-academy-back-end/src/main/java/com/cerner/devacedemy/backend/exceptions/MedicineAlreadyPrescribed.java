package com.cerner.devacedemy.backend.exceptions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT)
public class MedicineAlreadyPrescribed extends RuntimeException {

	private static final long serialVersionUID = 1L;

	Logger logger = LoggerFactory.getLogger(MedicineAlreadyPrescribed.class);

	private String resourceName;
	private String medicine;
	private long encounterId;

	public MedicineAlreadyPrescribed(String resourceName, String medicine, long encounterId) {
		super(String.format("%s %s already exists in encounter %d", resourceName, medicine, encounterId));
		this.resourceName = resourceName;
		this.medicine = medicine;
		this.encounterId = encounterId;
		logger.warn("Medicine Already prescribed for Patient in current Encounter");
	}

	public String getResourceName() {
		return resourceName;
	}

	public String getMedicine() {
		return medicine;
	}

	public long getEncounterId() {
		return encounterId;
	}
}
