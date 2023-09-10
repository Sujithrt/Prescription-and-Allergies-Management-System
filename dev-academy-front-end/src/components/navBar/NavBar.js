import React, { useState } from 'react';
import AppBar from '@mui/material/AppBar';
import Box from '@mui/material/Box';
import Toolbar from '@mui/material/Toolbar';
import IconButton from '@mui/material/IconButton';
import Typography from '@mui/material/Typography';
import MenuItem from '@mui/material/MenuItem';
import Menu from '@mui/material/Menu';
import AccountCircle from '@mui/icons-material/AccountCircle';
import MoreIcon from '@mui/icons-material/MoreVert';
import { useHistory } from 'react-router-dom';
import './../navBar/navBar.css';
import SearchBar from './../searchBar/SearchBar';
import { navBarStyle } from './../../Constants'

/**
 * Renders the Navigation Bar for the React Application 
 */
function PrimarySearchAppBar() {

    const history = useHistory();

    const [anchorEl, setAnchorEl] = useState(null);
    const [mobileMoreAnchorEl, setMobileMoreAnchorEl] = useState(null);

    const isMenuOpen = Boolean(anchorEl);
    const isMobileMenuOpen = Boolean(mobileMoreAnchorEl);

    /**
     * Opens a drop down list upon clicking the Profile Icon
     * @param {onClick} event 
     */
    const handleProfileMenuOpen = (event) => {
        setAnchorEl(event.currentTarget);
    };

    /**
     * Handles Navigation bar menu close when app is being used on mobile
     */
    const handleMobileMenuClose = () => {
        setMobileMoreAnchorEl(null);
    };

    /**
     * Handles Navigation bar menu close 
     */
    const handleMenuClose = () => {
        setAnchorEl(null);
        handleMobileMenuClose();
    };

    /**
     * Handles Navigation bar menu open when app is being used on mobile
     * @param {onClick} event 
     */
    const handleMobileMenuOpen = (event) => {
        setMobileMoreAnchorEl(event.currentTarget);
    };

    /**
     * Opens User profile when clicked
     */
    const handleOpenProfile = () => {
        setAnchorEl(null);
        handleMobileMenuClose();
        history.push('/user');
    }

    /**
     * Logs out from the Application
     */
    const logOut = () => {
        setAnchorEl(null);
        handleMobileMenuClose();
        sessionStorage.clear();
        history.push('/');
        window.location.reload()
    }

    const menuId = 'primary-search-account-menu';

    /**
     * Render menu on Larger displays
     */
    const renderMenu = (
        <Menu
            anchorEl={anchorEl}
            anchorOrigin={{
                vertical: 'top',
                horizontal: 'right',
            }}
            id={menuId}
            keepMounted
            transformOrigin={{
                vertical: 'top',
                horizontal: 'right',
            }}
            data-testid="menu"
            open={isMenuOpen}
            onClose={handleMenuClose}
        >
            <MenuItem data-testid="openUserProfile" style={navBarStyle} onClick={handleOpenProfile}>Profile</MenuItem>
            <MenuItem data-testid="clickLogout" style={navBarStyle} onClick={logOut}>Logout</MenuItem>
        </Menu>
    );

    /**
     * Render menu on mobile or smaller displays
     */
    const mobileMenuId = 'primary-search-account-menu-mobile';
    const renderMobileMenu = (
        <Menu
            anchorEl={mobileMoreAnchorEl}
            anchorOrigin={{
                vertical: 'top',
                horizontal: 'right',
            }}
            id={mobileMenuId}
            keepMounted
            transformOrigin={{
                vertical: 'top',
                horizontal: 'right',
            }}
            open={isMobileMenuOpen}
            onClose={handleMobileMenuClose}
        >
            <MenuItem style={navBarStyle} onClick={handleOpenProfile}>Profile</MenuItem>
            <MenuItem style={navBarStyle} onClick={logOut}>Logout</MenuItem>
        </Menu>
    );

    return (
        <Box sx={{ flexGrow: 1 }}>
            <AppBar position="fixed">
                <Toolbar>
                    <Typography
                        variant="h6"
                        noWrap
                        component="div"
                        sx={{ display: { xs: 'none', sm: 'block' } }}
                    >
                        <div data-testid="pnamsHeader" onClick={()=> history.push('/dashboard')} className="nav-link" to="/dashboard">PNAMS</div>
                    </Typography>
                    <SearchBar />
                    <Box sx={{ flexGrow: 1 }} />
                    <Box sx={{ display: { xs: 'none', md: 'flex' } }}>
                        <IconButton
                            data-testid="profileMenu"
                            size="large"
                            edge="end"
                            aria-label="account of current user"
                            aria-controls={menuId}
                            aria-haspopup="true"
                            onClick={handleProfileMenuOpen}
                            color="inherit"
                        >
                            <AccountCircle />
                        </IconButton>
                    </Box>
                    <Box sx={{ display: { xs: 'flex', md: 'none' } }}>
                        <IconButton
                            data-testid="profileMenuMobile"
                            size="large"
                            aria-label="show more"
                            aria-controls={mobileMenuId}
                            aria-haspopup="true"
                            onClick={handleMobileMenuOpen}
                            color="inherit"
                        >
                            <MoreIcon />
                        </IconButton>
                    </Box>
                </Toolbar>
            </AppBar>
            {renderMobileMenu}
            {renderMenu}
        </Box>
    );
}

export default PrimarySearchAppBar