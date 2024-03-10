import React, { useEffect, useState } from 'react';
import { Table,Button } from 'react-bootstrap';
import axios from 'axios';
import { toast } from 'react-toastify';
// neaparat instalat!!!! npm install --save react-stomp
import SockJsClient from 'react-stomp';
import Card from 'react-bootstrap/Card';
import { useNavigate } from "react-router-dom";

const UserDevice = () => {
    const [devices, setDevices] = useState([]);
    const navigate = useNavigate();
    const handleLogout = () => {
        localStorage.removeItem('userId');
        localStorage.removeItem('username');
        window.location.href = 'http://localhost:3000';
    };

    useEffect(() => {
        const id = localStorage.getItem('userId');
        axios.get(`http://localhost:8081/device/getByUserId?userid=${id}`)
            .then((response) => {
                setDevices(response.data);
                toast.success("User data fetched successfully!");
            })
            .catch((error) => {
                console.error('Error:', error);
                toast.error("Error fetching devices for the user");
            });
    }, []);

        //parte websockets
        const [isMsg, setIsMsg] = useState(false);
        const [message, setMessage] = useState('');
        const [topics, setTopics] = useState([]);

        let onConnected = () => {
            console.log("Connected!!")
            setTopics(['/topic/message']);
          }
        
        let onDisconnect = () => {
            console.log("DISConnected!!")
        }
    
        let onMessageReceived = (msg) => {
            setMessage(msg)
            setIsMsg(true)

            //setTimeout
        }

    return (
        <div className="App">
            <Button onClick={handleLogout} variant="danger" style={{marginLeft:"20px","marginTop":"20px"}}>Logout</Button>
            <div className='user-table'>
            <h1>User Device Page</h1>
            <h2><Button onClick={() => navigate(`/chat`)}>Chat</Button></h2>
            <Table variant='success' striped bordered hover className="small-table">
                <thead>
                    <tr>
                        <th>#id</th>
                        <th>Description</th>
                        <th>Address</th>
                        <th>Max En Cons</th>
                    </tr>
                </thead>
                <tbody>
                    {devices.map((device, index) => (
                        <tr key={index}>
                            <td>{device.id}</td>
                            <td>{device.description}</td>
                            <td>{device.address}</td>
                            <td>{device.mhec}</td>
                        </tr>
                    ))}
                </tbody>
            </Table>
            </div>
            <SockJsClient
            url={'http://localhost:8082/ws-message'}
            topics={topics}
            onConnect={onConnected}
            onDisconnect={onDisconnect}
            onMessage={msg => onMessageReceived(msg)}
            debug={false}
            />
            { isMsg && < Card bg='danger' style={{ width: '20rem',marginLeft:'40%',alignContent:'center' }}>
            <div>
            <Card.Text style={{textAlign:"center",padding:"20px"}} >{message}</Card.Text>
            </div></Card>}
        </div>
    );
};

export default UserDevice;
