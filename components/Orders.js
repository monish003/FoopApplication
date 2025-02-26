import React, { useEffect, useState } from 'react';
import axios from 'axios';
import '../style/Orders.css'; 

const Orders = () => {
  const [orders, setOrders] = useState([]);
  const [loading, setLoading] = useState(true);

  // Fetch orders from the backend
  const fetchOrders = async () => {
    try {
      const response = await axios.get('http://localhost:5002/api/orders'); 
      setOrders(response.data);
      setLoading(false);
    } catch (error) {
      console.error('Error fetching orders:', error);
    }
  };

  

  const handledelivery = (id) => {
    fetch(`http://localhost:5002/api/orders/${id}`, { method: 'DELETE' })
      .then((res) => {
        if (!res.ok) {
          throw new Error('Failed to delete order');
        }
        setOrders((prevOrders) =>
          prevOrders.filter((orders) => orders.id !== id)
        );
      })
      .catch((err) => alert('Error deleting order'));
  };


  useEffect(() => {
    fetchOrders(); 
    const interval = setInterval(fetchOrders, 5002); 
    return () => clearInterval(interval); 
  }, []);

  if (loading) return <div className="loading">Loading orders...</div>;

  return (
    <div className="orders-page">
      <h1>Order Management</h1>
      <div className="orders-list">
        {orders.map((order) => (
          <div key={order.id} className="order-card">
            <h3>Order #{order.id}</h3>
            <p><strong>Name:</strong> {order.name}</p>
            <p><strong>Price:</strong> ${order.price}</p>
            <p><strong>Total Price:</strong> ${order.totalprice}</p>
            <p><strong>Customer:</strong> {order.username}</p>
            <p><strong>Email:</strong> {order.email}</p>
            <p><strong>Phone:</strong> {order.phonenumber}</p>
            <p><strong>Placed At:</strong> {new Date(order.created_at).toLocaleString()}</p>
            <button
              className="delivered-button"
              onClick={() => handledelivery(order.id)}
            >
              Delivered?
            </button>
          </div>
        ))}
      </div>
    </div>
  );
};

export default Orders;
