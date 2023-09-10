import React from 'react';
import { shallowToJson } from "enzyme-to-json";
import { shallow, mount } from 'enzyme';
import { Router } from 'react-router-dom';
import LoginPage from '../pages/loginPage/LoginPage'
import axios from "axios"
import MockAdapter from "axios-mock-adapter"



describe("LoginPage Test", () => {
    it("LoginPage is rendered as expected", () => {
        const loginPage = shallow(<LoginPage />);
        expect(shallowToJson(loginPage)).toMatchSnapshot();
    });
});

describe("LoginPage onChange Test", () => {
    it("Check if Username text field is taking user input", () => {
        const wrapper = shallow(<LoginPage />)
        wrapper.find({ 'data-testid': 'username' }).simulate('change', { target: { name: 'username', value: 'abc def' } })
        expect(wrapper.find({ 'data-testid': 'username' }).prop('value')).toEqual('abc def');
    })

    it("Check if Password text field is taking user input", () => {
        const wrapper = shallow(<LoginPage />)
        wrapper.find({ 'data-testid': 'password' }).simulate('change', { target: { name: 'password', value: 'password123' } })
        expect(wrapper.find({ 'data-testid': 'password' }).prop('value')).toEqual('password123');
    })
});

describe('User login test', () => {
    let mock;

    beforeAll(() => {
        mock = new MockAdapter(axios);
    });

    afterEach(() => {
        mock.reset();

    });
    it('Username Empty', () => {
        const wrapper = shallow(<LoginPage />)
        wrapper.find({ 'data-testid': 'username' }).simulate('change', { target: { name: 'username', value: '' } })
        wrapper.find({ 'data-testid': 'password' }).simulate('change', { target: { name: 'password', value: 'password123' } })
        wrapper.find({ 'data-testid': 'submitButton' }).simulate('click', { preventDefault: () => { } });
    })
    it('Password Empty', () => {
        const wrapper = shallow(<LoginPage />)
        wrapper.find({ 'data-testid': 'username' }).simulate('change', { target: { name: 'username', value: 'John' } })
        wrapper.find({ 'data-testid': 'password' }).simulate('change', { target: { name: 'password', value: '' } })
        wrapper.find({ 'data-testid': 'submitButton' }).simulate('click', { preventDefault: () => { } });
    })
    it('successful login', () => {
        const data = { token: "eyJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJQcmVzY3JpcHRpb24tYW5kLUFsbGVyZ2llcy1NYW5hZ2VtZW50LUFwcCIsInN1YiI6IkpvaG4iLCJpYXQiOjE2MzYyNzY4MTUsImV4cCI6MTYzNjMxMjgxNX0.1GMUGOTHHPDfPECOSel15b_OPtgHbnXvBF0_iEyDxkY" }
        mock.onPost().reply(202, data)
        const wrapper = shallow(<LoginPage />)
        wrapper.find({ 'data-testid': 'username' }).simulate('change', { target: { name: 'username', value: 'John' } })
        wrapper.find({ 'data-testid': 'password' }).simulate('change', { target: { name: 'password', value: 'password123' } })
        wrapper.find({ 'data-testid': 'submitButton' }).simulate('click', { preventDefault: () => { } });
    })
    it('User not found in database', () => {
        mock.onPost().reply(204)
        const wrapper = shallow(<LoginPage />)
        wrapper.find({ 'data-testid': 'username' }).simulate('change', { target: { name: 'username', value: 'John' } })
        wrapper.find({ 'data-testid': 'password' }).simulate('change', { target: { name: 'password', value: 'password123' } })
        wrapper.find({ 'data-testid': 'submitButton' }).simulate('click', { preventDefault: () => { } });
    })
});