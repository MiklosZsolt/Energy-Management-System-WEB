import React from "react";
import './App.css';
import { Component } from "react";
import 'bootstrap/dist/css/bootstrap.min.css';
import UserTable from "./components/UserTable";
import 'react-toastify/dist/ReactToastify.css';
import { ToastContainer } from 'react-toastify';
import LoginForm from './components/LoginForm';
import Admin from "./components/Admin";





class Home extends Component {

  render() {
    return (
      <div >
        <div >
          <ToastContainer autoClose={3000} />
          {/* <div className="App" > */}
            <div className="App">
              <LoginForm />
            </div>
          </div>
        </div>
      // </div>
    );
  }
}

export default Home;