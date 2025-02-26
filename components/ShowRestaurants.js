import { useEffect, useState } from 'react';
import '../style/ShowRestaurants.css'; 

function ShowRestaurants() {
  const [restaurants, setRestaurants] = useState([]);

  useEffect(() => {
    fetch('http://localhost:5002/restaurants')
      .then((res) => res.json())
      .then((data) => setRestaurants(data))
      .catch((err) => alert('Error fetching restaurants'));
  }, []);

  const handleRemove = (id) => {
    fetch(`http://localhost:5002/restaurants/${id}`, { method: 'DELETE' })
      .then((res) => {
        if (!res.ok) {
          throw new Error('Failed to delete restaurant');
        }
        setRestaurants((prevRestaurants) =>
          prevRestaurants.filter((restaurant) => restaurant.id !== id)
        );
      })
      .catch((err) => alert('Error deleting restaurant'));
  };

  return (
    <section className="restaurants-section">
      <h2 className="restaurants-title">Restaurants</h2>
      <div className="restaurants-container">
        {restaurants.map((restaurant) => (
          <div className="restaurant-card" key={restaurant.id}>
            <h3 className="restaurant-name">{restaurant.name}</h3>
            <p className="restaurant-location">{restaurant.location}</p>
            <p className="restaurant-rating">Rating: {restaurant.rating} / 5</p>
            <button className="view-details-btn"
            onClick={(handleRemove)}
            >Remove</button>
          </div>
        ))}
      </div>
    </section>
  );
}

export default ShowRestaurants;
