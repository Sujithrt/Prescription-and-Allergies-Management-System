import React from 'react';
import { shallowToJson } from "enzyme-to-json";
import { shallow } from 'enzyme';
import ErrorComponent from '../pages/errorComponent/ErrorComponent'

describe("ErrorComponent Test", ()=>{
  it("ErrorComponent is rendered as expected", ()=>{
    const errorComponent = shallow(<ErrorComponent />);
    expect(shallowToJson(errorComponent)).toMatchSnapshot();
  });
});