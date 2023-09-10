package com.cerner.devacedemy.backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.cerner.devacedemy.backend.entities.MedConditionMedicineRelation;
import com.cerner.devacedemy.backend.entities.MedicalConditionMedicineRelationPK;

public interface MedicalConditionMedicineRelationRepository extends JpaRepository<MedConditionMedicineRelation, MedicalConditionMedicineRelationPK> {
	
	@Query("SELECT m.flag FROM MedConditionMedicineRelation m WHERE m.id.medicineId = ?1 AND m.id.medicalConditionId = ?2")
	public boolean checkMedConditionMedicineCompatibility(long medicineId, long medicalContitionId);
	
}
