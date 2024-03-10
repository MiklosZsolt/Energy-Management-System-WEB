import './App.css';
import React, { useEffect, useState } from 'react';
import Cookies from 'js-cookie';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import Home from './Home';
import UserTable from './components/UserTable';
import LoginForm from './components/LoginForm';
import Admin from './components/Admin';
import Device from './components/Device';
import NavbarClient from './components/NavbarClient';
import NavbarClient2 from './components/NavbarClient2';
import Navbar from './components/Navbar';
import { ProtectedRoute } from './components/ProtectedRoute';
import { Navigate } from 'react-router-dom';
import Client from './components/Client';
import ChatPage from './components/ChatPage'
function App() {
  const [isLoggedIn, setIsLoggedIn] = useState(false);
  const [userRole, setUserRole] = useState(""); 

  useEffect(() => {
    const userData = Cookies.get('userData');

    if (userData) {
      const user = JSON.parse(userData);
      setIsLoggedIn(true);
      setUserRole(user.role);
    } else {
      setIsLoggedIn(false);
    }
  }, []);
  console.log(userRole);

  const handleLogout = () => {
    Cookies.remove('userData');
    setIsLoggedIn(false);
  };

  return (
    <div className="App" >
    <div>
      <Router>
        {isLoggedIn ? (
          userRole === "admin" ? (
            <Navbar onLogout={handleLogout} />
          ) : userRole === "client" ? (
            <NavbarClient2 onLogout={handleLogout} />
          ) : (
            <Navbar onLogout={handleLogout} />
          )
        ) : (
          <NavbarClient />
        )}
        <Routes>
          <Route path="/" element={<Home />} />
          <Route path="/users" element={<UserTable />} />
          <Route path="/LoginPage" element={<LoginForm />} />
          <Route path="/Device" element={<ProtectedRoute element={<Device />} />} />
          <Route path="/admin" element={<ProtectedRoute element={<Admin />} />} />
          <Route path="/client" element={<ProtectedRoute element={<Client />} />} />
          <Route path="/chat" element={<ChatPage />} />

        </Routes>
      </Router>
    </div>
     </div>
  );
}

export default App;
