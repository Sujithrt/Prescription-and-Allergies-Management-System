import React, { useState, useEffect } from 'react';
import Button from 'react-bootstrap/Button';
import NavBar from './../../components/navBar/NavBar';
import PrescriptionHistory from '../../components/prescriptionHistory/PriscriptionHistory';
import './../patientProfile/patientProfile.css';
import { retrievePatientDetailsById, getPrescriptionHistory } from '../../api/PnamsService';
import Alert from 'react-bootstrap/Alert';
import AllergyTable from '../../components/allergyTable/AllergyTable';
import PrescribeMedicine from '../../components/prescribeMedicine/PrescribeMedicine';
import PatientMedicalConditions from '../../components/patientMedicalConditions/PatientMedicalConditions';
import PatientDetails from '../../components/patientDetails/PatientDetails';
import { useHistory } from 'react-router-dom';

/**
 * Renders the patient profile page with patient details including prescription history, allergies, 
 * medical conditions, and also allows User to add allergies and prescribe medicines to patient
 * @param {Patient Details} props 
 * @returns 
 */
export default function PatientProfile(props) {

    const history = useHistory();
    const [patient, setPatient] = useState({});
    const [isLoading, setIsLoading] = useState(true);
    const [id] = useState(props.match.params.id);
    const [prescriptionData, setPrescriptionData] = useState([]);
    const [prescriptionError, setPrescriptionError] = useState("");
    const [patientError, setPatientError] = useState("");

    useEffect(() => {
        /**
         * Gets patient details from database based on patient ID
         */
        setTimeout(() => retrievePatientDetailsById(id)
            .then((response) => {
                setPatient(response.data);
                setIsLoading(false);
            })
            .catch((error) => {
                if (error.status === 404) {
                    setPatientError("Invalid patient ID");
                } else {
                    history.push("/error");
                }
            }));

        /**
         * Gets the prescription history for a patient and displays it to the user
         */
        getPrescriptionHistory(id)
            .then((response) => {
                if (response.status === 200) {
                    setPrescriptionData(response.data)
                } else if (response.status === 204) {
                    setPrescriptionError("Patient does not have any Prescription History")
                    setPrescriptionData([])
                }
            })
            .catch((error) => {
                if(error.status === 500) {
                    history.push("/error");
                }
            });
    }, [id, history]);

    return (
        patientError ?
            <>
                <NavBar />
                <Alert variant="danger" className="error">
                    {patientError}
                </Alert>
            </> :
            (
                isLoading ?
                    <>
                        <NavBar />
                        <Alert variant="info" className="error">
                            Loading...
                        </Alert>
                    </> :
                    <>
                        <NavBar />
                        <div className="user">
                            <h1>Patient Information</h1>
                            <div className="userContainer">
                                <div className="userShow">
                                    <PatientDetails patient={patient} />
                                    {(patient.medicalConditions && patient.medicalConditions.length) ?
                                        <PatientMedicalConditions medicalConditions={patient.medicalConditions} /> :
                                        <></>}
                                    <PrescriptionHistory prescriptionData={prescriptionData} error={prescriptionError} /><br />
                                    <Button data-testid="backToDashboard" variant="primary" onClick={() => history.push('/dashboard')}>Back to Dashboard</Button>
                                </div>
                                <div className="userUpdate">
                                    <AllergyTable patient={patient} />
                                    <br />
                                    <PrescribeMedicine patient={patient} />
                                </div>
                            </div>
                        </div>
                    </>
            )
    );
}