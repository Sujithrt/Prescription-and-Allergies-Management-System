import React from 'react';
import { shallowToJson } from "enzyme-to-json";
import { shallow } from 'enzyme';
import PrescribeMedicine from '../components/prescribeMedicine/PrescribeMedicine'
import { patient, prescription, medicineList } from '../testConstants'
import axios from "axios"
import MockAdapter from "axios-mock-adapter"

jest.mock("react-router-dom", () => ({
    ...jest.requireActual("react-router-dom"),
    useLocation: () => ({
        pathname: "localhost:3000/patient/1001"
    })
}));

describe("PrescribeMedicine Test", () => {
    it("PrescribeMedicine is rendered as expected", () => {
        const prescribeMedicine = shallow(<PrescribeMedicine patient={patient} />);
        expect(shallowToJson(prescribeMedicine)).toMatchSnapshot();
    });
});

describe("User input Test", () => {
    it("Medicine change with user input", () => {
        const prescribeMedicine = shallow(<PrescribeMedicine patient={patient} />);
        const medicine = {label: "Dolo", value: 1, cause: "Causes MILD Tiredness in patient"}
        prescribeMedicine.find({ 'data-testid': "medicine" }).simulate('change', { target: { 'data-testid': 'medicine', value: medicine } })
        expect(prescribeMedicine.find({ 'data-testid': "medicine" }).prop('value').target.value).toEqual(medicine);
    });
    it("Dose change with user input", () => {
        const prescribeMedicine = shallow(<PrescribeMedicine patient={patient} />);
        prescribeMedicine.find({ 'data-testid': "doseField" }).simulate('change', { target: { 'data-testid': 'dose', value: '1 time a day' } })
        expect(prescribeMedicine.find({ 'data-testid': "doseField" }).prop('value')).toEqual('1 time a day');
    });
});

describe("test add medicine field", () => {
    it('click + button to add new field', () => {
        const prescribeMedicine = shallow(<PrescribeMedicine patient={patient} />);
        prescribeMedicine.find({ 'data-testid': 'addMedicineField' }).simulate('click')
    });
});

describe("test delete medicine field", () => {
    it('click delete button to remove medicine field', () => {
        const prescribeMedicine = shallow(<PrescribeMedicine patient={patient} />);
        prescribeMedicine.find({ 'data-testid': 'deleteMedicineField' }).simulate('click')
    });
});

describe("Prescribe Medicine in new encounter", () => {
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
    it('Some fields filled', () => {
        const prescribeMedicine = shallow(<PrescribeMedicine patient={patient} />);
        const medicine = {label: "Dolo", value: 1, cause: "Causes MILD Tiredness in patient"}
        prescribeMedicine.find({ 'data-testid': "medicine" }).simulate('change', { target: { 'data-testid': 'medicine', value: medicine } })
        prescribeMedicine.find({ 'data-testid': "doseField" }).simulate('change', { target: { 'data-testid': 'dose', value: '' } })
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

describe("Medicine Select component", () => {
    let mock;

    beforeAll(() => {
        mock = new MockAdapter(axios);
    });

    afterEach(() => {
        mock.reset();
    });
    it('test onInputChange of medicine search', () => {
        mock.onGet().reply(200, medicineList)
        const prescribeMedicine = shallow(<PrescribeMedicine patient={patient} />);
        prescribeMedicine.find({ 'data-testid': 'medicine' }).simulate('inputChange')
    });
});