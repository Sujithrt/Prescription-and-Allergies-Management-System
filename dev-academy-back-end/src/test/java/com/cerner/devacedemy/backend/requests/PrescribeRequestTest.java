package com.cerner.devacedemy.backend.requests;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

class PrescribeRequestTest {

	@Test
	void testVerify() {
		Medicine medicine = new Medicine();
		medicine.setLabel("Dolo");
		medicine.setValue(1);
		List<MedicineDataRequest> prescribedMedicineList = new ArrayList<MedicineDataRequest>();
		MedicineDataRequest medicineDataRequest = new MedicineDataRequest();
		medicineDataRequest.setDose("1 time a day");
		medicineDataRequest.setMedicine(medicine);
		prescribedMedicineList.add(medicineDataRequest);
		long patientId = 1001;
		PrescribeRequest prescribeRequest = new PrescribeRequest();
		prescribeRequest.setEncounterId(1);
		prescribeRequest.setPrescribedMedicineList(prescribedMedicineList);
		assertEquals(true, prescribeRequest.verify(patientId));
	}
	
	@Test
	void testVerifyFail1() {
		Medicine medicine = new Medicine();
		medicine.setLabel("Dolo");
		medicine.setValue(1);
		List<MedicineDataRequest> prescribedMedicineList = new ArrayList<MedicineDataRequest>();
		MedicineDataRequest medicineDataRequest = new MedicineDataRequest();
		medicineDataRequest.setDose("1 time a day");
		medicineDataRequest.setMedicine(medicine);
		prescribedMedicineList.add(medicineDataRequest);
		long patientId = 1001;
		PrescribeRequest prescribeRequest = new PrescribeRequest();
		prescribeRequest.setEncounterId(-1);
		prescribeRequest.setPrescribedMedicineList(prescribedMedicineList);
		assertEquals(false, prescribeRequest.verify(patientId));
	}

	@Test
	void testVerifyFail2() {
		Medicine medicine = new Medicine();
		medicine.setLabel("Dolo");
		medicine.setValue(1);
		List<MedicineDataRequest> prescribedMedicineList = new ArrayList<MedicineDataRequest>();
		MedicineDataRequest medicineDataRequest = new MedicineDataRequest();
		medicineDataRequest.setDose("1 time a day");
		medicineDataRequest.setMedicine(medicine);
		prescribedMedicineList.add(medicineDataRequest);
		long patientId = -1;
		PrescribeRequest prescribeRequest = new PrescribeRequest();
		prescribeRequest.setEncounterId(1);
		prescribeRequest.setPrescribedMedicineList(prescribedMedicineList);
		assertEquals(false, prescribeRequest.verify(patientId));
	}
	
	@Test
	void testVerifyFail3() {
		List<MedicineDataRequest> prescribedMedicineList = new ArrayList<MedicineDataRequest>();
		long patientId = 1001;
		PrescribeRequest prescribeRequest = new PrescribeRequest();
		prescribeRequest.setEncounterId(1);
		prescribeRequest.setPrescribedMedicineList(prescribedMedicineList);
		assertEquals(false, prescribeRequest.verify(patientId));
	}
	
	@Test
	void testVerifyFail4() {
		Medicine medicine = new Medicine();
		medicine.setLabel("");
		medicine.setValue(1);
		List<MedicineDataRequest> prescribedMedicineList = new ArrayList<MedicineDataRequest>();
		MedicineDataRequest medicineDataRequest = new MedicineDataRequest();
		medicineDataRequest.setDose("1 time a day");
		medicineDataRequest.setMedicine(medicine);
		prescribedMedicineList.add(medicineDataRequest);
		long patientId = 1001;
		PrescribeRequest prescribeRequest = new PrescribeRequest();
		prescribeRequest.setEncounterId(1);
		prescribeRequest.setPrescribedMedicineList(prescribedMedicineList);
		assertEquals(false, prescribeRequest.verify(patientId));
	}
	
	@Test
	void testVerifyFail5() {
		Medicine medicine = new Medicine();
		medicine.setLabel("Dolo");
		medicine.setValue(-1);
		List<MedicineDataRequest> prescribedMedicineList = new ArrayList<MedicineDataRequest>();
		MedicineDataRequest medicineDataRequest = new MedicineDataRequest();
		medicineDataRequest.setDose("1 time a day");
		medicineDataRequest.setMedicine(medicine);
		prescribedMedicineList.add(medicineDataRequest);
		long patientId = 1001;
		PrescribeRequest prescribeRequest = new PrescribeRequest();
		prescribeRequest.setEncounterId(1);
		prescribeRequest.setPrescribedMedicineList(prescribedMedicineList);
		assertEquals(false, prescribeRequest.verify(patientId));
	}
	
	@Test
	void testVerifyFail6() {
		Medicine medicine = new Medicine();
		medicine.setLabel("Dolo");
		medicine.setValue(1);
		List<MedicineDataRequest> prescribedMedicineList = new ArrayList<MedicineDataRequest>();
		MedicineDataRequest medicineDataRequest = new MedicineDataRequest();
		medicineDataRequest.setDose("");
		medicineDataRequest.setMedicine(medicine);
		prescribedMedicineList.add(medicineDataRequest);
		long patientId = 1001;
		PrescribeRequest prescribeRequest = new PrescribeRequest();
		prescribeRequest.setEncounterId(1);
		prescribeRequest.setPrescribedMedicineList(prescribedMedicineList);
		assertEquals(false, prescribeRequest.verify(patientId));
	}
}
