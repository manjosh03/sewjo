import React from 'react';
import { Link } from 'react-router-dom';

const Header = () => {
  const headerContainerStyles = {
    display: 'flex',
    flexDirection: 'column',
    alignItems: 'center',
    backgroundColor: '#f8f8f8',
    padding: '10px 20px',
    borderBottom: '1px solid #ddd',
  };

  const headerIconStyles = {
    width: '100px',
    height: '100px',
    marginBottom: '10px'
  };

  const navLinksContainerStyles = {
    display: 'flex',
    justifyContent: 'center'
  };

  const headerLinkStyles = {
    margin: '0 10px',
    color: '#333',
    textDecoration: 'none',
  };

  const dropdownStyles = {
    position: 'relative',
    display: 'inline-block',
  };

  const dropbtnStyles = {
    backgroundColor: '#f8f8f8',
    color: '#333',
    padding: '10px',
    border: 'none',
    cursor: 'pointer',
    fontSize: '16px',
  };

  const dropdownContentStyles = {
    display: 'none',
    position: 'absolute',
    backgroundColor: '#f9f9f9',
    boxShadow: '0px 8px 16px 0px rgba(0,0,0,0.2)',
    zIndex: '1',
  };

  const dropdownContentLinkStyles = {
    color: 'black',
    padding: '12px 16px',
    textDecoration: 'none',
    display: 'block',
  };

  const showDropdown = (e) => {
    const dropdown = e.currentTarget.querySelector('.dropdown-content');
    dropdown.style.display = 'block';
  };

  const hideDropdown = (e) => {
    const dropdown = e.currentTarget.querySelector('.dropdown-content');
    dropdown.style.display = 'none';
  };

  return (
    <header style={headerContainerStyles}>
      <img src="path-to-your-icon.png" alt="Logo" style={headerIconStyles} />
      <div style={navLinksContainerStyles}>
        <Link to="/" style={headerLinkStyles}>Home</Link>
        <Link to="/new-arrivals" style={headerLinkStyles}>New Arrivals</Link>
        <div
          className="dropdown"
          style={dropdownStyles}
          onMouseEnter={showDropdown}
          onMouseLeave={hideDropdown}
        >
          <button style={dropbtnStyles}>Shop</button>
          <div className="dropdown-content" style={dropdownContentStyles}>
            <Link to="/shop/all" style={dropdownContentLinkStyles}>All Products</Link>
            <Link to="/shop/category1" style={dropdownContentLinkStyles}>Category 1</Link>
            <Link to="/shop/category2" style={dropdownContentLinkStyles}>Category 2</Link>
          </div>
        </div>
        <Link to="/about-us" style={headerLinkStyles}>About Us</Link>
      </div>
      <input type="text" placeholder="Search..." style={{ padding: '5px', fontSize: '16px' }} />
    </header>
  );
};

export default Header;
