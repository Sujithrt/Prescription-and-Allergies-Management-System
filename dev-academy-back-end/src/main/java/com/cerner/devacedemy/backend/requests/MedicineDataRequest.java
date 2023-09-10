package com.cerner.devacedemy.backend.requests;

public class MedicineDataRequest {
	
	private String dose;
	private Medicine medicine;
	public String getDose() {
		return dose;
	}
	public void setDose(String dose) {
		this.dose = dose;
	}
	public Medicine getMedicine() {
		return medicine;
	}
	public void setMedicine(Medicine medicine) {
		this.medicine = medicine;
	}
	@Override
	public String toString() {
		return "MedicineDataRequest [dose=" + dose + ", medicine=" + medicine + "]";
	}
	
	/**
	 * Verifies the Medicine data passed in request body
	 * @return true or false
	 */
	public boolean verify() {
		if(!dose.equals("")) {
			return medicine.verify();
		} else {
			return false;
		}
	}

}
