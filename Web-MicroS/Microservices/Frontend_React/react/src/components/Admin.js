import React, { useEffect, useState } from "react";
import { DataGrid } from '@mui/x-data-grid';
import { HOST_PERSON } from "../Hosts";
import { toast } from 'react-toastify';
import axios from "axios";
import { Link } from 'react-router-dom';
import Cookies from 'js-cookie';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faTrash, faEdit } from '@fortawesome/free-solid-svg-icons';
import EditUserModal from './EditUserModal'
import Chat from "./ChatPage";
import { Button } from "react-bootstrap";
import { useNavigate } from "react-router-dom";
function Admin() {
  const navigate = useNavigate();
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
  const [userData, setUserData] = useState([]);
  const [editingUser, setEditingUser] = useState(null);

  // Update
  const [NewUserUpdate, setNewUserUpdate] = useState({
    id: '',
    username: '',
    password: '',
    role: '',
  });

  const handleInputUpdateChange = (e) => {
    const { name, value } = e.target;
    setNewUserUpdate({
      ...NewUserUpdate,
      [name]: value,
    });
  };

  const handleUpdateUser = () => {
    const userId = NewUserUpdate.id;
  
    // Extrage tokenul JWT din localStorage sau Cookies
    const jwtToken = localStorage.getItem('jwtToken') || Cookies.get('jwtToken');
  
    // Configurați headers pentru cererea axios
    const config = {
      headers: {
        'Authorization': `Bearer ${jwtToken}`,
        'Content-Type': 'application/json' // Adaugă acest header dacă serverul tău necesită
      }
    };
  
    axios.get(`http://localhost:8085/person/getById?id=${userId}`, config)
      .then((response) => {
        console.log("lofasz" + userId);

        const oldUsername = response.data.username;
        const newUsername = NewUserUpdate.username;
        console.log(`Numele vechi al utilizatorului: ${oldUsername}`);
        console.log(`Numele nou al utilizatorului: ${newUsername}`);
  
        const updatedUser = {
          username: newUsername,
          password: NewUserUpdate.password,
          role: NewUserUpdate.role,
        };
  
        axios.post(`http://localhost:8085/person/updatePerson/${userId}`, updatedUser, config)
        .then((userResponse) => {
            console.log(userResponse.data.message);
            setNewUserUpdate({ id: '', username: '', password: '', role: '' });
  
            // După actualizarea utilizatorului, actualizați dispozitivele
            axios.post(`http://localhost:8081/device/updateOwner`, {
              oldUsername: oldUsername,
              newUsername: newUsername,
            }, config)
              .then((deviceResponse) => {
                console.log(deviceResponse.data);
                // Reîncărcați pagina aici, după ce toate acțiunile au fost finalizate cu succes
                window.location.reload();
              })
              .catch((deviceError) => {
                console.error('Eroare la actualizarea dispozitivelor:', deviceError);
              });
          })
          .catch((userError) => {
            console.error('Eroare la actualizarea utilizatorului:', userError);
          });
      })
      .catch((getUserError) => {
        console.error('Eroare la obținerea utilizatorului:', getUserError);
      });
};

  
  

  // Insert
  const [newUser, setNewUser] = useState({
    username: '',
    password: '',
    role: '',
  });

  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setNewUser({
      ...newUser,
      [name]: value,
    });
  };

  const handleInsertUser = () => {
    const newUserObject = {
      username: newUser.username,
      password: newUser.password,
      role: newUser.role,
    };
  
    // Extrage tokenul JWT din localStorage sau Cookies
    const jwtToken = localStorage.getItem('jwtToken') || Cookies.get('jwtToken');
  
    // Configurați headers pentru cererea axios
    const config = {
      headers: {
        'Authorization': `Bearer ${jwtToken}`
      }
    };
  
    axios.post(`http://localhost:8085/person/save`, newUserObject, config)
      .then((response) => {
        console.log(response.data.message);
        setNewUser({ username: '', password: '', role: '' });
        window.location.reload();
      })
      .catch((error) => {
        console.error('Eroare la inserarea utilizatorului:', error);
      });
  };
  

  // Delete
  const handleDeleteUser = (userId, username) => {
    // Extrage tokenul JWT din localStorage sau Cookies
    const jwtToken = localStorage.getItem('jwtToken') || Cookies.get('jwtToken');
    console.log(`JWT Token: ${jwtToken}`);

    // Configurați headers pentru cererea axios
    const config = {
      headers: {
        'Authorization': `Bearer ${jwtToken}`
      }
    };
  
    axios.delete(`http://localhost:8085/person/delete/${userId}`, config)
      .then((response) => {
        toast.success("Utilizator șters cu succes");
        window.location.reload();
      })
      .catch((error) => {
        toast.error("Eroare la ștergerea utilizatorului");
      });
  
    axios.delete(`http://localhost:8085/person/deleteDeviceByUsername/${username}`, config)
      .then((response) => {
        toast.success("Dispozitive asociate utilizatorului au fost șterse");
        window.location.reload();
      })
      .catch((error) => {
        toast.error("Eroare la ștergerea dispozitivelor asociate utilizatorului");
      });
  };
  

  const handleEditUser = (user) => {
    setEditingUser(user);
  };

  const handleSaveEditedUser = (editedUser) => {
    setUserData((prevUserData) =>
      prevUserData.map((user) =>
        user.id === editedUser.id ? editedUser : user
      )
    );
  };

  const getAuthHeaders = () => {
    const token = Cookies.get('jwtToken');
    if (token) {
      return {
        'Accept': 'application/json',
        'Content-Type': 'application/json; charset=UTF-8',
        'Authorization': `Bearer ${token}`
      };
    }
    return {};
  };
  
  useEffect(() => {
    let isRendered = true;
  
    const headers = getAuthHeaders();
  
    if (headers.Authorization) {
      axios.get(`http://localhost:8085/person/all`, { headers })
        .then((response) => {
          if (isRendered) {
            setUserData(response.data);
            toast.success("Datele utilizatorilor au fost preluate cu succes!");
          }
        })
        .catch((error) => {
          if (isRendered) {
            toast.error("Eroare la preluarea datelor utilizatorilor");
          }
        });
    } else {
      if (isRendered) {
        toast.error("Accesul neautorizat. Vă rugăm să vă autentificați.");
      }
    }
  
    return () => {
      isRendered = false;
    };
  }, []);
  

  
  const columns = [
    { field: 'id', headerName: 'ID', width: 70 },
    { field: 'username', headerName: 'Username', width: 130 },
    { field: 'password', headerName: 'Password', width: 130 },
    { field: 'role', headerName: 'Role', width: 130 },
    {
      field: 'actions',
      headerName: 'Actions',
      width: 160,
      renderCell: (params) => (
        <div>
          <button onClick={() => handleDeleteUser(params.row.id, params.row.username)} className="btn btn-danger">
            <FontAwesomeIcon icon={faTrash} />
          </button>
         
        </div>
      ),
    },
  ];

  const rows = userData.map((user) => ({
    id: user.id,
    username: user.username,
    password: user.password,
    role: user.role,
  }));

  const storedUserData = Cookies.get('userData');
  let usernameCookie = "";

  if (storedUserData) {
    const userData = JSON.parse(storedUserData);
    usernameCookie = userData.username;
    console.log("Numele utilizatorului:", usernameCookie);
  } else {
    console.log("Cookie-ul 'userData' nu există sau nu conține date.");
  }

  return (
    <div>
     
      <hr></hr>
      <h1 style={{ textAlign: 'left', marginLeft:'80px' }}>Welcome {usernameCookie}</h1>
      <Button onClick={() => navigate(`/chat`)}>Chat</Button>
      <hr></hr>
      <br></br>
      <div style={{ height: 300, width: '90%' , margin: 'auto' }}>
        <div></div>
      <DataGrid
        rows={rows}
        columns={columns}
        initialState={{
          pagination: {
           
          },
        }}
        pageSizeOptions={[5, 10]}
        style={{ border: '2px solid #000' }}        
      />
      <div>
        <form>
          <h2>Insert user</h2>
          <input
            type="text"
            name="username"
            value={newUser.username}
            onChange={handleInputChange}
            placeholder="Username"
          />
          <input
            type="password"
            name="password"
            value={newUser.password}
            onChange={handleInputChange}
            placeholder="Password"
          />
          <input
            type="text"
            name="role"
            value={newUser.role}
            onChange={handleInputChange}
            placeholder="Role"
          />
          <button type="button" onClick={handleInsertUser}>
            Adăugare
          </button>
        </form>
      </div>
      <div>
        <h2>Update user</h2>
        <hr width></hr>
        <form>
          <input
            type="text"
            name="id"
            value={NewUserUpdate.id}
            onChange={handleInputUpdateChange}
            placeholder="ID"
          />
          <input
            type="text"
            name="username"
            value={NewUserUpdate.username}
            onChange={handleInputUpdateChange}
            placeholder="Username"
          />
          <input
            type="password"
            name="password"
            value={NewUserUpdate.password}
            onChange={handleInputUpdateChange}
            placeholder="Password"
          />
          <input
            type="text"
            name="role"
            value={NewUserUpdate.role}
            onChange={handleInputUpdateChange}
            placeholder="Role"
          />
          <button type="button" onClick={handleUpdateUser}>
            Actualizare
          </button>
        </form>
      </div>
    </div>
    </div>
  );
}

export default Admin;
