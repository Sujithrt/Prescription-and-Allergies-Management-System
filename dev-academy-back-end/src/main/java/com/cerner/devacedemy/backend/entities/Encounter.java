package com.cerner.devacedemy.backend.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name="ENCOUNTER")
public class Encounter {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	@Column(name="date")
	private String date;
	
	@OneToOne
	@JoinColumn(name = "patient_id", referencedColumnName = "id")
	private Patients patient;
	
	@OneToOne
	@JoinColumn(name = "physician_id", referencedColumnName = "id")
	private User user;

}
