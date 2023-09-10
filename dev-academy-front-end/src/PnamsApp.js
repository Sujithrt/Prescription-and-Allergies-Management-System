import './App.css';
import {
    BrowserRouter,
    Switch,
    Route,
} from "react-router-dom";
import LoginPage from './pages/loginPage/LoginPage';
import Dashboard from './pages/dashboard/Dashboard';
import UserInfo from './pages/userInfo/UserInfo';
import PatientProfile from './pages/patientProfile/PatientProfile';
import AuthenticatedRoute from './components/AuthenticatedRoute';
import ErrorComponent from './pages/errorComponent/ErrorComponent';

/**
 * Manages Routing between different pages of the Applicaiton
 * 
 */
function PnamsApp() {
    return (
        <BrowserRouter>
            <Switch>
                <Route exact path="/" component={LoginPage} />
                <Route exact path="/login" component={LoginPage} />
                <AuthenticatedRoute exact path="/dashboard" component={Dashboard} />
                <AuthenticatedRoute exact path="/user" component={UserInfo} />
                <AuthenticatedRoute exact path="/patient/:id" component={PatientProfile} />
                <Route path="" component={ErrorComponent} />
                <Route path="/error" component={ErrorComponent} />
            </Switch>
        </BrowserRouter>
    );
}

export default PnamsApp;
