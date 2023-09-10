package com.cerner.devacedemy.backend.entities;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name="PRESCRIPTION")
public class Prescription {
	
	@EmbeddedId
	private PrescriptionPK id;
	
	@MapsId("encounterId")
    @ManyToOne
    private Encounter encounter;
	
	@MapsId("medicineId")
    @ManyToOne
    private Medicines medicine;
	
	@Column(name = "dosage", nullable = false)
	private String dosage;
	
}
