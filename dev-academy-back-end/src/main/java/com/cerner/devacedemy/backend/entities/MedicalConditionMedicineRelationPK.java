package com.cerner.devacedemy.backend.entities;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class MedicalConditionMedicineRelationPK implements Serializable {

	private static final long serialVersionUID = 1L;

	@Column(name="medicine_id", unique = false)
	private long medicineId;

	@Column(name="medical_condition_id", unique = false)
	private long medicalConditionId;

	public MedicalConditionMedicineRelationPK() {}

	public MedicalConditionMedicineRelationPK(long medicineId, long medicalConditionId) {
		super();
		this.medicineId = medicineId;
		this.medicalConditionId = medicalConditionId;
	}

	public long getMedicineId() {
		return medicineId;
	}

	public void setMedicineId(long medicineId) {
		this.medicineId = medicineId;
	}

	public long getMedicalConditionId() {
		return medicalConditionId;
	}

	public void setMedicalConditionId(long medicalConditionId) {
		this.medicalConditionId = medicalConditionId;
	}

	@Override
	public int hashCode() {
		return Objects.hash(medicalConditionId, medicineId);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MedicalConditionMedicineRelationPK other = (MedicalConditionMedicineRelationPK) obj;
		return medicalConditionId == other.medicalConditionId && medicineId == other.medicineId;
	}
}