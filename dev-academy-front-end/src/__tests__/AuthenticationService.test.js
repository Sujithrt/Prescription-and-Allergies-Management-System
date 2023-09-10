import axios from "axios"
import MockAdapter from "axios-mock-adapter"
import { userLogin, fetchUserData, isUserLoggedIn } from "../api/AuthenticationService"

describe('User login test', () => {
    let mock;

    beforeAll(() => {
        mock = new MockAdapter(axios);
    });

    afterEach(() => {
        mock.reset();
    });
    it('successful login', () => {
        const values = { username: "John", password: "password123" }
        const data = { token: "eyJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJQcmVzY3JpcHRpb24tYW5kLUFsbGVyZ2llcy1NYW5hZ2VtZW50LUFwcCIsInN1YiI6IkpvaG4iLCJpYXQiOjE2MzYyNzY4MTUsImV4cCI6MTYzNjMxMjgxNX0.1GMUGOTHHPDfPECOSel15b_OPtgHbnXvBF0_iEyDxkY" }
        mock.onPost().reply(202, data)
        userLogin(values).then(response => {
            expect(response.data).toEqual(data)
        }).catch(error => {
            console.log(error)
        })
    })
});

describe('Get user data test', () => {
    let mock;

    beforeAll(() => {
        mock = new MockAdapter(axios);
    });

    afterEach(() => {
        mock.reset();
    });
    it('Successful get user data', () => {
        const userInfo = {
            "id": 1,
            "username": "John",
            "email": "john.cage@gmail.com",
            "role": "Physician",
            "firstName": "John",
            "lastName": "Cage",
            "speciality": "Cardiologist",
            "city": "Bangalore",
            "state": "Karnataka",
            "dob": "1975-06-12T18:30:00.000+00:00",
            "authorities": [
                {
                    "id": 1,
                    "roleCode": "USER",
                    "roleDescription": "User role",
                    "authority": "USER"
                },
                {
                    "id": 2,
                    "roleCode": "ADMIN",
                    "roleDescription": "Admin role",
                    "authority": "ADMIN"
                }
            ]
        }
        mock.onGet().reply(200, userInfo)
        fetchUserData().then(response => {
            expect(response.data).toEqual(userInfo)
        }).catch(error => {
            console.log(error)
        })
    })
});

describe('check if user is logged in', () => {
    beforeEach(() => {
        window.sessionStorage.clear()
    })
    it('user is logged in', () => {
        window.sessionStorage.setItem('USER_KEY', "eyJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJQcmVzY3JpcHRpb24tYW5kLUFsbGVyZ2llcy1NYW5hZ2VtZW50LUFwcCIsInN1YiI6IkpvaG4iLCJpYXQiOjE2MzYyNzY4MTUsImV4cCI6MTYzNjMxMjgxNX0.1GMUGOTHHPDfPECOSel15b_OPtgHbnXvBF0_iEyDxkY")
        expect(isUserLoggedIn()).toBe(true)
    })
    it('user is logged in', () => {
        expect(isUserLoggedIn()).toBe(false)
    })
});