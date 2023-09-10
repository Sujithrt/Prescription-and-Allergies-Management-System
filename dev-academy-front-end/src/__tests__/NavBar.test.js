import React from 'react';
import { shallowToJson } from "enzyme-to-json";
import { shallow, mount } from 'enzyme';
import { Router } from 'react-router-dom';
import NavBar from '../components/navBar/NavBar'

describe("NavBar Test", () => {
	it("NavBar is rendered as expected", () => {
		const navBar = shallow(<NavBar />);
		expect(shallowToJson(navBar)).toMatchSnapshot();
	});
	it('Check back to dashboard on App icon click', () => {
		const historyMock = { push: jest.fn(), location: {}, listen: jest.fn() };
		const navBar = mount(<Router history={historyMock}><NavBar /></Router>)
		navBar.find({ 'data-testid': 'pnamsHeader' }).simulate('click')
		expect(historyMock.push.mock.calls[0][0]).toEqual(`/dashboard`);
	});
});

describe("Profile Menu test", () => {
	it("test handleProfileMenuOpen", () => {
		const navBar = shallow(<NavBar />)
		navBar.find({ 'data-testid': 'profileMenu' }).simulate('click', { target: {} })
	});
	it("test handleMenuClose", () => {
		const navBar = shallow(<NavBar />)
		navBar.find({ 'data-testid': 'menu' }).simulate('close')
	});
	it("test handleProfileMenuOpen", () => {
		const navBar = shallow(<NavBar />)
		navBar.find({ 'data-testid': 'profileMenuMobile' }).simulate('click', { target: {} })
	});
});

describe("Menu Items test", () => {
	it("test handleOpenProfile", () => {
		const historyMock = { push: jest.fn(), location: {}, listen: jest.fn() };
		const navBar = mount(<Router history={historyMock}><NavBar /></Router>)
		navBar.find({ 'data-testid': 'openUserProfile' }).at(1).simulate('click')
		expect(historyMock.push.mock.calls[0][0]).toEqual(`/user`);
	});
	it("test handleMenuClose", () => {
		const historyMock = { push: jest.fn(), location: {}, listen: jest.fn() };
		const navBar = mount(<Router history={historyMock}><NavBar /></Router>)
		navBar.find({ 'data-testid': 'clickLogout' }).at(1).simulate('click')
		expect(historyMock.push.mock.calls[0][0]).toEqual(`/`);
	});
});