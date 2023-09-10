import React from 'react';
import { shallow } from 'enzyme';
import PrescribeMedicine from '../components/prescribeMedicine/PrescribeMedicine'
import { patient, prescription } from '../testConstants'
import axios from "axios"
import MockAdapter from "axios-mock-adapter"

jest.mock("react-router-dom", () => ({
    ...jest.requireActual("react-router-dom"),
    useLocation: () => ({
        pathname: "localhost:3000/patient/1001",
        search: "?encounterId=1"
    })
}));

describe("Prescribe Medicine in existing encounter", () => {
    let mock;

    beforeAll(() => {
        mock = new MockAdapter(axios);
    });

    afterEach(() => {
        mock.reset();
    });
    it('All fields filled', () => {
        mock.onPost().reply(201, prescription)
        const prescribeMedicine = shallow(<PrescribeMedicine patient={patient} />);
        const medicine = {label: "Dolo", value: 1, cause: "Causes MILD Tiredness in patient"}
        prescribeMedicine.find({ 'data-testid': "medicine" }).simulate('change', { target: { 'data-testid': 'medicine', value: medicine } })
        prescribeMedicine.find({ 'data-testid': "doseField" }).simulate('change', { target: { 'data-testid': 'dose', value: '1 time a day' } })
        prescribeMedicine.find({ 'data-testid': "prescribeButton" }).simulate('click', { preventDefault: () => { } })
    });
    it('Invalid input', () => {
        mock.onPost().reply(400)
        const prescribeMedicine = shallow(<PrescribeMedicine patient={patient} />);
        const medicine = {label: "Dolo", value: 1, cause: "Causes MILD Tiredness in patient"}
        prescribeMedicine.find({ 'data-testid': "medicine" }).simulate('change', { target: { 'data-testid': 'medicine', value: medicine } })
        prescribeMedicine.find({ 'data-testid': "doseField" }).simulate('change', { target: { 'data-testid': 'dose', value: '1 time a day' } })
        prescribeMedicine.find({ 'data-testid': "prescribeButton" }).simulate('click', { preventDefault: () => { } })
    });
    it('Medicine already prescribed', () => {
        mock.onPost().reply(409)
        const prescribeMedicine = shallow(<PrescribeMedicine patient={patient} />);
        const medicine = {label: "Dolo", value: 1, cause: "Causes MILD Tiredness in patient"}
        prescribeMedicine.find({ 'data-testid': "medicine" }).simulate('change', { target: { 'data-testid': 'medicine', value: medicine } })
        prescribeMedicine.find({ 'data-testid': "doseField" }).simulate('change', { target: { 'data-testid': 'dose', value: '1 time a day' } })
        prescribeMedicine.find({ 'data-testid': "prescribeButton" }).simulate('click', { preventDefault: () => { } })
    });
});