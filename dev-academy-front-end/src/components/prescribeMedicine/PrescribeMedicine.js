import React, { useState } from 'react';
import Select, { components } from 'react-select';
import Container from 'react-bootstrap/Container';
import Row from 'react-bootstrap/Row';
import IconButton from '@material-ui/core/IconButton';
import AddIcon from '@material-ui/icons/Add';
import { retrieveMedicines, prescribeMedicine } from '../../api/PnamsService';
import Button from 'react-bootstrap/Button';
import TextField from '@mui/material/TextField';
import { Alert } from 'react-bootstrap';
import './../../pages/patientProfile/patientProfile.css';
import { useLocation, useHistory } from 'react-router-dom';
import ReactTooltip from 'react-tooltip';
import WarningAmberIcon from '@mui/icons-material/WarningAmber';
import _ from 'lodash';

/**
 * Component that allows user to prescribe medicine to a patient
 * 
 * @returns 
 */
export default function PrescribeMedicine(props) {

    const search = useLocation().search;
    const history = useHistory();
    const { Option } = components;
    const [inputMedicineFields, setInputMedicineFields] = useState([
        { medicine: "", dose: "" }
    ]);
    const [error, setError] = useState("");
    const [medicineOptions, setMedicineOptions] = useState([]);

    /**
     * If encounterId is present in the query param, function will extract it and return the encounterId
     * @returns
     */
    const getEncounterIdFromQueryParam = () => {
        const encounterId = new URLSearchParams(search).get('encounterId');
        return encounterId
    }

    /**
     * Handles change in values of Select Medicine Search Bar
     * @param {Prescribe Medicine Form Index} index 
     * @param {onChange} event 
     */
    const handleChangeInput = (index, event) => {
        const values = [...inputMedicineFields];
        if (null !== event) {
            values[index]['medicine'] = event;

        } else {
            values[index]['medicine'] = "";
        }
        setInputMedicineFields(values);
    }

    /**
     * Handles change in values of dose input field
     * @param {Prescribe Medicine Form Index} index 
     * @param {onChange} event 
     */
    const handleChangeDose = (index, event) => {
        const values = [...inputMedicineFields];
        values[index]['dose'] = event.target.value;
        setInputMedicineFields(values);
    }

    /**
     * Adds a new Add Medicine Form which allows user to prescribe more medicines
     * @param {onClick} index 
     */
    const handleAddMedicine = (e) => {
        setInputMedicineFields([...inputMedicineFields, { medicine: "", dose: "" }]);
    }

    /**
     * Allows user to remove fields of Prescribe Medicine Form
     * @param {onClick} index 
     */
    const handleRemoveMedicine = (index) => {
        setError("");
        const values = [...inputMedicineFields];
        values.splice(index, 1);
        setInputMedicineFields(values);
    }

    /**
     * Allows user to prescribe medicine for a patient
     * @param {onClick} event 
     */
    const handlePrescribe = (event) => {
        event.preventDefault();
        const patientId = props.patient.id;
        let flag = 0;
        for (let i = 0; i < inputMedicineFields.length; i++) {
            if (inputMedicineFields[i].medicine === "" || inputMedicineFields[i].medicine.label === "" ||
                inputMedicineFields[i].medicine.value <= 0 || inputMedicineFields[i].dose === "" ||
                inputMedicineFields[i].medicine === null || inputMedicineFields[i].medicine.label === null
                || inputMedicineFields[i].medicine.value === null || inputMedicineFields[i].dose === null) {
                flag = 1;
                break;
            }
        }
        if (flag === 1) {
            setError("Please fill all fields");
        } else {
            setError("");
            if (getEncounterIdFromQueryParam() === null) {
                const medicinesData = {
                    encounterId: 0,
                    prescribedMedicineList: inputMedicineFields
                };
                prescribeMedicine(patientId, medicinesData)
                    .then((response) => {
                        window.location.search = "encounterId=" + response.data[0].encounter.id;
                        setInputMedicineFields([{ medicine: "", dose: "" }]);
                        setError("");
                    })
                    .catch((error) => {
                        if (error.response.status === 400) {
                            setError("Please check your input");
                        } else if (error.response.status === 409) {
                            setError("Medicine already prescribed in current encounter");
                        } else {
                            history.push("/error");
                        }
                    });
            } else {
                const medicinesData = {
                    encounterId: getEncounterIdFromQueryParam(),
                    prescribedMedicineList: inputMedicineFields
                };
                prescribeMedicine(patientId, medicinesData)
                    .then((response) => {
                        window.location.reload();
                        setInputMedicineFields([{ medicine: "", dose: "" }]);
                        setError("");
                    })
                    .catch((error) => {
                        if (error.response.status === 400) {
                            setError("Please check your input");
                        } else if (error.response.status === 409) {
                            setError("Medicine already prescribed in current encounter");
                        } else {
                            history.push("/error");
                        }
                    });
            }
        }
    }

    /**
     * Allows user to search for a medicine and retrieves medicines from database based on search text
     * @param searchText 
     */
    const searchMedicine = (searchText) => {
        _.debounce(loadMedicines, 1000)(searchText);
    }

    /**
     * Loads list of medicines from the database and shows it to the user
     * @param {onChange} searchText 
     * @returns 
     */
    const loadMedicines = async (searchText) => {
        retrieveMedicines(searchText, props.patient.id)
            .then((response) =>
                setMedicineOptions(response.data.map(i => ({ label: i.brandName, value: i.id, cause: i.cause })))
            )
            .catch((error) => {
                if (error.response.status === 500) {
                    history.push("/error");
                }
            })
    }

    /**
     * Customize select search component to shows warning labels for loaded medicines
     * @param props 
     * @returns 
     */
    const customSelectOption = (props) => (
        <Option {...props}>
            <span>{props.data.label}</span>
            <span className="warning" data-tip={props.data.cause}>
                {(props.data.cause) ?
                    <WarningAmberIcon fontSize="small" /> :
                    <></>}
            </span>
            <ReactTooltip place="top" effect="solid" />
        </Option>
    )

    return (
        <>
            <span className="userUpdateTitle">Medicines</span>
            <IconButton
                data-testid="addMedicineField"
                onClick={handleAddMedicine}>
                <AddIcon />
            </IconButton>
            <br />
            <form onSubmit={handlePrescribe}>
                <Container>
                    {!(error === "") && <Alert variant="danger">{error}</Alert>}
                    {
                        inputMedicineFields.map((inputMedicineField, index) => (
                            <Row key={index}>
                                <Select
                                    name="medicines"
                                    data-testid="medicine"
                                    options={medicineOptions}
                                    onChange={(event) => handleChangeInput(index, event)}
                                    value={inputMedicineFields[index].medicine}
                                    onInputChange={searchMedicine}
                                    className="search-bar"
                                    classNamePrefix="select"
                                    components={{ Option: customSelectOption }}
                                    placeholder="Select medicine..."
                                    isClearable={true}
                                />
                                <TextField
                                    data-testid="doseField"
                                    label="Dose"
                                    variant="standard"
                                    className="doseInput"
                                    name="dose"
                                    value={inputMedicineFields[index].dose}
                                    onChange={(event) => handleChangeDose(index, event)}
                                />
                                <div style={{ marginTop: "10px", marginLeft: "10px" }}>
                                    <Button data-testid="deleteMedicineField" variant="danger" onClick={() => handleRemoveMedicine(index)}>Delete</Button>
                                </div>
                            </Row>
                        ))
                    }
                    {!(inputMedicineFields && inputMedicineFields.length) ? <></> : <Button data-testid="prescribeButton" style={{ marginTop: "10px" }} variant="primary" onClick={handlePrescribe}>Prescribe</Button>}
                </Container>
            </form>
        </>
    )
}
