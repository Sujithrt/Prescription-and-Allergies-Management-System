package com.cerner.devacedemy.backend.requests;

import com.cerner.devacedemy.backend.entities.Allergies.Severity;
import com.cerner.devacedemy.backend.entities.Allergies.Status;
import com.cerner.devacedemy.backend.entities.Ingredients;


public class AddAllergyRequest {

	private Ingredients ingredient;
	private long encounterId;
	private String name;
	private String reaction;
	private Severity severity;
	private Status status;

	public Ingredients getIngredient() {
		return ingredient;
	}
	public void setIngredient(Ingredients ingredient) {
		this.ingredient = ingredient;
	}
	public long getEncounterId() {
		return encounterId;
	}
	public void setEncounterId(long encounterId) {
		this.encounterId = encounterId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
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
	public Status getStatus() {
		return status;
	}
	public void setStatus(Status status) {
		this.status = status;
	}

	/**
	 * Verify validity of Request body for Add allergy API
	 * 
	 * @param allergyRequest
	 * @param patientId
	 * @return true or false
	 */
	public boolean verify(long patientId) {
		if(!name.trim().equals("") && !reaction.trim().equals("") && severity != null && status != null  && 
				patientId > 0 && ingredient.getId() > 0 && !ingredient.getName().trim().equals("") && encounterId >= 0) {
			return true;
		} else {
			return false;
		}
	}
	@Override
	public String toString() {
		return "AddAllergyRequest [ingredient=" + ingredient + ", encounterId=" + encounterId + ", name=" + name
				+ ", reaction=" + reaction + ", severity=" + severity + ", status=" + status + "]";
	}
	
}
