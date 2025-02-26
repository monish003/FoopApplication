import React, { useState } from 'react';
import '../style/AddRestaurant.css';  
function AddRestaurant() {
    const [name, setName] = useState('');
    const [location, setLocation] = useState('');
    const [rating, setRating] = useState('');
    const [foodItems, setFoodItems] = useState([{ foodName: '', foodDescription: '', price: '' }]);
    const [message, setMessage] = useState('');

    const handleAddFoodItem = () => {
        setFoodItems([...foodItems, { foodName: '', foodDescription: '', price: '' }]);
    };

    const handleFoodChange = (index, field, value) => {
        const updatedFoodItems = [...foodItems];
        updatedFoodItems[index][field] = value;
        setFoodItems(updatedFoodItems);
    };

    const handleSubmit = async (e) => {
        e.preventDefault();

        try {
            const res = await fetch('http://localhost:5002/addRestaurant', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ name, location, rating, foodItems }),
            });
            const data = await res.text();
            setMessage(data);
        } catch (err) {
            setMessage('Error adding restaurant');
        }
    };

    return (
        
        <div className="add-restaurant-container">
            <h2>Add Restaurant</h2>
            <form onSubmit={handleSubmit} className="form-container">
                {/* Restaurant Name */}
                <div className="input-box">
                    <input
                        type="text"
                        placeholder="Restaurant Name"
                        value={name}
                        onChange={(e) => setName(e.target.value)}
                        className="input"
                        required
                    />
                </div>

                {/* Restaurant Location */}
                <div className="input-box">
                    <input
                        type="text"
                        placeholder="Location"
                        value={location}
                        onChange={(e) => setLocation(e.target.value)}
                        className="input"
                        required
                    />
                </div>

                {/* Restaurant Rating */}
                <div className="input-box">
                    <input
                        type="number"
                        placeholder="Rating (1-5)"
                        value={rating}
                        onChange={(e) => setRating(e.target.value)}
                        className="input"
                        required
                    />
                </div>
                <h3>Food Items</h3>
                {foodItems.map((food, index) => (
                    <div key={index} className="food-item-box">
                        <div className="input-box">
                            <input
                                type="text"
                                placeholder="Food Name"
                                value={food.foodName}
                                onChange={(e) => handleFoodChange(index, 'foodName', e.target.value)}
                                className="input"
                                required
                            />
                        </div>
                        <div className="input-box">
                            <input
                                type="text"
                                placeholder="Food Description"
                                value={food.foodDescription}
                                onChange={(e) => handleFoodChange(index, 'foodDescription', e.target.value)}
                                className="input"
                            />
                        </div>
                        <div className="input-box">
                            <input
                                type="number"
                                placeholder="Price"
                                value={food.price}
                                onChange={(e) => handleFoodChange(index, 'price', e.target.value)}
                                className="input"
                                required
                            />
                        </div>
                    </div>
                ))}
                <button type="button" onClick={handleAddFoodItem} className="add-button">
                    Add More Food
                </button>

                <button type="submit" className="submit-button">
                    Add Restaurant
                </button>
            </form>

            {message && <div className="message">{message}</div>}
        </div>
    
    );
}

export default AddRestaurant;
