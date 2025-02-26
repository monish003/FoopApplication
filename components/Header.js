import { Link } from 'react-router-dom';
import '../style/Header.css';

function Header() {
    return (
        <header className="header">
            <h1 className="logo">Food App Admin</h1>
            <nav>
                <ul className="nav-links">
                    <li><Link to="/">About Us</Link></li>
                    <li><Link to="/add-restaurants">Add Restaurants</Link></li>
                    <li><Link to="/add-tiffins">Add Tiffins</Link></li>
                    <li><Link to="/show-restaurants">Show Restaurants</Link></li>
                    <li><Link to="/show-tiffin-centers">Show Tiffin Centers</Link></li>
                    <li><Link to="/user-list">User List</Link></li>
                    <li><Link to="/order-list">Order List</Link></li>
                    <li><Link to="/payment-transaction">payment Transactions</Link></li>

                </ul>
            </nav>
        </header>
    );
}

export default Header;
