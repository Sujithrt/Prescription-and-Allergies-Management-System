package com.cerner.devacedemy.backend.controllers;

import java.security.NoSuchAlgorithmException;
import java.security.Principal;
import java.security.spec.InvalidKeySpecException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cerner.devacedemy.backend.config.JWTTokenHelper;
import com.cerner.devacedemy.backend.entities.PhysicianDetails;
import com.cerner.devacedemy.backend.entities.User;
import com.cerner.devacedemy.backend.exceptions.InvalidEmailOrPasswordException;
import com.cerner.devacedemy.backend.exceptions.ResourceNotFoundException;
import com.cerner.devacedemy.backend.requests.AuthenticationRequest;
import com.cerner.devacedemy.backend.response.LoginResponse;
import com.cerner.devacedemy.backend.response.UserInfo;
import com.cerner.devacedemy.backend.services.CustomUserService;

@RestController
@RequestMapping("/api/v1")
@CrossOrigin
public class AuthenticationController {

	Logger logger = LoggerFactory.getLogger(AuthenticationController.class);

	@Autowired
	private CustomUserService customUserService;

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	JWTTokenHelper jwtTokenHelper;

	/**
	 * POST REST API to register new user
	 * 
	 * @param user
	 * @return ResponseEntity
	 */
	@PostMapping("/auth/register")
	public ResponseEntity<User> register(@RequestBody User user) {
		try {
			logger.trace("Register end point /api/v1/auth/register");
			customUserService.registerUser(user);
			logger.info("User Registered");
			return new ResponseEntity<User>(user, HttpStatus.CREATED);
		} catch(DataIntegrityViolationException e) {
			logger.error("User already exists");
			return new ResponseEntity<User>(HttpStatus.CONFLICT);
		} catch(InvalidEmailOrPasswordException e) {
			return new ResponseEntity<User>(HttpStatus.BAD_REQUEST);
		}
	}

	/**
	 * POST REST API to validate user login credentials, generate and return JWT token if the user credentials are valid i.e.
	 * matches credentials stored in User Details Repository i.e. Database
	 * 
	 * @param authenticationRequest
	 * @return ResponseEntity
	 * @throws InvalidKeySpecException
	 * @throws NoSuchAlgorithmException
	 */
	@PostMapping("/auth/login")
	public ResponseEntity<LoginResponse> login(@RequestBody AuthenticationRequest authenticationRequest) throws InvalidKeySpecException, NoSuchAlgorithmException {
		if(authenticationRequest.verify()) {
			try {
				logger.trace("Login endpoint /api/v1/auth/login");
				logger.info("Authentication Request verified");
				logger.info(authenticationRequest.toString());
				final Authentication authentication = authenticationManager.authenticate(
						new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(), authenticationRequest.getPassword()));
				SecurityContextHolder.getContext().setAuthentication(authentication);
				User user = (User) authentication.getPrincipal();
				String jwtToken = jwtTokenHelper.generateToken(user.getUsername());
				logger.info("JWT Token generated");
				LoginResponse response = new LoginResponse();
				response.setToken(jwtToken);
				logger.debug(response.toString());
				return new ResponseEntity<LoginResponse>(response, HttpStatus.ACCEPTED);
			} catch(Exception e) {
				logger.error("Error in authenticating user, User does not exist");
				return new ResponseEntity<LoginResponse>(HttpStatus.NO_CONTENT);
			}
		} else {
			logger.error("Invalid Request body");
			return new ResponseEntity<LoginResponse>(HttpStatus.BAD_REQUEST);
		}
	}

	/**
	 * GET REST API to get user details
	 * 
	 * @param user
	 * @return ResponseEntity
	 */
	@GetMapping("/auth/userinfo")
	public ResponseEntity<UserInfo> getUserInfo(Principal user) {
		try {
			logger.trace("UserInfo endpoint /api/v1/auth/userinfo");
			User userObj = (User) customUserService.loadUserByUsername(user.getName());
			logger.info("User login info obtained");
			PhysicianDetails physician = customUserService.getPhysicianById(userObj.getId());
			logger.info("User Details obtained");

			UserInfo userInfo = new UserInfo();
			userInfo.setId(userObj.getId());
			userInfo.setUsername(userObj.getUsername());
			userInfo.setEmail(userObj.getEmail());
			userInfo.setRole(userObj.getRole());
			userInfo.setFirstName(physician.getFirstName());
			userInfo.setLastName(physician.getLastName());
			userInfo.setCity(physician.getCity());
			userInfo.setState(physician.getState());
			userInfo.setSpeciality(physician.getSpeciality());
			userInfo.setDob(physician.getDob());
			userInfo.setAuthorities(userObj.getAuthorities().toArray());
			logger.info("Response class initialized");
			logger.debug(userInfo.toString());
			return new ResponseEntity<UserInfo>(userInfo, HttpStatus.OK);
		} catch (ResourceNotFoundException e) {
			logger.error("User or Physician not found");
			return new ResponseEntity<UserInfo>(HttpStatus.NOT_FOUND);
		}
	}
}
