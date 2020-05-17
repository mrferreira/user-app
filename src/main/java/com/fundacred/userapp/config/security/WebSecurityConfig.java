package com.fundacred.userapp.config.security;

import com.fundacred.userapp.config.JwtRequestFilter;
import com.fundacred.userapp.config.JwtUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
	private final JwtUserDetailsService jwtUserDetailsService;
	private final JwtRequestFilter jwtRequestFilter;
	private final CustomAuthenticationFailureHandler customAuthenticationFailureHandler;
	private final AuthenticationManagerBuilder authenticationManagerBuilder;

	public WebSecurityConfig(JwtAuthenticationEntryPoint authenticationEntryPoint,
							 JwtUserDetailsService userDetailsService,
							 JwtRequestFilter jwtRequestFilter,
							 CustomAuthenticationFailureHandler customAuthenticationFailureHandler,
							 AuthenticationManagerBuilder authenticationManagerBuilder) {
		this.jwtAuthenticationEntryPoint = authenticationEntryPoint;
		this.jwtUserDetailsService = userDetailsService;
		this.jwtRequestFilter = jwtRequestFilter;
		this.customAuthenticationFailureHandler = customAuthenticationFailureHandler;
		this.authenticationManagerBuilder = authenticationManagerBuilder;
	}
	 
	@Bean
	public DaoAuthenticationProvider authProvider() {
	    DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
	    authProvider.setUserDetailsService(jwtUserDetailsService);
	    authProvider.setPasswordEncoder(passwordEncoder());
	    return authProvider;
	}

	public void configureGlobal() throws Exception {
		authenticationManagerBuilder.userDetailsService(jwtUserDetailsService).passwordEncoder(passwordEncoder());
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}
	
	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().antMatchers("/", "/v2/api-docs", "/configuration/ui", "/swagger-resources",
				"/configuration/security", "/swagger-ui.html", "/webjars/**", "/v2/swagger.json",
				"/actuator/**");
	}

	@Override
	protected void configure(HttpSecurity httpSecurity) throws Exception {
		httpSecurity.csrf().disable()
				.authorizeRequests()
				.antMatchers(
						"/",
						"/users/signup", 
						"/users/authenticate",
						"/v2/api-docs", 
						"/configuration/ui", 
						"/swagger-resources/**",
						"/configuration/security/**", 
						"/swagger-ui.html", 
						"/webjars/**", 
						"/v2/swagger.json").permitAll()
				.anyRequest().authenticated().and()
				.exceptionHandling().authenticationEntryPoint(jwtAuthenticationEntryPoint).and().sessionManagement()
				.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
				.sessionAuthenticationFailureHandler(customAuthenticationFailureHandler);
		httpSecurity.addFilterAfter(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
	}
	
}