package com.fundacred.userapp.error;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import io.jsonwebtoken.SignatureException;

@ControllerAdvice
public class GlobalRestExceptionHandler extends ResponseEntityExceptionHandler {

	@Autowired
	private ErrorMessage errorMessage;
	
	@ExceptionHandler(value = {UsernameNotFoundException.class, BadCredentialsException.class, 
			DisabledException.class, AccessDeniedException.class, SignatureException.class,
			CredentialsExpiredException.class})
	@ResponseStatus(value = HttpStatus.UNAUTHORIZED)
	protected ResponseEntity<Object> handleAccessConflict(RuntimeException ex, WebRequest req) {
        return handleExceptionInternal(ex, ex.getCause() instanceof CredentialsExpiredException ? errorMessage.invalidSession().json() : 
        	errorMessage.invalidUserPassword().json(), 
          new HttpHeaders(), HttpStatus.UNAUTHORIZED, req);
	}
}
