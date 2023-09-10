import React from 'react';
import {
    CalendarToday,
    LocationSearching,
    MailOutline,
    PermIdentity,
} from "@material-ui/icons";
import ContactPhoneOutlinedIcon from '@mui/icons-material/ContactPhoneOutlined';

/**
 * Displays Patients Details to the user
 * @param {patient} props 
 * @returns 
 */
export default function PatientDetails(props) {
    return (
        <>
            <div className="userShowUsername">Registration Number: {props.patient.id}</div>
            <span className="userShowTitle">Patient Details</span>
            <div className="patientDetails">
                <PermIdentity className="userShowIcon" />
                <span className="userShowInfoTitle">{props.patient.firstName} {props.patient.lastName}</span>
            </div>
            <div className="patientDetails">
                <CalendarToday className="userShowIcon" />
                <span className="userShowInfoTitle">{props.patient.age}</span>
            </div>
            <span className="userShowTitle">Contact Details</span>
            <div className="patientDetails">
                <MailOutline className="userShowIcon" />
                <span className="userShowInfoTitle">{props.patient.email}</span>
            </div>
            <div className="patientDetails">
                <ContactPhoneOutlinedIcon className="userShowIcon" />
                <span className="userShowInfoTitle">{props.patient.phoneNumber}</span>
            </div>
            <div className="patientDetails">
                <LocationSearching className="userShowIcon" />
                <span className="userShowInfoTitle">{props.patient.city} | {props.patient.state}</span>
            </div>
        </>
    )
}
