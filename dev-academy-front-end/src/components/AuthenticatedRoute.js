import React, { Component } from 'react';
import {Route, Redirect} from 'react-router-dom';
import { isUserLoggedIn } from './../api/AuthenticationService';

/**
 * Function to make sure that unauthenticated users cannot access secure routes. If user is logged
 * in i.e. Authenticated, then user can access route else, user is redirected to the login page
 */
export default class AuthenticatedRoute extends Component {
    render() {
        if(isUserLoggedIn()) {
            return <Route {...this.props}/>
        } else {
            return <Redirect to="/login"/>
        }
    }
}