import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import Header from './components/Header';
import AboutUs from './components/AboutUs';
import AddRestaurant from './components/AddRestaurant';
import AddTiffin from './components/AddTiffin';
import ShowRestaurants from './components/ShowRestaurants';
import ShowTiffinCenters from './components/ShowTiffinCenters';
import AddFood from './components/AddFood'; 
// import UsersList from './components/UserList';
import Orders from './components/Orders';
import PaymentTransaction from './components/PaymentTransaction';
import Subscription from './components/subscription';


function App() {
    return (
        <Router>
            <Header />
            <main style={{ padding: '20px' }}>
                <Routes>
                    <Route path="/" element={<AboutUs />} />
                    <Route path="/add-restaurants" element={<AddRestaurant />} />
                    <Route path="/add-tiffins" element={<AddTiffin />} />
                    <Route path="/show-restaurants" element={<ShowRestaurants />} />
                    <Route path="/show-tiffin-centers" element={<ShowTiffinCenters />} />
                    <Route path="/user-list" element={<Subscription/>}/>
                    <Route path="/order-list" element={<Orders/>}/>
                    <Route path="/payment-transaction" element={<PaymentTransaction/>}/>


                    <Route path="/foodapp/:restaurantId" element={<AddFood />} />

                </Routes>
            </main>
        </Router>
    );
}

export default App;
