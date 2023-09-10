import React, { useState } from 'react';
import { userLogin } from '../../api/AuthenticationService';
import { Alert } from 'react-bootstrap';
import Avatar from '@mui/material/Avatar';
import Button from '@mui/material/Button';
import CssBaseline from '@mui/material/CssBaseline';
import TextField from '@mui/material/TextField';
import Paper from '@mui/material/Paper';
import Box from '@mui/material/Box';
import Grid from '@mui/material/Grid';
import LockOutlinedIcon from '@mui/icons-material/LockOutlined';
import Typography from '@mui/material/Typography';
import { createTheme, ThemeProvider } from '@mui/material/styles';
import { useHistory } from 'react-router-dom';

const theme = createTheme();

/**
 * Renders the Login page which allows users to login to the applicaiton. Makes API calls to
 * authenticate users.
 */
const LoginPage = () => {
    const [values, setValues] = useState({
        username: '',
        password: ''
    });
    const [error, setError] = useState("");
    const history = useHistory();

    /**
     * Accepts user input and sends it for authentication by making a POST API call and if user is
     * valid i.e. registered, pushes user to the dashboard page. If user is not valid, then shows an 
     * error message
     * 
     * @param {onSubmit} evt 
     */
    const handleSubmit = (evt) => {
        evt.preventDefault();
        if (values.username === "") {
            setError("Please enter user name");
        } else if (values.password === "") {
            setError("Please enter password");
        } else {
            userLogin(values)
                .then((response) => {
                    if (response.status === 202) {
                        sessionStorage.setItem('USER_KEY', response.data.token);
                        history.push('/dashboard');
                    } else {
                        setError("Invalid Username or Password")
                    }
                })
                .catch((err) => {
                    history.push("/error");
                });
        }
    }

    /**
     * Updates state of username and password when updated by user
     * @param {onChange} e 
     */
    const handleChange = (e) => {
        setValues(values => ({
            ...values,
            [e.target.name]: e.target.value
        }));
    };

    return (
        <ThemeProvider theme={theme}>
            <Grid container component="main" sx={{ height: '100vh' }}>
                <CssBaseline />
                <Grid
                    item
                    xs={false}
                    sm={4}
                    md={7}
                    sx={{
                        backgroundImage: 'url(https://wallpaperbat.com/img/437364-medicine-wallpaper-top-free-medicine-background.jpg)',
                        backgroundSize: 'cover',
                        backgroundPosition: 'center',
                    }}
                />
                <Grid item xs={12} sm={8} md={5} component={Paper} elevation={6} square>
                    <Box
                        sx={{
                            my: 8,
                            mx: 4,
                            display: 'flex',
                            flexDirection: 'column',
                            alignItems: 'center',
                        }}
                    >
                        <Avatar sx={{ m: 1, bgcolor: 'secondary.main' }}>
                            <LockOutlinedIcon />
                        </Avatar>
                        <Typography component="h1" variant="h5">
                            Sign in
                        </Typography>
                        <Box component="form" noValidate onSubmit={handleSubmit} sx={{ mt: 1 }}>
                            {error && <Alert style={{ marginTop: '20px' }} variant="danger">{error}</Alert>}
                            <TextField
                                margin="normal"
                                required
                                fullWidth
                                data-testid="username"
                                label="Username"
                                name="username"
                                autoFocus
                                value={values.username}
                                onChange={handleChange}
                            />
                            <TextField
                                margin="normal"
                                required
                                fullWidth
                                name="password"
                                label="Password"
                                type="password"
                                data-testid="password"
                                value={values.password}
                                minLength={8}
                                autoComplete="current-password"
                                onChange={handleChange}
                            />
                            <Button
                                data-testid="submitButton"
                                type="submit"
                                fullWidth
                                onClick={handleSubmit}
                                variant="contained"
                                sx={{ mt: 3, mb: 2 }}
                            >
                                Sign In
                            </Button>
                        </Box>
                    </Box>
                </Grid>
            </Grid>
        </ThemeProvider>
    );
}

export default LoginPage;