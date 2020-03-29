package com.fundacred.userapp.config;

import java.util.ArrayList;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.fundacred.userapp.error.ErrorMessage;
import com.fundacred.userapp.service.UserService;

@Service
public class JwtUserDetailsService implements UserDetailsService {

	@Autowired
	private UserService userService;
	@Autowired
	private ErrorMessage errorMessage;
	
	@Override
	public UserDetails loadUserByUsername(String username) {
		com.fundacred.userapp.model.User model = userService.findByEmail(username).map(m -> m).orElse(null); 
		if (model != null) {
			User result = new User(model.getEmail(), model.getPassword(),
					new ArrayList<>());
			long timeout = 1000*60*30;
			if(model.getLastLogin().getTime() < (new Date().getTime() - timeout)) {
				throw new CredentialsExpiredException(errorMessage.invalidSession().getMensagem());
			}
			return result;
		} else {
			throw new UsernameNotFoundException(errorMessage.invalidUserPassword().getMensagem());
		}
	}
}