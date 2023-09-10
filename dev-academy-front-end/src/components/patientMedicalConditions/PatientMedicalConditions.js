import React from 'react';

/**
 * Diaplays patients' medical conditions if any to the user
 * @param {patient.medicalConditions} props 
 * @returns 
 */
export default function PatientMedicalConditions(props) {
    return (
        <>
            <span className="userShowTitle">Medical Conditions</span>
            <div className="patientDetails">
                <span>
                    {
                        props.medicalConditions.map(
                            condition =>
                                <div className="patientDetails" key={condition.id}>{condition.name}</div>
                        )
                    }
                </span>
            </div>
        </>
    )
}
