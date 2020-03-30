package com.fundacred.userapp.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.Optional;

import org.springframework.data.domain.Example;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import com.fundacred.userapp.config.JwtTokenUtil;
import com.fundacred.userapp.dao.UserRepository;
import com.fundacred.userapp.dto.JwtRequest;
import com.fundacred.userapp.dto.UserDTO;
import com.fundacred.userapp.error.ErrorMessage;
import com.fundacred.userapp.model.User;

@Service
public class UserService {
	
	private UserRepository userRepository;
	private JwtTokenUtil jwtTokenUtil;
	private PasswordEncoder passwordEncoder;
	private ErrorMessage errorMessage;
	
	public UserService(UserRepository userRepository,
			JwtTokenUtil tokenUtil, PasswordEncoder encoder,
			ErrorMessage errorMessage) {
		this.userRepository = userRepository;
		this.jwtTokenUtil = tokenUtil;
		this.passwordEncoder = encoder;
		this.errorMessage = errorMessage;
	}
	
	public Optional<User> findByEmail(String email) {	
		User filter = new User();
		filter.setEmail(email);
		Example<User> example = Example.of(filter);
		return userRepository.findOne(example);
	}
	
	@Transactional
	public Optional<User> loadAuthUser(String email, String password) {
		Optional<User> result = findByEmail(email.toLowerCase());
		validateAuthentication(result, password);
		return result;
	}
	
	private void validateAuthentication(Optional<User> ref, String password) {
		boolean noAuth = false;
		
		if(ref == null || !ref.isPresent() || password == null) {
			noAuth = true;
		} else {
			if(!passwordEncoder.matches(password, ref.get().getPassword())) {
				noAuth = true;
			}
		}
		
		if(noAuth) {
			throw new BadCredentialsException(errorMessage.invalidUserPassword().getMensagem());
		}
	}
	
	@Transactional 
	public Optional<User> loadProfile(Long id) {
		Optional<User> result = userRepository.findById(id);		
		validateProfile(result);
		return result;
	}
	
	private void validateProfile(Optional<User> ref) {
		boolean noAuth = false;
		
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
				
		if(ref == null || !ref.isPresent()) {
			noAuth = true;
		} else {
		
			if(ref.get().getEmail() != authentication.getName()) {
				noAuth = true;
			}
		}
		
		if(noAuth) {
			throw new BadCredentialsException(errorMessage.invalidUserPassword().getMensagem());
		}
	}

	@Transactional
	public Optional<?> save(@RequestBody UserDTO dto) {
		dto.setEmail(dto.getEmail().toLowerCase());
		Optional<?> existing = checkExistingEmail(dto.getEmail());
		if(existing == null || !(existing.get() instanceof ErrorMessage)) {
			User user = new User(dto.getName(), dto.getEmail(), passwordEncoder.encode(""+dto.getPassword()), dto.getPhones());		
			user.setToken(generateToken(user));
			user.setLastLogin(new Date());
			User result = userRepository.save(user);
			
			if(result.getPhones() != null) {
				result.getPhones().forEach(phone -> {
					phone.setUser(result);
				});
			}		
			userRepository.save(result);
			
			return Optional.of(userRepository.save(result));
		}
		
		return existing;
	}
	
	private Optional<?> checkExistingEmail(String email) {
		if(email != null) {
			User filter = new User();
			filter.setEmail(email);
			Optional<User> result = userRepository.findOne(Example.of(filter));
			if(result.isPresent()) {
				return Optional.of(errorMessage.emailAlreadyExists()); 
			}
		}
		
		return null;		
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
