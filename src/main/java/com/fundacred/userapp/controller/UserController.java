package com.fundacred.userapp.controller;

import java.util.Optional;

import javax.validation.constraints.NotNull;

import org.eclipse.jetty.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fundacred.userapp.dto.JwtRequest;
import com.fundacred.userapp.dto.UserDTO;
import com.fundacred.userapp.model.User;
import com.fundacred.userapp.service.UserService;

@RestController
@RequestMapping(value = "/users", 
	consumes = MediaType.APPLICATION_JSON_VALUE,
	produces = MediaType.APPLICATION_JSON_VALUE)
public class UserController {

	private final UserService service;

	private final AuthenticationManager authenticationManager;

	UserController(UserService service, AuthenticationManager authenticationManager) {
		this.service = service;
		this.authenticationManager = authenticationManager;
	}
		
	@PostMapping("/signup")
	public ResponseEntity<?> create(@NotNull @RequestBody UserDTO payload) {
		return service.save(payload)
				.map(rec -> ResponseEntity.status(HttpStatus.CREATED_201).body(rec))
				.orElse(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR_500).build());
	}
	
	@PostMapping("/authenticate")
	public ResponseEntity<?> authenticate(@NotNull @RequestBody JwtRequest authenticationRequest) throws Exception {
		authenticate(authenticationRequest.getUsername().toLowerCase(), authenticationRequest.getPassword());
		Optional<User> result = service.loadAuthUser(authenticationRequest.getUsername(), authenticationRequest.getPassword());
		return ResponseEntity.ok().body(result.get());
	}

	@GetMapping("/profile/{id}")
	public ResponseEntity<?> getProfile(@PathVariable Long id) {
		return  ResponseEntity.ok().body(service.loadProfile(id).get());
	}
	
	private void authenticate(String username, String password) throws Exception {
		authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));		
	}
}
