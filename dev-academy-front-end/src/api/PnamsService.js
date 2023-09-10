import axios from 'axios';
import { getToken } from './AuthenticationService';

/**
 * Retrieves list of patients based on search input in search bar
 * @param {onChange} searchText 
 */
export function retrievePatients(searchText) {
    return axios({
        method: 'GET',
        url: `/api/v1/auth/patients?userInput=${searchText}`,
        headers: {
            'Authorization': 'Bearer ' + getToken()
        }
    });
}

/**
 * Retrieves list of medicines for a patient with appropriate warnings about medicine compatibility with patient medical conditions
 * and recorded allergies
 * @param {onChange} searchText 
 * @param patientId 
 */
export function retrieveMedicines(searchText, patientId) {
    return axios({
        method: 'GET',
        url: `/api/v1/auth/medicines/${patientId}?userInput=${searchText}`,
        headers: {
            'Authorization': 'Bearer ' + getToken()
        }
    });
}

/**
 * Retrieves list of ingredients based on search input in ingredient search bar
 * @param {onChange} searchText 
 */
export function retrieveIngredients(searchText) {
    return axios({
        method: 'GET',
        url: `/api/v1/auth/ingredients?userInput=${searchText}`,
        headers: {
            'Authorization': 'Bearer ' + getToken()
        }
    });
}

/**
 * Retrieves patient details based on patient ID
 * @param id 
 */
export function retrievePatientDetailsById(id) {
    return axios({
        method: 'GET',
        url: `/api/v1/auth/patients/${id}`,
        headers: {
            'Authorization': 'Bearer ' + getToken()
        }
    });
}

/**
 * Allows physician to add a new allergy to a patient
 * @param patientId 
 * @param allergy 
 */
export function addNewAllergy(patientId, allergy) {
    return axios({
        method: 'POST',
        url: `/api/v1/auth/allergies/${patientId}`,
        headers: {
            'Authorization': 'Bearer ' + getToken()
        },
        data: allergy
    });
}

/**
 * Retrieves a list of recent patients for the logged in user
 */
export function getRecentPatientsForUser() {
    return axios({
        method: 'GET',
        url: `/api/v1/auth/dashboard`,
        headers: {
            'Authorization': 'Bearer ' + getToken()
        }
    });
}

/**
 * Retrieves the prescription history for a patient based on patient ID
 * @param patientId  
 */
export function getPrescriptionHistory(patientId) {
    return axios({
        method: 'GET',
        url: `/api/v1/auth/prescription/${patientId}`,
        headers: {
            'Authorization': 'Bearer ' + getToken()
        }
    });
}

/**
 * Allows physician to prescribe medicines for a patient
 * @param patientId 
 * @param medicinesData 
 */
export function prescribeMedicine(patientId, medicinesData) {
    return axios({
        method: 'POST',
        url: `/api/v1/auth/medicines/${patientId}`,
        headers: {
            'Authorization': 'Bearer ' + getToken()
        },
        data: medicinesData
    });
}