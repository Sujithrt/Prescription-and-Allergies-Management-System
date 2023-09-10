package com.cerner.devacedemy.backend.response;

import java.util.Date;

public class UserInfo {
	
	private long id;
	private String username;
	private String email;
	private String role;
	private String firstName;
	private String lastName;
	private String speciality;
	private String city;
	private String state;
	private Date dob;
	private Object authorities;
	
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
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
	public String getSpeciality() {
		return speciality;
	}
	public void setSpeciality(String speciality) {
		this.speciality = speciality;
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
	public Date getDob() {
		return dob;
	}
	public void setDob(Date dob) {
		this.dob = dob;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public Object getAuthorities() {
		return authorities;
	}
	public void setAuthorities(Object authorities) {
		this.authorities = authorities;
	}
	@Override
	public String toString() {
		return "UserInfo [id=" + id + ", username=" + username + ", email=" + email + ", role=" + role + ", firstName="
				+ firstName + ", lastName=" + lastName + ", speciality=" + speciality + ", city=" + city + ", state="
				+ state + ", dob=" + dob + ", authorities=" + authorities + "]";
	}

}
