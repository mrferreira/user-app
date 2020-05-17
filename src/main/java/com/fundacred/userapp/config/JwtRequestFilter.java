package com.fundacred.userapp.config;

import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.fundacred.userapp.error.ErrorMessage;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.SignatureException;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {

	private final JwtUserDetailsService jwtUserDetailsService;
	private final JwtTokenUtil jwtTokenUtil;
	private final ErrorMessage errorMessage;

	public JwtRequestFilter(@Lazy JwtUserDetailsService userDetailsService,
							JwtTokenUtil tokenUtil,
							ErrorMessage errorMessage) {
		this.jwtUserDetailsService = userDetailsService;
		this.jwtTokenUtil = tokenUtil;
		this.errorMessage = errorMessage;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws ServletException, IOException, SignatureException {
		final String requestTokenHeader = request.getHeader("Authorization");
		String username = null;
		String jwtToken = null;
		try {
			if (requestTokenHeader != null) {
				if(requestTokenHeader.startsWith("Bearer ")) {
					jwtToken = requestTokenHeader.substring(7);
					try {
						username = jwtTokenUtil.getUsernameFromToken(jwtToken);
						if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
							UserDetails userDetails = this.jwtUserDetailsService.loadUserByUsername(username);
							if (jwtTokenUtil.validateToken(jwtToken, userDetails)) {
								UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
										userDetails, null, userDetails.getAuthorities());
								usernamePasswordAuthenticationToken
										.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
								SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
							}
						}
					} catch (IllegalArgumentException e) {
						System.out.println("Unable to get JWT Token");
					} catch (ExpiredJwtException e) {
						throw new SignatureException("JWT Token has expired");
					}
				} else {
					throw new SignatureException("Token needs to begin with 'Bearer ' String");
				}
			}			
			chain.doFilter(request, response);
		} catch(UsernameNotFoundException ex) {
			response.setStatus(HttpStatus.UNAUTHORIZED.value());
			response.getWriter().write(errorMessage.invalidUserPassword().json());
		} catch(SignatureException ex) {
			response.setStatus(HttpStatus.UNAUTHORIZED.value());
			response.getWriter().write(errorMessage.unauthorized().json());
		}
	}
}