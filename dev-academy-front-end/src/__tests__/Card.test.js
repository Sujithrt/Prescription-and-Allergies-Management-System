import React from 'react';
import { shallowToJson } from "enzyme-to-json";
import { shallow, mount } from 'enzyme';
import Card from '../components/card/Card'
import { Router } from 'react-router-dom';
import { act } from '@testing-library/react-hooks';
import { patient } from '../testConstants';


describe("Card Test", () => {
    it("Card is rendered as expected", () => {
        const card = shallow(<Card patient={patient} />);
        expect(shallowToJson(card)).toMatchSnapshot();
    });
});

describe('Card More details button click', () => {
    it('Redirects to correct URL on click', () => {
        const historyMock = { push: jest.fn(), location: {}, listen: jest.fn() };
        const wrapper = mount(
            <Router history={historyMock}>
                <Card patient={patient} />
            </Router>,
        ).find('button')

        const { onClick } = wrapper.props();
        act(() => {
            onClick();
        });
        expect(historyMock.push.mock.calls[0][0]).toEqual(`/patient/${patient.id}`);
    });
})