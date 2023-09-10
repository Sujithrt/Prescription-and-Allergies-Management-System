package com.cerner.devacedemy.backend.controllers;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cerner.devacedemy.backend.entities.Allergies;
import com.cerner.devacedemy.backend.entities.Encounter;
import com.cerner.devacedemy.backend.entities.Ingredients;
import com.cerner.devacedemy.backend.entities.Patients;
import com.cerner.devacedemy.backend.entities.Prescription;
import com.cerner.devacedemy.backend.entities.User;
import com.cerner.devacedemy.backend.exceptions.AllergyAlreadyExistsException;
import com.cerner.devacedemy.backend.exceptions.IncorrectMedicineDataException;
import com.cerner.devacedemy.backend.exceptions.MedicineAlreadyPrescribedException;
import com.cerner.devacedemy.backend.exceptions.ResourceNotFoundException;
import com.cerner.devacedemy.backend.requests.AddAllergyRequest;
import com.cerner.devacedemy.backend.requests.MedicineDataRequest;
import com.cerner.devacedemy.backend.requests.PrescribeRequest;
import com.cerner.devacedemy.backend.response.DashboardResponse;
import com.cerner.devacedemy.backend.response.MedicineResponse;
import com.cerner.devacedemy.backend.response.PatientDetailsResponse;
import com.cerner.devacedemy.backend.response.PrescriptionResponse;
import com.cerner.devacedemy.backend.services.CustomUserService;

@RestController
@RequestMapping("/api/v1")
@CrossOrigin
public class PatientDetailsController {

	Logger logger = LoggerFactory.getLogger(PatientDetailsController.class);

	@Autowired
	private CustomUserService customUserService;

	/**
	 * GET REST API to get list of patients based on Search Input param i.e. userInput
	 * 
	 * @param userInput
	 * @return List<Patients>
	 */
	@GetMapping("/auth/patients")
	public ResponseEntity<List<Patients>> getPatients(@RequestParam String userInput) {
		logger.trace("Patients endpoint for Patient search API /api/v1/auth/patients?userInput=");
		List<Patients> patients = customUserService.listAll(userInput);
		logger.info("List of patients obtained");
		return new ResponseEntity<List<Patients>>(patients, HttpStatus.OK);
	}

	/**
	 * GET REST API to get Patient details based on patient ID
	 * 
	 * @param id
	 * @return Patients
	 */
	@GetMapping("/auth/patients/{id}")
	public ResponseEntity<PatientDetailsResponse> getPatientById(@PathVariable("id") long id) {
		try {
			logger.trace("Patients endpoint for Patient Details API /api/v1/auth/patients/patientID");
			PatientDetailsResponse patientResponse = new PatientDetailsResponse();
			Patients patient = customUserService.getPatientById(id);
			logger.info("Patient found with given ID " + id);
			List<Allergies> allergies = customUserService.listAllAllergiesForPatient(patient);
			logger.info("Allergies for given Patient obtained");
			patientResponse.setId(patient.getId());
			patientResponse.setFirstName(patient.getFirstName());
			patientResponse.setLastName(patient.getLastName());
			patientResponse.setGender(patient.getGender());
			patientResponse.setAge(patient.getAge());
			patientResponse.setPhoneNumber(patient.getPhoneNumber());
			patientResponse.setEmail(patient.getEmail());
			patientResponse.setCity(patient.getCity());
			patientResponse.setState(patient.getState());
			patientResponse.setMedicalConditions(patient.getMedicalConditions());
			patientResponse.setAllergies(allergies);
			logger.debug(patientResponse.toString());
			return new ResponseEntity<PatientDetailsResponse>(patientResponse, HttpStatus.OK);
		} catch(ResourceNotFoundException e) {
			logger.error("No patient exists with given patient ID");
			return new ResponseEntity<PatientDetailsResponse>(HttpStatus.NOT_FOUND);
		}
	}

	/**
	 * GET REST API to get list of medicines based on Search Input param i.e. userInput. Also passes relevant warnings to indicate
	 * incompatibility between medicine and patients' medical conditions or recorded allergies
	 * 
	 * @param userInput
	 * @param patientId
	 * @return List<MedicineResponse>
	 */
	@GetMapping("/auth/medicines/{patientId}")
	public ResponseEntity<List<MedicineResponse>> getMedicines(@RequestParam String userInput, 
			@PathVariable long patientId) {
		logger.trace("Medicines endpoint for Medicine search API /api/v1/auth/medicines?userInput=");
		List<MedicineResponse> medicineResponseList = customUserService.listAllMedicines(userInput, patientId);
		logger.info("Obtained filtered medicine list for patient");
		for(MedicineResponse medicineResponse: medicineResponseList) {
			logger.info(medicineResponse.toString());
		}
		return new ResponseEntity<List<MedicineResponse>>(medicineResponseList, HttpStatus.OK);
	}

	/**
	 * GET REST API to get list of ingredients based on Search Input param i.e. userInput
	 * 
	 * @param userInput
	 * @return List<Ingredients>
	 */
	@GetMapping("/auth/ingredients")
	public ResponseEntity<List<Ingredients>> getIngredients(@RequestParam String userInput) {
		logger.trace("Ingredients endpoint for Ingredient search API /api/v1/auth/ingredients?userInput=");
		List<Ingredients> ingredients = customUserService.listAllIngredients(userInput);
		logger.info("List of ingredients obtained");
		return new ResponseEntity<List<Ingredients>>(ingredients, HttpStatus.OK);
	}

	/**
	 * POST REST API to add a new Allergy to a patient
	 * 
	 * @param patientId
	 * @param allergyRequest
	 * @param request
	 * @return Allergies
	 */
	@PostMapping("auth/allergies/{patientId}")
	public ResponseEntity<Allergies> addAllergy(@PathVariable long patientId,
			@RequestBody AddAllergyRequest allergyRequest , HttpServletRequest request) {
		logger.debug(allergyRequest.toString());
		if(allergyRequest.verify(patientId)) {
			try {
				logger.info("Incoming add allergy request has been verified");
				logger.trace("Add Allergy endpoint for adding new allergy for patient /api/v1/auth/allergies/patientId");
				User user = (User) customUserService.loadUserByUsername(request.getUserPrincipal().getName());
				logger.info("Physician Obtained from username");
				Patients patient = customUserService.getPatientById(patientId);
				logger.info("Patient Obtained from patientId");
				Encounter encounter;
				if(allergyRequest.getEncounterId() == 0) {
					logger.info("Encounter ID is 0 in allergyRequest");
					logger.info("Creating a new Encounter and adding allergy for a patient");
					encounter = customUserService.createEncounter(user, patient);
				} else {
					long encounterId = allergyRequest.getEncounterId();
					try {
						logger.info("Encounter ID not 0 in allergyRequest");
						encounter = customUserService.getEncounterById(encounterId);
						logger.info("Existing encounter obtained");
					} catch(ResourceNotFoundException e) {
						logger.error("Invalid encounter ID passed in allergyRequest");
						return new ResponseEntity<Allergies>(HttpStatus.BAD_REQUEST);
					}
				}
				try {
					logger.info("Adding a new allergy");
					Allergies newAllergy = customUserService.addNewAllergy(encounter, allergyRequest, patient);
					return new ResponseEntity<Allergies>(newAllergy, HttpStatus.CREATED);
				} catch(AllergyAlreadyExistsException e) {
					logger.error("Allergy already exists for patient");
					return new ResponseEntity<Allergies>(HttpStatus.CONFLICT);
				}
			} catch(ResourceNotFoundException e) {
				logger.error("Invalid username or Patient ID");
				return new ResponseEntity<Allergies>(HttpStatus.BAD_REQUEST);
			}
		} else {
			logger.error("Invalid Request Body has been passed");
			return new ResponseEntity<Allergies>(HttpStatus.BAD_REQUEST);
		}
	}

	/**
	 * GET REST API to get list of most recent patients of logged in user
	 * 
	 * @param userId
	 * @return List<DashboardResponse>
	 */
	@GetMapping("/auth/dashboard")
	public ResponseEntity<List<DashboardResponse>> getRecentPatients(HttpServletRequest request) {
		try {
			logger.trace("Recent Patients for dashboard page /api/v1/auth/dashboard/userId");
			User user = (User) customUserService.loadUserByUsername(request.getUserPrincipal().getName());
			logger.info("Obtained user from userId");
			List<DashboardResponse> recentPatients = customUserService.listRecentPatients(user);
			for(DashboardResponse patient : recentPatients) {
				logger.debug(patient.toString());
			}
			logger.info("Obtained list of recent patients of a physician");
			return new ResponseEntity<List<DashboardResponse>>(recentPatients, HttpStatus.OK);
		} catch(ResourceNotFoundException e) {
			logger.error("User with username not found");
			return new ResponseEntity<List<DashboardResponse>>(HttpStatus.NOT_FOUND);
		} catch(RuntimeException e) {
			logger.warn("User does not have any patients");
			return new ResponseEntity<List<DashboardResponse>>(HttpStatus.NO_CONTENT);
		}
	}

	/**
	 * GET REST API to get a patients' prescription history
	 * 
	 * @param patientId
	 * @return List<PrescriptionResponse>
	 */
	@GetMapping("/auth/prescription/{patientId}")
	public ResponseEntity<List<PrescriptionResponse>> getPrescriptionHistory(@PathVariable long patientId) {
		try {
			logger.trace("Prescription History for a patient /api/v1/auth/prescription/patientId");
			Patients patient = customUserService.getPatientById(patientId);
			List<PrescriptionResponse> prescriptionHistory = customUserService.getPrescriptionHistory(patient);
			for(PrescriptionResponse prescription : prescriptionHistory) {
				logger.debug(prescription.toString());
			}
			logger.info("Obtained Prescription History for Patient");
			return new ResponseEntity<List<PrescriptionResponse>>(prescriptionHistory, HttpStatus.OK);
		} catch(ResourceNotFoundException e) {
			logger.error("Patient with patientId not found");
			return new ResponseEntity<List<PrescriptionResponse>>(HttpStatus.NOT_FOUND);
		} catch(RuntimeException e) {
			logger.warn("Patient has no prescription history");
			return new ResponseEntity<List<PrescriptionResponse>>(HttpStatus.NO_CONTENT);
		}
	}

	/**
	 * POST REST API that allows physician to prescribe medicines to a patient
	 * 
	 * @param patientId
	 * @param prescribeRequest
	 * @param request
	 * @return List<Prescription>
	 */
	@PostMapping("auth/medicines/{patientId}")
	public ResponseEntity<List<Prescription>> prescribeMedicines(@PathVariable long patientId,
			@RequestBody PrescribeRequest prescribeRequest, HttpServletRequest request) {
		logger.debug("Prescribe Request" + prescribeRequest.toString());
		if(prescribeRequest.verify(patientId)) {
			try {
				Encounter encounter;
				logger.trace("Prescribe Medicine endpoint for prescribing medicines for patient /api/v1/auth/medicines/patientId");
				logger.info("Incoming prescribe medicine request has been verified");
				User user = (User) customUserService.loadUserByUsername(request.getUserPrincipal().getName());
				logger.info("Physician Obtained from username");
				Patients patient = customUserService.getPatientById(patientId);
				logger.info("Patient Obtained from patientId");
				if(prescribeRequest.getEncounterId() == 0) {
					logger.info("Encounter ID is 0 in prescribeRequest");
					logger.info("Creating a new Encounter and prescribing medicines for a patient");
					encounter = customUserService.createEncounter(user, patient);
				} else {
					long encounterId = prescribeRequest.getEncounterId();
					try {
						logger.info("Encounter ID not 0 in prescribeRequest");
						encounter = customUserService.getEncounterById(encounterId);
						logger.info("Existing encounter obtained");
						logger.info("Prescribing medicines for a patient in existing encounter");
					} catch(ResourceNotFoundException e) {
						logger.error("Invalid encounter ID passed in prescribeRequest");
						return new ResponseEntity<List<Prescription>>(HttpStatus.BAD_REQUEST);
					}
				}
				List<Prescription> prescriptionList = new ArrayList<Prescription>(); 
				List<MedicineDataRequest> prescribedMedicineList = prescribeRequest.getPrescribedMedicineList();
				List<String> medicineList = new ArrayList<String>();
				for(MedicineDataRequest medicine: prescribedMedicineList) {
					if(!medicineList.contains(medicine.getMedicine().getLabel())) {					
						medicineList.add(medicine.getMedicine().getLabel());
						logger.info("Medicine List: " + medicineList);
					}
				}
				if(medicineList.size() == prescribedMedicineList.size()) {
					for(MedicineDataRequest medicine: prescribedMedicineList) {
						try {
							Prescription prescription = customUserService.prescribeMedicine(encounter, medicine, patient);
							prescriptionList.add(prescription);
						} catch(ResourceNotFoundException e) {
							logger.error("Invalid Medicine ID passed in prescribeRequest");
							return new ResponseEntity<List<Prescription>>(HttpStatus.BAD_REQUEST);
						} catch(MedicineAlreadyPrescribedException e) {
							logger.error("Medicine already prescribed in current encounter");
							return new ResponseEntity<List<Prescription>>(HttpStatus.CONFLICT);
						} catch(IncorrectMedicineDataException e) {
							logger.error("Incorrect Medicine Data passed in request body");
							return new ResponseEntity<List<Prescription>>(HttpStatus.BAD_REQUEST);
						}
					}
					customUserService.savePrescription(prescriptionList);
					return new ResponseEntity<List<Prescription>>(prescriptionList, HttpStatus.CREATED);
				} else {
					logger.error("Same medicine prescribed multiple times");
					return new ResponseEntity<List<Prescription>>(HttpStatus.BAD_REQUEST);
				}
			} catch(ResourceNotFoundException e) {
				logger.error("Invalid username or Patient ID");
				return new ResponseEntity<List<Prescription>>(HttpStatus.BAD_REQUEST);
			}
		} else {
			logger.error("Invalid Request Body has been passed");
			return new ResponseEntity<List<Prescription>>(HttpStatus.BAD_REQUEST);
		}
	}
}
