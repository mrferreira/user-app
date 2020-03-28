package com.fundacred.userapp.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fundacred.userapp.dto.UserDTO;
import com.fundacred.userapp.model.User;
import com.fundacred.userapp.service.UserService;

@RestController
public class UserController {

	private final UserService service;
	
	UserController(UserService service) {
		this.service = service;
	}
		
	@PostMapping("/users")
	ResponseEntity<User> create(@RequestBody UserDTO payload) {
		return service.save(payload)
				.map(rec -> ResponseEntity.ok().body(rec))
				.orElse(ResponseEntity.ok().build());
	}
	
	@PostMapping("/login")
	User login(@RequestBody UserDTO payload) {
		return null;
	}
	
}
