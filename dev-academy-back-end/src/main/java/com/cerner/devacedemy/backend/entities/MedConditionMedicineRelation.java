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
@Table(name="MEDICAL_CONDITION_MEDICINE_RELATION")
public class MedConditionMedicineRelation {
	
	@EmbeddedId
	private MedicalConditionMedicineRelationPK id; // $COVERAGE-IGNORE$
	
	@MapsId("medicineId")
    @ManyToOne
    private Medicines medicine;
	
	@MapsId("medicalConditionId")
    @ManyToOne
    private MedicalConditions medicalCondition;
	
	@Column(name="flag")
	private boolean flag;

}
