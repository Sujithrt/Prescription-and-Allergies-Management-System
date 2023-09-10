import React from 'react';
import { shallowToJson } from "enzyme-to-json";
import { shallow } from 'enzyme';
import PrescriptionHistory from '../components/prescriptionHistory/PriscriptionHistory'
import { prescriptionData } from './../testConstants'

describe("PrescriptionHistory Test", () => {
    it("PrescriptionHistory is rendered as expected", () => {
        const prescriptionHistory = shallow(<PrescriptionHistory prescriptionData={prescriptionData} error="" />);
        expect(shallowToJson(prescriptionHistory)).toMatchSnapshot();
    });
    it("Patient has no prescription history", () => {
        const prescriptionHistory = shallow(<PrescriptionHistory prescriptionData={[]} error="Patient does not have any Prescription History"/>);
        expect(shallowToJson(prescriptionHistory)).toMatchSnapshot();
    });
});

describe("PrescriptionHistory Test", () => {
    it("Open PrescriptionHistory Modal Window", () => {
        const prescriptionHistory = shallow(<PrescriptionHistory prescriptionData={prescriptionData} error=""/>);
        prescriptionHistory.find({ 'data-testid': 'viewPrescriptionHistoryButton' }).simulate('click')
    });
    it("Close PrescriptionHistory Modal Window by hide", () => {
        const prescriptionHistory = shallow(<PrescriptionHistory prescriptionData={prescriptionData} error=""/>);
        prescriptionHistory.find({ 'data-testid': 'hideMenu' }).simulate('hide')
    });
    it("Close PrescriptionHistory Modal Window by clicking close button", () => {
        const prescriptionHistory = shallow(<PrescriptionHistory prescriptionData={prescriptionData} error=""/>);
        prescriptionHistory.find({ 'data-testid': 'closeModalButton' }).simulate('click')
    });
});