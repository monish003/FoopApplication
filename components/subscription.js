import React, { useEffect, useState } from 'react';
import axios from 'axios';
import '../style/Subscription.css';

const Subscription = () => { 
    const [users, setUsers] = useState([]);
  
    
    const updateSubscription = async (id, status) => {
      try {
        const response = await fetch(`http://localhost:5000/users/${id}/subscription`, {
          method: 'PUT',
          headers: {
            'Content-Type': 'application/json',
          },
          body: JSON.stringify({ subscription_status: status }),
        });
    
        if (!response.ok) {
          throw new Error(`Failed to update subscription: ${response.statusText}`);
        }
    
        fetchUsers();
      } catch (error) {
        console.error('Error updating subscription:', error);
      }
    };

    const fetchUsers = async () => {
        try {
          const response = await axios.get("http://localhost:5002/users");
          setUsers(response.data);
        } catch (error) {
          console.error("Error fetching users:", error.message);
        }
      };
  
    useEffect(() => {
        fetchUsers()
    }, []);
  
    return (
      <div className="dashboard-container">
        <h1>Subscription Management</h1>
        <table className="subscription-table">
          <thead>
            <tr>
              <th>Name</th>
              <th>Email</th>
              <th>Phone</th>
              <th>Status</th>
              <th>Actions</th>
            </tr>
          </thead>
          <tbody>
            {users.map((user) => (
              <tr key={user.id}>
                <td>{user.username}</td>
                <td>{user.email}</td>
                <td>{user.phone}</td>
                <td className={`status ${user.subscription_status}`}>
                  {user.subscription_status}
                </td>
                <td>
                  <button
                    className="action-btn pause-btn"
                    onClick={() =>
                      updateSubscription(user.id, user.subscription_status === 'paused' ? 'active' : 'paused')
                    }
                  >
                    {user.subscription_status === 'paused' ? 'Resume' : 'Pause'}
                  </button>
                  <button
                    className="action-btn cancel-btn"
                    onClick={() => updateSubscription(user.id, 'cancelled')}
                  >
                    Cancel
                  </button>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    );
  };
  
  export default Subscription;
  
