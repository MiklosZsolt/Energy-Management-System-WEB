import React, { useState } from 'react';
import { Container, Row, Col, Card, Form, Button } from 'react-bootstrap';
import axios from 'axios';
import {  toast } from 'react-toastify';

const LoginForm = () => {
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');

    const handleSubmit = async (e) => {
        e.preventDefault();
        const payload = {
            username,
            password,
        };

        try {
            const response = await axios.post('http://localhost:8080/person/login', payload);
            toast.success("Log In successfully!");
            if(response.data.role === 1)
            { 
                window.location.href = '/user';
                localStorage.setItem('userId', response.data.id);
                localStorage.setItem('username', response.data.username);
                localStorage.setItem('role', 'user');
            }
            else if (response.data.role === 0){
                window.location.href = '/admin';
                localStorage.setItem('userId', response.data.id);
                localStorage.setItem('username', response.data.username);
                localStorage.setItem('role', 'admin');
            }
            
        } catch (error) {
            toast.error("Log In failed!");
            console.error('Error:', error);
        }
    };

    return (
        <Container>
            <Row className="justify-content-center">
                <Col md={6}>
                    <Card className="mt-5">
                        <Card.Header>Login</Card.Header>
                        <Card.Body>
                            <Form onSubmit={handleSubmit}>
                                <Form.Group>
                                    <Form.Label>Username</Form.Label>
                                    <Form.Control
                                        required
                                        type="text"
                                        placeholder="Enter your username"
                                        value={username}
                                        onChange={(e) => setUsername(e.target.value)}
                                    />
                                </Form.Group>
                                <Form.Group>
                                    <Form.Label>Password</Form.Label>
                                    <Form.Control
                                        required
                                        type="password"
                                        placeholder="Enter your password"
                                        value={password}
                                        onChange={(e) => setPassword(e.target.value)}
                                    />
                                </Form.Group>
                                <Button variant="primary" type="submit" style={{ marginTop: '10px' }}>
                                    Submit
                                </Button>
                            </Form>
                        </Card.Body>
                    </Card>
                </Col>
            </Row>
        </Container>
    );
};

export default LoginForm;