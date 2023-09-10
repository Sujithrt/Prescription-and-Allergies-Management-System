package com.cerner.devacedemy.backend.response;


public class MedicineResponse {
	
	private long id;
	private String brandName;
	private String cause = "";
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getBrandName() {
		return brandName;
	}
	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}
	public String getCause() {
		return cause;
	}
	public void setCause(String cause) {
		this.cause = cause;
	}
	
	@Override
	public String toString() {
		return "MedicineResponse [id=" + id + ", brandName=" + brandName + ", cause="
				+ cause + "]";
	}

}
