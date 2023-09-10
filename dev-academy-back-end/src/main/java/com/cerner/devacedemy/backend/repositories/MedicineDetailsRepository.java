package com.cerner.devacedemy.backend.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.cerner.devacedemy.backend.entities.Medicines;
import com.cerner.devacedemy.backend.response.IncompatibleAllergiesResponse;

public interface MedicineDetailsRepository extends JpaRepository<Medicines, Long> {
	
	/**
	 * Queries database to get a list of medicines stored in database based on searchInput
	 * @param searchInput
	 * @return
	 */
	@Query("SELECT m FROM Medicines m WHERE m.genericName LIKE %?1% OR m.brandName LIKE %?1%")
	public List<Medicines> findAll(String searchInput);
	
	/**
	 * Queries database to find a list of medicines which are incompatible with patients' recorded allergies
	 * @param patientId
	 * @return
	 */
	@Query("SELECT new com.cerner.devacedemy.backend.response.IncompatibleAllergiesResponse(m.id, a.reaction, "
			+ "a.severity) from Medicines m JOIN m.ingredients i JOIN Allergies a ON a.ingredient.id = i.id "
			+ "WHERE a.patient.id = ?1")
	public List<IncompatibleAllergiesResponse> findMedicinesIncompatibleWithAllergies(long patientId);
	
}
