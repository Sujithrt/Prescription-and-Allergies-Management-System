import React from 'react';
import { shallowToJson } from "enzyme-to-json";
import { shallow } from 'enzyme';
import AllergyTable from './../components/allergyTable/AllergyTable'
import { patient } from './../testConstants'

describe("AllergyTable Test", () => {
    it("AllergyTable is rendered as expected", () => {
        const allergyTable = shallow(<AllergyTable patient={patient}/>);
        expect(shallowToJson(allergyTable)).toMatchSnapshot();
    });
});

describe("Patient has no allergies", () => {
    const patientWithNoAllergy = {
        "id": 1,
        "firstName": "abc",
        "lastName": "def",
        "gender": "Male",
        "age":20,
        "phoneNumber": 1234567890,
        "email": "abc.def@gmail.com",
        "city": "Bangalore",
        "state": "Karnataka",
        "lastEncounterDate": "2021-11-03",
        "medicalConditions": [
            {
                "id": 1,
                "name": "Medical Condition 1"
            },
            {
                "id": 2,
                "name": "Medical Condition 2"
            }
        ],
        "allergies": []
    }
    it("No allergy alert displayed", () => {
        const allergyTable = shallow(<AllergyTable patient={patientWithNoAllergy}/>);
        expect(shallowToJson(allergyTable)).toMatchSnapshot();
    });
});