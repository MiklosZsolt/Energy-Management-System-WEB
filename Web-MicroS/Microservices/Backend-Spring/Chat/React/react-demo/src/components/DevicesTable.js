import React, { useEffect, useState } from 'react';
import { Table,Button } from 'react-bootstrap';
import axios from 'axios';
import { toast } from 'react-toastify';


const UserDevice = () => {
    const [devices, setDevices] = useState([]);

    const handleLogout = () => {
        localStorage.removeItem('userId');
        localStorage.removeItem('username');
        window.location.href = 'http://localhost:3000';
    };

    useEffect(() => {
        axios.get(`http://localhost:8081/device/all`)
            .then((response) => {
                setDevices(response.data);
                toast.success("User data fetched successfully!");
            })
            .catch((error) => {
                console.error('Error:', error);
                toast.error("Error fetching devices for the user");
            });
    }, []);


    return (
        <div className="App">
            <Button onClick={handleLogout} variant="danger" style={{marginLeft:"20px","marginTop":"20px"}}>Logout</Button>
            <div className='user-table'>
            <h1>Device Table</h1>
            <Table variant='success' striped bordered hover className="small-table">
                <thead>
                    <tr>
                        <th>#iddddddd</th>
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

        </div>
    );
};

export default UserDevice;
