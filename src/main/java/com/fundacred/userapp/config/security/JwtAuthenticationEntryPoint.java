package com.fundacred.userapp.config.security;

import java.io.IOException;
import java.io.Serializable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import com.fundacred.userapp.error.ErrorMessage;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint, Serializable {
	private static final long serialVersionUID = 1L;

	private final ErrorMessage errorMessage;

	public JwtAuthenticationEntryPoint(ErrorMessage errorMessage) {
		this.errorMessage = errorMessage;
	}
	
	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException authException) throws IOException {
		response.setStatus(HttpStatus.UNAUTHORIZED.value());
		response.getOutputStream()
        .println(errorMessage.unauthorized().json());
	}
}
