import { useState } from 'react';
import '../style/AddTiffin.css'; // Importing the external CSS file

function AddTiffin() {
  const [name, setName] = useState('');
  const [description, setDescription] = useState('');
  const [lunchPrice, setLunchPrice] = useState('');
  const [dinnerPrice, setDinnerPrice] = useState('');
  const [dateRange, setDateRange] = useState('');

  const handleSubmit = (e) => {
    e.preventDefault();
    fetch('http://localhost:5002/addTiffinCenter', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({
        name,
        description,
        lunch_price: lunchPrice,
        dinner_price: dinnerPrice,
        date_range: dateRange,
      }),
    })
      .then((res) => res.text())
      .then((msg) => alert(msg))
      .catch((err) => alert('Error adding tiffin center'));
  };

  return (
    <section className="add-tiffin-container">
      <h2 className="add-tiffin-title">Add Tiffin Center</h2>
      <form onSubmit={handleSubmit} className="add-tiffin-form">
        <input
          type="text"
          placeholder="Tiffin Center Name"
          value={name}
          onChange={(e) => setName(e.target.value)}
          required
          className="add-tiffin-input"
        />
        <input
          type="text"
          placeholder="Description"
          value={description}
          onChange={(e) => setDescription(e.target.value)}
          required
          className="add-tiffin-input"
        />
        <input
          type="number"
          placeholder="Lunch Price"
          value={lunchPrice}
          onChange={(e) => setLunchPrice(e.target.value)}
          required
          className="add-tiffin-input"
        />
        <input
          type="number"
          placeholder="Dinner Price"
          value={dinnerPrice}
          onChange={(e) => setDinnerPrice(e.target.value)}
          required
          className="add-tiffin-input"
        />
        <input
          type="text"
          placeholder="Date Range (e.g., 17 Dec - 17 Jan)"
          value={dateRange}
          onChange={(e) => setDateRange(e.target.value)}
          required
          className="add-tiffin-input"
        />
        <button type="submit" className="add-tiffin-button">
          Add Tiffin Center
        </button>
      </form>
    </section>
  );
}

export default AddTiffin;

