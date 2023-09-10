package com.cerner.devacedemy.backend.services;

import java.util.List;

import org.springframework.security.core.userdetails.UserDetailsService;

import com.cerner.devacedemy.backend.entities.Allergies;
import com.cerner.devacedemy.backend.entities.Encounter;
import com.cerner.devacedemy.backend.entities.Ingredients;
import com.cerner.devacedemy.backend.entities.Patients;
import com.cerner.devacedemy.backend.entities.PhysicianDetails;
import com.cerner.devacedemy.backend.entities.Prescription;
import com.cerner.devacedemy.backend.entities.User;
import com.cerner.devacedemy.backend.response.DashboardResponse;
import com.cerner.devacedemy.backend.response.MedicineResponse;
import com.cerner.devacedemy.backend.response.PrescriptionResponse;
import com.cerner.devacedemy.backend.requests.AddAllergyRequest;
import com.cerner.devacedemy.backend.requests.MedicineDataRequest;

public interface CustomUserService extends UserDetailsService {
	
	/**
	 * Stores User details into the repository (Database)
	 * 
	 * @param user
	 * @return User
	 */
	User registerUser(User user);
	
	/**
	 * Gets list of Patients from Patient Repository based on searchInput
	 * 
	 * @param searchInput
	 * @return List<Patients>
	 */
	List<Patients> listAll(String searchInput);
	
	/**
	 * Get the details of a particular patient based on Patient ID
	 * 
	 * @param id
	 * @return Patient
	 */
	Patients getPatientById(long id);
	
	/**
	 * Gets the details of Physician by physician ID
	 * 
	 * @param id
	 * @return PhysicianDetails
	 */
	PhysicianDetails getPhysicianById(long id);
	
	/**
	 * Gets list of Medicines from Medicines Repository based on searchInput and checks if medicines cause any
	 * allergic reaction or if medicines cause undesired reactions due to patients' medical conditions or
	 * recorded allergies and returns appropriate medicine details as response
	 * 
	 * @param searchInput
	 * @param patientId
	 * @return List<MedicineResponse>
	 */
	List<MedicineResponse> listAllMedicines(String searchInput, long patientId);
	
	/**
	 * Gets list of Ingredients from Ingredients Repository based on searchInput
	 * 
	 * @param searchInput
	 * @return List<Ingredients>
	 */
	List<Ingredients> listAllIngredients(String searchInput);
	
	/**
	 * Creating a new encounter between patient and physician to allow physician to add allergy or prescribe
	 * medicines for a patient
	 * 
	 * @param allergyRequest
	 * @param user
	 * @param patient
	 * @return Allergies
	 */
	Encounter createEncounter(User user, Patients patient);
	
	/**
	 * Function which allows physician to add a new allergy for a patient
	 * 
	 * @param encounter
	 * @param allergyRequest
	 * @param patient
	 * @return Allergies
	 */
	Allergies addNewAllergy(Encounter encounter, AddAllergyRequest allergyRequest, Patients patient);
	
	/**
	 * Function that gets a list of all allergies that a patient has
	 * 
	 * @param patient
	 * @return List<Allergies>
	 */
	List<Allergies> listAllAllergiesForPatient(Patients patient);
	
	/**
	 * Get existing encounter from database by ID
	 * 
	 * @param id
	 * @return Encounter
	 */
	Encounter getEncounterById(long id);
	
	/**
	 * Function to get most recent patients of a particular physician
	 * 
	 * @param user
	 * @return List<DashboardResponse>
	 */
	List<DashboardResponse> listRecentPatients(User user);
	
	/**
	 * Get ingredients from database by ID
	 * 
	 * @param id
	 * @return Ingredients
	 */
	Ingredients getIngredientById(long id);
	
	/**
	 * Function to retrieve prescription history of a particular patient
	 * 
	 * @param patient
	 * @return List<PrescriptionResponse>
	 */
	List<PrescriptionResponse> getPrescriptionHistory(Patients patient);
	
	/**
	 * Function that allows user to prescribe medicines for a patient
	 * 
	 * @param encounter
	 * @param medicineDataRequest
	 * @param patient
	 * @return Prescription
	 */
	Prescription prescribeMedicine(Encounter encounter, MedicineDataRequest medicineDataRequest, Patients patient);
	
	/**
	 * Function to save list of prescription data to the database
	 * @param prescriptionList
	 * @return List<Prescription>  
	 */
	List<Prescription> savePrescription(List<Prescription> prescriptionList);
}
