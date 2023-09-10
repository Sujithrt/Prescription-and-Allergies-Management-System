import React from 'react';
import { shallowToJson } from "enzyme-to-json";
import {  mount } from 'enzyme';
import Dashboard from '../pages/dashboard/Dashboard'
import { Router } from 'react-router-dom'
import { recentPatients, user } from './../testConstants'
import axios from "axios"
import MockAdapter from "axios-mock-adapter"

describe("Dashboard Test", () => {
    let mock;

    beforeAll(() => {
        mock = new MockAdapter(axios);
    });

    afterEach(() => {
        mock.reset();
    });
    it("Dashboard is rendered as expected with useEffect API calls", () => {
        mock.onGet('/api/v1/auth/userinfo').reply(200, user)
        mock.onGet('/api/v1/auth/dashboard').reply(200, recentPatients)
        const historyMock = { push: jest.fn(), location: {}, listen: jest.fn() };
        const dashboard = mount(<Router history={historyMock}><Dashboard /></Router>);
        expect(shallowToJson(dashboard)).toMatchSnapshot();
    });
    it("Invalid user data request 1", () => {
        mock.onGet('/api/v1/auth/userinfo').reply(404)
        mock.onGet('/api/v1/auth/dashboard').reply(404)
        const historyMock = { push: jest.fn(), location: {}, listen: jest.fn() };
        const dashboard = mount(<Router history={historyMock}><Dashboard /></Router>);
    });
    it("User has no recent patients", () => {
        mock.onGet('/api/v1/auth/userinfo').reply(200, user)
        mock.onGet('/api/v1/auth/dashboard').reply(204, [])
        const historyMock = { push: jest.fn(), location: {}, listen: jest.fn() };
        const dashboard = mount(<Router history={historyMock}><Dashboard /></Router>);
    });
});