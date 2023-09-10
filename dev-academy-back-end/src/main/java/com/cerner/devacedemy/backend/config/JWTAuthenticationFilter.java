package com.cerner.devacedemy.backend.config;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.web.filter.OncePerRequestFilter;

public class JWTAuthenticationFilter extends OncePerRequestFilter {
	
	private UserDetailsService userDetailsService;
	private JWTTokenHelper jwtTokenHelper;
	

	public JWTAuthenticationFilter(UserDetailsService userDetailsService, JWTTokenHelper jwtTokenHelper) {
		this.userDetailsService = userDetailsService;
		this.jwtTokenHelper = jwtTokenHelper;
	}

	/**
	 * Implementation of the doFilterInternal function which gets the JWT token from the HTTP request and validates it to
	 * make sure it is not expired or incorrect and once validated, it will either approve or block the HTTP Request
	 * 
	 * @throws ServletException
	 * @throws IOException
	 * @param request
	 * @param response
	 * @param filterchain
	 */
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		String authToken = jwtTokenHelper.getToken(request);
		
		if(null != authToken) {
			String username = jwtTokenHelper.getUsernameFromToken(authToken);
			
			if(null != username) {
				UserDetails userDetails = userDetailsService.loadUserByUsername(username);
				
				if(jwtTokenHelper.validateToken(authToken, userDetails)) {
					
					UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
					authentication.setDetails(new WebAuthenticationDetails(request));
					
					SecurityContextHolder.getContext().setAuthentication(authentication);
				}		
			}
		}
		
		filterChain.doFilter(request, response);
	}
}