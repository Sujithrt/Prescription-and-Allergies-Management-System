package com.cerner.devacedemy.backend.config;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.server.WebServerException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;

@Component
public class JWTTokenHelper {
	
	Logger logger = LoggerFactory.getLogger(getClass());

	@Value("${jwt.auth.app}")
	private String appName;

	@Value("${jwt.auth.secret_key}")
	private String secretKey;

	@Value("${jwt.auth.expires_in}")
	private int expiresIn;

	private SignatureAlgorithm SIGNATURE_ALGORITHM = SignatureAlgorithm.HS256;


	/**
	 * Function to obtain claims for created JWT token (username, issue date, expire date etc)
	 * 
	 * @param token
	 * @return claims
	 */
	private Claims getAllClaimsFromToken(String token) {
		Claims claims = null;
		try {
			claims = Jwts.parser()
					.setSigningKey(secretKey)
					.parseClaimsJws(token)
					.getBody();
		} catch (ExpiredJwtException e) {
			logger.warn(e.getMessage());
		} catch (UnsupportedJwtException e) {
			logger.warn(e.getMessage());
		} catch (MalformedJwtException e) {
			logger.warn(e.getMessage());
		} catch (SignatureException e) {
			logger.warn(e.getMessage());
		}
		return claims;
	}

	/**
	 * Function to extract User Name from created JWT token
	 * 
	 * @param token
	 * @return username of type String
	 */
	public String getUsernameFromToken(String token) {
		String username;
		try {
			final Claims claims = this.getAllClaimsFromToken(token);
			if(null == claims) {
				throw new WebServerException("Invalid JWT Token", null);
			}
			username = claims.getSubject();
		} catch (Exception e) {
			username = null;
		}
		return username;
	}

	/**
	 * Creates JWT token by taking in User Name
	 * 
	 * @param username
	 * @return token of type String
	 * @throws InvalidKeySpecException
	 * @throws NoSuchAlgorithmException
	 */
	public String generateToken(String username) throws InvalidKeySpecException, NoSuchAlgorithmException {

		return Jwts.builder()
				.setIssuer( appName )
				.setSubject(username)
				.setIssuedAt(new Date())
				.setExpiration(generateExpirationDate())
				.signWith( SIGNATURE_ALGORITHM, secretKey )
				.compact();
	}

	/**
	 * Generates expiration date for newly created token based on expires in value provided in application.properties
	 * file
	 * 
	 * @return Date
	 */
	private Date generateExpirationDate() {
		return new Date(new Date().getTime() + expiresIn * 1000);
	}

	/**
	 * Function returns true i.e. token valid if username is not null, username is same as username saved in userDetails
	 * and generated token has not expired, else returns false
	 * 
	 * @param token
	 * @param userDetails
	 * @return True or False depending on whether Token is valid or not
	 */
	public Boolean validateToken(String token, UserDetails userDetails) {
		final String username = getUsernameFromToken(token);
		return (
				username != null &&
				username.equals(userDetails.getUsername()) &&
				!isTokenExpired(token)
				);
	}

	/**
	 * Checks if token has expired
	 * 
	 * @param token
	 * @return true or false based on whether token has expired or not
	 */
	public boolean isTokenExpired(String token) {
		Date expireDate = getExpirationDate(token);
		return expireDate.before(new Date());
	}

	/**
	 * Function to obtain expiration date from the token
	 * 
	 * @param token
	 * @return Date which indicates a tokens' expiration date
	 */
	private Date getExpirationDate(String token) {
		Date expireDate;
		try {
			final Claims claims = this.getAllClaimsFromToken(token);
			expireDate = claims.getExpiration();
		} catch (Exception e) {
			expireDate = null;
		}
		return expireDate;
	}


	/**
	 * Function to obtain the date when the token was issued
	 * 
	 * @param token
	 * @return issue date of the token
	 */
	public Date getIssuedAtDateFromToken(String token) {
		Date issueAt;
		try {
			final Claims claims = this.getAllClaimsFromToken(token);
			issueAt = claims.getIssuedAt();
		} catch (Exception e) {
			issueAt = null;
		}
		return issueAt;
	}

	/**
	 * Function to extract the JWT token from the Http request header
	 * 
	 * @param request
	 * @return JWT token from Http Request
	 */
	public String getToken( HttpServletRequest request ) {

		String authHeader = getAuthHeaderFromHeader( request );
		if ( authHeader != null && authHeader.startsWith("Bearer ")) {
			return authHeader.substring(7);
		}

		return null;
	}
	
	/**
	 * Function returns the Authorization header from the Http Request 
	 * 
	 * @param request
	 * @return Autherization header from Http Request
	 */
	public String getAuthHeaderFromHeader( HttpServletRequest request ) {
		return request.getHeader("Authorization");
	}

}
