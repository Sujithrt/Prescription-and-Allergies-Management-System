package com.cerner.devacedemy.backend.requests;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import com.cerner.devacedemy.backend.entities.Allergies.Severity;
import com.cerner.devacedemy.backend.entities.Allergies.Status;
import com.cerner.devacedemy.backend.entities.Ingredients;

class AddAllergyRequestTest {

	@Test
	void testVerify() {
		long patientId = 1001;
		Ingredients ingredient = new Ingredients();
		ingredient.setId(1);
		ingredient.setName("Paracetamol");
		AddAllergyRequest allergyRequest = new AddAllergyRequest();
		allergyRequest.setName("Paracetamol Allergy");
		allergyRequest.setEncounterId(1);
		allergyRequest.setIngredient(ingredient);
		allergyRequest.setReaction("Tiredness");
		allergyRequest.setSeverity(Severity.MILD);
		allergyRequest.setStatus(Status.ACTIVE);
		assertEquals(true, allergyRequest.verify(patientId));
	}
	
	@Test
	void testVerifyFail1() {
		long patientId = 1001;
		Ingredients ingredient = new Ingredients();
		ingredient.setId(1);
		ingredient.setName("Paracetamol");
		AddAllergyRequest allergyRequest = new AddAllergyRequest();
		allergyRequest.setName("");
		allergyRequest.setEncounterId(1);
		allergyRequest.setIngredient(ingredient);
		allergyRequest.setReaction("Tiredness");
		allergyRequest.setSeverity(Severity.MILD);
		allergyRequest.setStatus(Status.ACTIVE);
		assertEquals(false, allergyRequest.verify(patientId));
	}
	
	@Test
	void testVerifyFail2() {
		long patientId = 1001;
		Ingredients ingredient = new Ingredients();
		ingredient.setId(1);
		ingredient.setName("Paracetamol");
		AddAllergyRequest allergyRequest = new AddAllergyRequest();
		allergyRequest.setName("Paracetamol Allergy");
		allergyRequest.setEncounterId(1);
		allergyRequest.setIngredient(ingredient);
		allergyRequest.setReaction("");
		allergyRequest.setSeverity(Severity.MILD);
		allergyRequest.setStatus(Status.ACTIVE);
		assertEquals(false, allergyRequest.verify(patientId));
	}
	
	@Test
	void testVerifyFail3() {
		long patientId = 1001;
		Ingredients ingredient = new Ingredients();
		ingredient.setId(1);
		ingredient.setName("Paracetamol");
		AddAllergyRequest allergyRequest = new AddAllergyRequest();
		allergyRequest.setName("Paracetamol Allergy");
		allergyRequest.setEncounterId(1);
		allergyRequest.setIngredient(ingredient);
		allergyRequest.setReaction("Tiredness");
		allergyRequest.setSeverity(null);
		allergyRequest.setStatus(Status.ACTIVE);
		assertEquals(false, allergyRequest.verify(patientId));
	}
	
	@Test
	void testVerifyFail4() {
		long patientId = 1001;
		Ingredients ingredient = new Ingredients();
		ingredient.setId(1);
		ingredient.setName("Paracetamol");
		AddAllergyRequest allergyRequest = new AddAllergyRequest();
		allergyRequest.setName("Paracetamol Allergy");
		allergyRequest.setEncounterId(1);
		allergyRequest.setIngredient(ingredient);
		allergyRequest.setReaction("Tiredness");
		allergyRequest.setSeverity(Severity.MILD);
		allergyRequest.setStatus(null);
		assertEquals(false, allergyRequest.verify(patientId));
	}
	
	@Test
	void testVerifyFail5() {
		long patientId = -1;
		Ingredients ingredient = new Ingredients();
		ingredient.setId(1);
		ingredient.setName("Paracetamol");
		AddAllergyRequest allergyRequest = new AddAllergyRequest();
		allergyRequest.setName("Paracetamol Allergy");
		allergyRequest.setEncounterId(1);
		allergyRequest.setIngredient(ingredient);
		allergyRequest.setReaction("Tiredness");
		allergyRequest.setSeverity(Severity.MILD);
		allergyRequest.setStatus(Status.ACTIVE);
		assertEquals(false, allergyRequest.verify(patientId));
	}

	@Test
	void testVerifyFail6() {
		long patientId = 1001;
		Ingredients ingredient = new Ingredients();
		ingredient.setId(-1);
		ingredient.setName("Paracetamol");
		AddAllergyRequest allergyRequest = new AddAllergyRequest();
		allergyRequest.setName("Paracetamol Allergy");
		allergyRequest.setEncounterId(1);
		allergyRequest.setIngredient(ingredient);
		allergyRequest.setReaction("Tiredness");
		allergyRequest.setSeverity(Severity.MILD);
		allergyRequest.setStatus(Status.ACTIVE);
		assertEquals(false, allergyRequest.verify(patientId));
	}
	
	@Test
	void testVerifyFail7() {
		long patientId = 1001;
		Ingredients ingredient = new Ingredients();
		ingredient.setId(1);
		ingredient.setName("");
		AddAllergyRequest allergyRequest = new AddAllergyRequest();
		allergyRequest.setName("Paracetamol Allergy");
		allergyRequest.setEncounterId(1);
		allergyRequest.setIngredient(ingredient);
		allergyRequest.setReaction("Tiredness");
		allergyRequest.setSeverity(Severity.MILD);
		allergyRequest.setStatus(Status.ACTIVE);
		assertEquals(false, allergyRequest.verify(patientId));
	}
	
	@Test
	void testVerifyFail8() {
		long patientId = 1001;
		Ingredients ingredient = new Ingredients();
		ingredient.setId(1);
		ingredient.setName("Paracetamol");
		AddAllergyRequest allergyRequest = new AddAllergyRequest();
		allergyRequest.setName("Paracetamol Allergy");
		allergyRequest.setEncounterId(-1);
		allergyRequest.setIngredient(ingredient);
		allergyRequest.setReaction("Tiredness");
		allergyRequest.setSeverity(Severity.MILD);
		allergyRequest.setStatus(Status.ACTIVE);
		assertEquals(false, allergyRequest.verify(patientId));
	}
}
