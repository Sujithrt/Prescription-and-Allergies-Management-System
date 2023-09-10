package com.cerner.devacedemy.backend.services.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.*;    

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.cerner.devacedemy.backend.entities.Allergies;
import com.cerner.devacedemy.backend.entities.Encounter;
import com.cerner.devacedemy.backend.entities.Ingredients;
import com.cerner.devacedemy.backend.entities.Medicines;
import com.cerner.devacedemy.backend.entities.Patients;
import com.cerner.devacedemy.backend.entities.PhysicianDetails;
import com.cerner.devacedemy.backend.entities.Prescription;
import com.cerner.devacedemy.backend.entities.PrescriptionPK;
import com.cerner.devacedemy.backend.entities.User;
import com.cerner.devacedemy.backend.exceptions.AllergyAlreadyExistsException;
import com.cerner.devacedemy.backend.exceptions.IncorrectMedicineDataException;
import com.cerner.devacedemy.backend.exceptions.InvalidEmailOrPasswordException;
import com.cerner.devacedemy.backend.exceptions.MedicineAlreadyPrescribedException;
import com.cerner.devacedemy.backend.exceptions.ResourceNotFoundException;
import com.cerner.devacedemy.backend.repositories.AllergiesRepository;
import com.cerner.devacedemy.backend.repositories.EncountersRepository;
import com.cerner.devacedemy.backend.repositories.IngredientDetailsRepository;
import com.cerner.devacedemy.backend.repositories.MedicineDetailsRepository;
import com.cerner.devacedemy.backend.repositories.PatientDetailsRepository;
import com.cerner.devacedemy.backend.repositories.PhysicianDetailsRepository;
import com.cerner.devacedemy.backend.repositories.PrescriptionRepository;
import com.cerner.devacedemy.backend.repositories.UserDetailsRepository;
import com.cerner.devacedemy.backend.requests.AddAllergyRequest;
import com.cerner.devacedemy.backend.requests.MedicineDataRequest;
import com.cerner.devacedemy.backend.response.DashboardResponse;
import com.cerner.devacedemy.backend.response.IncompatibleAllergiesResponse;
import com.cerner.devacedemy.backend.response.MedicineResponse;
import com.cerner.devacedemy.backend.response.PrescriptionResponse;
import com.cerner.devacedemy.backend.services.CustomUserService;

@Service
public class CustomUserServiceImpl implements CustomUserService {

	Logger logger = LoggerFactory.getLogger(CustomUserServiceImpl.class);

	@Autowired
	private PasswordEncoder passwordEncoder;

	UserDetailsRepository userDetailsRepository;
	PatientDetailsRepository patientDetailsRepository;
	PhysicianDetailsRepository physicianDetailsRepository;
	MedicineDetailsRepository medicineDetailsRepository;
	IngredientDetailsRepository ingredientDetailsRepository;
	AllergiesRepository allergiesRepository;
	EncountersRepository encountersRepository;
	PrescriptionRepository prescriptionRepository;

	public CustomUserServiceImpl(UserDetailsRepository userDetailsRepository, 
			PatientDetailsRepository patientDetailsRepository, PhysicianDetailsRepository physicianDetailsRepository,
			MedicineDetailsRepository medicineDetailsRepository, IngredientDetailsRepository ingredientDetailsRepository,
			AllergiesRepository allergiesRepository, EncountersRepository encountersRepository,
			PrescriptionRepository prescriptionRepository) {
		super();
		this.userDetailsRepository = userDetailsRepository;
		this.patientDetailsRepository = patientDetailsRepository;
		this.physicianDetailsRepository = physicianDetailsRepository;
		this.medicineDetailsRepository = medicineDetailsRepository;
		this.ingredientDetailsRepository = ingredientDetailsRepository;
		this.allergiesRepository = allergiesRepository;
		this.encountersRepository = encountersRepository;
		this.prescriptionRepository = prescriptionRepository;
	}

	/**
	 * Takes in username as input and returns the user details corresponding to the input user name
	 * 
	 * @return UserDetails
	 * @param username
	 * @throws UsernameNotFoundException
	 */
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		User user = userDetailsRepository.findByUsername(username);

		if(null == user) {
			throw new ResourceNotFoundException("User","username",username);
		}
		return user;
	}

	/**
	 * Function to validate the Registration Email ID
	 * 
	 * @param email
	 * @return true or false
	 */
	public boolean validateEmail(String email) {
		String regex = "^(.+)@(.+)$";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(email);
		return matcher.matches();
	}

	/**
	 * Stores User details into the repository (Database)
	 * 
	 * @param user
	 * @return User
	 */
	@Override
	public User registerUser(User user) throws DataIntegrityViolationException {
		if(validateEmail(user.getEmail())) {
			logger.info("Email ID is valid");
		} else {
			logger.error("Invalid Email");
			throw new InvalidEmailOrPasswordException("Email", user.getEmail());
		}
		if(user.getPassword().length() >= 8) {
			user.setPassword(passwordEncoder.encode(user.getPassword()));
			logger.info("Password are valid");
			logger.info("Password encoded using BCrypt Password Encoder");
			return userDetailsRepository.save(user);
		} else {
			logger.error("Password too short");
			throw new InvalidEmailOrPasswordException("Password", user.getPassword());
		}
	}

	/**
	 * Gets list of Patients from Patient Repository based on searchInput
	 * 
	 * @param searchInput
	 * @return List<Patients>
	 */
	@Override
	public List<Patients> listAll(String searchInput) {
		if(null != searchInput) {
			return patientDetailsRepository.findAll(searchInput);
		}
		return patientDetailsRepository.findAll();
	}

	/**
	 * Get the details of a particular patient based on Patient ID
	 * 
	 * @param id
	 * @return Patient
	 */
	@Override
	public Patients getPatientById(long id) {
		return patientDetailsRepository.findById(id).orElseThrow( 
				() -> new ResourceNotFoundException("Patient", "Id", id) );
	}

	/**
	 * Gets the details of Logged in User
	 * 
	 * @param id
	 * @return PhysicianDetails
	 */
	@Override
	public PhysicianDetails getPhysicianById(long id) {
		return physicianDetailsRepository.findById(id).orElseThrow(
				() -> new ResourceNotFoundException("Physician", "Id", id) );
	}

	/**
	 * Gets list of Medicines from Medicines Repository based on searchInput and checks if medicines cause any
	 * allergic reaction or if medicines cause undesired reactions due to patients' medical conditions or
	 * recorded allergies and returns appropriate medicine details as response
	 * 
	 * @param searchInput
	 * @param patientId
	 * @return List<MedicineResponse>
	 */
	@Override
	public List<MedicineResponse> listAllMedicines(String searchInput, long patientId) {
		List<MedicineResponse> medicineResponseList = new ArrayList<MedicineResponse>();
		List<Medicines> medicines;
		if(null != searchInput) {
			medicines = medicineDetailsRepository.findAll(searchInput);
		} else {
			medicines = medicineDetailsRepository.findAll();
		}
		List<IncompatibleAllergiesResponse> allergyMedicines = medicineDetailsRepository.findMedicinesIncompatibleWithAllergies(patientId);
		List<Long> allergyMedicineIds = new ArrayList<Long>();
		for(IncompatibleAllergiesResponse medicineId: allergyMedicines) {
			logger.info(medicineId.toString());
			allergyMedicineIds.add(medicineId.getMedicineId());
		}
		List<Long> medConditionMedicineIds = patientDetailsRepository.findMedicinesIncompatibleWithMedicalConditions(patientId);
		for(Medicines medicine: medicines) {
			MedicineResponse medicineResponse = new MedicineResponse();
			medicineResponse.setBrandName(medicine.getBrandName());
			medicineResponse.setId(medicine.getId());
			if(medConditionMedicineIds.contains(medicine.getId())) {
				medicineResponse.setCause("Incompatible with patients' medical conditions");
			}
			else if(allergyMedicineIds.contains(medicine.getId())) {
				for(IncompatibleAllergiesResponse incompatibleAllergiesResponse: allergyMedicines) {
					if(incompatibleAllergiesResponse.getMedicineId() == medicine.getId()) {
						medicineResponse.setCause(medicineResponse.getCause() + "Causes " + incompatibleAllergiesResponse.getSeverity() +
								" " + incompatibleAllergiesResponse.getReaction() + " in patient");
					}
				}
			}
			medicineResponseList.add(medicineResponse);
		}
		return medicineResponseList;
	}

	/**
	 * Gets list of Ingredients from Ingredients Repository based on searchInput
	 * 
	 * @param searchInput
	 * @return List<Ingredients>
	 */
	@Override
	public List<Ingredients> listAllIngredients(String searchInput) {
		if(null != searchInput) {
			return ingredientDetailsRepository.findAll(searchInput);
		}
		return ingredientDetailsRepository.findAll();
	}

	/**
	 * Get existing encounter from database by ID
	 * 
	 * @param id
	 * @return Encounter
	 */
	@Override
	public Encounter getEncounterById(long id) {
		return encountersRepository.findById(id).orElseThrow(
				() -> new ResourceNotFoundException("Encounter", "Id", id) );
	}

	/**
	 * Get ingredients from database by ID
	 * 
	 * @param id
	 * @return Ingredients
	 */
	@Override
	public Ingredients getIngredientById(long id) {
		return ingredientDetailsRepository.findById(id).orElseThrow(
				() -> new ResourceNotFoundException("Ingredient", "Id", id) );
	}

	/**
	 * Creating a new encounter between patient and physician to allow physician to add allergy or prescribe
	 * medicines for a patient
	 * 
	 * @param allergyRequest
	 * @param user
	 * @param patient
	 * @return Allergies
	 */
	@Override
	public Encounter createEncounter(User user, Patients patient) {
		Encounter encounter = new Encounter();
		Date date = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		String formatterDate = formatter.format(date);
		encounter.setDate(formatterDate);
		encounter.setUser(user);
		encounter.setPatient(patient);
		return encounter;
	}

	/**
	 * Function which allows physician to add a new allergy for a patient
	 * 
	 * @param encounter
	 * @param allergyRequest
	 * @param patient
	 * @return Allergies
	 */
	@Override
	public Allergies addNewAllergy(Encounter encounter, AddAllergyRequest allergyRequest, Patients patient) {
		if(allergiesRepository.getNumberOfAllergiesWithGivenName(patient, allergyRequest.getIngredient()) >= 1) {
			throw new AllergyAlreadyExistsException("Allergy", allergyRequest.getName(), patient.getId());
		} else {
			Allergies newAllergy = new Allergies();
			Ingredients ingredient = new Ingredients();
			ingredient.setId(allergyRequest.getIngredient().getId());
			ingredient.setName(allergyRequest.getIngredient().getName());
			newAllergy.setIngredient(ingredient);
			newAllergy.setName(allergyRequest.getName());
			newAllergy.setPatient(patient);
			newAllergy.setReaction(allergyRequest.getReaction());
			newAllergy.setSeverity(allergyRequest.getSeverity());
			newAllergy.setStatus(allergyRequest.getStatus());
			newAllergy.setEncounter(encounter);
			allergiesRepository.save(newAllergy);
			return newAllergy;
		}
	}

	/**
	 * Function that gets a list of all allergies that a patient has
	 * 
	 * @param patient
	 * @return List<Allergies>
	 */
	@Override
	public List<Allergies> listAllAllergiesForPatient(Patients patient) {
		return allergiesRepository.getAllergiesForPatient(patient);
	}

	/**
	 * Function to get most recent patients of a particular physician
	 * 
	 * @param user
	 * @return List<DashboardResponse>
	 */
	@Override
	public List<DashboardResponse> listRecentPatients(User user) {
		Pageable topSix = PageRequest.of(0, 6, Sort.by("e.id").descending().and(Sort.by("e.date").descending()));
		List<DashboardResponse> patients = patientDetailsRepository.recentPatients(user, topSix);
		if(patients.size() == 0) {
			throw new RuntimeException("You do not have any patients yet");
		} else {
			return patients;
		}
	}

	/**
	 * Function to retrieve prescription history of a particular patient
	 * 
	 * @param patient
	 * @return List<PrescriptionResponse>
	 */
	@Override
	public List<PrescriptionResponse> getPrescriptionHistory(Patients patient) {
		List<PrescriptionResponse> prescriptionHistory = prescriptionRepository.listPrescriptionHistory(patient);
		if(prescriptionHistory.size() == 0) {
			throw new RuntimeException("Patient does not have any Prescription History");
		} else {
			return prescriptionHistory;
		}
	}

	/**
	 * Function that allows user to prescribe medicines for a patient
	 * 
	 * @param encounter
	 * @param medicineDataRequest
	 * @param patient
	 * @return Prescription
	 */
	@Override
	public Prescription prescribeMedicine(Encounter encounter, MedicineDataRequest medicineDataRequest, Patients patient) {
		try {
			Medicines medicine = medicineDetailsRepository.findById(medicineDataRequest.getMedicine().getValue())
					.orElseThrow( () -> new ResourceNotFoundException("Medicine", "Id", medicineDataRequest.getMedicine().getValue()));
			if(!medicine.getBrandName().trim().equals(medicineDataRequest.getMedicine().getLabel().trim())) {
				logger.info("Extracted from database " + medicine.getBrandName());
				logger.info("In prescribe request " + medicineDataRequest.getMedicine().getLabel());
				throw new IncorrectMedicineDataException("Medicine", medicineDataRequest.getMedicine().getLabel(), medicine.getId());
			}
			Prescription prescription = new Prescription();
			encountersRepository.save(encounter);
			logger.info(encounter.toString());
			PrescriptionPK prescriptionPK = new PrescriptionPK(encounter.getId(), medicine.getId());
			if(prescriptionRepository.isMedicinePrescribed(encounter.getId(), medicine.getId()) == 1) {
				throw new MedicineAlreadyPrescribedException("Medicine", medicine.getBrandName(), encounter.getId());
			} else {
				prescription.setId(prescriptionPK);
				prescription.setEncounter(encounter);
				prescription.setMedicine(medicine);
				prescription.setDosage(medicineDataRequest.getDose());
				return prescription;
			}
		} catch(ResourceNotFoundException e) {
			throw e;
		}

	}

	/**
	 * Function to save list of prescription data to the database
	 * @param prescriptionList
	 * @return List<Prescription>  
	 */
	@Override
	public List<Prescription> savePrescription(List<Prescription> prescriptionList) {
		return prescriptionRepository.saveAll(prescriptionList);
	}

}