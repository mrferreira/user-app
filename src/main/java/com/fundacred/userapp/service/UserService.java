package com.fundacred.userapp.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import com.fundacred.userapp.config.JwtTokenUtil;
import com.fundacred.userapp.config.JwtUserDetailsService;
import com.fundacred.userapp.dto.JwtRequest;
import com.fundacred.userapp.dto.UserDTO;
import com.fundacred.userapp.model.User;
import com.fundacred.userapp.persistence.UserRepository;

@Service
public class UserService {
	
	@Autowired
	UserRepository userRepository;
	@Autowired
	private JwtTokenUtil jwtTokenUtil;
	@Autowired
	private JwtUserDetailsService userDetailsService;
	
	@Transactional
	public Optional<User> findByEmail(String email) {	
		User filter = new User();
		filter.setEmail(email);
		Example<User> example = Example.of(filter);
		return userRepository.findOne(example);
	}

	@Transactional
	public Optional<User> save(@RequestBody UserDTO dto) {
		User user = new User(dto.getName(), dto.getEmail(), dto.getPassword(), dto.getPhones());		
		user.setToken(generateToken(user));
		user.setLastLogin(new Date());
		User result = userRepository.save(user);
		
		return Optional.of(userRepository.save(result));
	}
	
	private String generateToken(User user) {
		JwtRequest authRequest = new JwtRequest();
		authRequest.setUsername(user.getEmail());
		authRequest.setPassword(user.getPassword());
		UserDetails userDetails = new org.springframework.security.core.userdetails.User(
				user.getEmail(), user.getPassword(), new ArrayList<>());
		return jwtTokenUtil.generateToken(userDetails);
	}
}
