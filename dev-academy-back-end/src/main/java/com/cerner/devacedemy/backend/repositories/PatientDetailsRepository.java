package com.cerner.devacedemy.backend.repositories;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.cerner.devacedemy.backend.entities.Patients;
import com.cerner.devacedemy.backend.entities.User;
import com.cerner.devacedemy.backend.response.DashboardResponse;

public interface PatientDetailsRepository extends JpaRepository<Patients, Long> {
	
	/**
	 * Queries database to get a list of patients stored in database based on searchInput
	 * @param searchInput
	 * @return
	 */
	@Query("SELECT p from Patients p WHERE p.firstName LIKE %?1% OR p.lastName LIKE %?1%")
	public List<Patients> findAll(String searchInput);
	
	/**
	 * Queries database to get a list of most recent patients for a particular physician
	 * @param user
	 * @param pageable
	 * @return
	 */
	@Query("SELECT new com.cerner.devacedemy.backend.response.DashboardResponse(p.id, p.firstName, "
			+ "p.lastName, p.age, p.gender, e.date) FROM Patients p JOIN Encounter e ON p.id = e.patient "
			+ "WHERE e.id IN (SELECT MAX(e.id) FROM Encounter e WHERE e.user = ?1 GROUP BY "
			+ "e.patient)")
	public List<DashboardResponse> recentPatients(User user, Pageable pageable);
	
	/**
	 * Queries database to get a list of medicines which are incompatible with patients' medical conditions
	 * @param patientId
	 * @return
	 */
	@Query("SELECT mcmr.id.medicineId FROM Patients p JOIN p.medicalConditions mc JOIN "
			+ "MedConditionMedicineRelation mcmr ON mcmr.id.medicalConditionId = mc.id WHERE p.id = ?1 "
			+ "AND mcmr.flag = 1 GROUP BY mcmr.id.medicineId")
	public List<Long> findMedicinesIncompatibleWithMedicalConditions(long patientId);
	
}
