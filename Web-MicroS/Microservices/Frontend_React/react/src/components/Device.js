import React, { useEffect, useState } from "react";
import { DataGrid } from "@mui/x-data-grid";
import {  toast } from 'react-toastify';
import axios from "axios";
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faTrash, faEdit  } from '@fortawesome/free-solid-svg-icons';
import Cookies from 'js-cookie';

function Device() {

  const userData = Cookies.get('userData');
  const usernameCookie = JSON.parse(userData);
  console.log(usernameCookie.username);




  const [deviceData, setDeviceData] = useState([]);
  const [newDevice, setNewDevice] = useState({
    description: "",
    address: "",
    mhec: "",
    usernameOwner: "", // Adăugat usernameOwner
  });
  const [NewDeviceUpdate, setNewDeviceUpdate] = useState({
    id: "", // Am adăugat ID-ul pentru actualizare
    description: "",
    address: "",
    mhec: "",
    usernameOwner: "",
  });

  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setNewDevice({
      ...newDevice,
      [name]: value,
    });
  };

  const handleInsertDevice = () => {
    const headers = getAuthHeaders();

    // Verificați dacă există o persoană cu numele specificat în câmpul `usernameOwner`
    axios
      .post(`http://localhost:8085/person/CheckifUserClient/${newDevice.usernameOwner}`, {}, { headers })
      .then((response) => {
        if (response.data && response.data.role === "client") {
          axios
            .post(`http://localhost:8085/person/saveDevice`, newDevice, { headers })
            .then((response) => {
              console.log(response.data.message);
              setNewDevice({
                description: "",
                address: "",
                mhec: "",
                usernameOwner: "",
              });
              window.location.reload();
            })
            .catch((error) => {
              console.error("Eroare la inserarea dispozitivului:", error);
            });
        } else {
          toast.error(
            "Nu există o persoană cu acest nume sau persoana nu are rolul 'client'."
          );
        }
      })
      .catch((error) => {
        toast.error("Eroare la verificarea numelui de utilizator:");
      });
};


  const handleInputUpdateChange = (e) => {
    const { name, value } = e.target;
    setNewDeviceUpdate({
      ...NewDeviceUpdate,
      [name]: value,
    });
  };

  const handleUpdateDevice = () => {
    const headers = getAuthHeaders();

    // Verificați dacă există o persoană cu numele specificat în câmpul `usernameOwner`
    axios
      .post(`http://localhost:8085/person/CheckifUserClient/${NewDeviceUpdate.usernameOwner}`, {}, { headers })
      .then((response) => {
        if (response.data.role === "client") {
          // Dacă există o singură persoană cu acel nume și are rolul "client"
          const deviceId = NewDeviceUpdate.id;
          const updatedDevice = {
            description: NewDeviceUpdate.description,
            address: NewDeviceUpdate.address,
            mhec: NewDeviceUpdate.mhec,
            usernameOwner: NewDeviceUpdate.usernameOwner,
          };
  
          axios
            .post(`http://localhost:8085/person/updateDevice/${deviceId}`, updatedDevice, { headers })
            .then((response) => {
              console.log(response.data.message);
              setNewDeviceUpdate({
                id: "", // Resetăm ID-ul
                description: "",
                address: "",
                mhec: "",
                usernameOwner: "",
              });
              window.location.reload();
            })
            .catch((error) => {
              console.error("Eroare la actualizarea dispozitivului:", error);
            });
        } else {
          toast.error("Nu există o persoană cu acest nume sau persoana nu are rolul 'client'.");
        }
      })
      .catch((error) => {
        console.error("Eroare la verificarea numelui de utilizator:", error);
      });
};

  

const handleDeleteDevice = (deviceId) => {
  const headers = getAuthHeaders(); // Obțineți anteturile de autorizare

  axios
    .delete(`http://localhost:8085/person/deleteDevice/${deviceId}`, { headers })
    .then((response) => {
      window.location.reload();
    })
    .catch((error) => {
      toast.error("Eroare la ștergerea dispozitivului");
    });
};


  const getAuthHeaders = () => {
    const token = Cookies.get('jwtToken');
    return token ? {
      'Accept': 'application/json',
      'Content-Type': 'application/json; charset=UTF-8',
      'Authorization': `Bearer ${token}`
    } : {};
  };
  
  useEffect(() => {
    let isRendered = true;
    const headers = getAuthHeaders();
  
    if (headers.Authorization) {
      axios.get(`http://localhost:8085/person/allDevice`, { headers })
        .then((response) => {
          if (isRendered) {
            setDeviceData(response.data);
            toast.success("Datele dispozitivului au fost preluate cu succes!");
          }
        })
        .catch((error) => {
          if (isRendered) {
            toast.error("Eroare la preluarea datelor dispozitivului");
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
    { field: 'description', headerName: 'Description', width: 130 },
    { field: 'address', headerName: 'Adress', width: 130 },
    { field: 'mhec', headerName: 'MHEC', width: 130 },
    { field: 'usernameOwner', headerName: 'Owner', width: 130 },
    {
      field: 'update',
      headerName: 'Update',
      width: 100,
      
    },
    {
      field: 'delete',
      headerName: 'Delete',
      width: 100,
      renderCell: (params) => (
        <div>
          <button onClick={() => handleDeleteDevice(params.row.id)} className="btn btn-danger">
  <FontAwesomeIcon icon={faTrash} />
</button>
        </div>
            ),
    },
  ];

  const rows = deviceData.map((device) => ({
    id: device.id,
    description: device.description,
    address: device.address,
    mhec: device.mhec,
    usernameOwner: device.usernameOwner,
  }));

  return (
    <div>
     
    <hr></hr>
  <h1 style={{ textAlign: 'left', marginLeft:'80px' }}>Welcome {usernameCookie.username}</h1>
    <hr></hr>
    <br></br>
    <div style={{ height: 300, width: '90%' , margin: 'auto' }}>
      <DataGrid
        rows={rows}
        columns={columns}
        initialState={{
          pagination: {
          },
        }}
        pageSizeOptions={[5, 10]}
        style={{ border: '2px solid #000' }}
        //checkboxSelection
      />
      {/* <DataGrid
        rows={rows}
        columns={columns}
        pageSize={5}
        checkboxSelection
      /> */}
      <form>
        <h2>Insert device</h2>
        <input
          type="text"
          name="description"
          value={newDevice.description}
          onChange={handleInputChange}
          placeholder="Description"
        />
        <input
          type="text"
          name="address"
          value={newDevice.address}
          onChange={handleInputChange}
          placeholder="Adress"
        />
        <input
          type="text"
          name="mhec"
          value={newDevice.mhec}
          onChange={handleInputChange}
          placeholder="MHEC"
        />
        <input
          type="text"
          name="usernameOwner"
          value={newDevice.usernameOwner}
          onChange={handleInputChange}
          placeholder="Owner"
        />
        <button type="button" onClick={handleInsertDevice}>
          Adăugare
        </button>
      </form>
      <h2>Update device</h2>
      <form>
        <input
          type="text"
          name="id"
          value={NewDeviceUpdate.id}
          onChange={handleInputUpdateChange}
          placeholder="ID"
        />
        <input
          type="text"
          name="description"
          value={NewDeviceUpdate.description}
          onChange={handleInputUpdateChange}
          placeholder="Description"
        />
        <input
          type="text"
          name="address"
          value={NewDeviceUpdate.address}
          onChange={handleInputUpdateChange}
          placeholder="Adress"
        />
        <input
          type="text"
          name="mhec"
          value={NewDeviceUpdate.mhec}
          onChange={handleInputUpdateChange}
          placeholder="MHEC"
        />
        <input
          type="text"
          name="usernameOwner"
          value={NewDeviceUpdate.usernameOwner}
          onChange={handleInputUpdateChange}
          placeholder="Owner"
        />
        <button type="button" onClick={handleUpdateDevice}>
          Actualizare
        </button>
      </form>
    </div>
    </div>
  );
}

export default Device;