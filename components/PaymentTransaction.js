import React, { useEffect, useState } from "react";
import axios from 'axios';
import "../style/PaymentTransaction.css";

const PaymentTransaction = () => {
  const [transactions, setTransactions] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");
  const [upiID, setUpiID] = useState("8095929416@ybl");

  useEffect(() => {
    const fetchTransactions = async () => {
      try {
        const response = await fetch("http://localhost:5002/api/orders");
        if (!response.ok) {
          throw new Error(`HTTP error! Status: ${response.status}`);
        }
        const data = await response.json();
        setTransactions(data);
      } catch (err) {
        console.error("Error fetching transactions:", err.message);
        setError("Failed to fetch transactions. Please try again later.");
      } finally {
        setLoading(false);
      }
    };

    fetchTransactions();
  }, []);

  const handleSaveUpiID = async () => {
    try {
      const response = await axios.post("http://localhost:5002/api/update-upi", {
        upiID,
      });

      alert(response.data.message);
    } catch (error) {
      console.error("Error updating UPI ID:", error);
      alert("Failed to update UPI ID. Please try again.");
    }
  };

  const calculateRevenue = (interval) => {
    const now = new Date();
    let filtered = transactions;

    if (interval === "daily") {
      filtered = transactions.filter(
        (t) => new Date(t.created_at).toDateString() === now.toDateString()
      );
    } else if (interval === "weekly") {
      const oneWeekAgo = new Date(now);
      oneWeekAgo.setDate(now.getDate() - 7);
      filtered = transactions.filter(
        (t) => new Date(t.created_at) >= oneWeekAgo
      );
    } else if (interval === "monthly") {
      const currentMonth = now.getMonth();
      filtered = transactions.filter(
        (t) => new Date(t.created_at).getMonth() === currentMonth
      );
    }

    return filtered.reduce((sum, t) => sum + parseFloat(t.totalprice), 0);
  };

  if (loading) {
    return <div>Loading...</div>;
  }

  if (error) {
    return <div className="error-message">{error}</div>;
  }

  return (
    <div className="container">
      <h1>Payment Transactions</h1>

      {/* Transaction History Table */}
      <h2>Transaction History</h2>
      <table>
        <thead>
          <tr>
            <th>Order ID</th>
            <th>Name</th>
            <th>Price</th>
            <th>Total Price</th>
            <th>Username</th>
            <th>Email</th>
            <th>Phone</th>
            <th>Payment Mode</th>
          </tr>
        </thead>
        <tbody>
          {transactions.map((transaction) => (
            <tr key={transaction.id}>
              <td>{transaction.id}</td>
              <td>{transaction.name}</td>
              <td>{transaction.price}</td>
              <td>{transaction.totalprice}</td>
              <td>{transaction.username}</td>
              <td>{transaction.email}</td>
              <td>{transaction.phonenumber}</td>
              <td>{transaction.paymentmode}</td>
            </tr>
          ))}
        </tbody>
      </table>

      {/* Edit UPI ID */}
      <h2>Edit UPI ID</h2>
      <div className="input-group">
        <label>Current UPI ID:</label>
        <input
          type="text"
          value={upiID}
          onChange={(e) => setUpiID(e.target.value)}
        />
        <button onClick={handleSaveUpiID}>Save</button>
      </div>

      {/* Revenue Insights */}
      <h2>Revenue Insights</h2>
      <div className="revenue-insights">
        <p>Daily Revenue: ₹{calculateRevenue("daily").toFixed(2)}</p>
        <p>Weekly Revenue: ₹{calculateRevenue("weekly").toFixed(2)}</p>
        <p>Monthly Revenue: ₹{calculateRevenue("monthly").toFixed(2)}</p>
      </div>
    </div>
  );
};

export default PaymentTransaction;
