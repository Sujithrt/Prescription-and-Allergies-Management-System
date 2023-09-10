import React from 'react';
import { shallowToJson } from "enzyme-to-json";
import { shallow, mount } from 'enzyme';
import { Router } from 'react-router-dom';
import UserInfo from '../pages/userInfo/UserInfo'
import { user } from './../testConstants'
import axios from "axios"
import MockAdapter from "axios-mock-adapter"

jest.mock('moment', () => () => ({ format: () => '2018–01–30' }));

describe("UserInfo Test", () => {
	let mock;

	beforeAll(() => {
		mock = new MockAdapter(axios);
	});

	afterEach(() => {
		mock.reset();
	});
	it("UserInfo is rendered as expected", () => {
		mock.onGet().reply(200, user)
		const historyMock = { push: jest.fn(), location: {}, listen: jest.fn() };
		const userInfo = mount(<Router history={historyMock}><UserInfo /></Router>);
		expect(shallowToJson(userInfo)).toMatchSnapshot();
	});
});

describe('back to dashboard', () => {
	it('backToDashboard button clicked', () => {
		const historyMock = { push: jest.fn(), location: {}, listen: jest.fn() };
		const userInfo = mount(<Router history={historyMock}><UserInfo /></Router>)
        userInfo.find({ 'data-testid': 'backToDashboard' }).at(1).simulate('click');
		expect(historyMock.push.mock.calls[0][0]).toEqual(`/dashboard`);
	})
})