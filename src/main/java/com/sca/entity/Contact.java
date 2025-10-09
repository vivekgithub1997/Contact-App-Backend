package com.sca.entity;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name="contact")
@Data
public class Contact implements Serializable{
	
	private static final long serialVersionUID = -8309875615397206539L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private int cId;
	private String name;
	private String secondName;
	private String email;
	private String phone;
	private String description;

	@ManyToOne
	@JsonIgnore
	private User user;

}
