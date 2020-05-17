package com.fundacred.userapp.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.UpdateTimestamp;

/**
 * Class representing a user entity
 * @author misael
 * @since 2020-03-27
 */
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "USER")
public class User implements Serializable {

	private static final long serialVersionUID = -5381791566804245111L;
	
	@GenericGenerator(
	        name = "S_USER",
	        strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
	        parameters = {
	                @Parameter(name = "sequence_name", value = "S_USER"),
	                @Parameter(name = "initial_value", value = "1"),
	                @Parameter(name = "increment_size", value = "1")
	        }
	)
	@Id
	@Column(name = "USER_ID")
	@GeneratedValue(generator = "S_USER", strategy = GenerationType.SEQUENCE)	
	private Long id;
	@NotNull
	@Column(name = "NAME")
	private String name;
	@NotNull
	@Column(name = "EMAIL")
	private String email;
	@NotNull
	@Column(name = "PASSWORD")
	private String password;
	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true) 
	private List<Phone> phones = new ArrayList<Phone>();
	@CreationTimestamp
	@Column(name = "CREATED_AT")
	private Date created;
	@UpdateTimestamp
	@Column(name = "MODIFIED_AT")
	private Date modified;
	@Column(name = "LAST_LOGIN")
	private Date lastLogin;
	@Column(name = "token")
	private String token;
	
	public User(String name, String email, String password, List<Phone> phones) {
		this.name = name;
		this.email = email;
		this.password = password;
		if(phones != null) {
			phones.forEach(p -> addPhone(p));
		}
	}

	public void addPhone(Phone phone) {
		phones.add(phone);
	}
	
	public void removePhone(Phone phone) {
		phones.remove(phone);
	}
}
