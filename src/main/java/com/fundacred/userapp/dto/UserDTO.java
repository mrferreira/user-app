package com.fundacred.userapp.dto;

import java.io.Serializable;
import java.util.List;

import javax.validation.constraints.NotNull;

import com.fundacred.userapp.model.Phone;

/**
 * DTO representing user data
 * @author misael
 * @since 2020-03-27
 */
public class UserDTO implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Long id;
	@NotNull
	private String name;
	@NotNull
	private String email;
	@NotNull
	private String password;
	private List<Phone> phones;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public List<Phone> getPhones() {
		return phones;
	}
	public void setPhones(List<Phone> phones) {
		this.phones = phones;
	}
	
	

}
