import React from 'react';
import { shallowToJson } from "enzyme-to-json";
import { shallow } from 'enzyme';
import PnamsApp from './../PnamsApp';

describe("PnamsApp Test", ()=>{
  it("PnamsApp is rendered as expected", ()=>{
    const pnamsApp = shallow(<PnamsApp />);
    expect(shallowToJson(pnamsApp)).toMatchSnapshot();
  });
});