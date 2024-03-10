import React, { useState, useEffect, useRef } from 'react';
import SockJsClient from 'react-stomp';
import '../App.css';
import { Button, Form } from 'react-bootstrap';

const Chat = () => {
    const [messages, setMessages] = useState([]);
    const [messageInput, setMessageInput] = useState('');
    const [username, setUsername] = useState('');
    const [isTyping, setIsTyping] = useState('');
    const clientRef = useRef(null);
    const typingTimeoutRef = useRef(null);

    useEffect(() => {
        const userDataString = localStorage.getItem('userData');
        if (userDataString) {
            const userData = JSON.parse(userDataString);
            setUsername(userData.username);
        }
        return () => {
            if (clientRef.current && clientRef.current.deactivate) {
                clientRef.current.deactivate();
            }
        };
    }, []);

    const handleMessageChange = (e) => {
        setMessageInput(e.target.value);
    
        if (typingTimeoutRef.current) {
            clearTimeout(typingTimeoutRef.current);
        }
    
        // Trimite notificarea "is typing" cu numele utilizatorului
        if (e.target.value && !isTyping) {
            if (clientRef.current && clientRef.current.sendMessage) {
                const typingNotification = {
                    type: 'TYPING',
                    sender: username,
                    receiver: 'admin',
                    content: `${username} is typing...`
                };
                clientRef.current.sendMessage('/app/typing', JSON.stringify(typingNotification));
            }
        }
    };

// Folosește efectul pentru a șterge isTyping atunci când se trimite un mesaj
useEffect(() => {
    if (!isTyping) {
        return;
    }

    const timer = setTimeout(() => {
        // Setează isTyping pe false după un anumit timp
        setIsTyping(false);
    }, 500); // Aici poți ajusta timpul în milisecunde

    return () => {
        clearTimeout(timer);
    };
}, [isTyping]);
    
    
    const handleSendMessage = () => {
        const message = {
            sender: username,
            receiver: 'admin',
            content: messageInput,
        };
        if (clientRef.current && clientRef.current.sendMessage) {
            clientRef.current.sendMessage('/app/chat', JSON.stringify(message));
        }
        setMessageInput('');
        
        // Setează isTyping pe false după trimiterea mesajului
        setIsTyping(false);
    };
    

    const onMessageReceived = (msg) => {
        if (msg.type && msg.type === 'TYPING') {
            // Verificați dacă notificarea "is typing" vine de la utilizatorul opus și actualizați starea
            if (msg.sender !== username) {
                setIsTyping(msg.content);
            } else {
                setIsTyping('');
            }
        } else {
            setMessages(prevMessages => [...prevMessages, msg]);
        }
    };
    return (
        <div className="chat-container">
            <div className="message-container">
                {messages.map((msg, index) => (
                    <div key={index} className={`message ${msg.sender === username ? 'sender-me' : 'sender-other'}`}>
                        {msg.sender}: {msg.content}
                    </div>
                ))}
        {isTyping && <div className="typing-notification">{isTyping}</div>}
            </div>
            <Form>
                <Form.Control
                    type="text"
                    value={messageInput}
                    onChange={handleMessageChange}
                    placeholder="Type your message..."
                    className="bg-dark text-light" 
                />
            </Form>
            <Button variant='dark' onClick={handleSendMessage}>Send</Button>
            <SockJsClient
                url="http://localhost:8083/ws"
                topics={['/topic/messages', '/topic/typing']}
                onMessage={onMessageReceived}
                ref={client => { clientRef.current = client; }}
            />
        </div>
    );
};

export default Chat;