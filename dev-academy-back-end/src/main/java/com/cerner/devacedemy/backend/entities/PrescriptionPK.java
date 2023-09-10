package com.cerner.devacedemy.backend.entities;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class PrescriptionPK implements Serializable {

	private static final long serialVersionUID = 1L;

	@Column(name="encounter_id")
	private long encounterId;
	
	@Column(name="medicine_id")
    private long medicineId;

    public PrescriptionPK() {}

	public PrescriptionPK(long encounterId, long medicineId) {
		super();
		this.encounterId = encounterId;
		this.medicineId = medicineId;
	}

	public long getEncounterId() {
		return encounterId;
	}

	public void setEncounterId(long encounterId) {
		this.encounterId = encounterId;
	}

	public long getMedicineId() {
		return medicineId;
	}

	public void setMedicineId(long medicineId) {
		this.medicineId = medicineId;
	}

	@Override
	public int hashCode() {
		return Objects.hash(encounterId, medicineId);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PrescriptionPK other = (PrescriptionPK) obj;
		return encounterId == other.encounterId && medicineId == other.medicineId;
	}
	
}