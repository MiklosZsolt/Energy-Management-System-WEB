// Navbar.js
import React from 'react';
import { AppBar, Toolbar, Button } from '@mui/material';
import { Link } from 'react-router-dom';
import Cookies from 'js-cookie';
import logo from './logo.jpg'; // Înlocuiți 'path-to-your-logo.png' cu calea reală către imagine.

function NavbarClient2() {

  
  const handleLogout = () => {
    Cookies.remove('userData');
    window.location.href = "/LoginPage";

  };

  return (
    <AppBar position="static">
      <Toolbar>
      <img src={logo} alt="Logo" style={{ height: '40px', marginRight: '20px' }} />

      <Button color="inherit" onClick={handleLogout} style={{ marginLeft: 'auto' }}>Logout</Button>
      </Toolbar>
    </AppBar>
  );
}

export default NavbarClient2;
