import React from 'react';
import App from '../App';
import { shallowToJson } from "enzyme-to-json";
import { shallow } from 'enzyme';

describe("App Test", () => {
    it("App is rendered as expected", () => {
        const app = shallow(<App />);
        expect(shallowToJson(app)).toMatchSnapshot();
    });
});