// Navbar.js
import React from 'react';
import { AppBar, Toolbar, Button } from '@mui/material';
import { Link } from 'react-router-dom';
import Cookies from 'js-cookie';
import logo from './logo.jpg'; // Înlocuiți 'path-to-your-logo.png' cu calea reală către imagine.

function NavbarClient() {

  
  const handleLogout = () => {
    Cookies.remove('userData');
  };

  return (
    <AppBar position="static">
      <Toolbar>
      <img src={logo} alt="Logo" style={{ height: '40px', marginRight: '20px' }} />
        <Button component={Link} to="." color="inherit">Home</Button>
        {/* <Button component={Link} to="/LoginPage" color="inherit" onClick={handleLogout}>Logout</Button> */}
      </Toolbar>
    </AppBar>
  );
}

export default NavbarClient;
