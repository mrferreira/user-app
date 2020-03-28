package com.fundacred.userapp.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fundacred.userapp.dto.JwtRequest;
import com.fundacred.userapp.dto.UserDTO;
import com.fundacred.userapp.error.ErrorMessage;
import com.fundacred.userapp.model.User;
import com.fundacred.userapp.service.UserService;

@RestController
@RequestMapping("/users")
public class UserController {

	private final UserService service;
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	UserController(UserService service) {
		this.service = service;
	}
	
	@GetMapping("/test")
	ResponseEntity<User> getByEmailTest(@RequestParam String email) {
		return service.findByEmail(email)
				.map(rec -> ResponseEntity.ok().body(rec))
				.orElse(ResponseEntity.notFound().build());
	}
		
	@PostMapping("/create")
	ResponseEntity<User> create(@RequestBody UserDTO payload) {
		return service.save(payload)
				.map(rec -> ResponseEntity.ok().body(rec))
				.orElse(ResponseEntity.ok().build());
	}
	
	@PostMapping("/authenticate")
	public ResponseEntity<?> authenticate(@RequestBody JwtRequest authenticationRequest) throws Exception {
		authenticate(authenticationRequest.getUsername(), authenticationRequest.getPassword());
		Optional<User> result = service.findByEmail(authenticationRequest.getUsername());
		
		return result.isPresent()  && result.filter(f -> f.getPassword() == authenticationRequest.getPassword()).isPresent() ? 
				ResponseEntity.ok().body(result.get()) : 
				ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ErrorMessage.unauthorized());
	}

	private void authenticate(String username, String password) throws Exception {
		try {
			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
		} catch (DisabledException e) {
			throw new Exception("USER_DISABLED", e);
		} catch (BadCredentialsException e) {
			throw new Exception("INVALID_CREDENTIALS", e);
		}
	}
}
