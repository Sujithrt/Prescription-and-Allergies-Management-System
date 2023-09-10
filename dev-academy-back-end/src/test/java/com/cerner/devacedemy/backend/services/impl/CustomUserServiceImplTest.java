package com.cerner.devacedemy.backend.services.impl;

import static org.junit.jupiter.api.Assertions.*;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import com.cerner.devacedemy.backend.config.JWTTokenHelper;
import com.cerner.devacedemy.backend.config.RestAuthenticationEntryPoint;
import com.cerner.devacedemy.backend.entities.Allergies.Severity;
import com.cerner.devacedemy.backend.entities.Allergies.Status;
import com.cerner.devacedemy.backend.entities.Allergies;
import com.cerner.devacedemy.backend.entities.Authority;
import com.cerner.devacedemy.backend.entities.Categories;
import com.cerner.devacedemy.backend.entities.Encounter;
import com.cerner.devacedemy.backend.entities.Ingredients;
import com.cerner.devacedemy.backend.entities.MedicalConditions;
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
import com.cerner.devacedemy.backend.requests.Medicine;
import com.cerner.devacedemy.backend.requests.MedicineDataRequest;
import com.cerner.devacedemy.backend.response.DashboardResponse;
import com.cerner.devacedemy.backend.response.IncompatibleAllergiesResponse;
import com.cerner.devacedemy.backend.response.MedicineResponse;
import com.cerner.devacedemy.backend.response.PrescriptionResponse;
import com.cerner.devacedemy.backend.services.CustomUserService;


@RunWith(SpringRunner.class)
@WebMvcTest(value = CustomUserServiceImpl.class)
class CustomUserServiceImplTest {
	
	@MockBean
	JWTTokenHelper jwtTokenHelper;
	
	@MockBean
	private RestAuthenticationEntryPoint restAuthenticationEntryPoint;
	
	@MockBean
	private UserDetailsRepository userDetailsRepository;
	
	@MockBean
	private PatientDetailsRepository patientDetailsRepository;
	
	@MockBean
	private PhysicianDetailsRepository physicianDetailsRepository;
	
	@MockBean
	private MedicineDetailsRepository medicineDetailsRepository;
	
	@MockBean
	private IngredientDetailsRepository ingredientDetailsRepository;
	
	@MockBean
	private AllergiesRepository allergiesRepository;
	
	@MockBean
	private EncountersRepository encountersRepository;
		
	@MockBean
	private PrescriptionRepository prescriptionRepository;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private CustomUserService customUserService;


	@Test
	void testLoadUserByUsername() {
		List<Authority> authorityList = new ArrayList<Authority>();
		Authority mockAuthority = new Authority();
		mockAuthority.setId(1);
		mockAuthority.setRoleCode("USER");
		mockAuthority.setRoleDescription("User Role");
		authorityList.add(mockAuthority);
		User mockUser = new User();
		mockUser.setAuthorities(authorityList);
		mockUser.setEmail("john@email.com");
		mockUser.setEnabled(true);
		mockUser.setId(1);
		mockUser.setPassword(passwordEncoder.encode("password123"));
		mockUser.setRole("Physician");
		mockUser.setUsername("John");
		
		Mockito.when(userDetailsRepository.findByUsername(Mockito.anyString())).thenReturn(mockUser);
		assertEquals(mockUser, customUserService.loadUserByUsername("John"));
	}
	
	@Test
	void testFailLoadUserByUsername() {
		Mockito.when(userDetailsRepository.findByUsername(Mockito.anyString())).thenReturn(null);
		assertThrows(ResourceNotFoundException.class, () -> customUserService.loadUserByUsername("John"));
	}
	
	@Test
	void testRegisterUser() {
		List<Authority> authorityList = new ArrayList<Authority>();
		Authority mockAuthority = new Authority();
		mockAuthority.setId(1);
		mockAuthority.setRoleCode("USER");
		mockAuthority.setRoleDescription("User Role");
		authorityList.add(mockAuthority);
		User mockUser = new User();
		mockUser.setAuthorities(authorityList);
		mockUser.setEmail("john@email.com");
		mockUser.setEnabled(true);
		mockUser.setId(1);
		mockUser.setPassword(passwordEncoder.encode("password123"));
		mockUser.setRole("Physician");
		mockUser.setUsername("John");
		
		Mockito.when(userDetailsRepository.save(Mockito.any())).thenReturn(mockUser);
		
		assertEquals(mockUser, customUserService.registerUser(mockUser));
	}
	
	@Test
	void testInvalidEmailRegisterUser() {
		List<Authority> authorityList = new ArrayList<Authority>();
		Authority mockAuthority = new Authority();
		mockAuthority.setId(1);
		mockAuthority.setRoleCode("USER");
		mockAuthority.setRoleDescription("User Role");
		authorityList.add(mockAuthority);
		User mockUser = new User();
		mockUser.setAuthorities(authorityList);
		mockUser.setEmail("johnemail.com");
		mockUser.setEnabled(true);
		mockUser.setId(1);
		mockUser.setPassword("password123");
		mockUser.setRole("Physician");
		mockUser.setUsername("John");
		
		Mockito.when(userDetailsRepository.save(Mockito.any())).thenReturn(mockUser);
		
		assertThrows(InvalidEmailOrPasswordException.class, () -> customUserService.registerUser(mockUser));
	}
	
	@Test
	void testInvalidPasswordRegisterUser() {
		List<Authority> authorityList = new ArrayList<Authority>();
		Authority mockAuthority = new Authority();
		mockAuthority.setId(1);
		mockAuthority.setRoleCode("USER");
		mockAuthority.setRoleDescription("User Role");
		authorityList.add(mockAuthority);
		User mockUser = new User();
		mockUser.setAuthorities(authorityList);
		mockUser.setEmail("john@email.com");
		mockUser.setEnabled(true);
		mockUser.setId(1);
		mockUser.setPassword("pass");
		mockUser.setRole("Physician");
		mockUser.setUsername("John");
		
		Mockito.when(userDetailsRepository.save(Mockito.any())).thenReturn(mockUser);
		
		assertThrows(InvalidEmailOrPasswordException.class, () -> customUserService.registerUser(mockUser));
	}

	@Test
	void testListAllWithSearchInput() {
		List<Patients> mockPatientList = new ArrayList<Patients>();
		MedicalConditions mockMedicalConditions1 = new MedicalConditions();
		mockMedicalConditions1.setName("Cephalalgia (headache)");
		mockMedicalConditions1.setId(1);
		MedicalConditions mockMedicalConditions2 = new MedicalConditions();
		mockMedicalConditions2.setName("Rhinovirus (common cold)");
		List<MedicalConditions> medConditionsList1 = new ArrayList<MedicalConditions>();
		medConditionsList1.add(mockMedicalConditions1);
		medConditionsList1.add(mockMedicalConditions2);
		mockMedicalConditions2.setId(2);
		Patients patient1 = new Patients();
		patient1.setId(1001);
		patient1.setFirstName("abc");
		patient1.setLastName("def");
		patient1.setAge(21);
		patient1.setCity("Bangalore");
		patient1.setEmail("abc.def@gmail.com");
		patient1.setGender("Male");
		patient1.setState("Karnataka");
		patient1.setPhoneNumber(9876543210L);
		patient1.setMedicalConditions(medConditionsList1);
		
		MedicalConditions mockMedicalConditions3 = new MedicalConditions();
		mockMedicalConditions3.setName("Otitis externa (swimmer's ear)");
		mockMedicalConditions3.setId(3);
		List<MedicalConditions> medConditionsList2 = new ArrayList<MedicalConditions>();
		medConditionsList2.add(mockMedicalConditions1);
		medConditionsList2.add(mockMedicalConditions2);
		Patients patient2 = new Patients();
		patient2.setId(1002);
		patient2.setFirstName("ghi");
		patient2.setLastName("jkl");
		patient2.setAge(25);
		patient2.setCity("Bangalore");
		patient2.setEmail("ghi.jkl@gmail.com");
		patient2.setGender("Male");
		patient2.setState("Karnataka");
		patient2.setPhoneNumber(9876543211L);
		patient2.setMedicalConditions(medConditionsList2);
		
		mockPatientList.add(patient1);
		mockPatientList.add(patient2);
		
		Mockito.when(patientDetailsRepository.findAll(Mockito.anyString())).thenReturn(mockPatientList);
		assertEquals(mockPatientList, customUserService.listAll("la"));
	}
	
	@Test
	void testListAllEmptySearchInput() {
		List<Patients> mockPatientList = new ArrayList<Patients>();
		MedicalConditions mockMedicalConditions1 = new MedicalConditions();
		mockMedicalConditions1.setName("Cephalalgia (headache)");
		mockMedicalConditions1.setId(1);
		MedicalConditions mockMedicalConditions2 = new MedicalConditions();
		mockMedicalConditions2.setName("Rhinovirus (common cold)");
		List<MedicalConditions> medConditionsList1 = new ArrayList<MedicalConditions>();
		medConditionsList1.add(mockMedicalConditions1);
		medConditionsList1.add(mockMedicalConditions2);
		mockMedicalConditions2.setId(2);
		Patients patient1 = new Patients();
		patient1.setId(1001);
		patient1.setFirstName("abc");
		patient1.setLastName("def");
		patient1.setAge(21);
		patient1.setCity("Bangalore");
		patient1.setEmail("abc.def@gmail.com");
		patient1.setGender("Male");
		patient1.setState("Karnataka");
		patient1.setPhoneNumber(9876543210L);
		patient1.setMedicalConditions(medConditionsList1);
		
		MedicalConditions mockMedicalConditions3 = new MedicalConditions();
		mockMedicalConditions3.setName("Otitis externa (swimmer's ear)");
		mockMedicalConditions3.setId(3);
		List<MedicalConditions> medConditionsList2 = new ArrayList<MedicalConditions>();
		medConditionsList2.add(mockMedicalConditions1);
		medConditionsList2.add(mockMedicalConditions2);
		Patients patient2 = new Patients();
		patient2.setId(1002);
		patient2.setFirstName("ghi");
		patient2.setLastName("jkl");
		patient2.setAge(25);
		patient2.setCity("Bangalore");
		patient2.setEmail("ghi.jkl@gmail.com");
		patient2.setGender("Male");
		patient2.setState("Karnataka");
		patient2.setPhoneNumber(9876543211L);
		patient2.setMedicalConditions(medConditionsList2);
		
		mockPatientList.add(patient1);
		mockPatientList.add(patient2);
		
		Mockito.when(patientDetailsRepository.findAll()).thenReturn(mockPatientList);
		assertEquals(mockPatientList, customUserService.listAll(null));
	}

	@Test
	void testGetPatientById() {
		MedicalConditions mockMedicalConditions1 = new MedicalConditions();
		mockMedicalConditions1.setName("Cephalalgia (headache)");
		mockMedicalConditions1.setId(1);
		MedicalConditions mockMedicalConditions2 = new MedicalConditions();
		mockMedicalConditions2.setName("Rhinovirus (common cold)");
		List<MedicalConditions> medConditionsList1 = new ArrayList<MedicalConditions>();
		medConditionsList1.add(mockMedicalConditions1);
		medConditionsList1.add(mockMedicalConditions2);
		mockMedicalConditions2.setId(2);
		Patients patient1 = new Patients();
		patient1.setId(1001);
		patient1.setFirstName("abc");
		patient1.setLastName("def");
		patient1.setAge(21);
		patient1.setCity("Bangalore");
		patient1.setEmail("abc.def@gmail.com");
		patient1.setGender("Male");
		patient1.setState("Karnataka");
		patient1.setPhoneNumber(9876543210L);
		patient1.setMedicalConditions(medConditionsList1);
		
		Mockito.when(patientDetailsRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(patient1));
		
		assertEquals(patient1, customUserService.getPatientById(1001));
	}

	@Test
	void testGetPatientByIdFail() {
		assertThrows(ResourceNotFoundException.class, () -> customUserService.getPatientById(1001));
	}
	
	@Test
	void testGetPhysicianById() {
		User mockUser = new User();
		List<Authority> authorityList=new ArrayList<>();		
		authorityList.add(new Authority("USER","User role"));
		authorityList.add(new Authority("ADMIN","Admin role"));
		mockUser.setId(1);
		mockUser.setUsername("John");
		mockUser.setEnabled(true);
		mockUser.setRole("Physician");
		mockUser.setEmail("john@gmail.com");
		mockUser.setPassword(passwordEncoder.encode("password123"));
		mockUser.setAuthorities(authorityList);
		PhysicianDetails mockPhysician = new PhysicianDetails();
		mockPhysician.setId(1);
		mockPhysician.setFirstName("John");
		mockPhysician.setLastName("Cage");
		mockPhysician.setCity("Bangalore");
		mockPhysician.setDob(new Date());
		mockPhysician.setSpeciality("Cardiologist");
		mockPhysician.setState("Karnataka");
		mockPhysician.setUser(mockUser);
		
		Mockito.when(physicianDetailsRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(mockPhysician));
		
		assertEquals(mockPhysician, customUserService.getPhysicianById(1));
	}
	
	@Test
	void testGetPhysicianByIdFail() {
		assertThrows(ResourceNotFoundException.class, () -> customUserService.getPhysicianById(1));
	}

	@Test
	void testListAllMedicines() {
		List<Medicines> mockMedicineList = new ArrayList<Medicines>();

		List<Ingredients> mockIngredientsList1 = new ArrayList<Ingredients>();
		Ingredients mockIngredient1 = new Ingredients();
		mockIngredient1.setId(1);
		mockIngredient1.setName("Paracetamol");
		mockIngredientsList1.add(mockIngredient1);
		Categories mockCategory1 = new Categories();
		mockCategory1.setId(1);
		mockCategory1.setName("Antipyretics");
		Medicines medicine1 = new Medicines();
		medicine1.setBrandName("Dolo");
		medicine1.setGenericName("Paracetamol");
		medicine1.setId(1);
		medicine1.setCategory(mockCategory1);
		medicine1.setIngredients(mockIngredientsList1);
		
		List<Ingredients> mockIngredientsList2 = new ArrayList<Ingredients>();
		Ingredients mockIngredient2 = new Ingredients();
		mockIngredient2.setId(2);
		mockIngredient2.setName("Acetaminophen");
		mockIngredientsList2.add(mockIngredient2);
		Categories mockCategory2 = new Categories();
		mockCategory2.setId(2);
		mockCategory2.setName("Analgesics");
		Medicines medicine2 = new Medicines();
		medicine2.setBrandName("Tylenol");
		medicine2.setGenericName("Acetaminophen");
		medicine2.setId(2);
		medicine2.setCategory(mockCategory2);
		medicine2.setIngredients(mockIngredientsList2);
		
		List<Ingredients> mockIngredientsList3 = new ArrayList<Ingredients>();
		Ingredients mockIngredient3 = new Ingredients();
		mockIngredient3.setId(3);
		mockIngredient3.setName("Ibuprofen");
		mockIngredientsList3.add(mockIngredient3);
		Categories mockCategory3 = new Categories();
		mockCategory3.setId(3);
		mockCategory3.setName("Antimalarial drugs");
		Medicines medicine3 = new Medicines();
		medicine3.setBrandName("Advil");
		medicine3.setGenericName("Ibuprofen");
		medicine3.setId(3);
		medicine3.setCategory(mockCategory3);
		medicine3.setIngredients(mockIngredientsList3);
		
		List<Ingredients> mockIngredientsList4 = new ArrayList<Ingredients>();
		Ingredients mockIngredient4 = new Ingredients();
		mockIngredient4.setId(4);
		mockIngredient4.setName("Aspirin");
		mockIngredientsList4.add(mockIngredient4);
		Categories mockCategory4 = new Categories();
		mockCategory4.setId(4);
		mockCategory4.setName("Antibiotics");
		Medicines medicine4 = new Medicines();
		medicine4.setBrandName("Bayer Aspirin");
		medicine4.setGenericName("Aspirin");
		medicine4.setId(4);
		medicine4.setCategory(mockCategory4);
		medicine4.setIngredients(mockIngredientsList4);
		
		mockMedicineList.add(medicine1);
		mockMedicineList.add(medicine2);
		mockMedicineList.add(medicine3);
		mockMedicineList.add(medicine4);
		
		Mockito.when(medicineDetailsRepository.findAll(Mockito.anyString())).thenReturn(mockMedicineList);
		
		List<IncompatibleAllergiesResponse> mockAllergyMedicines = new ArrayList<IncompatibleAllergiesResponse>();
		IncompatibleAllergiesResponse mockIncompatibleAllergiesResponse1 = new IncompatibleAllergiesResponse();
		mockIncompatibleAllergiesResponse1.setMedicineId(1);
		mockIncompatibleAllergiesResponse1.setReaction("Tiredness");
		mockIncompatibleAllergiesResponse1.setSeverity(Severity.MILD);
		IncompatibleAllergiesResponse mockIncompatibleAllergiesResponse2 = new IncompatibleAllergiesResponse(2, "Tiredness", Severity.MODERATE);
		mockAllergyMedicines.add(mockIncompatibleAllergiesResponse1);
		mockAllergyMedicines.add(mockIncompatibleAllergiesResponse2);
		
		List<Long> mockMedConditionMedicines = new ArrayList<Long>(Arrays.asList(3L, 4L));
		
		List<MedicineResponse> mockMedicineResponseList = new ArrayList<MedicineResponse>();
		MedicineResponse medResponse1 = new MedicineResponse();
		medResponse1.setBrandName("Dolo");
		medResponse1.setCause("Causes MILD Tiredness in patient");
		medResponse1.setId(1);
		
		MedicineResponse medResponse2 = new MedicineResponse();
		medResponse2.setBrandName("Tylenol");
		medResponse2.setCause("Causes MODERATE Tiredness in patient");
		medResponse2.setId(2);
		
		MedicineResponse medResponse3 = new MedicineResponse();
		medResponse3.setBrandName("Advil");
		medResponse3.setCause("Incompatible with patients' medical conditions");
		medResponse3.setId(3);
		
		MedicineResponse medResponse4 = new MedicineResponse();
		medResponse4.setBrandName("Bayer Aspirin");
		medResponse4.setCause("Incompatible with patients' medical conditions");
		medResponse4.setId(4);
		
		mockMedicineResponseList.add(medResponse1);
		mockMedicineResponseList.add(medResponse2);
		mockMedicineResponseList.add(medResponse3);
		mockMedicineResponseList.add(medResponse4);
		
		Mockito.when(medicineDetailsRepository.findMedicinesIncompatibleWithAllergies(Mockito.anyLong())).thenReturn(mockAllergyMedicines);
		Mockito.when(patientDetailsRepository.findMedicinesIncompatibleWithMedicalConditions(Mockito.anyLong())).thenReturn(mockMedConditionMedicines);

		assertEquals(mockMedicineResponseList.get(0).getBrandName(), customUserService.listAllMedicines("a", 1001).get(0).getBrandName());
		assertEquals(mockMedicineResponseList.get(0).getCause(), customUserService.listAllMedicines("a", 1001).get(0).getCause());
		assertEquals(mockMedicineResponseList.get(0).getId(), customUserService.listAllMedicines("a", 1001).get(0).getId());
		assertEquals(mockMedicineResponseList.get(1).getBrandName(), customUserService.listAllMedicines("a", 1001).get(1).getBrandName());
		assertEquals(mockMedicineResponseList.get(1).getCause(), customUserService.listAllMedicines("a", 1001).get(1).getCause());
		assertEquals(mockMedicineResponseList.get(1).getId(), customUserService.listAllMedicines("a", 1001).get(1).getId());
		assertEquals(mockMedicineResponseList.get(2).getBrandName(), customUserService.listAllMedicines("a", 1001).get(2).getBrandName());
		assertEquals(mockMedicineResponseList.get(2).getCause(), customUserService.listAllMedicines("a", 1001).get(2).getCause());
		assertEquals(mockMedicineResponseList.get(2).getId(), customUserService.listAllMedicines("a", 1001).get(2).getId());
		assertEquals(mockMedicineResponseList.get(3).getBrandName(), customUserService.listAllMedicines("a", 1001).get(3).getBrandName());
		assertEquals(mockMedicineResponseList.get(3).getCause(), customUserService.listAllMedicines("a", 1001).get(3).getCause());
		assertEquals(mockMedicineResponseList.get(3).getId(), customUserService.listAllMedicines("a", 1001).get(3).getId());
	}

	@Test
	void testListAllMedicinesWithoutSearchText() {
		List<Medicines> mockMedicineList = new ArrayList<Medicines>();

		List<Ingredients> mockIngredientsList1 = new ArrayList<Ingredients>();
		Ingredients mockIngredient1 = new Ingredients();
		mockIngredient1.setId(1);
		mockIngredient1.setName("Paracetamol");
		mockIngredientsList1.add(mockIngredient1);
		Categories mockCategory1 = new Categories();
		mockCategory1.setId(1);
		mockCategory1.setName("Antipyretics");
		Medicines medicine1 = new Medicines();
		medicine1.setBrandName("Dolo");
		medicine1.setGenericName("Paracetamol");
		medicine1.setId(1);
		medicine1.setCategory(mockCategory1);
		medicine1.setIngredients(mockIngredientsList1);
		
		List<Ingredients> mockIngredientsList2 = new ArrayList<Ingredients>();
		Ingredients mockIngredient2 = new Ingredients();
		mockIngredient2.setId(2);
		mockIngredient2.setName("Acetaminophen");
		mockIngredientsList2.add(mockIngredient2);
		Categories mockCategory2 = new Categories();
		mockCategory2.setId(2);
		mockCategory2.setName("Analgesics");
		Medicines medicine2 = new Medicines();
		medicine2.setBrandName("Tylenol");
		medicine2.setGenericName("Acetaminophen");
		medicine2.setId(2);
		medicine2.setCategory(mockCategory2);
		medicine2.setIngredients(mockIngredientsList2);
		
		List<Ingredients> mockIngredientsList3 = new ArrayList<Ingredients>();
		Ingredients mockIngredient3 = new Ingredients();
		mockIngredient3.setId(3);
		mockIngredient3.setName("Ibuprofen");
		mockIngredientsList3.add(mockIngredient3);
		Categories mockCategory3 = new Categories();
		mockCategory3.setId(3);
		mockCategory3.setName("Antimalarial drugs");
		Medicines medicine3 = new Medicines();
		medicine3.setBrandName("Advil");
		medicine3.setGenericName("Ibuprofen");
		medicine3.setId(3);
		medicine3.setCategory(mockCategory3);
		medicine3.setIngredients(mockIngredientsList3);
		
		List<Ingredients> mockIngredientsList4 = new ArrayList<Ingredients>();
		Ingredients mockIngredient4 = new Ingredients();
		mockIngredient4.setId(4);
		mockIngredient4.setName("Aspirin");
		mockIngredientsList4.add(mockIngredient4);
		Categories mockCategory4 = new Categories();
		mockCategory4.setId(4);
		mockCategory4.setName("Antibiotics");
		Medicines medicine4 = new Medicines();
		medicine4.setBrandName("Bayer Aspirin");
		medicine4.setGenericName("Aspirin");
		medicine4.setId(4);
		medicine4.setCategory(mockCategory4);
		medicine4.setIngredients(mockIngredientsList4);
		
		mockMedicineList.add(medicine1);
		mockMedicineList.add(medicine2);
		mockMedicineList.add(medicine3);
		mockMedicineList.add(medicine4);
		
		Mockito.when(medicineDetailsRepository.findAll()).thenReturn(mockMedicineList);
		
		List<IncompatibleAllergiesResponse> mockAllergyMedicines = new ArrayList<IncompatibleAllergiesResponse>();
		IncompatibleAllergiesResponse mockIncompatibleAllergiesResponse1 = new IncompatibleAllergiesResponse();
		mockIncompatibleAllergiesResponse1.setMedicineId(1);
		mockIncompatibleAllergiesResponse1.setReaction("Tiredness");
		mockIncompatibleAllergiesResponse1.setSeverity(Severity.MILD);
		IncompatibleAllergiesResponse mockIncompatibleAllergiesResponse2 = new IncompatibleAllergiesResponse();
		mockIncompatibleAllergiesResponse2.setMedicineId(2);
		mockIncompatibleAllergiesResponse2.setReaction("Tiredness");
		mockIncompatibleAllergiesResponse2.setSeverity(Severity.MODERATE);
		mockAllergyMedicines.add(mockIncompatibleAllergiesResponse1);
		mockAllergyMedicines.add(mockIncompatibleAllergiesResponse2);
		
		List<Long> mockMedConditionMedicines = new ArrayList<Long>(Arrays.asList(3L, 4L));
		
		List<MedicineResponse> mockMedicineResponseList = new ArrayList<MedicineResponse>();
		MedicineResponse medResponse1 = new MedicineResponse();
		medResponse1.setBrandName("Dolo");
		medResponse1.setCause("Causes MILD Tiredness in patient");
		medResponse1.setId(1);
		
		MedicineResponse medResponse2 = new MedicineResponse();
		medResponse2.setBrandName("Tylenol");
		medResponse2.setCause("Causes MODERATE Tiredness in patient");
		medResponse2.setId(2);
		
		MedicineResponse medResponse3 = new MedicineResponse();
		medResponse3.setBrandName("Advil");
		medResponse3.setCause("Incompatible with patients' medical conditions");
		medResponse3.setId(3);
		
		MedicineResponse medResponse4 = new MedicineResponse();
		medResponse4.setBrandName("Bayer Aspirin");
		medResponse4.setCause("Incompatible with patients' medical conditions");
		medResponse4.setId(4);
		
		mockMedicineResponseList.add(medResponse1);
		mockMedicineResponseList.add(medResponse2);
		mockMedicineResponseList.add(medResponse3);
		mockMedicineResponseList.add(medResponse4);
		
		Mockito.when(medicineDetailsRepository.findMedicinesIncompatibleWithAllergies(Mockito.anyLong())).thenReturn(mockAllergyMedicines);
		Mockito.when(patientDetailsRepository.findMedicinesIncompatibleWithMedicalConditions(Mockito.anyLong())).thenReturn(mockMedConditionMedicines);

		assertEquals(mockMedicineResponseList.get(0).getBrandName(), customUserService.listAllMedicines(null, 1001).get(0).getBrandName());
		assertEquals(mockMedicineResponseList.get(0).getCause(), customUserService.listAllMedicines(null, 1001).get(0).getCause());
		assertEquals(mockMedicineResponseList.get(0).getId(), customUserService.listAllMedicines(null, 1001).get(0).getId());
		assertEquals(mockMedicineResponseList.get(1).getBrandName(), customUserService.listAllMedicines(null, 1001).get(1).getBrandName());
		assertEquals(mockMedicineResponseList.get(1).getCause(), customUserService.listAllMedicines(null, 1001).get(1).getCause());
		assertEquals(mockMedicineResponseList.get(1).getId(), customUserService.listAllMedicines(null, 1001).get(1).getId());
		assertEquals(mockMedicineResponseList.get(2).getBrandName(), customUserService.listAllMedicines(null, 1001).get(2).getBrandName());
		assertEquals(mockMedicineResponseList.get(2).getCause(), customUserService.listAllMedicines(null, 1001).get(2).getCause());
		assertEquals(mockMedicineResponseList.get(2).getId(), customUserService.listAllMedicines(null, 1001).get(2).getId());
		assertEquals(mockMedicineResponseList.get(3).getBrandName(), customUserService.listAllMedicines(null, 1001).get(3).getBrandName());
		assertEquals(mockMedicineResponseList.get(3).getCause(), customUserService.listAllMedicines(null, 1001).get(3).getCause());
		assertEquals(mockMedicineResponseList.get(3).getId(), customUserService.listAllMedicines(null, 1001).get(3).getId());
}
	
	@Test
	void testListAllIngredients() {
		List<Ingredients> mockIngredientsList = new ArrayList<Ingredients>();
		Ingredients mockIngredient1 = new Ingredients();
		mockIngredient1.setId(1);
		mockIngredient1.setName("Paracetamol");
		Ingredients mockIngredient2 = new Ingredients();
		mockIngredient2.setId(2);
		mockIngredient2.setName("Acetaminophen");
		mockIngredientsList.add(mockIngredient1);
		mockIngredientsList.add(mockIngredient2);
		
		Mockito.when(ingredientDetailsRepository.findAll("a")).thenReturn(mockIngredientsList);
		assertEquals(mockIngredientsList, customUserService.listAllIngredients("a"));
	}
	
	@Test
	void testListAllIngredientsWithEmptySearchInput() {
		List<Ingredients> mockIngredientsList = new ArrayList<Ingredients>();
		Ingredients mockIngredient1 = new Ingredients();
		mockIngredient1.setId(1);
		mockIngredient1.setName("Paracetamol");
		Ingredients mockIngredient2 = new Ingredients();
		mockIngredient2.setId(2);
		mockIngredient2.setName("Acetaminophen");
		mockIngredientsList.add(mockIngredient1);
		mockIngredientsList.add(mockIngredient2);
		
		Mockito.when(ingredientDetailsRepository.findAll()).thenReturn(mockIngredientsList);
		assertEquals(mockIngredientsList, customUserService.listAllIngredients(null));
	}

	@Test
	void testGetEncounterById() {
		List<Authority> authorityList = new ArrayList<Authority>();
		Authority mockAuthority = new Authority();
		mockAuthority.setId(1);
		mockAuthority.setRoleCode("USER");
		mockAuthority.setRoleDescription("User Role");
		authorityList.add(mockAuthority);
		User mockUser = new User();
		mockUser.setAuthorities(authorityList);
		mockUser.setEmail("john@email.com");
		mockUser.setEnabled(true);
		mockUser.setId(1);
		mockUser.setPassword(passwordEncoder.encode("password123"));
		mockUser.setRole("Physician");
		mockUser.setUsername("John");
		
		MedicalConditions mockMedicalConditions1 = new MedicalConditions();
		mockMedicalConditions1.setName("Cephalalgia (headache)");
		mockMedicalConditions1.setId(1);
		List<MedicalConditions> medConditionsList = new ArrayList<MedicalConditions>();
		medConditionsList.add(mockMedicalConditions1);
		Patients mockPatient = new Patients();
		mockPatient.setId(1001);
		mockPatient.setFirstName("abc");
		mockPatient.setLastName("def");
		mockPatient.setAge(21);
		mockPatient.setCity("Bangalore");
		mockPatient.setEmail("abc.def@gmail.com");
		mockPatient.setGender("Male");
		mockPatient.setState("Karnataka");
		mockPatient.setPhoneNumber(9876543210L);
		mockPatient.setMedicalConditions(medConditionsList);
		
		Encounter mockEncounter = new Encounter();
		mockEncounter.setId(1);
		mockEncounter.setDate("2021-09-11");
		mockEncounter.setPatient(mockPatient);
		mockEncounter.setUser(mockUser);
		
		Mockito.when(encountersRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(mockEncounter));
		assertEquals(mockEncounter, customUserService.getEncounterById(1));
	}
	
	@Test
	void testGetEncounterByIdFail() {
		assertThrows(ResourceNotFoundException.class, () -> customUserService.getEncounterById(1));
	}

	@Test
	void testGetIngredientById() {
		Ingredients mockIngredient = new Ingredients();
		mockIngredient.setId(1);
		mockIngredient.setName("Paracetamol");
		
		Mockito.when(ingredientDetailsRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(mockIngredient));
		assertEquals(mockIngredient, customUserService.getIngredientById(1));
	}
	
	@Test
	void testGetIngredientByIdFail() {
		assertThrows(ResourceNotFoundException.class, () -> customUserService.getIngredientById(1));
	}

	@Test
	void testCreateEncounter() {
		List<Authority> authorityList = new ArrayList<Authority>();
		Authority mockAuthority = new Authority();
		mockAuthority.setId(1);
		mockAuthority.setRoleCode("USER");
		mockAuthority.setRoleDescription("User Role");
		authorityList.add(mockAuthority);
		User mockUser = new User();
		mockUser.setAuthorities(authorityList);
		mockUser.setEmail("john@email.com");
		mockUser.setEnabled(true);
		mockUser.setId(1);
		mockUser.setPassword(passwordEncoder.encode("password123"));
		mockUser.setRole("Physician");
		mockUser.setUsername("John");
		
		MedicalConditions mockMedicalConditions1 = new MedicalConditions();
		mockMedicalConditions1.setName("Cephalalgia (headache)");
		mockMedicalConditions1.setId(1);
		List<MedicalConditions> medConditionsList = new ArrayList<MedicalConditions>();
		medConditionsList.add(mockMedicalConditions1);
		Patients mockPatient = new Patients();
		mockPatient.setId(1001);
		mockPatient.setFirstName("abc");
		mockPatient.setLastName("def");
		mockPatient.setAge(21);
		mockPatient.setCity("Bangalore");
		mockPatient.setEmail("abc.def@gmail.com");
		mockPatient.setGender("Male");
		mockPatient.setState("Karnataka");
		mockPatient.setPhoneNumber(9876543210L);
		mockPatient.setMedicalConditions(medConditionsList);
		
		Encounter mockEncounter = new Encounter();
		Date date = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		String formatterDate = formatter.format(date);
		mockEncounter.setDate(formatterDate);
		mockEncounter.setPatient(mockPatient);
		mockEncounter.setUser(mockUser);
		
		assertEquals(mockEncounter, customUserService.createEncounter(mockUser, mockPatient));
	}

	@Test
	void testAddNewAllergy() {
		Mockito.when(allergiesRepository.getNumberOfAllergiesWithGivenName(Mockito.any(), Mockito.any())).thenReturn(0);
		
		List<Authority> authorityList = new ArrayList<Authority>();
		Authority mockAuthority = new Authority();
		mockAuthority.setId(1);
		mockAuthority.setRoleCode("USER");
		mockAuthority.setRoleDescription("User Role");
		authorityList.add(mockAuthority);
		User mockUser = new User();
		mockUser.setAuthorities(authorityList);
		mockUser.setEmail("john@email.com");
		mockUser.setEnabled(true);
		mockUser.setId(1);
		mockUser.setPassword(passwordEncoder.encode("password123"));
		mockUser.setRole("Physician");
		mockUser.setUsername("John");
		
		MedicalConditions mockMedicalConditions1 = new MedicalConditions();
		mockMedicalConditions1.setName("Cephalalgia (headache)");
		mockMedicalConditions1.setId(1);
		List<MedicalConditions> medConditionsList = new ArrayList<MedicalConditions>();
		medConditionsList.add(mockMedicalConditions1);
		Patients mockPatient = new Patients();
		mockPatient.setId(1001);
		mockPatient.setFirstName("abc");
		mockPatient.setLastName("def");
		mockPatient.setAge(21);
		mockPatient.setCity("Bangalore");
		mockPatient.setEmail("abc.def@gmail.com");
		mockPatient.setGender("Male");
		mockPatient.setState("Karnataka");
		mockPatient.setPhoneNumber(9876543210L);
		mockPatient.setMedicalConditions(medConditionsList);
		
		Encounter mockEncounter = new Encounter();
		Date date = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		String formatterDate = formatter.format(date);
		mockEncounter.setId(1);
		mockEncounter.setDate(formatterDate);
		mockEncounter.setPatient(mockPatient);
		mockEncounter.setUser(mockUser);
		
		Allergies mockAllergy = new Allergies();
		Ingredients ingredient = new Ingredients();
		ingredient.setId(1);
		ingredient.setName("Paracetamol");
		mockAllergy.setIngredient(ingredient);
		mockAllergy.setName("Paracetamol Allergy");
		mockAllergy.setPatient(mockPatient);
		mockAllergy.setReaction("Tiredness");
		mockAllergy.setSeverity(Severity.MILD);
		mockAllergy.setStatus(Status.ACTIVE);
		mockAllergy.setEncounter(mockEncounter);
		
		AddAllergyRequest mockAllergyRequest = new AddAllergyRequest();
		mockAllergyRequest.setEncounterId(1);
		mockAllergyRequest.setIngredient(ingredient);
		mockAllergyRequest.setName("Paracetamol Allergy");
		mockAllergyRequest.setReaction("Tiredness");
		mockAllergyRequest.setSeverity(Severity.MILD);
		mockAllergyRequest.setStatus(Status.ACTIVE);
		
		assertEquals(mockAllergy, customUserService.addNewAllergy(mockEncounter, mockAllergyRequest, mockPatient));
	}
	
	@Test
	void testAddNewAllergyFail() {
		Mockito.when(allergiesRepository.getNumberOfAllergiesWithGivenName(Mockito.any(), Mockito.any())).thenReturn(1);
		
		List<Authority> authorityList = new ArrayList<Authority>();
		Authority mockAuthority = new Authority();
		mockAuthority.setId(1);
		mockAuthority.setRoleCode("USER");
		mockAuthority.setRoleDescription("User Role");
		authorityList.add(mockAuthority);
		User mockUser = new User();
		mockUser.setAuthorities(authorityList);
		mockUser.setEmail("john@email.com");
		mockUser.setEnabled(true);
		mockUser.setId(1);
		mockUser.setPassword(passwordEncoder.encode("password123"));
		mockUser.setRole("Physician");
		mockUser.setUsername("John");
		
		MedicalConditions mockMedicalConditions1 = new MedicalConditions();
		mockMedicalConditions1.setName("Cephalalgia (headache)");
		mockMedicalConditions1.setId(1);
		List<MedicalConditions> medConditionsList = new ArrayList<MedicalConditions>();
		medConditionsList.add(mockMedicalConditions1);
		Patients mockPatient = new Patients();
		mockPatient.setId(1001);
		mockPatient.setFirstName("abc");
		mockPatient.setLastName("def");
		mockPatient.setAge(21);
		mockPatient.setCity("Bangalore");
		mockPatient.setEmail("abc.def@gmail.com");
		mockPatient.setGender("Male");
		mockPatient.setState("Karnataka");
		mockPatient.setPhoneNumber(9876543210L);
		mockPatient.setMedicalConditions(medConditionsList);
		
		Encounter mockEncounter = new Encounter();
		Date date = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		String formatterDate = formatter.format(date);
		mockEncounter.setId(1);
		mockEncounter.setDate(formatterDate);
		mockEncounter.setPatient(mockPatient);
		mockEncounter.setUser(mockUser);
		
		Allergies mockAllergy = new Allergies();
		Ingredients ingredient = new Ingredients();
		ingredient.setId(1);
		ingredient.setName("Paracetamol");
		mockAllergy.setIngredient(ingredient);
		mockAllergy.setName("Paracetamol Allergy");
		mockAllergy.setPatient(mockPatient);
		mockAllergy.setReaction("Tiredness");
		mockAllergy.setSeverity(Severity.MILD);
		mockAllergy.setStatus(Status.ACTIVE);
		mockAllergy.setEncounter(mockEncounter);
		
		AddAllergyRequest mockAllergyRequest = new AddAllergyRequest();
		mockAllergyRequest.setEncounterId(1);
		mockAllergyRequest.setIngredient(ingredient);
		mockAllergyRequest.setName("Paracetamol Allergy");
		mockAllergyRequest.setReaction("Tiredness");
		mockAllergyRequest.setSeverity(Severity.MILD);
		mockAllergyRequest.setStatus(Status.ACTIVE);
		
		assertThrows(AllergyAlreadyExistsException.class, () -> customUserService.addNewAllergy(mockEncounter, mockAllergyRequest, mockPatient));
	}

	@Test
	void testListAllAllergiesForPatient() {
		
		List<Authority> authorityList = new ArrayList<Authority>();
		Authority mockAuthority = new Authority();
		mockAuthority.setId(1);
		mockAuthority.setRoleCode("USER");
		mockAuthority.setRoleDescription("User Role");
		authorityList.add(mockAuthority);
		User mockUser = new User();
		mockUser.setAuthorities(authorityList);
		mockUser.setEmail("john@email.com");
		mockUser.setEnabled(true);
		mockUser.setId(1);
		mockUser.setPassword(passwordEncoder.encode("password123"));
		mockUser.setRole("Physician");
		mockUser.setUsername("John");
		
		MedicalConditions mockMedicalConditions1 = new MedicalConditions();
		mockMedicalConditions1.setName("Cephalalgia (headache)");
		mockMedicalConditions1.setId(1);
		List<MedicalConditions> medConditionsList = new ArrayList<MedicalConditions>();
		medConditionsList.add(mockMedicalConditions1);
		Patients mockPatient = new Patients();
		mockPatient.setId(1001);
		mockPatient.setFirstName("abc");
		mockPatient.setLastName("def");
		mockPatient.setAge(21);
		mockPatient.setCity("Bangalore");
		mockPatient.setEmail("abc.def@gmail.com");
		mockPatient.setGender("Male");
		mockPatient.setState("Karnataka");
		mockPatient.setPhoneNumber(9876543210L);
		mockPatient.setMedicalConditions(medConditionsList);
		
		Encounter mockEncounter = new Encounter();
		Date date = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		String formatterDate = formatter.format(date);
		mockEncounter.setId(1);
		mockEncounter.setDate(formatterDate);
		mockEncounter.setPatient(mockPatient);
		mockEncounter.setUser(mockUser);
		
		List<Allergies> mockAllergiesList = new ArrayList<Allergies>();
		
		Allergies mockAllergy1 = new Allergies();
		Ingredients ingredient = new Ingredients();
		ingredient.setId(1);
		ingredient.setName("Paracetamol");
		mockAllergy1.setIngredient(ingredient);
		mockAllergy1.setName("Paracetamol Allergy");
		mockAllergy1.setPatient(mockPatient);
		mockAllergy1.setReaction("Tiredness");
		mockAllergy1.setSeverity(Severity.MILD);
		mockAllergy1.setStatus(Status.ACTIVE);
		mockAllergy1.setEncounter(mockEncounter);
		
		Allergies mockAllergy2 = new Allergies();
		Ingredients ingredient2 = new Ingredients();
		ingredient2.setId(2);
		ingredient2.setName("Ibuprofin");
		mockAllergy2.setIngredient(ingredient2);
		mockAllergy2.setName("Ibuprofin Allergy");
		mockAllergy2.setPatient(mockPatient);
		mockAllergy2.setReaction("Tiredness");
		mockAllergy2.setSeverity(Severity.MILD);
		mockAllergy2.setStatus(Status.ACTIVE);
		mockAllergy2.setEncounter(mockEncounter);
		
		mockAllergiesList.add(mockAllergy1);
		mockAllergiesList.add(mockAllergy2);
		
		Mockito.when(allergiesRepository.getAllergiesForPatient(Mockito.any())).thenReturn(mockAllergiesList);
		assertEquals(mockAllergiesList, customUserService.listAllAllergiesForPatient(mockPatient));
	}

	@Test
	void testListRecentPatients() {
		
		List<Authority> authorityList = new ArrayList<Authority>();
		Authority mockAuthority = new Authority();
		mockAuthority.setId(1);
		mockAuthority.setRoleCode("USER");
		mockAuthority.setRoleDescription("User Role");
		authorityList.add(mockAuthority);
		User mockUser = new User();
		mockUser.setAuthorities(authorityList);
		mockUser.setEmail("john@email.com");
		mockUser.setEnabled(true);
		mockUser.setId(1);
		mockUser.setPassword(passwordEncoder.encode("password123"));
		mockUser.setRole("Physician");
		mockUser.setUsername("John");
		
		List<DashboardResponse> mockRecentPatients = new ArrayList<DashboardResponse>();
		DashboardResponse mockPatient1 = new DashboardResponse();
		mockPatient1.setAge(25);
		mockPatient1.setDate("2021-10-29");
		mockPatient1.setFirstName("abc");
		mockPatient1.setGender("Male");
		mockPatient1.setId(1001);
		mockPatient1.setLastName("def");
		
		DashboardResponse mockPatient2 = new DashboardResponse(1, "ghi", "jkl", 50, "Female", "2021-10-29");
		
		mockRecentPatients.add(mockPatient1);
		mockRecentPatients.add(mockPatient2);
		
		Mockito.when(patientDetailsRepository.recentPatients(Mockito.any(), Mockito.any())).thenReturn(mockRecentPatients);
		assertEquals(mockRecentPatients, customUserService.listRecentPatients(mockUser));
	}
	
	@Test
	void testListRecentPatientsFail() {
		
		List<Authority> authorityList = new ArrayList<Authority>();
		Authority mockAuthority = new Authority();
		mockAuthority.setId(1);
		mockAuthority.setRoleCode("USER");
		mockAuthority.setRoleDescription("User Role");
		authorityList.add(mockAuthority);
		User mockUser = new User();
		mockUser.setAuthorities(authorityList);
		mockUser.setEmail("john@email.com");
		mockUser.setEnabled(true);
		mockUser.setId(1);
		mockUser.setPassword(passwordEncoder.encode("password123"));
		mockUser.setRole("Physician");
		mockUser.setUsername("John");
		
		List<DashboardResponse> mockRecentPatients = new ArrayList<DashboardResponse>();
		
		Mockito.when(patientDetailsRepository.recentPatients(Mockito.any(), Mockito.any())).thenReturn(mockRecentPatients);
		assertThrows(RuntimeException.class, () -> customUserService.listRecentPatients(mockUser));
	}

	@Test
	void testGetPrescriptionHistory() {
		
		MedicalConditions mockMedicalConditions1 = new MedicalConditions();
		mockMedicalConditions1.setName("Cephalalgia (headache)");
		mockMedicalConditions1.setId(1);
		List<MedicalConditions> medConditionsList = new ArrayList<MedicalConditions>();
		medConditionsList.add(mockMedicalConditions1);
		Patients mockPatient = new Patients();
		mockPatient.setId(1001);
		mockPatient.setFirstName("abc");
		mockPatient.setLastName("def");
		mockPatient.setAge(21);
		mockPatient.setCity("Bangalore");
		mockPatient.setEmail("abc.def@gmail.com");
		mockPatient.setGender("Male");
		mockPatient.setState("Karnataka");
		mockPatient.setPhoneNumber(9876543210L);
		mockPatient.setMedicalConditions(medConditionsList);
		
		List<PrescriptionResponse> mockPrescriptionResponseList = new ArrayList<PrescriptionResponse>();
		PrescriptionResponse mockPrescriptionResponse1 = new PrescriptionResponse();
		mockPrescriptionResponse1.setBrandName("Dolo");
		mockPrescriptionResponse1.setDate("2021-10-29");
		mockPrescriptionResponse1.setDosage("1 time a day");
		mockPrescriptionResponse1.setGenericName("Paracetamol");
		
		PrescriptionResponse mockPrescriptionResponse2 = new PrescriptionResponse("Advil", "Ibuprofin", "3 times a day", "2021-10-29");
		
		mockPrescriptionResponseList.add(mockPrescriptionResponse1);
		mockPrescriptionResponseList.add(mockPrescriptionResponse2);
		
		Mockito.when(prescriptionRepository.listPrescriptionHistory(Mockito.any())).thenReturn(mockPrescriptionResponseList);
		assertEquals(mockPrescriptionResponseList, customUserService.getPrescriptionHistory(mockPatient));
	}
	
	@Test
	void testGetPrescriptionHistoryFail() {
		
		MedicalConditions mockMedicalConditions1 = new MedicalConditions();
		mockMedicalConditions1.setName("Cephalalgia (headache)");
		mockMedicalConditions1.setId(1);
		List<MedicalConditions> medConditionsList = new ArrayList<MedicalConditions>();
		medConditionsList.add(mockMedicalConditions1);
		Patients mockPatient = new Patients();
		mockPatient.setId(1001);
		mockPatient.setFirstName("abc");
		mockPatient.setLastName("def");
		mockPatient.setAge(21);
		mockPatient.setCity("Bangalore");
		mockPatient.setEmail("abc.def@gmail.com");
		mockPatient.setGender("Male");
		mockPatient.setState("Karnataka");
		mockPatient.setPhoneNumber(9876543210L);
		mockPatient.setMedicalConditions(medConditionsList);
		
		List<PrescriptionResponse> mockPrescriptionResponseList = new ArrayList<PrescriptionResponse>();
		
		Mockito.when(prescriptionRepository.listPrescriptionHistory(Mockito.any())).thenReturn(mockPrescriptionResponseList);
		assertThrows(RuntimeException.class, () -> customUserService.getPrescriptionHistory(mockPatient));
	}

	@Test
	void testPrescribeMedicine() {
		List<Authority> authorityList = new ArrayList<Authority>();
		Authority mockAuthority = new Authority();
		mockAuthority.setId(1);
		mockAuthority.setRoleCode("USER");
		mockAuthority.setRoleDescription("User Role");
		authorityList.add(mockAuthority);
		User mockUser = new User();
		mockUser.setAuthorities(authorityList);
		mockUser.setEmail("john@email.com");
		mockUser.setEnabled(true);
		mockUser.setId(1);
		mockUser.setPassword(passwordEncoder.encode("password123"));
		mockUser.setRole("Physician");
		mockUser.setUsername("John");
		MedicalConditions mockMedicalConditions1 = new MedicalConditions();
		mockMedicalConditions1.setName("Cephalalgia (headache)");
		mockMedicalConditions1.setId(1);
		List<MedicalConditions> medConditionsList = new ArrayList<MedicalConditions>();
		medConditionsList.add(mockMedicalConditions1);
		Patients mockPatient = new Patients();
		mockPatient.setId(1001);
		mockPatient.setFirstName("abc");
		mockPatient.setLastName("def");
		mockPatient.setAge(21);
		mockPatient.setCity("Bangalore");
		mockPatient.setEmail("abc.def@gmail.com");
		mockPatient.setGender("Male");
		mockPatient.setState("Karnataka");
		mockPatient.setPhoneNumber(9876543210L);
		mockPatient.setMedicalConditions(medConditionsList);
		Encounter mockEncounter = new Encounter();
		Date date = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		String formatterDate = formatter.format(date);
		mockEncounter.setId(1);
		mockEncounter.setDate(formatterDate);
		mockEncounter.setPatient(mockPatient);
		mockEncounter.setUser(mockUser);
		List<Ingredients> mockIngredientsList1 = new ArrayList<Ingredients>();
		Ingredients mockIngredient1 = new Ingredients();
		mockIngredient1.setId(1);
		mockIngredient1.setName("Paracetamol");
		mockIngredientsList1.add(mockIngredient1);
		Categories mockCategory1 = new Categories();
		mockCategory1.setId(1);
		mockCategory1.setName("Antipyretics");
		Medicines medicine1 = new Medicines();
		medicine1.setBrandName("Dolo");
		medicine1.setGenericName("Paracetamol");
		medicine1.setId(1);
		medicine1.setCategory(mockCategory1);
		medicine1.setIngredients(mockIngredientsList1);
		PrescriptionPK mockPk1 = new PrescriptionPK(1,1);
		Prescription mockPrescription1 = new Prescription();
		mockPrescription1.setDosage("1 time a day");
		mockPrescription1.setEncounter(mockEncounter);
		mockPrescription1.setId(mockPk1);
		mockPrescription1.setMedicine(medicine1);
		
		Medicine mockMedicine = new Medicine();
		mockMedicine.setLabel("Dolo");
		mockMedicine.setValue(1);
		
		MedicineDataRequest mockMedicineDataRequest = new MedicineDataRequest();
		mockMedicineDataRequest.setDose("1 time a day");
		mockMedicineDataRequest.setMedicine(mockMedicine);
		
		Mockito.when(medicineDetailsRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(medicine1));
		Mockito.when(prescriptionRepository.isMedicinePrescribed(Mockito.anyLong(), Mockito.anyLong())).thenReturn(0);
		assertEquals(mockPrescription1, customUserService.prescribeMedicine(mockEncounter, mockMedicineDataRequest, mockPatient));
	}
	
	@Test
	void testResourceNotFoundExceptionPrescribeMedicine() {
		List<Authority> authorityList = new ArrayList<Authority>();
		Authority mockAuthority = new Authority();
		mockAuthority.setId(1);
		mockAuthority.setRoleCode("USER");
		mockAuthority.setRoleDescription("User Role");
		authorityList.add(mockAuthority);
		User mockUser = new User();
		mockUser.setAuthorities(authorityList);
		mockUser.setEmail("john@email.com");
		mockUser.setEnabled(true);
		mockUser.setId(1);
		mockUser.setPassword(passwordEncoder.encode("password123"));
		mockUser.setRole("Physician");
		mockUser.setUsername("John");
		MedicalConditions mockMedicalConditions1 = new MedicalConditions();
		mockMedicalConditions1.setName("Cephalalgia (headache)");
		mockMedicalConditions1.setId(1);
		List<MedicalConditions> medConditionsList = new ArrayList<MedicalConditions>();
		medConditionsList.add(mockMedicalConditions1);
		Patients mockPatient = new Patients();
		mockPatient.setId(1001);
		mockPatient.setFirstName("abc");
		mockPatient.setLastName("def");
		mockPatient.setAge(21);
		mockPatient.setCity("Bangalore");
		mockPatient.setEmail("abc.def@gmail.com");
		mockPatient.setGender("Male");
		mockPatient.setState("Karnataka");
		mockPatient.setPhoneNumber(9876543210L);
		mockPatient.setMedicalConditions(medConditionsList);
		Encounter mockEncounter = new Encounter();
		Date date = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		String formatterDate = formatter.format(date);
		mockEncounter.setId(1);
		mockEncounter.setDate(formatterDate);
		mockEncounter.setPatient(mockPatient);
		mockEncounter.setUser(mockUser);
		List<Ingredients> mockIngredientsList1 = new ArrayList<Ingredients>();
		Ingredients mockIngredient1 = new Ingredients();
		mockIngredient1.setId(1);
		mockIngredient1.setName("Paracetamol");
		mockIngredientsList1.add(mockIngredient1);
		Categories mockCategory1 = new Categories();
		mockCategory1.setId(1);
		mockCategory1.setName("Antipyretics");
		Medicines medicine1 = new Medicines();
		medicine1.setBrandName("Dolo");
		medicine1.setGenericName("Paracetamol");
		medicine1.setId(1);
		medicine1.setCategory(mockCategory1);
		medicine1.setIngredients(mockIngredientsList1);
		PrescriptionPK mockPk1 = new PrescriptionPK(1,1);
		Prescription mockPrescription1 = new Prescription();
		mockPrescription1.setDosage("1 time a day");
		mockPrescription1.setEncounter(mockEncounter);
		mockPrescription1.setId(mockPk1);
		mockPrescription1.setMedicine(medicine1);
		
		Medicine mockMedicine = new Medicine();
		mockMedicine.setLabel("Dolo");
		mockMedicine.setValue(1);
		
		MedicineDataRequest mockMedicineDataRequest = new MedicineDataRequest();
		mockMedicineDataRequest.setDose("1 time a day");
		mockMedicineDataRequest.setMedicine(mockMedicine);
		
		assertThrows(ResourceNotFoundException.class, () -> customUserService.prescribeMedicine(mockEncounter, mockMedicineDataRequest, mockPatient));
	}
	
	@Test
	void testMedicineAlreadyPrescribedExceptionPrescribeMedicine() {
		List<Authority> authorityList = new ArrayList<Authority>();
		Authority mockAuthority = new Authority();
		mockAuthority.setId(1);
		mockAuthority.setRoleCode("USER");
		mockAuthority.setRoleDescription("User Role");
		authorityList.add(mockAuthority);
		User mockUser = new User();
		mockUser.setAuthorities(authorityList);
		mockUser.setEmail("john@email.com");
		mockUser.setEnabled(true);
		mockUser.setId(1);
		mockUser.setPassword(passwordEncoder.encode("password123"));
		mockUser.setRole("Physician");
		mockUser.setUsername("John");
		MedicalConditions mockMedicalConditions1 = new MedicalConditions();
		mockMedicalConditions1.setName("Cephalalgia (headache)");
		mockMedicalConditions1.setId(1);
		List<MedicalConditions> medConditionsList = new ArrayList<MedicalConditions>();
		medConditionsList.add(mockMedicalConditions1);
		Patients mockPatient = new Patients();
		mockPatient.setId(1001);
		mockPatient.setFirstName("abc");
		mockPatient.setLastName("def");
		mockPatient.setAge(21);
		mockPatient.setCity("Bangalore");
		mockPatient.setEmail("abc.def@gmail.com");
		mockPatient.setGender("Male");
		mockPatient.setState("Karnataka");
		mockPatient.setPhoneNumber(9876543210L);
		mockPatient.setMedicalConditions(medConditionsList);
		Encounter mockEncounter = new Encounter();
		Date date = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		String formatterDate = formatter.format(date);
		mockEncounter.setId(1);
		mockEncounter.setDate(formatterDate);
		mockEncounter.setPatient(mockPatient);
		mockEncounter.setUser(mockUser);
		List<Ingredients> mockIngredientsList1 = new ArrayList<Ingredients>();
		Ingredients mockIngredient1 = new Ingredients();
		mockIngredient1.setId(1);
		mockIngredient1.setName("Paracetamol");
		mockIngredientsList1.add(mockIngredient1);
		Categories mockCategory1 = new Categories();
		mockCategory1.setId(1);
		mockCategory1.setName("Antipyretics");
		Medicines medicine1 = new Medicines();
		medicine1.setBrandName("Dolo");
		medicine1.setGenericName("Paracetamol");
		medicine1.setId(1);
		medicine1.setCategory(mockCategory1);
		medicine1.setIngredients(mockIngredientsList1);
		PrescriptionPK mockPk1 = new PrescriptionPK(1,1);
		Prescription mockPrescription1 = new Prescription();
		mockPrescription1.setDosage("1 time a day");
		mockPrescription1.setEncounter(mockEncounter);
		mockPrescription1.setId(mockPk1);
		mockPrescription1.setMedicine(medicine1);
		
		Medicine mockMedicine = new Medicine();
		mockMedicine.setLabel("Dolo");
		mockMedicine.setValue(1);
		
		MedicineDataRequest mockMedicineDataRequest = new MedicineDataRequest();
		mockMedicineDataRequest.setDose("1 time a day");
		mockMedicineDataRequest.setMedicine(mockMedicine);
		
		Mockito.when(medicineDetailsRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(medicine1));
		Mockito.when(prescriptionRepository.isMedicinePrescribed(Mockito.anyLong(), Mockito.anyLong())).thenReturn(1);
		assertThrows(MedicineAlreadyPrescribedException.class, () -> customUserService.prescribeMedicine(mockEncounter, mockMedicineDataRequest, mockPatient));
	}
	
	@Test
	void testIncorrectMedicineDataExceptionPrescribeMedicine() {
		List<Authority> authorityList = new ArrayList<Authority>();
		Authority mockAuthority = new Authority();
		mockAuthority.setId(1);
		mockAuthority.setRoleCode("USER");
		mockAuthority.setRoleDescription("User Role");
		authorityList.add(mockAuthority);
		User mockUser = new User();
		mockUser.setAuthorities(authorityList);
		mockUser.setEmail("john@email.com");
		mockUser.setEnabled(true);
		mockUser.setId(1);
		mockUser.setPassword(passwordEncoder.encode("password123"));
		mockUser.setRole("Physician");
		mockUser.setUsername("John");
		MedicalConditions mockMedicalConditions1 = new MedicalConditions();
		mockMedicalConditions1.setName("Cephalalgia (headache)");
		mockMedicalConditions1.setId(1);
		List<MedicalConditions> medConditionsList = new ArrayList<MedicalConditions>();
		medConditionsList.add(mockMedicalConditions1);
		Patients mockPatient = new Patients();
		mockPatient.setId(1001);
		mockPatient.setFirstName("abc");
		mockPatient.setLastName("def");
		mockPatient.setAge(21);
		mockPatient.setCity("Bangalore");
		mockPatient.setEmail("abc.def@gmail.com");
		mockPatient.setGender("Male");
		mockPatient.setState("Karnataka");
		mockPatient.setPhoneNumber(9876543210L);
		mockPatient.setMedicalConditions(medConditionsList);
		Encounter mockEncounter = new Encounter();
		Date date = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		String formatterDate = formatter.format(date);
		mockEncounter.setId(1);
		mockEncounter.setDate(formatterDate);
		mockEncounter.setPatient(mockPatient);
		mockEncounter.setUser(mockUser);
		List<Ingredients> mockIngredientsList1 = new ArrayList<Ingredients>();
		Ingredients mockIngredient1 = new Ingredients();
		mockIngredient1.setId(1);
		mockIngredient1.setName("Paracetamol");
		mockIngredientsList1.add(mockIngredient1);
		Categories mockCategory1 = new Categories();
		mockCategory1.setId(1);
		mockCategory1.setName("Antipyretics");
		Medicines medicine1 = new Medicines();
		medicine1.setBrandName("Advil");
		medicine1.setGenericName("Paracetamol");
		medicine1.setId(1);
		medicine1.setCategory(mockCategory1);
		medicine1.setIngredients(mockIngredientsList1);
		PrescriptionPK mockPk1 = new PrescriptionPK(1,1);
		Prescription mockPrescription1 = new Prescription();
		mockPrescription1.setDosage("1 time a day");
		mockPrescription1.setEncounter(mockEncounter);
		mockPrescription1.setId(mockPk1);
		mockPrescription1.setMedicine(medicine1);
		
		Medicine mockMedicine = new Medicine();
		mockMedicine.setLabel("Dolo");
		mockMedicine.setValue(1);
		
		MedicineDataRequest mockMedicineDataRequest = new MedicineDataRequest();
		mockMedicineDataRequest.setDose("1 time a day");
		mockMedicineDataRequest.setMedicine(mockMedicine);
		
		Mockito.when(medicineDetailsRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(medicine1));
		assertThrows(IncorrectMedicineDataException.class, () -> customUserService.prescribeMedicine(mockEncounter, mockMedicineDataRequest, mockPatient));
	}
	
	@Test
	void testSavePrescription() {
		List<Authority> authorityList = new ArrayList<Authority>();
		Authority mockAuthority = new Authority();
		mockAuthority.setId(1);
		mockAuthority.setRoleCode("USER");
		mockAuthority.setRoleDescription("User Role");
		authorityList.add(mockAuthority);
		User mockUser = new User();
		mockUser.setAuthorities(authorityList);
		mockUser.setEmail("john@email.com");
		mockUser.setEnabled(true);
		mockUser.setId(1);
		mockUser.setPassword(passwordEncoder.encode("password123"));
		mockUser.setRole("Physician");
		mockUser.setUsername("John");
		MedicalConditions mockMedicalConditions1 = new MedicalConditions();
		mockMedicalConditions1.setName("Cephalalgia (headache)");
		mockMedicalConditions1.setId(1);
		List<MedicalConditions> medConditionsList = new ArrayList<MedicalConditions>();
		medConditionsList.add(mockMedicalConditions1);
		Patients mockPatient = new Patients();
		mockPatient.setId(1001);
		mockPatient.setFirstName("abc");
		mockPatient.setLastName("def");
		mockPatient.setAge(21);
		mockPatient.setCity("Bangalore");
		mockPatient.setEmail("abc.def@gmail.com");
		mockPatient.setGender("Male");
		mockPatient.setState("Karnataka");
		mockPatient.setPhoneNumber(9876543210L);
		mockPatient.setMedicalConditions(medConditionsList);
		Encounter mockEncounter = new Encounter();
		Date date = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		String formatterDate = formatter.format(date);
		mockEncounter.setId(1);
		mockEncounter.setDate(formatterDate);
		mockEncounter.setPatient(mockPatient);
		mockEncounter.setUser(mockUser);
		List<Ingredients> mockIngredientsList1 = new ArrayList<Ingredients>();
		Ingredients mockIngredient1 = new Ingredients();
		mockIngredient1.setId(1);
		mockIngredient1.setName("Paracetamol");
		mockIngredientsList1.add(mockIngredient1);
		Categories mockCategory1 = new Categories();
		mockCategory1.setId(1);
		mockCategory1.setName("Antipyretics");
		Medicines medicine1 = new Medicines();
		medicine1.setBrandName("Dolo");
		medicine1.setGenericName("Paracetamol");
		medicine1.setId(1);
		medicine1.setCategory(mockCategory1);
		medicine1.setIngredients(mockIngredientsList1);
		List<Prescription> mockPrescriptionList = new ArrayList<Prescription>();
		PrescriptionPK mockPk1 = new PrescriptionPK(1,1);
		Prescription mockPrescription1 = new Prescription();
		mockPrescription1.setDosage("1 time a day");
		mockPrescription1.setEncounter(mockEncounter);
		mockPrescription1.setId(mockPk1);
		mockPrescription1.setMedicine(medicine1);
		mockPrescriptionList.add(mockPrescription1);
		
		Mockito.when(prescriptionRepository.saveAll(Mockito.anyList())).thenReturn(mockPrescriptionList);
		assertEquals(mockPrescriptionList, customUserService.savePrescription(mockPrescriptionList));
	}

}
