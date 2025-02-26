import React, { useState } from 'react';
import { useParams } from 'react-router-dom';
import '../style/AddRestaurant.css'; // Ensure styles are correctly applied

function AddFood() {
    const { restaurantId } = useParams(); // Get the restaurantId from the URL
    console.log("Restaurant ID:", restaurantId);

    const [foodName, setFoodName] = useState('');
    const [foodDescription, setFoodDescription] = useState('');
    const [price, setPrice] = useState('');
    const [message, setMessage] = useState('');

    const handleSubmit = (e) => {
        e.preventDefault();

        fetch('http://localhost:5002/addFood', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ restaurantId, foodName, foodDescription, price }),
        })
            .then((res) => res.text())
            .then((data) => {
                setMessage(data);
            })
            .catch((err) => {
                setMessage('Error adding food item');
            });
    };

    return (
        <div className="container">
            <h2>Add Food to Restaurant {restaurantId}</h2>
            <form onSubmit={handleSubmit}>
                <div>
                    <label>Food Name</label>
                    <input
                        type="text"
                        value={foodName}
                        onChange={(e) => setFoodName(e.target.value)}
                        required
                    />
                </div>
                <div>
                    <label>Food Description</label>
                    <textarea
                        value={foodDescription}
                        onChange={(e) => setFoodDescription(e.target.value)}
                    ></textarea>
                </div>
                <div>
                    <label>Price</label>
                    <input
                        type="number"
                        value={price}
                        onChange={(e) => setPrice(e.target.value)}
                        required
                    />
                </div>
                <button type="submit">Add Food</button>
            </form>

            {message && <p>{message}</p>}
        </div>
    );
}

export default AddFood;
