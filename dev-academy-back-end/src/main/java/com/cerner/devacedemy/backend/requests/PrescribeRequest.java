package com.cerner.devacedemy.backend.requests;

import java.util.List;


public class PrescribeRequest {
	
	private long encounterId;
	private List<MedicineDataRequest> prescribedMedicineList;
	public long getEncounterId() {
		return encounterId;
	}
	public void setEncounterId(long encounterId) {
		this.encounterId = encounterId;
	}
	public List<MedicineDataRequest> getPrescribedMedicineList() {
		return prescribedMedicineList;
	}
	public void setPrescribedMedicineList(List<MedicineDataRequest> prescribedMedicineList) {
		this.prescribedMedicineList = prescribedMedicineList;
	}

	/**
	 * Verifies the Request body for medicine prescription
	 * @param patientId
	 * @return true or false
	 */
	public boolean verify(long patientId) {
		if(encounterId >= 0  && patientId > 0) {
			if(prescribedMedicineList.isEmpty()) {
				return false;
			} else {
				for(MedicineDataRequest medicine: prescribedMedicineList) {
					if(!medicine.verify()) {
						return false;
					}
				}
			}
			return true;
		} else {
			return false;
		}
	}
	@Override
	public String toString() {
		return "PrescribeRequest [encounterId=" + encounterId + ", prescribedMedicineList=" + prescribedMedicineList
				+ "]";
	}
}
