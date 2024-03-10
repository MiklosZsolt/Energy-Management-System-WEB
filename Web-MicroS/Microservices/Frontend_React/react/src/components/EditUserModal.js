// EditUserModal.js
import React, { useState, useEffect } from "react";
import Button from 'react-bootstrap/Button';
import Modal from 'react-bootstrap/Modal';
import { HOST_PERSON } from "../Hosts";
import axios from "axios";

function EditUserModal({ user, onSave, onClose }) {
  const [editedUser, setEditedUser] = useState({ ...user });

  useEffect(() => {
    if (user) {
      setEditedUser(user);
    }
  }, [user]);

  const handleInputChange = (event) => {
    const { name, value } = event.target;
    setEditedUser({
      ...editedUser,
      [name]: value,
    });
  };

  const handleSaveUser = async () => {
    try {
      if (editedUser.role === "admin") {
        editedUser.role = 0;
      } else if (editedUser.role === "client") {
        editedUser.role = 1;
      }
      axios.post(`${HOST_PERSON}/edit/${editedUser.id}`, {
        username: editedUser.username,
        password: editedUser.password,
        role: editedUser.role,
      });
      onSave(editedUser);
    } catch (error) {
      console.error(error);
    }
    onClose();
  };

  return (
    user && (
      <Modal show={true} onHide={onClose}>
        <Modal.Header closeButton>
          <Modal.Title>Edit User</Modal.Title>
        </Modal.Header>
        <Modal.Body>
          <form>
            <div className="form-group">
              <label htmlFor="username">Username</label>
              <input
                type="text"
                className="form-control"
                id="username"
                name="username"
                value={editedUser.username}
                onChange={handleInputChange}
              />
            </div>
            <div className="form-group">
              <label htmlFor="password">Password</label>
              <input
                type="password"
                className="form-control"
                id="password"
                name="password"
                value={editedUser.password}
                onChange={handleInputChange}
              />
            </div>
            <div className="form-group">
              <label htmlFor="role">Role</label>
              <select
                className="form-control"
                id="role"
                name="role"
                value={editedUser.role}
                onChange={handleInputChange}
              >
                <option value="admin">Admin</option>
                <option value="client">Client</option>
              </select>
            </div>
          </form>
        </Modal.Body>
        <Modal.Footer>
          <Button variant="secondary" onClick={onClose}>
            Close
          </Button>
          <Button variant="primary" onClick={handleSaveUser}>
            Save User
          </Button>
        </Modal.Footer>
      </Modal>
    )
  );
}

export default EditUserModal;
