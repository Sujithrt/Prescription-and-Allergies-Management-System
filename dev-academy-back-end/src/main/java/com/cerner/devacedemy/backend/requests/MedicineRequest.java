package com.cerner.devacedemy.backend.requests;

public class MedicineRequest {

	private String label;
	private long value;
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	public long getValue() {
		return value;
	}
	public void setValue(long value) {
		this.value = value;
	}
	@Override
	public String toString() {
		return "MedicineRequest [label=" + label + ", value=" + value + "]";
	}

}
