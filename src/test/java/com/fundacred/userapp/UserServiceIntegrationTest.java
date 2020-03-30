package com.fundacred.userapp;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertTrue;

import java.util.Date;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import com.fundacred.userapp.config.JwtTokenUtil;
import com.fundacred.userapp.dao.UserRepository;
import com.fundacred.userapp.dto.UserDTO;
import com.fundacred.userapp.error.ErrorMessage;
import com.fundacred.userapp.model.Phone;
import com.fundacred.userapp.model.User;
import com.fundacred.userapp.service.UserService;

@SpringBootTest
@RunWith(SpringRunner.class)
public class UserServiceIntegrationTest {

	private static final String DDD = "21";
	private static final String NUMBER = "987654321";
	private static final String NAME = "JOHN DOE";
	private static final String EMAIL = "jdoe@gmail.com";
	private static final String PASSWORD = "102030";
	
	@MockBean
	UserRepository userRepository;
	@MockBean
	JwtTokenUtil tokenUtil;
	@MockBean
	PasswordEncoder encoder;
	@MockBean
	ErrorMessage errorMessage;
	
	private UserService userService;
	
	@Before
	public void init() {
		userService = new UserService(userRepository, tokenUtil, encoder, errorMessage);
	    User johnDoe = new User();
	    johnDoe.setName(NAME);
	    johnDoe.setEmail(EMAIL);
	    johnDoe.setPassword(PASSWORD);
	    johnDoe.getPhones().add(new Phone(NUMBER, DDD));
	    johnDoe.setLastLogin(new Date(0));
	    
	 
	    User filter = new User();
	    filter.setEmail(johnDoe.getEmail());
	    Mockito.when(userRepository.findOne(Mockito.any()))
	      .thenReturn(Optional.of(johnDoe));
	    
	    ErrorMessage emailExists = new ErrorMessage();
	    emailExists.setMensagem("Email existente");
	    Mockito.when(errorMessage.emailAlreadyExists())
	    	.thenReturn(emailExists);
	    
	    ErrorMessage invalidUserPwd = new ErrorMessage();
	    invalidUserPwd.setMensagem("Usuario/senha invalidos");
	    Mockito.when(errorMessage.invalidUserPassword())
	    	.thenReturn(invalidUserPwd);
	    
	    Mockito.when(encoder.matches(Mockito.anyString(), Mockito.anyString()))
	    	.thenReturn(false);
	}
	
	@Test
	public void whenValidEmail_thenReturUser() {
		Optional<User> user = userService.findByEmail(EMAIL);
		assertTrue(user.isPresent());
		assertThat(user.get().getEmail()).isEqualTo(EMAIL);
		assertThat(user.get().getName()).isEqualTo(NAME);
	}
	
	@Test
	public void whenExistingEmail_thenFail() {
		UserDTO save = new UserDTO();
		save.setEmail(EMAIL);
		save.setName(NAME);
		save.setPassword(PASSWORD);
		Optional<?> user = userService.save(save);
		assertTrue(user.isPresent() && user.get() instanceof ErrorMessage);
		assertThat(((ErrorMessage)user.get()).getMensagem())
		.isEqualTo("Email existente");
	}
	
	@Test(expected = BadCredentialsException.class)
	public void whenLoginEmailInexistent_thenFail() {
		Optional<?> result = userService.loadAuthUser(EMAIL, PASSWORD);
		assertTrue(result.isPresent() && result.get() instanceof ErrorMessage);
		assertThat(((ErrorMessage)result.get()).getMensagem())
		.isEqualTo("Usuario/senha invalidos");
	}
	
	@Test(expected = BadCredentialsException.class)
	public void whenLoginPasswordInvalid_thenFail() {		
		String password = "10";
		Optional<?> result = userService.loadAuthUser(EMAIL, password);
		assertTrue(result.isPresent() && result.get() instanceof ErrorMessage);
		assertThat(((ErrorMessage)result.get()).getMensagem())
		.isEqualTo("Usuario/senha invalidos");
	}
}
