package com.cerner.devacedemy.backend.config;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringRunner;

import com.cerner.devacedemy.backend.services.CustomUserService;

import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.test.context.support.WithMockUser;


@RunWith(SpringRunner.class)
@WebMvcTest(value = RestAuthenticationEntryPoint.class)
class RestAuthenticationEntryPointTest {

	@Autowired
	private RestAuthenticationEntryPoint authenticationEntryPoint;
	
	@MockBean
	private CustomUserService customUserService;
	
	@MockBean
	private JWTTokenHelper jwtTokenHelper;

	@Test
	@WithMockUser
	void testCommence() throws IOException, ServletException {
		MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        AuthenticationException ex = new AuthenticationCredentialsNotFoundException("");

        authenticationEntryPoint.commence(request, response, ex);

        assertEquals(HttpServletResponse.SC_UNAUTHORIZED, response.getStatus());
	}

}
