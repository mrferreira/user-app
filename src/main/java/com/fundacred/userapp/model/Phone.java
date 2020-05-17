package com.fundacred.userapp.model;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import com.fasterxml.jackson.annotation.JsonBackReference;

/**
 * Class representing a phone entity
 * @author misael
 * @since 2020-03-27
 */
@Getter
@Setter
@EqualsAndHashCode
@Entity
@Table(name = "PHONE",
		uniqueConstraints = @UniqueConstraint(columnNames = {"NUMBER", "DDD", "USER_ID"}))
public class Phone implements Serializable {

	
	private static final long serialVersionUID = 5554560104130388468L;
	
	@GenericGenerator(
	        name = "S_PHONE",
	        strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
	        parameters = {
	                @Parameter(name = "sequence_name", value = "S_PHONE"),
	                @Parameter(name = "initial_value", value = "1"),
	                @Parameter(name = "increment_size", value = "1")
	        }
	)
	@Id
	@Column(name = "PHONE_ID")
	@GeneratedValue(generator = "S_PHONE", strategy = GenerationType.SEQUENCE)
	private Long id;
	@ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
	@JoinColumn(name = "USER_ID")
	@JsonBackReference
	private User user;
	@NotNull
	@Column(name = "NUMBER")
	private String number;
	@NotNull
	@Column(name = "DDD")
	private String ddd;
	
	public Phone() {}
	
	public Phone(String number, String ddd) {
		this.number = number;
		this.ddd = ddd;
	}
}
