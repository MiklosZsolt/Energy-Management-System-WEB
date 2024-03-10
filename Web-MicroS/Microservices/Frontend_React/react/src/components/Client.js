import React, { useEffect, useState } from "react";
import { DataGrid } from '@mui/x-data-grid';
import { toast, ToastContainer } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';
import axios from "axios";
import Cookies from 'js-cookie';
import SockJsClient from 'react-stomp';
import { Bar } from 'react-chartjs-2';
import Chart from 'chart.js/auto';
import Chat from "./ChatPage";
import { Button } from "react-bootstrap";
import { useNavigate } from "react-router-dom";
function Client() {
  const [tableColor, setTableColor] = useState('');
  const [isLoggedIn, setIsLoggedIn] = useState(false);
  const [userRole, setUserRole] = useState(""); 
  const [deviceData, setDeviceData] = useState([]);
  const [topics, setTopics] = useState(['/topic/message']);
  const [chartData, setChartData] = useState({});
  const userData = Cookies.get('userData');
  const usernameCookie = JSON.parse(userData);
  const [chartInstance, setChartInstance] = useState(null);
  const navigate = useNavigate();

  const onConnected = () => {
    console.log("Connected!!");
    setTopics(prevTopics => [...prevTopics, '/topic/message']);
  };
  
  const onDisconnect = () => {
    console.log("DISConnected!!");
  };

  const onMessageReceived = (msg) => {
    if (msg === "over") {
      setTableColor('red');
      toast.error("A fost depasit bugetul");
    }
    console.log(msg);
  };

  const getAuthHeaders = () => {
    const token = Cookies.get('jwtToken');
    return token ? {
      'Accept': 'application/json',
      'Content-Type': 'application/json; charset=UTF-8',
      'Authorization': `Bearer ${token}`
    } : {};
  };
  
  const fetchMeasurementData = () => {
    const headers = getAuthHeaders();
  
    if (headers.Authorization) {
      axios.get('http://localhost:8085/person/allM', { headers })
        .then((response) => {
          if (Array.isArray(response.data) && response.data.length > 0) {
            const measurementData = response.data;
            const labels = measurementData.map((data) => data.iddevice);
            const dataValues = measurementData.map((data) => data.val);
  
            // Actualizează starea chartData
            setChartData({
              labels: labels,
              datasets: [
                {
                  label: 'Measurement Values',
                  data: dataValues,
                  backgroundColor: 'rgba(75,192,192,0.6)',
                  borderColor: 'rgba(75,192,192,1)',
                  borderWidth: 1,
                },
              ],
            });
          } else {
            toast.error("Received data is empty or not an array");
          }
        })
        .catch((error) => {
          toast.error("Error fetching measurement data: " + error.message);
        });
    } else {
      toast.error("Accesul neautorizat. Vă rugăm să vă autentificați.");
    }
  };
  
  
  useEffect(() => {
    const userData = Cookies.get('userData');
  
    if (userData) {
      const user = JSON.parse(userData);
      setIsLoggedIn(true);
      setUserRole(user.role);
  
      let isRendered = true;
  
      const headers = getAuthHeaders();
  
      axios.get(`http://localhost:8085/person/getByUsernameOwner/${user.username}`, { headers })
        .then((response) => {
          if (isRendered) {
            if (Array.isArray(response.data)) {
              setDeviceData(response.data);
              toast.success("User data fetched successfully!");
            } else {
              toast.error("Received data is not an array");
            }
          }
        })
        .catch((error) => {
          if (isRendered) {
            toast.error("Error fetching user data: " + error.message);
          }
        });
  
      fetchMeasurementData();
  
      return () => {
        isRendered = false;
      };
    } else {
      setIsLoggedIn(false);
    }
  }, []);
  

  const columns = [
    { field: 'id', headerName: 'ID', width: 70 },
    { field: 'description', headerName: 'Description', width: 130 },
    { field: 'address', headerName: 'Address', width: 130 },
    { field: 'mhec', headerName: 'MHEC', width: 130 },
  ];

  return (
    <div>
      <ToastContainer />

      <hr></hr>
      <h1 style={{ textAlign: 'left', marginLeft:'80px' }}>Welcome  {usernameCookie.username}</h1>
      <hr></hr>
      <Button onClick={() => navigate(`/chat`)}>Chat</Button>
      <br></br>
      <div style={{ height: 300, width: '90%' , margin: 'auto' }}>
        <div className="custom-datagrid">
          <DataGrid 
            rows={deviceData}
            columns={columns}
            rowStyle={{
              // Define your row styles here if needed
            }}
            initialState={{
              pagination: {
                paginationModel: { page: 0, pageSize: 5 },
              },
            }}
            pageSizeOptions={[5, 10]}
            className={tableColor}
          />
        </div>
      </div>

      <div style={{ height: 200, width: '90%', margin: 'auto' }}>
        {chartData.labels && chartData.labels.length > 0 ? (
          <Bar
            data={chartData}
            options={{
              maintainAspectRatio: false,
              responsive: true,
              scales: {
                x: {
                  title: {
                    display: true,
                    text: 'Devices',
                  },
                },
                y: {
                  title: {
                    display: true,
                    text: 'MHEC Values',
                  },
                },
              },
            }}
          />
        ) : (
          <p>No data available for chart</p>
        )}
      </div>

      <SockJsClient
        url={'http://localhost:8082/ws-message'}
        topics={topics}
        onConnect={onConnected}
        onDisconnect={onDisconnect}
        onMessage={msg => onMessageReceived(msg)}
        debug={false}
      />
    </div>
  );
}

export default Client;