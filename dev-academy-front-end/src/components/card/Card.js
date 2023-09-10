import React from 'react';
import Box from '@mui/material/Box';
import Card from '@mui/material/Card';
import CardActions from '@mui/material/CardActions';
import CardContent from '@mui/material/CardContent';
import Button from 'react-bootstrap/Button';
import Typography from '@mui/material/Typography';
import './../card/card.css';
import { useHistory } from 'react-router-dom';

/**
 * Displays Patient Details and allows User to click a button to get more patient details
 * @param {Patient Details} props 
 * @returns 
 */
export default function OutlinedCard(props) {

    const history = useHistory();

    return (
        <Box sx={{ minWidth: 275 }} className="box">
            <Card variant="outlined" className="cards">
                <React.Fragment>
                    <CardContent>
                        <Typography sx={{ fontSize: 14 }} color="text.secondary" gutterBottom>
                            Registration Number - {props.patient.id}<br />
                        </Typography>
                        <Typography variant="h5">
                            {props.patient.firstName} {props.patient.lastName}
                        </Typography>
                        <Typography variant="body2">
                            Age: {props.patient.age}<br /><br />
                            Gender: {props.patient.gender}<br />
                        </Typography>
                        <Typography sx={{ fontSize: 14 }} color="text.secondary" gutterBottom>
                            Last Encounter Date: {props.patient.date}<br />
                        </Typography>
                    </CardContent>
                    <span className="button-align"><CardActions><br /><br />
                        <Button variant="primary" onClick={() => history.push(`/patient/${props.patient.id}`)}>More Details</Button>
                    </CardActions></span>
                </React.Fragment>
            </Card>
        </Box>
    );
}