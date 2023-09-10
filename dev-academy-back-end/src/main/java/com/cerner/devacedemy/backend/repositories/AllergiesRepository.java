package com.cerner.devacedemy.backend.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.cerner.devacedemy.backend.entities.Allergies;
import com.cerner.devacedemy.backend.entities.Ingredients;
import com.cerner.devacedemy.backend.entities.Patients;

public interface AllergiesRepository extends JpaRepository<Allergies, Long> {
	List<Allergies> findByPatientId(long patientId);

	/**
	 * Queries the database for allergies of a patient with a given name
	 * @param patient
	 * @param allergyName
	 * @return
	 */
	@Query("SELECT count(*) FROM Allergies a WHERE a.patient=?1 AND a.ingredient = ?2")
	public int getNumberOfAllergiesWithGivenName(Patients patient, Ingredients ingredient);
	
	/**
	 * Queries the database to get a list of all the recorded allergies for a patient
	 * @param patient
	 * @return
	 */
	@Query("SELECT a FROM Allergies a WHERE a.patient=?1")
	public List<Allergies> getAllergiesForPatient(Patients patient);

}
