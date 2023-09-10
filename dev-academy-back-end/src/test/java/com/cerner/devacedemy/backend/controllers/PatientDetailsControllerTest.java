package com.cerner.devacedemy.backend.controllers;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.cerner.devacedemy.backend.config.JWTTokenHelper;
import com.cerner.devacedemy.backend.config.RestAuthenticationEntryPoint;
import com.cerner.devacedemy.backend.entities.Allergies;
import com.cerner.devacedemy.backend.entities.Allergies.Severity;
import com.cerner.devacedemy.backend.entities.Allergies.Status;
import com.cerner.devacedemy.backend.exceptions.AllergyAlreadyExistsException;
import com.cerner.devacedemy.backend.exceptions.IncorrectMedicineDataException;
import com.cerner.devacedemy.backend.exceptions.MedicineAlreadyPrescribedException;
import com.cerner.devacedemy.backend.exceptions.ResourceNotFoundException;
import com.cerner.devacedemy.backend.entities.Authority;
import com.cerner.devacedemy.backend.entities.Categories;
import com.cerner.devacedemy.backend.entities.Encounter;
import com.cerner.devacedemy.backend.entities.Ingredients;
import com.cerner.devacedemy.backend.entities.MedicalConditions;
import com.cerner.devacedemy.backend.entities.Medicines;
import com.cerner.devacedemy.backend.entities.Patients;
import com.cerner.devacedemy.backend.entities.Prescription;
import com.cerner.devacedemy.backend.entities.PrescriptionPK;
import com.cerner.devacedemy.backend.entities.User;
import com.cerner.devacedemy.backend.requests.Medicine;
import com.cerner.devacedemy.backend.requests.MedicineDataRequest;
import com.cerner.devacedemy.backend.response.DashboardResponse;
import com.cerner.devacedemy.backend.response.MedicineResponse;
import com.cerner.devacedemy.backend.response.PrescriptionResponse;
import com.cerner.devacedemy.backend.services.CustomUserService;

@RunWith(SpringRunner.class)
@WebMvcTest(value = PatientDetailsController.class)
@AutoConfigureMockMvc
class PatientDetailsControllerTest {

	@Autowired
	private MockMvc mockMvc;
	
	@MockBean
	private CustomUserService customUserService;

	@MockBean
	JWTTokenHelper jwtTokenHelper;
	
	@MockBean
	private RestAuthenticationEntryPoint restAuthenticationEntryPoint;
	
	@MockBean
	private PasswordEncoder passwordEncoder;
	
	@Test
	@WithMockUser
	void testGetPatients() throws Exception {
		
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
		
		Mockito.when(customUserService.listAll(Mockito.anyString())).thenReturn(mockPatientList);
		
		RequestBuilder requestBuilder = MockMvcRequestBuilders
				.get("/api/v1/auth/patients")
				.param("userInput", "abc")
				.accept(MediaType.APPLICATION_JSON);
		
		mockMvc.perform(requestBuilder)
			.andExpect(status().isOk());
	}

	@Test
	@WithMockUser
	void testGetPatientById() throws Exception {
		
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
		mockEncounter.setDate("2021-09-11");
		mockEncounter.setId(1);
		mockEncounter.setPatient(mockPatient);
		mockEncounter.setUser(mockUser);
		
		Ingredients mockIngredient = new Ingredients();
		mockIngredient.setId(1);
		mockIngredient.setName("Paracetamol");
		
		List<Allergies> mockAllergiesList = new ArrayList<Allergies>();
		Allergies mockAllergy1 = new Allergies();
		mockAllergy1.setId(1);
		mockAllergy1.setEncounter(mockEncounter);
		mockAllergy1.setIngredient(mockIngredient);
		mockAllergy1.setName("Paracetamol Allergy");
		mockAllergy1.setPatient(mockPatient);
		mockAllergy1.setReaction("Tiredness");
		mockAllergy1.setSeverity(Severity.MILD);
		mockAllergy1.setStatus(Status.ACTIVE);
		mockAllergiesList.add(mockAllergy1);
		
		Mockito.when(customUserService.getPatientById(Mockito.anyLong())).thenReturn(mockPatient);
		Mockito.when(customUserService.listAllAllergiesForPatient(Mockito.any())).thenReturn(mockAllergiesList);
		
		RequestBuilder requestBuilder = MockMvcRequestBuilders
				.get("/api/v1/auth/patients/1001")
				.accept(MediaType.APPLICATION_JSON);
		
		mockMvc.perform(requestBuilder)
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.id", is((int)mockPatient.getId())))
		.andExpect(jsonPath("$.firstName", is(mockPatient.getFirstName())));
		
	}
	
	@Test
	@WithMockUser
	void testResourceNotFoundExceptionGetPatientById() throws Exception {
		Mockito.when(customUserService.getPatientById(Mockito.anyLong())).thenThrow(new ResourceNotFoundException("Patient", "Id", 1001));
		
		RequestBuilder requestBuilder = MockMvcRequestBuilders
				.get("/api/v1/auth/patients/1001")
				.accept(MediaType.APPLICATION_JSON);
		
		mockMvc.perform(requestBuilder)
		.andExpect(status().isNotFound());
		
	}
	
	@Test
	@WithMockUser
	void testGetMedicines() throws Exception {
		List<MedicineResponse> mockMedicineResponseList = new ArrayList<MedicineResponse>();
		MedicineResponse mockMedicineResponse1 = new MedicineResponse();
		mockMedicineResponse1.setBrandName("Dolo");
		mockMedicineResponse1.setId(1);
		mockMedicineResponse1.setCause("");
		
		MedicineResponse mockMedicineResponse2 = new MedicineResponse();
		mockMedicineResponse2.setBrandName("Tylenol");
		mockMedicineResponse2.setId(2);
		mockMedicineResponse2.setCause("Incompatible with patients' medical conditions");
		
		MedicineResponse mockMedicineResponse3 = new MedicineResponse();
		mockMedicineResponse3.setBrandName("Advil");
		mockMedicineResponse3.setId(3);
		mockMedicineResponse3.setCause("Causes CRITICAL Tiredness in patient");
		
		mockMedicineResponseList.add(mockMedicineResponse1);
		mockMedicineResponseList.add(mockMedicineResponse2);
		mockMedicineResponseList.add(mockMedicineResponse3);
		
		Mockito.when(customUserService.listAllMedicines(Mockito.anyString(), Mockito.anyLong())).thenReturn(mockMedicineResponseList);
		
		RequestBuilder requestBuilder = MockMvcRequestBuilders
				.get("/api/v1/auth/medicines/1001")
				.param("userInput", "d")
				.accept(MediaType.APPLICATION_JSON);
		
		mockMvc.perform(requestBuilder)
			.andExpect(status().isOk());
	}
	
	@Test
	@WithMockUser
	void testGetIngredients() throws Exception {
		
		List<Ingredients> mockIngredientList = new ArrayList<Ingredients>();
		
		Ingredients ingredient1 = new Ingredients();
		ingredient1.setId(1L);
		ingredient1.setName("Paracetamol");
		
		Ingredients ingredient2 = new Ingredients();
		ingredient2.setId(2L);
		ingredient2.setName("Acetaminophen");
		
		mockIngredientList.add(ingredient1);
		mockIngredientList.add(ingredient2);
		
		Mockito.when(customUserService.listAllIngredients(Mockito.anyString())).thenReturn(mockIngredientList);
		
		RequestBuilder requestBuilder = MockMvcRequestBuilders
				.get("/api/v1/auth/ingredients")
				.param("userInput", "p")
				.accept(MediaType.APPLICATION_JSON);
		
		mockMvc.perform(requestBuilder)
			.andExpect(status().isOk());
	}
	
	@Test
	@WithMockUser
	void testAddAllergyWithEncounter0() throws Exception {
		
		Ingredients mockIngredient = new Ingredients();
		mockIngredient.setId(1);
		mockIngredient.setName("Paracetamol");
		
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
		
		String mockAllergyRequest = "{\"ingredient\": {\"id\": 3,\"name\": \"Ibuprofin\"}, \"encounterId\": 0, \"name\": \"Ibuprofin Allergy\", \"reaction\": \"Increased Perspiration\", \"severity\": \"MODERATE\", \"status\": \"ACTIVE\"}";
		
		Encounter mockEncounter = new Encounter();
		mockEncounter.setDate("2021-09-11");
		mockEncounter.setPatient(mockPatient);
		mockEncounter.setUser(mockUser);
		
		Allergies mockAllergy = new Allergies();
		mockAllergy.setEncounter(mockEncounter);
		mockAllergy.setId(1);
		mockAllergy.setIngredient(mockIngredient);
		mockAllergy.setName("Paracetamol Allergy");
		mockAllergy.setReaction("Tiredness");
		mockAllergy.setSeverity(Severity.MILD);
		mockAllergy.setStatus(Status.ACTIVE);
		mockAllergy.setPatient(mockPatient);
		
		Mockito.when(customUserService.loadUserByUsername(Mockito.anyString())).thenReturn(mockUser);
		Mockito.when(customUserService.getPatientById(Mockito.anyLong())).thenReturn(mockPatient);
		Mockito.when(customUserService.createEncounter(Mockito.any(), Mockito.any())).thenReturn(mockEncounter);
		Mockito.when(customUserService.addNewAllergy(Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(mockAllergy);
		
		RequestBuilder requestBuilder = MockMvcRequestBuilders
				.post("/api/v1/auth/allergies/1001")
				.accept(MediaType.APPLICATION_JSON)
				.content(mockAllergyRequest)
				.contentType(MediaType.APPLICATION_JSON);
		
		mockMvc.perform(requestBuilder)
		.andExpect(status().isCreated())
		.andExpect(jsonPath("$.name", is(mockAllergy.getName())));
		
	}
	
	@Test
	@WithMockUser
	void testFailVerifyAddAllergy() throws Exception {
	
		String mockAllergyRequest = "{\"ingredient\": {\"id\": 3,\"name\": \"\"}, \"encounterId\": 1, \"name\": \"Ibuprofin Allergy\", \"reaction\": \"Increased Perspiration\", \"severity\": \"MODERATE\", \"status\": \"ACTIVE\"}";
		
		RequestBuilder requestBuilder = MockMvcRequestBuilders
				.post("/api/v1/auth/allergies/1001")
				.accept(MediaType.APPLICATION_JSON)
				.content(mockAllergyRequest)
				.contentType(MediaType.APPLICATION_JSON);
		
		mockMvc.perform(requestBuilder)
		.andExpect(status().isBadRequest());
		
	}
	
	@Test
	@WithMockUser
	void testFailLoadPatientAddAllergy() throws Exception {
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
	
		String mockAllergyRequest = "{\"ingredient\": {\"id\": 3,\"name\": \"Ibuprofin\"}, \"encounterId\": 1, \"name\": \"Ibuprofin Allergy\", \"reaction\": \"Increased Perspiration\", \"severity\": \"MODERATE\", \"status\": \"ACTIVE\"}";
		
		Mockito.when(customUserService.loadUserByUsername(Mockito.anyString())).thenReturn(mockUser);
		Mockito.when(customUserService.getPatientById(Mockito.anyLong())).thenThrow(new ResourceNotFoundException("Patient", "Id", 1001));
		
		RequestBuilder requestBuilder = MockMvcRequestBuilders
				.post("/api/v1/auth/allergies/1001")
				.accept(MediaType.APPLICATION_JSON)
				.content(mockAllergyRequest)
				.contentType(MediaType.APPLICATION_JSON);
		
		mockMvc.perform(requestBuilder)
		.andExpect(status().isBadRequest());
		
	}
	
	@Test
	@WithMockUser
	void testFailLoadUserAddAllergy() throws Exception {
	
		String mockAllergyRequest = "{\"ingredient\": {\"id\": 3,\"name\": \"Ibuprofin\"}, \"encounterId\": 0, \"name\": \"Ibuprofin Allergy\", \"reaction\": \"Increased Perspiration\", \"severity\": \"MODERATE\", \"status\": \"ACTIVE\"}";
		
		Mockito.when(customUserService.loadUserByUsername(Mockito.anyString())).thenThrow(new ResourceNotFoundException("User","username","username"));
		
		RequestBuilder requestBuilder = MockMvcRequestBuilders
				.post("/api/v1/auth/allergies/1001")
				.accept(MediaType.APPLICATION_JSON)
				.content(mockAllergyRequest)
				.contentType(MediaType.APPLICATION_JSON);
		
		mockMvc.perform(requestBuilder)
		.andExpect(status().isBadRequest());
		
	}
	
	@Test
	@WithMockUser
	void testAddAllergyWithEncounterNot0ButExists() throws Exception {
		
		Ingredients mockIngredient = new Ingredients();
		mockIngredient.setId(1);
		mockIngredient.setName("Paracetamol");
		
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
		
		String mockAllergyRequest = "{\"ingredient\": {\"id\": 3,\"name\": \"Ibuprofin\"}, \"encounterId\": 1, \"name\": \"Ibuprofin Allergy\", \"reaction\": \"Increased Perspiration\", \"severity\": \"MODERATE\", \"status\": \"ACTIVE\"}";
		
		Encounter mockEncounter = new Encounter();
		mockEncounter.setId(1);
		mockEncounter.setDate("2021-09-11");
		mockEncounter.setPatient(mockPatient);
		mockEncounter.setUser(mockUser);
		
		Allergies mockAllergy = new Allergies();
		mockAllergy.setEncounter(mockEncounter);
		mockAllergy.setId(1);
		mockAllergy.setIngredient(mockIngredient);
		mockAllergy.setName("Paracetamol Allergy");
		mockAllergy.setReaction("Tiredness");
		mockAllergy.setSeverity(Severity.MILD);
		mockAllergy.setStatus(Status.ACTIVE);
		mockAllergy.setPatient(mockPatient);
		
		Mockito.when(customUserService.loadUserByUsername(Mockito.anyString())).thenReturn(mockUser);
		Mockito.when(customUserService.getPatientById(Mockito.anyLong())).thenReturn(mockPatient);
		Mockito.when(customUserService.getEncounterById(Mockito.anyLong())).thenReturn(mockEncounter);
		Mockito.when(customUserService.addNewAllergy(Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(mockAllergy);
		
		RequestBuilder requestBuilder = MockMvcRequestBuilders
				.post("/api/v1/auth/allergies/1001")
				.accept(MediaType.APPLICATION_JSON)
				.content(mockAllergyRequest)
				.contentType(MediaType.APPLICATION_JSON);
		
		mockMvc.perform(requestBuilder)
		.andExpect(status().isCreated())
		.andExpect(jsonPath("$.name", is(mockAllergy.getName())));
		
	}
	
	@Test
	@WithMockUser
	void testAddAllergyWithEncounterNot0ButDoesNotExist() throws Exception {
		
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
		
		String mockAllergyRequest = "{\"ingredient\": {\"id\": 3,\"name\": \"Ibuprofin\"}, \"encounterId\": 1, \"name\": \"Ibuprofin Allergy\", \"reaction\": \"Increased Perspiration\", \"severity\": \"MODERATE\", \"status\": \"ACTIVE\"}";
		
		Mockito.when(customUserService.loadUserByUsername(Mockito.anyString())).thenReturn(mockUser);
		Mockito.when(customUserService.getPatientById(Mockito.anyLong())).thenReturn(mockPatient);
		Mockito.when(customUserService.getEncounterById(Mockito.anyLong())).thenThrow(new ResourceNotFoundException("Encounter", "Id", 1));
		
		RequestBuilder requestBuilder = MockMvcRequestBuilders
				.post("/api/v1/auth/allergies/1001")
				.accept(MediaType.APPLICATION_JSON)
				.content(mockAllergyRequest)
				.contentType(MediaType.APPLICATION_JSON);
		
		mockMvc.perform(requestBuilder)
		.andExpect(status().isBadRequest());
		
	}
	
	@Test
	@WithMockUser
	void testAllergyAlreadyExistsExceptionAddAllergy() throws Exception {
		
		Ingredients mockIngredient = new Ingredients();
		mockIngredient.setId(1);
		mockIngredient.setName("Paracetamol");
		
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
		
		String mockAllergyRequest = "{\"ingredient\": {\"id\": 3,\"name\": \"Ibuprofin\"}, \"encounterId\": 1, \"name\": \"Ibuprofin Allergy\", \"reaction\": \"Increased Perspiration\", \"severity\": \"MODERATE\", \"status\": \"ACTIVE\"}";
		
		Encounter mockEncounter = new Encounter();
		mockEncounter.setId(1);
		mockEncounter.setDate("2021-09-11");
		mockEncounter.setPatient(mockPatient);
		mockEncounter.setUser(mockUser);
		
		Mockito.when(customUserService.loadUserByUsername(Mockito.anyString())).thenReturn(mockUser);
		Mockito.when(customUserService.getPatientById(Mockito.anyLong())).thenReturn(mockPatient);
		Mockito.when(customUserService.getEncounterById(Mockito.anyLong())).thenReturn(mockEncounter);
		Mockito.when(customUserService.addNewAllergy(Mockito.any(), Mockito.any(), Mockito.any()))
			.thenThrow(new AllergyAlreadyExistsException("Allergy", "Paracetamol Allergy", mockPatient.getId()));
		
		RequestBuilder requestBuilder = MockMvcRequestBuilders
				.post("/api/v1/auth/allergies/1001")
				.accept(MediaType.APPLICATION_JSON)
				.content(mockAllergyRequest)
				.contentType(MediaType.APPLICATION_JSON);
		
		mockMvc.perform(requestBuilder)
		.andExpect(status().isConflict());
		
	}
	
	@Test
	@WithMockUser
	void testGetRecentPatients() throws Exception {
		
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
		
		Mockito.when(customUserService.loadUserByUsername(Mockito.anyString())).thenReturn(mockUser);
		
		List<DashboardResponse> mockRecentPatients = new ArrayList<DashboardResponse>();
		DashboardResponse mockPatient1 = new DashboardResponse();
		mockPatient1.setAge(40);
		mockPatient1.setDate("2021-10-12");
		mockPatient1.setFirstName("Abc");
		mockPatient1.setGender("Male");
		mockPatient1.setId(1001);
		mockPatient1.setLastName("Def");
		
		DashboardResponse mockPatient2 = new DashboardResponse();
		mockPatient2.setAge(50);
		mockPatient2.setDate("2021-07-11");
		mockPatient2.setFirstName("Ghi");
		mockPatient2.setGender("Female");
		mockPatient2.setId(1002);
		mockPatient2.setLastName("Jkl");
		
		mockRecentPatients.add(mockPatient1);
		mockRecentPatients.add(mockPatient2);
		
		Mockito.when(customUserService.listRecentPatients(Mockito.any())).thenReturn(mockRecentPatients);
		
		RequestBuilder requestBuilder = MockMvcRequestBuilders
				.get("/api/v1/auth/dashboard")
				.accept(MediaType.APPLICATION_JSON);
		
		mockMvc.perform(requestBuilder)
			.andExpect(status().isOk());
		
	}
	
	@Test
	@WithMockUser
	void testResourceNotFoundExceptionGetRecentPatients() throws Exception {	
		Mockito.when(customUserService.loadUserByUsername(Mockito.anyString())).thenThrow(new ResourceNotFoundException("User","username","username"));
		
		RequestBuilder requestBuilder = MockMvcRequestBuilders
				.get("/api/v1/auth/dashboard")
				.accept(MediaType.APPLICATION_JSON);
		
		mockMvc.perform(requestBuilder)
			.andExpect(status().isNotFound());
		
	}
	
	@Test
	@WithMockUser
	void testRuntimeExceptionGetRecentPatients() throws Exception {	
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
		
		Mockito.when(customUserService.loadUserByUsername(Mockito.anyString())).thenReturn(mockUser);
		
		Mockito.when(customUserService.listRecentPatients(Mockito.any())).thenThrow(new RuntimeException("You do not have any patients yet"));
		
		RequestBuilder requestBuilder = MockMvcRequestBuilders
				.get("/api/v1/auth/dashboard")
				.accept(MediaType.APPLICATION_JSON);
		
		mockMvc.perform(requestBuilder)
			.andExpect(status().isNoContent());
		
	}
	
	@Test
	@WithMockUser
	void testGetPrescriptionHistory() throws Exception {
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
		
		List<PrescriptionResponse> mockPrescriptionList = new ArrayList<PrescriptionResponse>();
		
		PrescriptionResponse mockPrescription1 = new PrescriptionResponse();
		mockPrescription1.setBrandName("Dolo");
		mockPrescription1.setGenericName("Paracetamol");
		mockPrescription1.setDosage("4 times a day");
		mockPrescription1.setDate("2021-03-12");
		
		PrescriptionResponse mockPrescription2 = new PrescriptionResponse();
		mockPrescription2.setBrandName("Tylenol");
		mockPrescription2.setGenericName("Acetaminophen");
		mockPrescription2.setDosage("3 times a day");
		mockPrescription2.setDate("2021-03-12");
		
		mockPrescriptionList.add(mockPrescription1);
		mockPrescriptionList.add(mockPrescription2);
		
		Mockito.when(customUserService.getPatientById(Mockito.anyLong())).thenReturn(mockPatient);
		Mockito.when(customUserService.getPrescriptionHistory(Mockito.any())).thenReturn(mockPrescriptionList);
		
		RequestBuilder requestBuilder = MockMvcRequestBuilders
				.get("/api/v1/auth/prescription/1001")
				.accept(MediaType.APPLICATION_JSON);
		
		mockMvc.perform(requestBuilder)
			.andExpect(status().isOk());
	}
	
	@Test
	@WithMockUser
	void testResourceNotFoundExceptionGetPrescriptionHistory() throws Exception {	
		Mockito.when(customUserService.getPatientById(Mockito.anyLong())).thenThrow(new ResourceNotFoundException("Patient", "Id", 1001));
		
		RequestBuilder requestBuilder = MockMvcRequestBuilders
				.get("/api/v1/auth/prescription/1001")
				.accept(MediaType.APPLICATION_JSON);
		
		mockMvc.perform(requestBuilder)
			.andExpect(status().isNotFound());
		
	}
	
	@Test
	@WithMockUser
	void testRuntimeExceptionGetPrescriptionHistory() throws Exception {	
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
		
		Mockito.when(customUserService.getPatientById(Mockito.anyLong())).thenReturn(mockPatient);
		
		Mockito.when(customUserService.getPrescriptionHistory(Mockito.any())).thenThrow(new RuntimeException("Patient does not have any Prescription History"));
		
		RequestBuilder requestBuilder = MockMvcRequestBuilders
				.get("/api/v1/auth/prescription/1001")
				.accept(MediaType.APPLICATION_JSON);
		
		mockMvc.perform(requestBuilder)
			.andExpect(status().isNoContent());
		
	}
	
	@Test
	@WithMockUser
	void testPrescribeMedicinesWithEncounter0() throws Exception {
				
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
		mockEncounter.setDate("2021-09-11");
		mockEncounter.setPatient(mockPatient);
		mockEncounter.setUser(mockUser);
		
		Medicine mockMedicine1 = new Medicine();
		mockMedicine1.setLabel("Dolo");
		mockMedicine1.setValue(1);
		
		List<MedicineDataRequest> prescribedMedicineList = new ArrayList<MedicineDataRequest>();
		
		MedicineDataRequest mockMedicineData1 = new MedicineDataRequest();
		mockMedicineData1.setDose("1 time a day");
		mockMedicineData1.setMedicine(mockMedicine1);
		
		prescribedMedicineList.add(mockMedicineData1);
		
		Categories mockCategory = new Categories();
		mockCategory.setId(1);
		mockCategory.setName("Antipyretics");
		
		List<Ingredients> mockIngredientsList = new ArrayList<Ingredients>();
		
		Ingredients mockIngredient = new Ingredients();
		mockIngredient.setId(1);
		mockIngredient.setName("Paracetamol");
		
		mockIngredientsList.add(mockIngredient);
		
		Medicines mockMedicine = new Medicines();
		mockMedicine.setBrandName("Dolo");
		mockMedicine.setGenericName("Paracetamol");
		mockMedicine.setCategory(mockCategory);
		mockMedicine.setId(1);
		mockMedicine.setIngredients(mockIngredientsList);
		
		PrescriptionPK prescriptionPK = new PrescriptionPK(1, 1);
		
		Prescription mockPrescription = new Prescription();
		mockPrescription.setDosage("1 time a day");
		mockPrescription.setEncounter(mockEncounter);
		mockPrescription.setId(prescriptionPK);
		mockPrescription.setMedicine(mockMedicine);
		
		String mockPrescribeRequest = "{\"encounterId\": 0, \"prescribedMedicineList\": [{\"dose\": \"1 time a day\", \"medicine\": {\"label\": \"Paracetamol\", \"value\": 1}}]}";

		Mockito.when(customUserService.loadUserByUsername(Mockito.any())).thenReturn(mockUser);
		Mockito.when(customUserService.getPatientById(Mockito.anyLong())).thenReturn(mockPatient);
		Mockito.when(customUserService.createEncounter(mockUser, mockPatient)).thenReturn(mockEncounter);
		Mockito.when(customUserService.prescribeMedicine(Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(mockPrescription);
	
		RequestBuilder requestBuilder = MockMvcRequestBuilders
				.post("/api/v1/auth/medicines/1001")
				.accept(MediaType.APPLICATION_JSON)
				.content(mockPrescribeRequest)
				.contentType(MediaType.APPLICATION_JSON);
		
		mockMvc.perform(requestBuilder)
		.andExpect(status().isCreated());
	}
	
	@Test
	@WithMockUser
	void testPrescribeMedicinesWithEncounterNot0ButExists() throws Exception {
				
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
		mockEncounter.setDate("2021-09-11");
		mockEncounter.setId(1);
		mockEncounter.setPatient(mockPatient);
		mockEncounter.setUser(mockUser);
		
		Medicine mockMedicine1 = new Medicine();
		mockMedicine1.setLabel("Dolo");
		mockMedicine1.setValue(1);
		
		List<MedicineDataRequest> prescribedMedicineList = new ArrayList<MedicineDataRequest>();
		
		MedicineDataRequest mockMedicineData1 = new MedicineDataRequest();
		mockMedicineData1.setDose("1 time a day");
		mockMedicineData1.setMedicine(mockMedicine1);
		
		prescribedMedicineList.add(mockMedicineData1);
		
		Categories mockCategory = new Categories();
		mockCategory.setId(1);
		mockCategory.setName("Antipyretics");
		
		List<Ingredients> mockIngredientsList = new ArrayList<Ingredients>();
		
		Ingredients mockIngredient = new Ingredients();
		mockIngredient.setId(1);
		mockIngredient.setName("Paracetamol");
		
		mockIngredientsList.add(mockIngredient);
		
		Medicines mockMedicine = new Medicines();
		mockMedicine.setBrandName("Dolo");
		mockMedicine.setGenericName("Paracetamol");
		mockMedicine.setCategory(mockCategory);
		mockMedicine.setId(1);
		mockMedicine.setIngredients(mockIngredientsList);
		
		PrescriptionPK prescriptionPK = new PrescriptionPK(1, 1);
		
		Prescription mockPrescription = new Prescription();
		mockPrescription.setDosage("1 time a day");
		mockPrescription.setEncounter(mockEncounter);
		mockPrescription.setId(prescriptionPK);
		mockPrescription.setMedicine(mockMedicine);
		
		String mockPrescribeRequest = "{\"encounterId\": 1, \"prescribedMedicineList\": [{\"dose\": \"1 time a day\", \"medicine\": {\"label\": \"Paracetamol\", \"value\": 1}}]}";

		Mockito.when(customUserService.loadUserByUsername(Mockito.any())).thenReturn(mockUser);
		Mockito.when(customUserService.getPatientById(Mockito.anyLong())).thenReturn(mockPatient);
		Mockito.when(customUserService.getEncounterById(Mockito.anyLong())).thenReturn(mockEncounter);
		Mockito.when(customUserService.prescribeMedicine(Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(mockPrescription);
	
		RequestBuilder requestBuilder = MockMvcRequestBuilders
				.post("/api/v1/auth/medicines/1001")
				.accept(MediaType.APPLICATION_JSON)
				.content(mockPrescribeRequest)
				.contentType(MediaType.APPLICATION_JSON);
		
		mockMvc.perform(requestBuilder)
		.andExpect(status().isCreated());
	}
	
	@Test
	@WithMockUser
	void testPrescribeMedicinesWithEncounterNot0ButDoesNotExist() throws Exception {
				
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
		
		String mockPrescribeRequest = "{\"encounterId\": 1, \"prescribedMedicineList\": [{\"dose\": \"1 time a day\", \"medicine\": {\"label\": \"Paracetamol\", \"value\": 1}}]}";

		Mockito.when(customUserService.loadUserByUsername(Mockito.any())).thenReturn(mockUser);
		Mockito.when(customUserService.getPatientById(Mockito.anyLong())).thenReturn(mockPatient);
		Mockito.when(customUserService.getEncounterById(Mockito.anyLong())).thenThrow(new ResourceNotFoundException("Encounter", "Id", 1));
	
		RequestBuilder requestBuilder = MockMvcRequestBuilders
				.post("/api/v1/auth/medicines/1001")
				.accept(MediaType.APPLICATION_JSON)
				.content(mockPrescribeRequest)
				.contentType(MediaType.APPLICATION_JSON);
		
		mockMvc.perform(requestBuilder)
		.andExpect(status().isBadRequest());
	}
	
	@Test
	@WithMockUser
	void testFailLoadPatientPrescribeMedicines() throws Exception {
				
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
		
		
		String mockPrescribeRequest = "{\"encounterId\": 1, \"prescribedMedicineList\": [{\"dose\": \"1 time a day\", \"medicine\": {\"label\": \"Paracetamol\", \"value\": 1}}]}";

		Mockito.when(customUserService.loadUserByUsername(Mockito.any())).thenReturn(mockUser);
		Mockito.when(customUserService.getPatientById(Mockito.anyLong())).thenThrow(new ResourceNotFoundException("Patient", "Id", 1001));
	
		RequestBuilder requestBuilder = MockMvcRequestBuilders
				.post("/api/v1/auth/medicines/1001")
				.accept(MediaType.APPLICATION_JSON)
				.content(mockPrescribeRequest)
				.contentType(MediaType.APPLICATION_JSON);
		
		mockMvc.perform(requestBuilder)
		.andExpect(status().isBadRequest());
	}
	
	@Test
	@WithMockUser
	void testFailLoadUserPrescribeMedicines() throws Exception {
				
		String mockPrescribeRequest = "{\"encounterId\": 1, \"prescribedMedicineList\": [{\"dose\": \"1 time a day\", \"medicine\": {\"label\": \"Paracetamol\", \"value\": 1}}]}";

		Mockito.when(customUserService.loadUserByUsername(Mockito.any())).thenThrow(new ResourceNotFoundException("User", "username", "username"));
	
		RequestBuilder requestBuilder = MockMvcRequestBuilders
				.post("/api/v1/auth/medicines/1001")
				.accept(MediaType.APPLICATION_JSON)
				.content(mockPrescribeRequest)
				.contentType(MediaType.APPLICATION_JSON);
		
		mockMvc.perform(requestBuilder)
		.andExpect(status().isBadRequest());
	}
	
	@Test
	@WithMockUser
	void testFailVerify1PrescribeMedicines() throws Exception {
				
		String mockPrescribeRequest = "{\"encounterId\": -1, \"prescribedMedicineList\": [{\"dose\": \"1 time a day\", \"medicine\": {\"label\": \"Paracetamol\", \"value\": 1}}]}";
	
		RequestBuilder requestBuilder = MockMvcRequestBuilders
				.post("/api/v1/auth/medicines/1001")
				.accept(MediaType.APPLICATION_JSON)
				.content(mockPrescribeRequest)
				.contentType(MediaType.APPLICATION_JSON);
		
		mockMvc.perform(requestBuilder)
		.andExpect(status().isBadRequest());
	}
	
	@Test
	@WithMockUser
	void testFailVerify2PrescribeMedicines() throws Exception {
				
		String mockPrescribeRequest = "{\"encounterId\": 0, \"prescribedMedicineList\": []}";
	
		RequestBuilder requestBuilder = MockMvcRequestBuilders
				.post("/api/v1/auth/medicines/1001")
				.accept(MediaType.APPLICATION_JSON)
				.content(mockPrescribeRequest)
				.contentType(MediaType.APPLICATION_JSON);
		
		mockMvc.perform(requestBuilder)
		.andExpect(status().isBadRequest());
	}
	
	@Test
	@WithMockUser
	void testFailVerify3PrescribeMedicines() throws Exception {
				
		String mockPrescribeRequest = "{\"encounterId\": 0, \"prescribedMedicineList\": [{\"dose\": \"\", \"medicine\": {\"label\": \"Paracetamol\", \"value\": 1}}]}";
	
		RequestBuilder requestBuilder = MockMvcRequestBuilders
				.post("/api/v1/auth/medicines/1001")
				.accept(MediaType.APPLICATION_JSON)
				.content(mockPrescribeRequest)
				.contentType(MediaType.APPLICATION_JSON);
		
		mockMvc.perform(requestBuilder)
		.andExpect(status().isBadRequest());
	}
	
	@Test
	@WithMockUser
	void testFailVerify4PrescribeMedicines() throws Exception {
				
		String mockPrescribeRequest = "{\"encounterId\": 0, \"prescribedMedicineList\": [{\"dose\": \"1 time a day\", \"medicine\": {\"label\": \"\", \"value\": 1}}]}";
	
		RequestBuilder requestBuilder = MockMvcRequestBuilders
				.post("/api/v1/auth/medicines/1001")
				.accept(MediaType.APPLICATION_JSON)
				.content(mockPrescribeRequest)
				.contentType(MediaType.APPLICATION_JSON);
		
		mockMvc.perform(requestBuilder)
		.andExpect(status().isBadRequest());
	}
	
	@Test
	@WithMockUser
	void testResourceNotFoundExceptionPrescribeMedicines() throws Exception {
				
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
		mockEncounter.setDate("2021-09-11");
		mockEncounter.setPatient(mockPatient);
		mockEncounter.setUser(mockUser);
		
		String mockPrescribeRequest = "{\"encounterId\": 0, \"prescribedMedicineList\": [{\"dose\": \"1 time a day\", \"medicine\": {\"label\": \"Paracetamol\", \"value\": 1}}]}";

		Mockito.when(customUserService.loadUserByUsername(Mockito.any())).thenReturn(mockUser);
		Mockito.when(customUserService.getPatientById(Mockito.anyLong())).thenReturn(mockPatient);
		Mockito.when(customUserService.createEncounter(mockUser, mockPatient)).thenReturn(mockEncounter);
		Mockito.when(customUserService.prescribeMedicine(Mockito.any(), Mockito.any(), Mockito.any()))
			.thenThrow(new ResourceNotFoundException("Medicine", "Id", 1));
	
		RequestBuilder requestBuilder = MockMvcRequestBuilders
				.post("/api/v1/auth/medicines/1001")
				.accept(MediaType.APPLICATION_JSON)
				.content(mockPrescribeRequest)
				.contentType(MediaType.APPLICATION_JSON);
		
		mockMvc.perform(requestBuilder)
		.andExpect(status().isBadRequest());
	}
	
	@Test
	@WithMockUser
	void testMedicineAlreadyPrescribedExceptionExceptionPrescribeMedicines() throws Exception {
				
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
		mockEncounter.setDate("2021-09-11");
		mockEncounter.setPatient(mockPatient);
		mockEncounter.setUser(mockUser);
		
		String mockPrescribeRequest = "{\"encounterId\": 0, \"prescribedMedicineList\": [{\"dose\": \"1 time a day\", \"medicine\": {\"label\": \"Paracetamol\", \"value\": 1}}]}";

		Mockito.when(customUserService.loadUserByUsername(Mockito.any())).thenReturn(mockUser);
		Mockito.when(customUserService.getPatientById(Mockito.anyLong())).thenReturn(mockPatient);
		Mockito.when(customUserService.createEncounter(mockUser, mockPatient)).thenReturn(mockEncounter);
		Mockito.when(customUserService.prescribeMedicine(Mockito.any(), Mockito.any(), Mockito.any()))
			.thenThrow(new MedicineAlreadyPrescribedException("Medicine", "Paracetamol", 1));
	
		RequestBuilder requestBuilder = MockMvcRequestBuilders
				.post("/api/v1/auth/medicines/1001")
				.accept(MediaType.APPLICATION_JSON)
				.content(mockPrescribeRequest)
				.contentType(MediaType.APPLICATION_JSON);
		
		mockMvc.perform(requestBuilder)
		.andExpect(status().isConflict());
	}
	
	@Test
	@WithMockUser
	void testIncorrectMedicineDataExceptionPrescribeMedicines() throws Exception {
				
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
		mockEncounter.setDate("2021-09-11");
		mockEncounter.setPatient(mockPatient);
		mockEncounter.setUser(mockUser);
		
		String mockPrescribeRequest = "{\"encounterId\": 0, \"prescribedMedicineList\": [{\"dose\": \"1 time a day\", \"medicine\": {\"label\": \"Paracetamol\", \"value\": 1}}]}";

		Mockito.when(customUserService.loadUserByUsername(Mockito.any())).thenReturn(mockUser);
		Mockito.when(customUserService.getPatientById(Mockito.anyLong())).thenReturn(mockPatient);
		Mockito.when(customUserService.createEncounter(mockUser, mockPatient)).thenReturn(mockEncounter);
		Mockito.when(customUserService.prescribeMedicine(Mockito.any(), Mockito.any(), Mockito.any()))
			.thenThrow(new IncorrectMedicineDataException("Medicine", "Paracetamol", 2));
	
		RequestBuilder requestBuilder = MockMvcRequestBuilders
				.post("/api/v1/auth/medicines/1001")
				.accept(MediaType.APPLICATION_JSON)
				.content(mockPrescribeRequest)
				.contentType(MediaType.APPLICATION_JSON);
		
		mockMvc.perform(requestBuilder)
		.andExpect(status().isBadRequest());
	}
	
	@Test
	@WithMockUser
	void testSameMedicinePrescribed() throws Exception {
				
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
		mockEncounter.setDate("2021-09-11");
		mockEncounter.setPatient(mockPatient);
		mockEncounter.setUser(mockUser);
		
		Medicine mockMedicine1 = new Medicine();
		mockMedicine1.setLabel("Dolo");
		mockMedicine1.setValue(1);
		
		List<MedicineDataRequest> prescribedMedicineList = new ArrayList<MedicineDataRequest>();
		
		MedicineDataRequest mockMedicineData1 = new MedicineDataRequest();
		mockMedicineData1.setDose("1 time a day");
		mockMedicineData1.setMedicine(mockMedicine1);
		
		MedicineDataRequest mockMedicineData2 = new MedicineDataRequest();
		mockMedicineData2.setDose("1 time a day");
		mockMedicineData2.setMedicine(mockMedicine1);
		
		prescribedMedicineList.add(mockMedicineData1);
		prescribedMedicineList.add(mockMedicineData2);
		
		String mockPrescribeRequest = "{\"encounterId\": 0, \"prescribedMedicineList\": [{\"dose\": \"1 time a day\", \"medicine\": {\"label\": \"Dolo\", \"value\": 1}},{\"dose\": \"1 time a day\", \"medicine\": {\"label\": \"Dolo\", \"value\": 1}}]}";

		Mockito.when(customUserService.loadUserByUsername(Mockito.any())).thenReturn(mockUser);
		Mockito.when(customUserService.getPatientById(Mockito.anyLong())).thenReturn(mockPatient);
		Mockito.when(customUserService.createEncounter(mockUser, mockPatient)).thenReturn(mockEncounter);
	
		RequestBuilder requestBuilder = MockMvcRequestBuilders
				.post("/api/v1/auth/medicines/1001")
				.accept(MediaType.APPLICATION_JSON)
				.content(mockPrescribeRequest)
				.contentType(MediaType.APPLICATION_JSON);
		
		mockMvc.perform(requestBuilder)
		.andExpect(status().isBadRequest());
	}
	
}
