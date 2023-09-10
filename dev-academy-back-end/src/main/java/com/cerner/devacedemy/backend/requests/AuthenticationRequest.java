package com.cerner.devacedemy.backend.requests;

public class AuthenticationRequest {
	
	private String username;
	public void setUsername(String username) {
		this.username = username;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	private String password;
	
	public String getUsername() {
		return username;
	}
	public String getPassword() {
		return password;
	}
	@Override
	public String toString() {
		return "AuthenticationRequest [username=" + username + ", password=" + password + "]";
	}

	/**
	 * Verifies the Request body in login request
	 * @return true or false
	 */
	public boolean verify() {
		if(username.equals("") || password.equals("")) {
			return false;
		}
		return true;
	}
}
