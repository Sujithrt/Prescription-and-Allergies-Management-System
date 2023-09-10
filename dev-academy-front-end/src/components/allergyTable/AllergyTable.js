import React from 'react';
import AddAllergyPopUp from '../addAllergyPopUp/AddAllergyPopUp';
import Container from 'react-bootstrap/Container';
import './../../pages/patientProfile/patientProfile.css';
import Alert from 'react-bootstrap/Alert';

/**
 * Displays all allergies of patient in the form of a table
 * @param {Patient} props 
 * @returns 
 */
export default function AllergyTable(props) {
    return (
        <>
            <span className="userUpdateTitle">Allergies</span><br /><br />
            <Container>
                {(props.patient.allergies && props.patient.allergies.length) ?<table className="table">
                    <thead>
                        <tr>
                            <th>Name</th>
                            <th>Caused By</th>
                            <th>Reactions</th>
                            <th>Severity</th>
                            <th>Status</th>
                        </tr>
                    </thead>
                    <tbody>
                        {
                            props.patient.allergies.map(
                                allergy =>
                                    <tr key={allergy.id}>
                                        <td>{allergy.name}</td>
                                        <td>{allergy.ingredient.name}</td>
                                        <td>{allergy.reaction}</td>
                                        <td>{allergy.severity}</td>
                                        <td>{allergy.status}</td>
                                    </tr>
                            )
                        }
                    </tbody>
                </table> : <Alert variant="primary">Patient does not have any recorded allergies</Alert>}
                <AddAllergyPopUp patientDetails={props.patient} />
            </Container>
        </>
    )
}
