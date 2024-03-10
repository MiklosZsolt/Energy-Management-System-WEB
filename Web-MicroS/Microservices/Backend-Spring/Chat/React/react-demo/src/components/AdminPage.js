import React, { useEffect, useState } from "react";
import Chat from "./ChatPage";
import { Button } from "react-bootstrap";
import { useNavigate } from "react-router-dom";

const AdminPage = () => {
    const navigate = useNavigate();
    return (
        <div className="App">
        <h1>AdminPage</h1>
        <Button onClick={() => navigate(`/chat`)}>Chat</Button>
        </div>
    );
};

export default AdminPage;