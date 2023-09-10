package com.cerner.devacedemy.backend.response;


public class PatientDetailsResponse {
	
	private long id;
	private String firstName;
	private String lastName;
	private String gender;
	private int age;
	private long phoneNumber;
	private String email;
	private String city;
	private String state;
	private Object medicalConditions;
	private Object allergies;
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
	public long getPhoneNumber() {
		return phoneNumber;
	}
	public void setPhoneNumber(long phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public Object getMedicalConditions() {
		return medicalConditions;
	}
	public void setMedicalConditions(Object medicalConditions) {
		this.medicalConditions = medicalConditions;
	}
	public Object getAllergies() {
		return allergies;
	}
	public void setAllergies(Object allergies) {
		this.allergies = allergies;
	}
	@Override
	public String toString() {
		return "PatientDetailsResponse [id=" + id + ", firstName=" + firstName + ", lastName=" + lastName + ", gender="
				+ gender + ", age=" + age + ", phoneNumber=" + phoneNumber + ", email=" + email + ", city=" + city
				+ ", state=" + state + ", medicalConditions=" + medicalConditions + ", allergies=" + allergies + "]";
	}

}
