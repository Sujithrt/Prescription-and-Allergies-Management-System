package com.cerner.devacedemy.backend.requests;

public class Medicine {

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
		return "Medicine [label=" + label + ", value=" + value + "]";
	}
	
	/**
	 * Verify validity of Medicine details in Request body
	 * 
	 * @return true or false
	 */
	public boolean verify() {
		if(!label.equals("") && value > 0) {
			return true;
		} else {
			return false;
		}
	}

}
