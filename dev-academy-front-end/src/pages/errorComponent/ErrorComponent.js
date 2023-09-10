import React from 'react';
import Alert from 'react-bootstrap/Alert';
import './../errorComponent/errorComponent.css';
import NavBar from './../../components/navBar/NavBar';

/**
 * Renders an error page when user tries to access and undefined route
 * 
 */
function ErrorComponent() {
	return (
		<>
			<NavBar />
			<Alert variant="warning" className="error">
				An Error Occurred. Please Contact Support at 123-456-789
			</Alert>
		</>
	);
}

export default ErrorComponent;
