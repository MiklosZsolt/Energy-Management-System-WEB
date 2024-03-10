import React from 'react';
import { useLocation, Navigate } from 'react-router-dom';
import Cookies from 'js-cookie';

export function ProtectedRoute({ element }) {
  const userData = Cookies.get('userData');
  const location = useLocation();

  if (userData) {
    // Există date de autentificare în cookie, permite accesul
    return element;
  } else {
    // Nu există date de autentificare, redirecționează către pagina de autentificare
    return <Navigate to="/LoginPage" state={{ from: location }} />;
  }
}




export default ProtectedRoute;
