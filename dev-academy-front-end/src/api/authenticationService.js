import axios from 'axios';

/**
 * Function to obtain JWT token from session storage
 * @returns token
 */
export const getToken = () => {
    return sessionStorage.getItem('USER_KEY');
}

/**
 * POST API call for User Login
 * @param authRequest 
 * @returns 
 */
export function userLogin(authRequest) {
    return axios({
        method: 'POST',
        url: `/api/v1/auth/login`,
        data: authRequest
    });
}

/**
 * GET API call to obtain user details of logged in user 
 * @param authRequest 
 * @returns 
 */
export function fetchUserData() {
    return axios({
        method: 'GET',
        url: `/api/v1/auth/userinfo`,
        headers: {
            'Authorization': 'Bearer ' + getToken()
        }
    });
}

/**
 * Function to check if user is logged in by checking is JWT token is stored in session storage
 * @returns true or false
 */
export function isUserLoggedIn() {
    let token = sessionStorage.getItem('USER_KEY');
    if (token === null) {
        return false;
    } else {
        return true;
    }
}