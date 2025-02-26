import { useEffect, useState } from 'react';
import '../style/showTiffinCenter.css'; // Import the CSS for better styling

function ShowTiffinCenters() {
  const [tiffinCenters, setTiffinCenters] = useState([]);

  useEffect(() => {
    fetch('http://localhost:5002/tiffin-centers')
      .then((res) => res.json())
      .then((data) => setTiffinCenters(data))
      .catch((err) => alert('Error fetching tiffin centers'));
  }, []);

  return (
    <section className="tiffin-centers-section">
      <h2 className="tiffin-centers-title">Tiffin Centers</h2>
      <div className="tiffin-centers-container">
        {tiffinCenters.map((center) => (
          <div className="tiffin-card" key={center.id}>
            <h3 className="tiffin-name">{center.name}</h3>
            <p className="tiffin-description">{center.description}</p>
            <div className="pricing">
              <span className="price">Lunch: ₹{center.lunch_price}</span>
              <span className="price">Dinner: ₹{center.dinner_price}</span>
            </div>
            <p className="date-range">Valid: {center.date_range}</p>
            {/* <button className="view-details-btn">View Details</button> */}
          </div>
        ))}
      </div>
    </section>
  );
}

export default ShowTiffinCenters;
