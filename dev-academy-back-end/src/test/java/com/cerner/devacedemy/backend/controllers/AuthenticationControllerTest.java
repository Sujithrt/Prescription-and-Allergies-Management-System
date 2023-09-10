package com.cerner.devacedemy.backend.controllers;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.cerner.devacedemy.backend.config.JWTTokenHelper;
import com.cerner.devacedemy.backend.config.RestAuthenticationEntryPoint;
import com.cerner.devacedemy.backend.entities.Authority;
import com.cerner.devacedemy.backend.entities.PhysicianDetails;
import com.cerner.devacedemy.backend.entities.User;
import com.cerner.devacedemy.backend.exceptions.InvalidEmailOrPasswordException;
import com.cerner.devacedemy.backend.exceptions.ResourceNotFoundException;
import com.cerner.devacedemy.backend.requests.AuthenticationRequest;
import com.cerner.devacedemy.backend.response.UserInfo;
import com.cerner.devacedemy.backend.services.CustomUserService;;


@RunWith(SpringRunner.class)
@WebMvcTest(value = AuthenticationController.class)
class AuthenticationControllerTest {
	
	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
			
	@MockBean
	private AuthenticationManager authenticationManager;
	
	@MockBean
	JWTTokenHelper jwtTokenHelper;
	
	@MockBean
	private CustomUserService customUserService;
	
	@MockBean
	private RestAuthenticationEntryPoint restAuthenticationEntryPoint;
	
	@MockBean
	private Authentication authentication;
	
	@MockBean
	private UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken;
	
	@MockBean
	AuthenticationRequest authenticationRequest;
	
	@MockBean
	SecurityContext securityContext;
	
	@MockBean
	private UserInfo mockUserInfo;
		
	@Test
	void testRegisterUser() throws Exception {
		String exampleUser = "{\r\n"
				+ "    \"username\": \"Sujith\",\r\n"
				+ "    \"password\": \"password123\",\r\n"
				+ "    \"email\": \"sujith@gmail.com\",\r\n"
				+ "    \"role\": \"Physician\",\r\n"
				+ "    \"authorities\": [\r\n"
				+ "        {\r\n"
				+ "            \"roleCode\": \"USER\",\r\n"
				+ "            \"roleDescription\": \"User role\"\r\n"
				+ "        },\r\n"
				+ "        {\r\n"
				+ "            \"roleCode\": \"ADMIN\",\r\n"
				+ "            \"roleDescription\": \"Admin role\"\r\n"
				+ "        }\r\n"
				+ "    ]\r\n"
				+ "}";
		User mockUser = new User();
		List<Authority> authorityList=new ArrayList<>();		
		authorityList.add(new Authority("USER","User role"));
		authorityList.add(new Authority("ADMIN","Admin role"));
		mockUser.setUsername("Sujith");
		mockUser.setEmail("sujith@gmail.com");
		mockUser.setPassword(passwordEncoder.encode("password123"));
		mockUser.setAuthorities(authorityList);
		Mockito.when(
				customUserService.registerUser(Mockito.any())).thenReturn(mockUser);
		
		RequestBuilder requestBuilder = MockMvcRequestBuilders
				.post("/api/v1/auth/register")
				.accept(MediaType.APPLICATION_JSON).content(exampleUser)
				.contentType(MediaType.APPLICATION_JSON);
		
		mockMvc.perform(requestBuilder)
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.username", is(mockUser.getUsername())))
				.andExpect(jsonPath("$.email", is(mockUser.getEmail())));
	}
	
	@Test
	void testDataIntegrityViolationExceptionRegisterUser() throws Exception {
		String exampleUser = "{\r\n"
				+ "    \"username\": \"Sujith\",\r\n"
				+ "    \"password\": \"password123\",\r\n"
				+ "    \"email\": \"sujith@gmail.com\",\r\n"
				+ "    \"role\": \"Physician\",\r\n"
				+ "    \"authorities\": [\r\n"
				+ "        {\r\n"
				+ "            \"roleCode\": \"USER\",\r\n"
				+ "            \"roleDescription\": \"User role\"\r\n"
				+ "        },\r\n"
				+ "        {\r\n"
				+ "            \"roleCode\": \"ADMIN\",\r\n"
				+ "            \"roleDescription\": \"Admin role\"\r\n"
				+ "        }\r\n"
				+ "    ]\r\n"
				+ "}";
		
		Mockito.when(
				customUserService.registerUser(Mockito.any())).thenThrow(new DataIntegrityViolationException("User already exists"));
		
		RequestBuilder requestBuilder = MockMvcRequestBuilders
				.post("/api/v1/auth/register")
				.accept(MediaType.APPLICATION_JSON)
				.content(exampleUser)
				.contentType(MediaType.APPLICATION_JSON);
		
		mockMvc.perform(requestBuilder)
				.andExpect(status().isConflict());
	}
	
	@Test
	void testInvalidEmailExceptionRegisterUser() throws Exception {
		String exampleUser = "{\r\n"
				+ "    \"username\": \"Sujith\",\r\n"
				+ "    \"password\": \"password123\",\r\n"
				+ "    \"email\": \"sujith@gmail.com\",\r\n"
				+ "    \"role\": \"Physician\",\r\n"
				+ "    \"authorities\": [\r\n"
				+ "        {\r\n"
				+ "            \"roleCode\": \"USER\",\r\n"
				+ "            \"roleDescription\": \"User role\"\r\n"
				+ "        },\r\n"
				+ "        {\r\n"
				+ "            \"roleCode\": \"ADMIN\",\r\n"
				+ "            \"roleDescription\": \"Admin role\"\r\n"
				+ "        }\r\n"
				+ "    ]\r\n"
				+ "}";
		
		Mockito.when(
				customUserService.registerUser(Mockito.any())).thenThrow(new InvalidEmailOrPasswordException("Email", "sujithgmail.com"));
		
		RequestBuilder requestBuilder = MockMvcRequestBuilders
				.post("/api/v1/auth/register")
				.accept(MediaType.APPLICATION_JSON)
				.content(exampleUser)
				.contentType(MediaType.APPLICATION_JSON);
		
		mockMvc.perform(requestBuilder)
				.andExpect(status().isBadRequest());
	}
	
	@Test
	void testInvalidPasswordExceptionRegisterUser() throws Exception {
		String exampleUser = "{\r\n"
				+ "    \"username\": \"Sujith\",\r\n"
				+ "    \"password\": \"password123\",\r\n"
				+ "    \"email\": \"sujith@gmail.com\",\r\n"
				+ "    \"role\": \"Physician\",\r\n"
				+ "    \"authorities\": [\r\n"
				+ "        {\r\n"
				+ "            \"roleCode\": \"USER\",\r\n"
				+ "            \"roleDescription\": \"User role\"\r\n"
				+ "        },\r\n"
				+ "        {\r\n"
				+ "            \"roleCode\": \"ADMIN\",\r\n"
				+ "            \"roleDescription\": \"Admin role\"\r\n"
				+ "        }\r\n"
				+ "    ]\r\n"
				+ "}";
		
		Mockito.when(
				customUserService.registerUser(Mockito.any())).thenThrow(new InvalidEmailOrPasswordException("Password", "pass"));
		
		RequestBuilder requestBuilder = MockMvcRequestBuilders
				.post("/api/v1/auth/register")
				.accept(MediaType.APPLICATION_JSON)
				.content(exampleUser)
				.contentType(MediaType.APPLICATION_JSON);
		
		mockMvc.perform(requestBuilder)
				.andExpect(status().isBadRequest());
	}

	@Test
	@WithMockUser
	void testGetUserInfo() throws Exception {
		User mockUser = new User();
		List<Authority> authorityList=new ArrayList<>();		
		authorityList.add(new Authority("USER","User role"));
		authorityList.add(new Authority("ADMIN","Admin role"));
		mockUser.setId(1);
		mockUser.setUsername("John");
		mockUser.setEnabled(true);
		mockUser.setRole("Physician");
		mockUser.setEmail("john@gmail.com");
		mockUser.setPassword(passwordEncoder.encode("password123"));
		mockUser.setAuthorities(authorityList);
		PhysicianDetails mockPhysician = new PhysicianDetails();
		mockPhysician.setId(1);
		mockPhysician.setFirstName("John");
		mockPhysician.setLastName("Cage");
		mockPhysician.setCity("Bangalore");
		mockPhysician.setDob(new Date());
		mockPhysician.setSpeciality("Cardiologist");
		mockPhysician.setState("Karnataka");
		mockPhysician.setUser(mockUser);
		Mockito.when(
				customUserService.loadUserByUsername(Mockito.anyString())).thenReturn(mockUser);
		Mockito.when(
				customUserService.getPhysicianById(Mockito.anyLong())).thenReturn(mockPhysician);
		
		RequestBuilder requestBuilder = MockMvcRequestBuilders
				.get("/api/v1/auth/userinfo")
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON);
		
		mockMvc.perform(requestBuilder)
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.username", is(mockUser.getUsername())));
	}
	
	@Test
	@WithMockUser
	void testUserResourceNotFoundExceptionGetUserInfo() throws Exception {
		Mockito.when(
				customUserService.loadUserByUsername(Mockito.anyString())).thenThrow(new ResourceNotFoundException("User","username", "username"));
		
		RequestBuilder requestBuilder = MockMvcRequestBuilders
				.get("/api/v1/auth/userinfo")
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON);
		
		mockMvc.perform(requestBuilder)
			.andExpect(status().isNotFound());
	}
	
	@Test
	@WithMockUser
	void testPhysicianResourceNotFoundExceptionGetUserInfo() throws Exception {
		User mockUser = new User();
		List<Authority> authorityList=new ArrayList<>();		
		authorityList.add(new Authority("USER","User role"));
		authorityList.add(new Authority("ADMIN","Admin role"));
		mockUser.setId(1);
		mockUser.setUsername("John");
		mockUser.setEnabled(true);
		mockUser.setRole("Physician");
		mockUser.setEmail("john@gmail.com");
		mockUser.setPassword(passwordEncoder.encode("password123"));
		mockUser.setAuthorities(authorityList);
		Mockito.when(
				customUserService.loadUserByUsername(Mockito.anyString())).thenReturn(mockUser);
		Mockito.when(
				customUserService.getPhysicianById(Mockito.anyLong())).thenThrow(new ResourceNotFoundException("Physician", "Id", 1));
		
		RequestBuilder requestBuilder = MockMvcRequestBuilders
				.get("/api/v1/auth/userinfo")
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON);
		
		mockMvc.perform(requestBuilder)
			.andExpect(status().isNotFound());
	}

	@Test
	void testVerifyFailLogin() throws Exception {
				
		String exampleUser = "{\r\n"
				+ "    \"username\": \"\",\r\n"
				+ "    \"password\": \"password123\"\r\n"
				+ "}";

		RequestBuilder requestBuilder = MockMvcRequestBuilders
				.post("/api/v1/auth/login")
				.accept(MediaType.APPLICATION_JSON).content(exampleUser)
				.contentType(MediaType.APPLICATION_JSON);
		mockMvc.perform(requestBuilder)
			.andExpect(status().isBadRequest());
	}
	
	@Test
	void testFailLogin() throws Exception {
		
		Mockito.when(authenticationManager.authenticate(Mockito.any())).thenReturn(authentication);
		
		String exampleUser = "{\r\n"
				+ "    \"username\": \"Sujithrt\",\r\n"
				+ "    \"password\": \"password123\"\r\n"
				+ "}";

		RequestBuilder requestBuilder = MockMvcRequestBuilders
				.post("/api/v1/auth/login")
				.accept(MediaType.APPLICATION_JSON).content(exampleUser)
				.contentType(MediaType.APPLICATION_JSON);
		mockMvc.perform(requestBuilder)
			.andExpect(status().isNoContent());
	}
	
	@Test
	void testLogin() throws Exception {
		
		Mockito.when(authenticationManager.authenticate(Mockito.any())).thenReturn(authentication);

		User mockUser = new User();
		List<Authority> authorityList=new ArrayList<>();		
		authorityList.add(new Authority("USER","User role"));
		authorityList.add(new Authority("ADMIN","Admin role"));
		mockUser.setUsername("Sujith");
		mockUser.setEmail("sujith@gmail.com");
		mockUser.setPassword(passwordEncoder.encode("password123"));
		mockUser.setAuthorities(authorityList);
		
		Mockito.when(authentication.getPrincipal()).thenReturn(mockUser);
		
		String exampleUser = "{\r\n"
				+ "    \"username\": \"Sujithrt\",\r\n"
				+ "    \"password\": \"password123\"\r\n"
				+ "}";

		RequestBuilder requestBuilder = MockMvcRequestBuilders
				.post("/api/v1/auth/login")
				.accept(MediaType.APPLICATION_JSON).content(exampleUser)
				.contentType(MediaType.APPLICATION_JSON);
		mockMvc.perform(requestBuilder)
			.andExpect(status().isAccepted());
	}
}
