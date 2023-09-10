package com.cerner.devacedemy.backend.response;

import com.cerner.devacedemy.backend.entities.Allergies.Severity;

public class IncompatibleAllergiesResponse {

	private long medicineId;
	private String reaction;
	private Severity severity;
	
	public IncompatibleAllergiesResponse() {
	}
	
	public IncompatibleAllergiesResponse(long medicineId, String reaction, Severity severity) {
		super();
		this.medicineId = medicineId;
		this.reaction = reaction;
		this.severity = severity;
	}

	public long getMedicineId() {
		return medicineId;
	}

	public void setMedicineId(long medicineId) {
		this.medicineId = medicineId;
	}

	public String getReaction() {
		return reaction;
	}

	public void setReaction(String reaction) {
		this.reaction = reaction;
	}

	public Severity getSeverity() {
		return severity;
	}

	public void setSeverity(Severity severity) {
		this.severity = severity;
	}

	@Override
	public String toString() {
		return "IncompatibleAllergiesResponse [medicineId=" + medicineId + ", reaction=" + reaction + ", severity="
				+ severity + "]";
	}
}
