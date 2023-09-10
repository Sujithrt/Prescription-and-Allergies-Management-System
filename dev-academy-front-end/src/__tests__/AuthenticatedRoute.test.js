import AuthenticatedRoute from './../components/AuthenticatedRoute';
import React from 'react';
import { mount } from 'enzyme';
import { MemoryRouter, Redirect } from 'react-router-dom';

describe('Check if authencation in routing is working', () => {
    beforeEach(() => {
        window.sessionStorage.clear()
    })
    it('should render component if user has been authenticated', () => {
        const AComponent = () => <div>AComponent</div>;
        const props = { path: '/aprivatepath', component: AComponent };
        window.sessionStorage.setItem('USER_KEY', "eyJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJQcmVzY3JpcHRpb24tYW5kLUFsbGVyZ2llcy1NYW5hZ2VtZW50LUFwcCIsInN1YiI6IkpvaG4iLCJpYXQiOjE2MzYyNzY4MTUsImV4cCI6MTYzNjMxMjgxNX0.1GMUGOTHHPDfPECOSel15b_OPtgHbnXvBF0_iEyDxkY")
        const enzymeWrapper = mount(
            <MemoryRouter initialEntries={[props.path]}>
                <AuthenticatedRoute />
            </MemoryRouter>,
        );
        const history = enzymeWrapper.find('Router').prop('history');
        expect(history.location.pathname).toBe('/aprivatepath');
    });

    it('should redirect to login page if user is not authenticated', () => {
        const AComponent = () => <div>AComponent</div>;
        const props = { path: '/aprivatepath', component: AComponent };

        const enzymeWrapper = mount(
            <MemoryRouter initialEntries={[props.path]}>
                <AuthenticatedRoute />
            </MemoryRouter>,
        );
        const history = enzymeWrapper.find('Router').prop('history');
        expect(history.location.pathname).toBe('/login');
    });
});