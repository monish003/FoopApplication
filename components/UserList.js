import React, { useEffect, useState } from "react";
import { FaEdit } from "react-icons/fa"; // Importing edit icon from react-icons
import "../style/UserList.css"; // Import custom styles

const UsersList = () => {
  const [users, setUsers] = useState([]);
  const [editUserId, setEditUserId] = useState(null);
  const [userData, setUserData] = useState({
    username: "",
    email: "",
    phone: "",
    dob: "",
    password: "",
  });

  useEffect(() => {
    // Fetching users data
    fetch("http://localhost:5002/users") // Updated endpoint to fetch users
      .then((response) => response.json())
      .then((data) => setUsers(data))
      .catch((error) => console.error("Error fetching users:", error));
  }, []);

  const handleEditClick = (user) => {
    setEditUserId(user.id);
    setUserData({
      username: user.username,
      email: user.email,
      phone: user.phone,
      dob: user.dob,
      password: user.password,
    });
  };

  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setUserData((prevData) => ({
      ...prevData,
      [name]: value,
    }));
  };

  const handleSaveClick = () => {
    console.log("Saving user data:", userData);
    fetch(`http://localhost:5002/updateUser/${editUserId}`, { // Updated URL for saving the user
      method: "PUT",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify(userData),
    })
      .then((response) => response.json())
      .then((data) => {
        setUsers((prevUsers) =>
          prevUsers.map((user) =>
            user.id === editUserId ? { ...user, ...userData } : user
          )
        );
        setEditUserId(null);
      })
      .catch((error) => console.error("Error saving user:", error));
  };

  return (
    <div className="users-container">
      {users.map((user) => (
        <div key={user.id} className="user-card">
          <div className="card-header">
            <div className="username">{user.username}</div>
            <div className="phone">{user.phone}</div>
            <FaEdit onClick={() => handleEditClick(user)} className="edit-icon" />
          </div>
          {editUserId === user.id && (
            <div className="edit-form">
              <input
                type="text"
                name="username"
                value={userData.username}
                onChange={handleInputChange}
                className="input-field"
              />
              <input
                type="email"
                name="email"
                value={userData.email}
                onChange={handleInputChange}
                className="input-field"
              />
              <input
                type="text"
                name="phone"
                value={userData.phone}
                onChange={handleInputChange}
                className="input-field"
              />
              <input
                type="date"
                name="dob"
                value={userData.dob}
                onChange={handleInputChange}
                className="input-field"
              />
              <input
                type="password"
                name="password"
                value={userData.password}
                onChange={handleInputChange}
                className="input-field"
              />
              <button onClick={handleSaveClick} className="save-btn">
                Save
              </button>
            </div>
          )}
        </div>
      ))}
    </div>
  );
};

export default UsersList;
