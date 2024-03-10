import React, { useState } from "react";
import axios from "axios";
import Cookies from 'js-cookie';
import { HOST_PERSON } from "../Hosts";
import "./AuthForm.css";
import {  toast } from 'react-toastify';
import { ToastContainer } from "react-toastify";


export default function AuthForm(props) {


  
  let [authMode, setAuthMode] = useState("signin");

  const changeAuthMode = () => {
    setAuthMode(authMode === "signin" ? "signup" : "signin");
  }

  const [nume, setNume] = useState('');
  const [parola, setParola] = useState('');

  const handleNumeChange = (event) => {
    setNume(event.target.value);
  };

  const handleParolaChange = (event) => {
    setParola(event.target.value);
  };

  const handleSubmit = async (event) => {
    event.preventDefault();
    const username = nume; // Înlocuiește cu valoarea corectă a numelui de utilizator
    const password = parola; // Înlocuiește cu valoarea corectă a parolei
  
    try {
      // Face cererea POST către server
      const response = await axios.post(`http://localhost:8085/person/login`, { username, password });
      const data = response.data;
  
      // Verifică răspunsul și setează informațiile necesare în localStorage și cookie-uri
      if (data && data.token) {
        const { token, role } = data;
  
        Cookies.set('jwtToken', token);
        localStorage.setItem('userData', JSON.stringify({ role, username }));
        Cookies.set('userData', JSON.stringify({ role, username }));
        console.log(token);
        // Redirecționează în funcție de rolul utilizatorului
        if (role === "[admin]") {
          window.location.href = "/Admin";
        } else if (role === "[client]") {
          window.location.href = "/client";
        }
      } else {
        toast.error("Invalid credentials!");
      }
    } catch (error) {
      console.error("Error while making requests:", error);
      toast.error("Authentication failed!");
    }
  };
  

    
    //   const storedUserData = Cookies.get('userData');
    //   if (storedUserData) {
    //     const userData = JSON.parse(storedUserData);
    //     console.log("Detalii ale utilizatorului:", userData);
    //   }
    // } catch (error) {
    //   console.error("Eroare la efectuarea cererilor:", error);
    // }
    
    
 


//   const handleSubmit = async (event) => {
//     event.preventDefault();
//     const username = nume;
//     const password = parola;

//     try {
//         const response = await axios.post(`${HOST_PERSON}/getByUserandPass`, { username, password });
//         const data = response.data;
        
//         if (data === "admin") {
//             localStorage.setItem('userData', JSON.stringify({ role: 'admin', username: username }));
//             Cookies.set('userData', JSON.stringify({ role: 'admin', username: username }));
//             window.location.href = "/Admin";
//         } else if (data === "client") {
//             localStorage.setItem('userData', JSON.stringify({ role: 'client', username: username }));
//             Cookies.set('userData', JSON.stringify({ role: 'client', username: username }));
//             window.location.href = "/client";
//         } else {
//             toast.error("Invalid credentials!");
//         }
//     } catch (error) {
//         console.error("Eroare la efectuarea cererilor:", error);
//         toast.error("Authentication failed!");
//     }
// };



  return (
    
    <div className="Auth-form-container">
      <div className="Auth-frame">
        <form className="Auth-form" onSubmit={handleSubmit}>
          <div className="Auth-form-content">
            <h3 className="Auth-form-title">
              {authMode === "signin" ? "Sign In" : "Sign Up"}
            </h3>
            <div className="text-center">
              {authMode === "signin" ? (
                <p>
                  Not registered yet?{" "}
                  <span className="link-primary" onClick={changeAuthMode}>
                    Sign Up
                  </span>
                </p>
              ) : (
                <p>
                  Already registered?{" "}
                  <span className="link-primary" onClick={changeAuthMode}>
                    Sign In
                  </span>
                </p>
              )}
            </div>
            <div>
              <label>Username</label>
              <input
                type="text"
                className="form-control mt-1"
                placeholder="Username"
                value={nume}
                onChange={handleNumeChange}
              />
            </div>
            <div className="form-group mt-3">
              <label>Password</label>
              <input
                type="password"
                className="form-control mt-1"
                placeholder="Password"
                value={parola}
                onChange={handleParolaChange}
              />
            </div>
            <div className="d-grid gap-2 mt-3">
              <button type="submit" className="btn btn-primary">
                Submit
              </button>
            </div>
            <p className="text-center mt-2">
              Forgot <a href="#">password?</a>
            </p>
          </div>
        </form>
      </div>
    </div>
  
  );
}


// import React, { useState } from "react";
// import axios from "axios";
// import { HOST_PERSON } from "../Hosts";
// import "./AuthForm.css"; // Asigură-te că importezi fișierul CSS cu stilurile

// export default function AuthForm(props) {
//   let [authMode, setAuthMode] = useState("signin");

//   const changeAuthMode = () => {
//     setAuthMode(authMode === "signin" ? "signup" : "signin");
//   }

//   const [nume, setNume] = useState('');
//   const [parola, setParola] = useState('');


//   const handleNumeChange = (event) => {
//     setNume(event.target.value);
//   };
  
//   const handleParolaChange = (event) => {
//     setParola(event.target.value);
//   };
  

//   const handleSubmit = async (event) => {
//     event.preventDefault();

//     // Preia numele și parola introduse din starea componentei
//     const username = nume;
//     const password = parola;
//     // console.log(nume);
//     // console.log(parola);


//     try {
//       const response = await axios.get(`${HOST_PERSON}/getByUserandPass?username=${username}&password=${password}`);

//       const data = response.data;
//       console.log(data);

//       if (data === "admin") {
//         window.location.href = "/Admin";
//       } else if (data === "client") {
//         window.location.href = "/client";
//       }
//     } catch (error) {
//       console.error("Eroare la efectuarea cererilor:", error);
//     }
//   };





//   return (
//     <div className="Auth-form-container">
//       <div className="Auth-frame"> {/* Adaugă containerul cu cadru */}
//       <form className="Auth-form" onSubmit={handleSubmit}>
//           <div className="Auth-form-content">
//             <h3 className="Auth-form-title">
//               {authMode === "signin" ? "Sign In" : "Sign Up"}
//             </h3>
//             <div className="text-center">
//               {authMode === "signin" ? (
//                 <p>
//                   Not registered yet?{" "}
//                   <span className="link-primary" onClick={changeAuthMode}>
//                     Sign Up
//                   </span>
//                 </p>
//               ) : (
//                 <p>
//                   Already registered?{" "}
//                   <span className="link-primary" onClick={changeAuthMode}>
//                     Sign In
//                   </span>
//                 </p>
//               )}
//             </div>
//             {authMode === "signup" && (
//               <div className="form-group mt-3">
//                 <label>Full Name</label>
//                 <input
//                   type="text"
//                   className="form-control mt-1"
//                   placeholder="e.g Jane Doe"
//                 />
//               </div>
//             )}
//             <div>
//               <label>Username</label>
//               <input
//                 type="text"
//                 className="form-control mt-1"
//                 placeholder="Username"
//                 value={nume}
//                 onChange={handleNumeChange}
//               />
//             </div>
//             <div className="form-group mt-3">
//               <label>Password</label>
//               <input
//                 type="password"
//                 className="form-control mt-1"
//                 placeholder="Password"
//                 value={parola}
//                 onChange={handleParolaChange}
//               />
//             </div>

//             <div className="d-grid gap-2 mt-3">
//               <button type="submit" className="btn btn-primary">
//                 Submit
//               </button>
//             </div>
//             <p className="text-center mt-2">
//               Forgot <a href="#">password?</a>
//             </p>
//           </div>
//         </form>
//       </div>
//     </div>
//   );
// }
