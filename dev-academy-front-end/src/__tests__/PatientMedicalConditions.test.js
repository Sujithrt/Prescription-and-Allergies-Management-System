import React from 'react';
import { shallowToJson } from "enzyme-to-json";
import { shallow } from 'enzyme';
import { patient } from './../testConstants'
import PatientMedicalConditions from '../components/patientMedicalConditions/PatientMedicalConditions'

describe("PatientMedicalConditions Test", ()=>{
  it("PatientMedicalConditions is rendered as expected", ()=>{
    const patientMedicalConditions = shallow(<PatientMedicalConditions medicalConditions={patient.medicalConditions}/>);
    expect(shallowToJson(patientMedicalConditions)).toMatchSnapshot();
  });
});