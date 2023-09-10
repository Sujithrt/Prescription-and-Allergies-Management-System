import React from 'react';
import PatientProfile from '../pages/patientProfile/PatientProfile'
import axios from "axios"
import MockAdapter from "axios-mock-adapter"
import { mount } from 'enzyme'
import { Router } from 'react-router-dom'
import { shallowToJson } from "enzyme-to-json";
import { patient } from './../testConstants'


describe('Patient Profile API test', () => {
    let mock;

    beforeAll(() => {
        mock = new MockAdapter(axios);
    });

    afterEach(() => {
        mock.reset();
    });
    it('Patient details rendered', () => {
        mock.onGet().reply(200, patient)
        const setIsLoading = jest.fn()
        React.useState = jest.fn(() => [false, setIsLoading])
        const historyMock = { push: jest.fn(), location: {}, listen: jest.fn() };
        const patientProfile = mount(<Router history={historyMock}><PatientProfile match={{ params: { id: 1001 }, isExact: true, path: "", url: "" }} /></Router>)
        expect(shallowToJson(patientProfile)).toMatchSnapshot();
    })
});
