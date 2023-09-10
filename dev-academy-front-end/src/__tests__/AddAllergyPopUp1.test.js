import React from 'react';
import { shallowToJson } from "enzyme-to-json";
import { shallow, mount } from 'enzyme';
import AddAllergyPopUp from '../components/addAllergyPopUp/AddAllergyPopUp'
import axios from "axios"
import MockAdapter from "axios-mock-adapter"
import { patient, allergy } from '../testConstants'
import { Router } from 'react-router-dom'

jest.mock("react-router-dom", () => ({
    ...jest.requireActual("react-router-dom"),
    useLocation: () => ({
        pathname: "localhost:3000/patient/1001"
    })
}));

describe("AddAllergyPopUp Test", () => {
    it("AddAllergyPopUp is rendered as expected", () => {
        const addAllergyPopUp = shallow(<AddAllergyPopUp patientDetails={patient} />);
        expect(shallowToJson(addAllergyPopUp)).toMatchSnapshot();
    });
});

describe("User input Test", () => {
    it("Allergy Name change with user input", () => {
        const addAllergyPopUp = shallow(<AddAllergyPopUp patientDetails={patient} />);
        addAllergyPopUp.find({ 'data-testid': "allergyName" }).simulate('change', { target: { 'data-testid': 'allergyName', value: 'Paracetamol Allergy' } })
        expect(addAllergyPopUp.find({ 'data-testid': "allergyName" }).prop('value')).toEqual('Paracetamol Allergy');
    });
    it("Allergy Reaction change with user input", () => {
        const addAllergyPopUp = shallow(<AddAllergyPopUp patientDetails={patient} />);
        addAllergyPopUp.find({ 'data-testid': "allergyReaction" }).simulate('change', { target: { 'data-testid': 'allergyReaction', value: 'Tiredness' } })
        expect(addAllergyPopUp.find({ 'data-testid': "allergyReaction" }).prop('value')).toEqual('Tiredness');
    });
    it("Allergy Ingredients change with user input", () => {
        const addAllergyPopUp = shallow(<AddAllergyPopUp patientDetails={patient} />);
        const ingredient = { label: "Paracetamol", value: 1 }
        addAllergyPopUp.find({ 'data-testid': "ingredients" }).simulate('change', { target: { 'data-testid': 'ingredients', value: ingredient } })
        expect(addAllergyPopUp.find({ 'data-testid': "ingredients" }).prop('value').target.value).toEqual(ingredient);
    });
    it("Allergy Severity change with user input", () => {
        const addAllergyPopUp = shallow(<AddAllergyPopUp patientDetails={patient} />);
        const severity = { label: "MILD", value: "MILD" }
        addAllergyPopUp.find({ 'data-testid': "severity" }).simulate('change', { target: { 'data-testid': 'severity', value: severity } })
        expect(addAllergyPopUp.find({ 'data-testid': "severity" }).prop('value').target.value).toEqual(severity);
    });
    it("Allergy Status change with user input", () => {
        const addAllergyPopUp = shallow(<AddAllergyPopUp patientDetails={patient} />);
        const status = { label: "ACTIVE", value: "ACTIVE" }
        addAllergyPopUp.find({ 'data-testid': "status" }).simulate('change', { target: { 'data-testid': 'status', value: status } })
        expect(addAllergyPopUp.find({ 'data-testid': "status" }).prop('value').target.value).toEqual(status);
    });
});

describe('test add allergy in new encounter', () => {
    let mock;

    beforeAll(() => {
        mock = new MockAdapter(axios);
        jest.mock("react-router-dom", () => ({
            ...jest.requireActual("react-router-dom"),
            useLocation: () => ({
                search: null
            })
        }));
    });

    afterEach(() => {
        mock.reset();
    });
    it('add allergy with all details filled', () => {
        mock.onPost().reply(201, allergy)
        const addAllergyPopUp = shallow(<AddAllergyPopUp patientDetails={patient} />);
        addAllergyPopUp.find({ 'data-testid': "allergyName" }).simulate('change', { target: { 'data-testid': 'allergyName', value: 'Paracetamol Allergy' } })
        addAllergyPopUp.find({ 'data-testid': "allergyReaction" }).simulate('change', { target: { 'data-testid': 'allergyReaction', value: 'Tiredness' } })
        addAllergyPopUp.find({ 'data-testid': "ingredients" }).simulate('change', { target: { 'data-testid': 'ingredients', value: { label: "Paracetamol", value: 1 } } })
        addAllergyPopUp.find({ 'data-testid': "severity" }).simulate('change', { target: { 'data-testid': 'severity', value: { label: "MILD", value: "MILD" } } })
        addAllergyPopUp.find({ 'data-testid': "status" }).simulate('change', { target: { 'data-testid': 'status', value: { label: "ACTIVE", value: "ACTIVE" } } })
        addAllergyPopUp.find({ 'data-testid': "addAllergyButton" }).simulate('click', { preventDefault: () => { } })
    });
    it('add allergy with some details filled', () => {
        const addAllergyPopUp = shallow(<AddAllergyPopUp patientDetails={patient} />);
        addAllergyPopUp.find({ 'data-testid': "allergyName" }).simulate('change', { target: { 'data-testid': 'allergyName', value: '' } })
        addAllergyPopUp.find({ 'data-testid': "allergyReaction" }).simulate('change', { target: { 'data-testid': 'allergyReaction', value: 'Tiredness' } })
        addAllergyPopUp.find({ 'data-testid': "ingredients" }).simulate('change', { target: { 'data-testid': 'ingredients', value: { label: "Paracetamol", value: 1 } } })
        addAllergyPopUp.find({ 'data-testid': "severity" }).simulate('change', { target: { 'data-testid': 'severity', value: { label: "MILD", value: "MILD" } } })
        addAllergyPopUp.find({ 'data-testid': "status" }).simulate('change', { target: { 'data-testid': 'status', value: { label: "ACTIVE", value: "ACTIVE" } } })
        addAllergyPopUp.find({ 'data-testid': "addAllergyButton" }).simulate('click', { preventDefault: () => { } })
    });
    it('Allergy already exists error', () => {
        mock.onPost().reply(409)
        const addAllergyPopUp = shallow(<AddAllergyPopUp patientDetails={patient} />);
        addAllergyPopUp.find({ 'data-testid': "allergyName" }).simulate('change', { target: { 'data-testid': 'allergyName', value: 'Paracetamol Allergy' } })
        addAllergyPopUp.find({ 'data-testid': "allergyReaction" }).simulate('change', { target: { 'data-testid': 'allergyReaction', value: 'Tiredness' } })
        addAllergyPopUp.find({ 'data-testid': "ingredients" }).simulate('change', { target: { 'data-testid': 'ingredients', value: { label: "Paracetamol", value: 1 } } })
        addAllergyPopUp.find({ 'data-testid': "severity" }).simulate('change', { target: { 'data-testid': 'severity', value: { label: "MILD", value: "MILD" } } })
        addAllergyPopUp.find({ 'data-testid': "status" }).simulate('change', { target: { 'data-testid': 'status', value: { label: "ACTIVE", value: "ACTIVE" } } })
        addAllergyPopUp.find({ 'data-testid': "addAllergyButton" }).simulate('click', { preventDefault: () => { } })
    });
    it('Unknown error', () => {
        mock.onPost().reply(500)
        const addAllergyPopUp = shallow(<AddAllergyPopUp patientDetails={patient} />);
        addAllergyPopUp.find({ 'data-testid': "allergyName" }).simulate('change', { target: { 'data-testid': 'allergyName', value: 'Paracetamol Allergy' } })
        addAllergyPopUp.find({ 'data-testid': "allergyReaction" }).simulate('change', { target: { 'data-testid': 'allergyReaction', value: 'Tiredness' } })
        addAllergyPopUp.find({ 'data-testid': "ingredients" }).simulate('change', { target: { 'data-testid': 'ingredients', value: { label: "Paracetamol", value: 1 } } })
        addAllergyPopUp.find({ 'data-testid': "severity" }).simulate('change', { target: { 'data-testid': 'severity', value: { label: "MILD", value: "MILD" } } })
        addAllergyPopUp.find({ 'data-testid': "status" }).simulate('change', { target: { 'data-testid': 'status', value: { label: "ACTIVE", value: "ACTIVE" } } })
        addAllergyPopUp.find({ 'data-testid': "addAllergyButton" }).simulate('click', { preventDefault: () => { } })
    });
});

describe('modal window', () => {
    it('Check open modal window upon add allergy button click', () => {
        const addAllergyPopUp = shallow(<AddAllergyPopUp patientDetails={patient} />)
        addAllergyPopUp.find({ 'data-testid': 'openAddAllergyModal' }).simulate('click')
    });
    it('Check close modal window upon x button click', () => {
        const addAllergyPopUp = shallow(<AddAllergyPopUp patientDetails={patient} />)
        addAllergyPopUp.find({ 'data-testid': 'addAllergyModal' }).simulate('hide')
    });
});

describe('Modal Window clear form', () => {
    it('Check clearAllergyDetails function upon component mount', () => {
        const historyMock = { push: jest.fn(), location: {}, listen: jest.fn() };
        const addAllergyPopUp = mount(<Router history={historyMock}><AddAllergyPopUp patientDetails={patient} /></Router>)
        expect(shallowToJson(addAllergyPopUp)).toMatchSnapshot()
    });
});
