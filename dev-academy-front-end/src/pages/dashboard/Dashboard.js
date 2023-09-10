import React, { useState, useEffect } from 'react';
import { Container } from 'react-bootstrap';
import styled from 'styled-components';
import { fetchUserData } from '../../api/AuthenticationService';
import { getRecentPatientsForUser } from './../../api/PnamsService';
import Card from '../../components/card/Card';
import NavBar from '../../components/navBar/NavBar';
import { useHistory } from 'react-router-dom';


const MainWrapper = styled.div`
    padding-top:80px;
    text-align: center;
`;
/**
 * Renders the Dashboard page for the Logged in user with Cards of most recent patients
 * 
 */
const Dashboard = () => {

    const history = useHistory();

    const [data, setData] = useState({});
    const [recentPatients, setRecentPatients] = useState([]);
    const [error, setError] = useState("");

    useEffect(() => {
        /**
         * Fetches logged in users' data
         */
        fetchUserData().then((response) => {
            setData(response.data);
        }).catch((e) => {
            if (e.status === 404) {
                console.log(e)
                sessionStorage.clear();
                history.push('/');
            } else {
                history.push("/error");
            }
        });

        /**
         * Fetches a list of recent patients of the user
         */
        getRecentPatientsForUser(sessionStorage.getItem('USER_ID'))
            .then((response) => {
                if (response.status === 204) {
                    setError("You do not have any patients")
                } else {
                    setRecentPatients(response.data)
                }
            })
            .catch((error) => {
                if (error.status === 404) {
                    sessionStorage.clear();
                    history.push('/');
                } else {
                    history.push("/error");
                }
            });
    }, [history]);

    return (
        <>
            <NavBar />
            <Container>
                <MainWrapper>
                    <h4>Hello {`${data.username}`}</h4>
                    <br></br>
                    {(error !== "") ? <h5>You do not have any patients</h5> : <h5>Recently visited Patients</h5>}
                    {
                        recentPatients.map(
                            patient =>
                                <Card key={patient.id} patient={patient} />
                        )
                    }
                </MainWrapper>
            </Container>
        </>
    );
}

export default Dashboard;