package com.cerner.devacedemy.backend.requests;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class AuthenticationRequestTest {

	@Test
	void testVerify() {
		AuthenticationRequest authenticationRequest = new AuthenticationRequest();
		authenticationRequest.setPassword("password123");
		authenticationRequest.setUsername("John");
		assertEquals(true, authenticationRequest.verify());
	}
	
	@Test
	void testVerifyFail1() {
		AuthenticationRequest authenticationRequest = new AuthenticationRequest();
		authenticationRequest.setPassword("");
		authenticationRequest.setUsername("John");
		assertEquals(false, authenticationRequest.verify());
	}

	@Test
	void testVerifyFail2() {
		AuthenticationRequest authenticationRequest = new AuthenticationRequest();
		authenticationRequest.setPassword("password123");
		authenticationRequest.setUsername("");
		assertEquals(false, authenticationRequest.verify());
	}
}
