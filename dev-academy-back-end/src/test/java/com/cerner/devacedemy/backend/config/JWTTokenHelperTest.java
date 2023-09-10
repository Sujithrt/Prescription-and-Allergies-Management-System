package com.cerner.devacedemy.backend.config;

import static org.junit.jupiter.api.Assertions.*;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Date;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.junit4.SpringRunner;

import com.cerner.devacedemy.backend.services.CustomUserService;


@RunWith(SpringRunner.class)
@WebMvcTest(value = JWTTokenHelper.class)
class JWTTokenHelperTest {
	
	Logger logger = LoggerFactory.getLogger(getClass());
		
	@MockBean
	private CustomUserService customUserService;
	
	@MockBean
	private RestAuthenticationEntryPoint restAuthenticationEntryPoint;
	
	@MockBean
	private UserDetails userDetails;
	
	@MockBean
	private HttpServletRequest request;
	
	@Autowired
	private JWTTokenHelper jwtTokenHelper;
	
	@Test
	void testGetUsernameFromToken() throws InvalidKeySpecException, NoSuchAlgorithmException {
		String token = jwtTokenHelper.generateToken("Ram");
		assertEquals("Ram", jwtTokenHelper.getUsernameFromToken(token));
	}
	
	@Test
	void testGetUsernameFromMalformedJwtExceptionToken() {
		String token = "eyJhbGciOiJIUzI1NiJ9..rupDgnpI-15n3dBXK7N4zG_F-qLj1hAMZWUu_GakWr4";
		assertEquals(null, jwtTokenHelper.getUsernameFromToken(token));
	}
	
	@Test
	void testGetUsernameFromExpiredJwtExceptionToken() {
		String token = "eyJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJQcmVzY3JpcHRpb24tYW5kLUFsbGVyZ2llcy1NYW5hZ2VtZW50LUFwcCIsInN1YiI6IkpvaG4iLCJpYXQiOjE2MzU0ODIxOTIsImV4cCI6MTYzNTUxODE5Mn0.aj7Nvf55o-Zj5lQCxi8spFpJzBVUbX_xCJDUDVSfqlM";
		assertEquals(null, jwtTokenHelper.getUsernameFromToken(token));
	}
	
	@Test
	void testGetUsernameFromUnsupportedJwtExceptionToken() throws InvalidKeySpecException, NoSuchAlgorithmException {
		String token = jwtTokenHelper.generateToken("Ram");
		String[] parts = token.split(Pattern.quote("."));
		token = parts[0] + "." + parts[1] + ".";
		assertEquals(null, jwtTokenHelper.getUsernameFromToken(token));
	}
	
	@Test
	void testGetUsernameFromSignatureExceptionToken() {
		String token = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJza3AifQ.XUEsUFF3qm6fOeCG8xDLuRWjyd4kOh4g01olU_BsRyfqyI66MRhqmK-mxrAWsD17Ylmj-fZRRZUTRqxCQixxXQ";
		assertEquals(null, jwtTokenHelper.getUsernameFromToken(token));
	}

	@Test
	void testGenerateToken() throws InvalidKeySpecException, NoSuchAlgorithmException {
		String username = "John";
		assertEquals("eyJhbGciOiJIUzI1NiJ9", jwtTokenHelper.generateToken(username).substring(0, 20));
	}

	@Test
	void testIsTokenExpiredFalse() throws InvalidKeySpecException, NoSuchAlgorithmException {
		String token = jwtTokenHelper.generateToken("Ram");
		assertEquals(false, jwtTokenHelper.isTokenExpired(token));
	}
	
	@Test
	void testIsTokenExpiredTrue() {
		String token = "eyJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJQcmVzY3JpcHRpb24tYW5kLUFsbGVyZ2llcy1NYW5hZ2VtZW50LUFwcCIsInN1YiI6IkpvaG4iLCJpYXQiOjE2MzU0ODIxOTIsImV4cCI6MTYzNTUxODE5Mn0.aj7Nvf55o-Zj5lQCxi8spFpJzBVUbX_xCJDUDVSfqlM";
		assertThrows(NullPointerException.class, () -> jwtTokenHelper.isTokenExpired(token));
	}

	@Test
	void testGetIssuedAtDateFromToken() throws InvalidKeySpecException, NoSuchAlgorithmException {
		String token = jwtTokenHelper.generateToken("Ram");
		Date date = jwtTokenHelper.getIssuedAtDateFromToken(token);
		assertEquals(date, jwtTokenHelper.getIssuedAtDateFromToken(token));
	}

	@Test
	void testGetIssuedAtDateFromTokenFail() {
		String token = "eyJhbGciOiJIUzI1NiJ9..rupDgnpI-15n3dBXK7N4zG_F-qLj1hAMZWUu_GakWr4";
		assertEquals(null, jwtTokenHelper.getIssuedAtDateFromToken(token));
	}
	
	@Test
	void testGetToken() {
		Mockito.when(jwtTokenHelper.getAuthHeaderFromHeader(request)).thenReturn("Bearer eyJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJQcmVzY3JpcHRpb24tYW5kLUFsbGVyZ2llcy1NYW5hZ2VtZW50LUFwcCIsInN1YiI6IlJhbSIsImlhdCI6MTYzNTU3MTMwNiwiZXhwIjoxNjM1NjA3MzA2fQ.rupDgnpI-15n3dBXK7N4zG_F-qLj1hAMZWUu_GakWr4");
		String token = "eyJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJQcmVzY3JpcHRpb24tYW5kLUFsbGVyZ2llcy1NYW5hZ2VtZW50LUFwcCIsInN1YiI6IlJhbSIsImlhdCI6MTYzNTU3MTMwNiwiZXhwIjoxNjM1NjA3MzA2fQ.rupDgnpI-15n3dBXK7N4zG_F-qLj1hAMZWUu_GakWr4";
		assertEquals(token, jwtTokenHelper.getToken(request));
	}
	
	@Test
	void testGetTokenNoBearerFail() {
		Mockito.when(jwtTokenHelper.getAuthHeaderFromHeader(request)).thenReturn("eyJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJQcmVzY3JpcHRpb24tYW5kLUFsbGVyZ2llcy1NYW5hZ2VtZW50LUFwcCIsInN1YiI6IlJhbSIsImlhdCI6MTYzNTU3MTMwNiwiZXhwIjoxNjM1NjA3MzA2fQ.rupDgnpI-15n3dBXK7N4zG_F-qLj1hAMZWUu_GakWr4");
		assertEquals(null, jwtTokenHelper.getToken(request));
	}
	
	@Test
	void testGetTokenFail() {
		Mockito.when(jwtTokenHelper.getAuthHeaderFromHeader(request)).thenReturn(null);
		assertEquals(null, jwtTokenHelper.getToken(request));
	}
	
	@Test
	void testValidateToken() throws InvalidKeySpecException, NoSuchAlgorithmException {
		String token = jwtTokenHelper.generateToken("Ram");
		Mockito.when(userDetails.getUsername()).thenReturn("Ram");
		assertEquals(true, jwtTokenHelper.validateToken(token, userDetails));
	}
	
	@Test
	void testValidateTokenFail1() {
		String token = "eyJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJQcmVzY3JpcHRpb24tYW5kLUFsbGVyZ2llcy1NYW5hZ2VtZW50LUFwcCIsInN1YiI6IlJhbSIsImlhdCI6MTYzNTgzODE4NSwiZXhwIjoxNjM1ODc0MTg1fQ.U79oP1oGrSbVZVx5Q0K0Ci9lX3OrU30flBCgrhDtiu4";
		Mockito.when(userDetails.getUsername()).thenReturn(null);
		assertEquals(false, jwtTokenHelper.validateToken(token, userDetails));
	}
	
	@Test
	void testValidateTokenFail2() throws InvalidKeySpecException, NoSuchAlgorithmException {
		String token = jwtTokenHelper.generateToken("Ram");;
		Mockito.when(userDetails.getUsername()).thenReturn(null);
		assertEquals(false, jwtTokenHelper.validateToken(token, userDetails));
	}
}
