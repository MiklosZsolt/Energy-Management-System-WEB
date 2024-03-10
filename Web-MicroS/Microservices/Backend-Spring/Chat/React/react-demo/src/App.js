import './App.css';
import {
  BrowserRouter as Router,
  Routes,
  Route
} from "react-router-dom";
import Home from "./Home";
import UserDevice from './components/UserDevices';
import AdminPage from './components/AdminPage';
import ChatPage from './components/ChatPage';

function App(){
      
  return ( 
    <div>
    <Router>
      <Routes>
        <Route path="/" element={<Home />} />
        <Route path="/user" element={<UserDevice />} />
        <Route path="/admin" element={<AdminPage />} />
        <Route path="/chat" element={<ChatPage />} />
      </Routes>
    </Router>
  </div>
  );

}

export default App;
