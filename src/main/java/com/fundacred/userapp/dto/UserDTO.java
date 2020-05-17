package com.fundacred.userapp.dto;

import java.io.Serializable;
import java.util.List;

import javax.validation.constraints.NotNull;

import com.fundacred.userapp.model.Phone;
import lombok.Getter;
import lombok.Setter;

/**
 * DTO representing user data
 * @author misael
 * @since 2020-03-27
 */
@Getter
@Setter
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

}
