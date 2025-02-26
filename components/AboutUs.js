
import React, { useEffect, useState } from "react";
import Chart from "chart.js/auto";
import "../style/AboutUs.css";

function AboutUs() {
    const [ordersToday, setOrdersToday] = useState(0);
    const [revenueToday, setRevenueToday] = useState(0);
    const [restaurants, setRestaurants] = useState([]);
    const [tiffins, settiffins] = useState([]);

    useEffect(() => {
        fetch("http://localhost:5002/dashboard-data") 
            .then((response) => response.json())
            .then((data) => {
                setOrdersToday(data.ordersToday);
                setRevenueToday(data.revenueToday);
                drawCharts(data.ordersToday, data.revenueToday);
            })
            .catch((error) => console.error("Error fetching dashboard data:", error));

        // Fetch restaurant data
        fetch("http://localhost:5002/restaurants/list") 
            .then((response) => response.json())
            .then((data) => {
                setRestaurants(data);
                console.log("Restaurants data fetched:", data); 
            })
            .catch((error) => console.error("Error fetching restaurant data:", error));

        
        fetch("http://localhost:5002/tiffins/list") 
            .then((response) => response.json())
            .then((data) => {
                settiffins(data);
                console.log("Tiffen data fetched:", data); 
            })
            .catch((error) => console.error("Error fetching Tiffins data:", error));    

    }, []);

    let ordersChart, revenueChart; 
const drawCharts = (orders, revenue) => {
    if (ordersChart) ordersChart.destroy();
    if (revenueChart) revenueChart.destroy();

    ordersChart = new Chart(document.getElementById("ordersChart"), {
        type: "bar",
        data: {
            labels: ["Today"],
            datasets: [
                {
                    label: "Orders",
                    data: [orders],
                    backgroundColor: "#4caf50",
                },
            ],
        },
    });

    revenueChart = new Chart(document.getElementById("revenueChart"), {
        type: "bar",
        data: {
            labels: ["Today"],
            datasets: [
                {
                    label: "Revenue ($)",
                    data: [revenue],
                    backgroundColor: "#2196f3",
                },
            ],
        },
    });
};


    return (
        <div>
            {/* Header */}
            {/* <header className="dashboard-header">
                <h1>Food Booking Admin Dashboard</h1>
            </header> */}

            {/* Dashboard Section */}
            <main className="dashboard-main">
                <section className="dashboard-stats">
                    <div className="stat-card">
                        <h3>Number of Items Ordered Today</h3>
                        <canvas id="ordersChart"></canvas>
                        <p>Total Ordered Items: {ordersToday}</p>
                    </div>
                    <div className="stat-card">
                        <h3>Revenue Earned Today</h3>
                        <canvas id="revenueChart"></canvas>
                        <p>Total Revenue: Rs.{Number(revenueToday).toFixed(2)}</p>
                    </div>
                </section>

                {/* Restaurant List */}
                <section className="restaurant-list">
                <h2>Restaurants</h2>
                <div className="restaurants-container">
                      {restaurants.length === 0 ? (
                          <p>Loading restaurants...</p>
                  ) : (
                      restaurants.map((restaurant) => (
                     <div key={restaurant.restaurantId} className="restaurant-card">
                    <h3>{restaurant.name}</h3>
                    <p>Location: {restaurant.location}</p>
                    <p>Rating: ⭐ {restaurant.rating}/5</p>
                </div>
            ))
        )}
    </div>
</section>

<section className="restaurant-list">
                <h2>Tiffins</h2>
                <div className="restaurants-container">
                      {tiffins.length === 0 ? (
                          <p>Loading tiffins list...</p>
                  ) : (
                    tiffins.map((tiffin) => (
                     <div key={tiffin.id} className="restaurant-card">
                    <h3>{tiffin.name}</h3>
                    <p>Description: {tiffin.description}</p>
                </div>
            ))
        )}
    </div>
</section>



                {/* About Us Section */}
                {/* <section className="about-us">
                    <h2>About Us</h2>
                    <p>
                        Welcome to the Food App Admin Dashboard. Manage restaurants, foods, and tiffin services
                        easily with our intuitive interface. This dashboard is designed to streamline operations and
                        enhance user experience.
                    </p>
                </section> */}
            </main>
        </div>
    );
}

export default AboutUs;
