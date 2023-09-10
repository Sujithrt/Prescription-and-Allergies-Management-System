import React from 'react';
import { shallowToJson } from "enzyme-to-json";
import { shallow } from 'enzyme';
import { patient } from './../testConstants'
import PatientDetails from '../components/patientDetails/PatientDetails'

describe("PatientDetails Test", ()=>{
  it("PatientDetails is rendered as expected", ()=>{
    const patientDetails = shallow(<PatientDetails patient={patient}/>);
    expect(shallowToJson(patientDetails)).toMatchSnapshot();
  });
});