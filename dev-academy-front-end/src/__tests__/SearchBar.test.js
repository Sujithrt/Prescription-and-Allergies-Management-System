import React from 'react';
import { shallowToJson } from "enzyme-to-json";
import { shallow, mount } from 'enzyme';
import { Router } from 'react-router-dom';
import SearchBar from '../components/searchBar/SearchBar'

describe("SearchBar Test", () => {
    it("SearchBar is rendered as expected", () => {
        const searchBar = shallow(<SearchBar />);
        expect(shallowToJson(searchBar)).toMatchSnapshot();
    });
});