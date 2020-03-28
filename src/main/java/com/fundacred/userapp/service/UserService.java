package com.fundacred.userapp.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import com.fundacred.userapp.dto.UserDTO;
import com.fundacred.userapp.model.User;
import com.fundacred.userapp.persistence.UserRepository;

@Service
public class UserService {
	
	@Autowired
	UserRepository userRepository;
	
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
		return Optional.of(userRepository.save(user));
	}
}
