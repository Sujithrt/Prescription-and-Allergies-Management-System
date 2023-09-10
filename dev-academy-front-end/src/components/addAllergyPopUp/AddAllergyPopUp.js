import React, { useState } from 'react';
import Modal from 'react-bootstrap/Modal';
import Button from 'react-bootstrap/Button';
import './../addAllergyPopUp/addAllergyPopUp.css';
import Container from 'react-bootstrap/Container';
import Row from 'react-bootstrap/Row';
import Col from 'react-bootstrap/Col';
import AsyncSelect from 'react-select/async';
import Select from 'react-select';
import makeAnimated from 'react-select/animated';
import { retrieveIngredients, addNewAllergy } from '../../api/PnamsService';
import { severityValues, statusValues } from './../../Constants';
import { Alert } from 'react-bootstrap';
import { useLocation, useHistory } from 'react-router-dom';
import TextField from '@mui/material/TextField';

const animatedComponent = makeAnimated();


/**
 * Opens Model Window when User clicks Add Allergy to allow user to add a new allergy
 * 
 */
export default function AddAllergyPopUp(props) {

    const search = useLocation().search;
    const history = useHistory();

    const [modalShow, setModalShow] = useState(false);
    const [name, setName] = useState("");
    const [ingredient, setIngredient] = useState("");
    const [reaction, setReaction] = useState("");
    const [severity, setSeverity] = useState("");
    const [status, setStatus] = useState("");
    const [error, setError] = useState("");

    /**
     * Allows user to type and search for ingredients from database
     * @param {onChange} ingredient 
     */
    const onChange = (ingredient) => {
        setIngredient(ingredient);
    }

    /**
     * If encounterId is present in the query param, function will extract it and return the encounterId
     * @returns
     */
    const getEncounterIdFromQueryParam = () => {
        const encounterId = new URLSearchParams(search).get('encounterId');
        return encounterId;
    }

    /**
     * Allows user to search for a particular ingredient from a list of ingredients obtained from the database
     * @param {onChange} searchText 
     * @returns 
     */
    const loadIngredients = async (searchText) => (
        retrieveIngredients(searchText)
            .then((response) =>
                response.data.map(i => ({ label: i.name, value: i.id }))
            )
            .catch((error) => {
                history.push("/error");
            })
    )

    /**
     * Allows user to add a new allergy for a patient
     */
    const handleAddAllergy = (event) => {
        event.preventDefault();
        if (name.trim() !== "" && ingredient !== "" && reaction.trim() !== "" && severity !== "" && status !== "" &&
            name !== null && ingredient !== null && reaction !== null && severity !== null && status !== null) {
            const patientId = props.patientDetails.id
            if (getEncounterIdFromQueryParam() === null) {
                const allergyData = {
                    encounterId: 0,
                    name: name,
                    ingredient: { id: ingredient.value, name: ingredient.label },
                    reaction: reaction,
                    severity: severity.value,
                    status: status.value
                };
                addNewAllergy(patientId, allergyData)
                    .then((response) => {
                        window.location.search = "encounterId=" + response.data.encounter.id;
                        setModalShow(false);
                        setName("");
                        setIngredient("");
                        setReaction("");
                        setSeverity("");
                        setStatus("");
                        setError("");
                    })
                    .catch((error) => {
                        if (error.response.status === 409) {
                            setError("Allergy already exists for the patient");
                            setName("");
                            setIngredient("");
                            setReaction("");
                            setSeverity("");
                            setStatus("");
                        } else {
                            history.push("/error");
                        }
                    });
            } else {
                const allergyData = {
                    encounterId: getEncounterIdFromQueryParam(),
                    name: name,
                    ingredient: { id: ingredient.value, name: ingredient.label },
                    reaction: reaction,
                    severity: severity.value,
                    status: status.value
                };
                addNewAllergy(patientId, allergyData)
                    .then((response) => {
                        window.location.reload();
                        setModalShow(false);
                        setName("");
                        setIngredient("");
                        setReaction("");
                        setSeverity("");
                        setStatus("");
                        setError("");
                    })
                    .catch((error) => {
                        if (error.response.status === 409) {
                            setError("Allergy already exists for the patient");
                            setName("");
                            setIngredient("");
                            setReaction("");
                            setSeverity("");
                            setStatus("");
                        } else if (error.response.status === 400) {
                            setError("Invalid Encounter ID");
                            setName("");
                            setIngredient("");
                            setReaction("");
                            setSeverity("");
                            setStatus("");
                        } else {
                            history.push("/error");
                        }
                    });
            }
        } else {
            setError("Please enter all details of allergy");
        }
    }

    /**
     * Clears allergy details from the Add Allergy Modal Window
     */
    const clearAllergyDetails = () => {
        setError("");
        setName("");
        setIngredient("");
        setReaction("");
        setSeverity("");
        setStatus("");
    }

    /**
     * Calls clearAllergyDetails() whenever modal window is closed
     */
    React.useEffect(() => {
        clearAllergyDetails();
    }, [modalShow])

    return (
        <>
            <Button data-testid="openAddAllergyModal" variant="primary" onClick={() => setModalShow(true)}>
                Add Allergy
            </Button>
            <Modal
                data-testid="addAllergyModal"
                style={{zIndex: 2000}}
                show={modalShow}
                onHide={() => setModalShow(false)}
                size="md"
                aria-labelledby="contained-modal-title-vcenter"
                centered
            >
                <Modal.Header closeButton>
                    <Modal.Title id="contained-modal-title-vcenter">
                        Add Allergy
                    </Modal.Title>
                </Modal.Header>
                <Modal.Body>
                    <form onSubmit={handleAddAllergy}>
                        <Container>
                            <Row className="justify-content-md-center">
                                <Col md={10}>
                            {!(error === "") && <Alert variant="danger">{error}</Alert>}
                            </Col>
                            </Row>
                            <Row className="justify-content-md-center textfield">
                                <Col md={10}>
                                    <TextField
                                        data-testid="allergyName"
                                        label="Allergy Name" 
                                        variant="standard"
                                        className="updateInput"
                                        value={name}
                                        onChange={event => setName(event.target.value)}
                                    />
                                </Col>
                            </Row>
                            <Row className="justify-content-md-center">
                                <Col md={10}>
                                    <AsyncSelect
                                        data-testid="ingredients"
                                        required
                                        className="search-box"
                                        components={animatedComponent}
                                        value={ingredient}
                                        onChange={onChange}
                                        isClearable={true}
                                        placeholder="Ingredient causing allergy"
                                        loadOptions={loadIngredients}
                                    />
                                </Col>
                            </Row>
                            <Row className="justify-content-md-center textfield">
                                <Col md={10}>
                                    <TextField
                                        data-testid="allergyReaction"
                                        label="Reaction" 
                                        variant="standard"
                                        className="updateInput"
                                        value={reaction}
                                        onChange={event => setReaction(event.target.value)}
                                    />
                                </Col>
                            </Row>
                            <Row className="justify-content-md-center">
                                <Col md={10}>
                                    <Select
                                        required
                                        data-testid="severity"
                                        className="search-box"
                                        components={animatedComponent}
                                        value={severity}
                                        options={severityValues}
                                        onChange={(severity) => setSeverity(severity)}
                                        isClearable={true}
                                        placeholder="Allergy Severity"
                                    />
                                </Col>
                            </Row>
                            <Row className="justify-content-md-center">
                                <Col md={10}>
                                    <Select
                                        required
                                        data-testid="status"
                                        className="search-box"
                                        components={animatedComponent}
                                        value={status}
                                        options={statusValues}
                                        onChange={(status) => setStatus(status)}
                                        isClearable={true}
                                        placeholder="Allergy Status"
                                    />
                                </Col>
                            </Row>
                            <Row className="justify-content-md-center">
                                <Col md={5}>
                                <Button data-testid="addAllergyButton" className="updateInput" onClick={handleAddAllergy}>Add</Button>
                                </Col>
                            </Row>
                        </Container>
                    </form>
                </Modal.Body>
            </Modal>
        </>
    )
}

