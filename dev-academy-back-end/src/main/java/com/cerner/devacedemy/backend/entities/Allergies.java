package com.cerner.devacedemy.backend.entities;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name="ALLERGIES")
public class Allergies {
	
	public enum Severity {
		NONE,
	    MILD,
	    MODERATE,
	    CRITICAL
	}
	
	public enum Status {
	    ACTIVE,
	    INACTIVE
	}
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	@Column(name = "name", nullable = false)
	private String name;
	
	@Column(name = "reaction")
	private String reaction;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "severity")
	private Severity severity;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "status")
	private Status status;
	
	@OneToOne(cascade = {CascadeType.MERGE})
	@JoinColumn(name = "caused_by_ing_id", referencedColumnName = "id")
	private Ingredients ingredient;
	
	@ManyToOne(cascade = {CascadeType.ALL})
	@JoinColumn(name = "encounter_id", referencedColumnName = "id")
	private Encounter encounter;
	
	@ManyToOne(cascade = {CascadeType.MERGE})
	@JoinColumn(name = "patient_id", referencedColumnName = "id")
	private Patients patient;

}
