import React, { useState, useEffect } from 'react';
import {
    CalendarToday,
    LocationSearching,
    MailOutline,
    PermIdentity,
} from "@material-ui/icons";
import "./../userInfo/userInfo.css";
import Button from 'react-bootstrap/Button';
import AccountCircle from '@mui/icons-material/AccountCircle';
import NavBar from './../../components/navBar/NavBar';
import moment from 'moment';
import { fetchUserData } from '../../api/AuthenticationService';
import { useHistory } from 'react-router-dom';


/**
 * Renders page to display logged in users' details
 * 
 */
export default function UserInfo() {

    const [user, setUser] = useState({});

    const history = useHistory();

    useEffect(() => {
        /**
        * Fetches logged in users' data
        */
        fetchUserData().then((response) => {
            setUser(response.data);
        }).catch((e) => {
            if (e.status === 404) {
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
            <div className="user">
                <div className="userTitleContainer">
                    <h1>User Information</h1>
                </div>
                <div className="userContainer">
                    <div className="userShow">
                        <div className="userShowTop">
                            <AccountCircle />
                            <div className="userShowTopTitle">
                                <span className="userShowUsername">{user.firstName} {user.lastName}</span>
                                <span className="userShowUserTitle">{user.speciality}</span>
                            </div>
                        </div>
                        <div className="userShowBottom">
                            <span className="userShowTitle">User Details</span>
                            <div className="userShowInfo">
                                <PermIdentity className="userShowIcon" />
                                <span className="userShowInfoTitle">{user.username}</span>
                            </div>
                            <div className="userShowInfo">
                                <CalendarToday className="userShowIcon" />
                                <span className="userShowInfoTitle">{moment(user.dob).format("YYYY-MM-DD")}</span>
                            </div>
                            <span className="userShowTitle">Contact Details</span>
                            <div className="userShowInfo">
                                <MailOutline className="userShowIcon" />
                                <span className="userShowInfoTitle">{user.email}</span>
                            </div>
                            <div className="userShowInfo">
                                <LocationSearching className="userShowIcon" />
                                <span className="userShowInfoTitle">{user.city} | {user.state}</span>
                            </div>
                        </div>
                        <Button data-testid="backToDashboard" onClick={() => history.push('/dashboard')} variant="primary">Back to Dashboard</Button>
                    </div>
                </div>
            </div>
        </>
    );
}