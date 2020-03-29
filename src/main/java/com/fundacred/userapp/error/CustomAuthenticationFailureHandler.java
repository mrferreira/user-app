package com.fundacred.userapp.error;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class CustomAuthenticationFailureHandler 
implements AuthenticationFailureHandler {

  private ObjectMapper objectMapper = new ObjectMapper();
  
  @Autowired
  ErrorMessage errorMessage;

  @Override
  public void onAuthenticationFailure(
    HttpServletRequest request,
    HttpServletResponse response,
    AuthenticationException exception) 
    throws IOException, ServletException {

      response.setStatus(HttpStatus.UNAUTHORIZED.value());      

      response.getOutputStream()
        .println(objectMapper.writeValueAsString(errorMessage.unauthorized));
  }
}