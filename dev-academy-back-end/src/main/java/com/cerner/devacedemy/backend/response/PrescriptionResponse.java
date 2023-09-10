package com.cerner.devacedemy.backend.response;

public class PrescriptionResponse {
	
	private String brandName;
	private String genericName;
	private String dosage;
	private String date;
	public PrescriptionResponse() {
	}
	public PrescriptionResponse(String brandName, String genericName, String dosage, String date) {
		super();
		this.brandName = brandName;
		this.genericName = genericName;
		this.dosage = dosage;
		this.date = date;
	}
	public String getBrandName() {
		return brandName;
	}
	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}
	public String getGenericName() {
		return genericName;
	}
	public void setGenericName(String genericName) {
		this.genericName = genericName;
	}
	public String getDosage() {
		return dosage;
	}
	public void setDosage(String dosage) {
		this.dosage = dosage;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	@Override
	public String toString() {
		return "PrescriptionResponse [brandName=" + brandName + ", genericName=" + genericName + ", dosage=" + dosage
				+ ", date=" + date + "]";
	}

}
