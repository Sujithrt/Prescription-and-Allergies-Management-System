import React from 'react';
import { shallow } from 'enzyme';
import AddAllergyPopUp from '../components/addAllergyPopUp/AddAllergyPopUp'
import axios from "axios"
import MockAdapter from "axios-mock-adapter"
import { patient, allergy } from '../testConstants'

jest.mock("react-router-dom", () => ({
    ...jest.requireActual("react-router-dom"),
    useLocation: () => ({
        pathname: "localhost:3000/patient/1001",
        search: "?encounterId=1"
    })
}));

describe('test add allergy in existing encounter', () => {
    let mock;

    beforeAll(() => {
        mock = new MockAdapter(axios);
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
    it('Invalid Encounter Id error', () => {
        mock.onPost().reply(400)
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
