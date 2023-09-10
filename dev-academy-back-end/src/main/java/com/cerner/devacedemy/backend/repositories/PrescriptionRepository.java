package com.cerner.devacedemy.backend.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.cerner.devacedemy.backend.entities.Patients;
import com.cerner.devacedemy.backend.entities.Prescription;
import com.cerner.devacedemy.backend.entities.PrescriptionPK;
import com.cerner.devacedemy.backend.response.PrescriptionResponse;

public interface PrescriptionRepository extends JpaRepository<Prescription, PrescriptionPK> {

	/**
	 * Queries database to get a patients' prescription history
	 * @param patient
	 * @return
	 */
	@Query("SELECT new com.cerner.devacedemy.backend.response.PrescriptionResponse(m.brandName, m.genericName, p.dosage,"
			+ " e.date) FROM Encounter e JOIN Prescription p ON e.id = p.id.encounterId JOIN Medicines m "
			+ "ON p.id.medicineId = m.id WHERE e.patient = ?1 ORDER BY e.date DESC, e.id DESC")
	public List<PrescriptionResponse> listPrescriptionHistory(Patients patient);
	
	/**
	 * Queries database to find the number of times a medicine has been prescribed in a given encounter
	 * @param encounter
	 * @param medicine
	 * @return
	 */
	@Query("SELECT count(*) FROM Prescription p WHERE p.id.encounterId = ?1 AND p.id.medicineId = ?2")
	public int isMedicinePrescribed(Long encounter, Long medicine);

}
