import React from 'react';
import { AppBar, Toolbar, Button } from '@mui/material';
import { Link } from 'react-router-dom';
import Cookies from 'js-cookie';

// Importați imaginea logo-ului sau specificați calea către ea.
import logo from './logo.jpg'; // Înlocuiți 'path-to-your-logo.png' cu calea reală către imagine.

function Navbar() {
  const handleLogout = () => {
    Cookies.remove('userData');
    Cookies.remove('jwtToken');
    window.location.href = "/LoginPage";
  };

  return (
    <AppBar position="static">
      <Toolbar>
        {/* Adăugați imaginea logo-ului aici */}
        <img src={logo} alt="Logo" style={{ height: '40px', marginRight: '20px' }} />
        
        {/* Butonul pentru navigare și butonul de Logout */}
        <Button component={Link} to="/admin" color="inherit">Persons</Button>
        <Button component={Link} to="/device" color="inherit">Devices</Button>
        <Button color="inherit" onClick={handleLogout} style={{ marginLeft: 'auto' }}>Logout</Button>
      </Toolbar>
    </AppBar>
  );
}

export default Navbar;
