import React, { useState } from 'react';
import Modal from 'react-bootstrap/Modal';
import Button from 'react-bootstrap/Button';
import Alert from 'react-bootstrap/Alert';

/**
 * Opens a Model window which shows patient prescription history
 * 
 */
export default function PrescriptionHistory(props) {

    const [modalShow, setModalShow] = useState(false);

    return (
        <>
            <Button data-testid="viewPrescriptionHistoryButton" style={{marginBottom: "10px", marginTop: "10px"}} variant="primary" onClick={() => setModalShow(true)}>
                View Patient Priscription History
            </Button>
            <Modal
                data-testid="hideMenu"
                style={{ zIndex: 2000 }}
                show={modalShow}
                onHide={() => setModalShow(false)}
                size="lg"
                aria-labelledby="contained-modal-title-vcenter"
                centered
            >
                <Modal.Header closeButton>
                    <Modal.Title id="contained-modal-title-vcenter">
                        Prescription History
                    </Modal.Title>
                </Modal.Header>
                { (!props.error) ? <Modal.Body>
                    <table className="table">
                    <thead>
                        <tr>
                            <th>Brand Name</th>
                            <th>Generic Name</th>
                            <th>Dosage</th>
                            <th>Date of Prescription</th>
                        </tr>
                    </thead>
                    <tbody>
                        {
                            props.prescriptionData.map(
                                (prescription, index) =>
                                    <tr key={index}>
                                        <td>{prescription.brandName}</td>
                                        <td>{prescription.genericName}</td>
                                        <td>{prescription.dosage}</td>
                                        <td>{prescription.date}</td>
                                    </tr>
                            )
                        }
                    </tbody>
                    </table>
                </Modal.Body> : <Alert variant="primary">{props.error}</Alert>}
                <Modal.Footer>
                    <Button data-testid="closeModalButton" onClick={() => setModalShow(false)}>Close</Button>
                </Modal.Footer>
            </Modal>
        </>
    )
}

