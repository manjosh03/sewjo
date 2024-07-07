import React from 'react';
import { Link } from 'react-router-dom';

const Banners = () => {
  const bannerContainerStyles = {
    position: 'sticky',
    top: '0',
    width: '100%',
    backgroundColor: '#fff',
    zIndex: '1000',
  };

  const bannerTextStyles = {
    padding: '10px 20px',
    backgroundColor: '#f8f8f8',
    borderBottom: '1px solid #ddd',
  };

  return (
    <div style={bannerContainerStyles}>
      <div style={bannerTextStyles}>
        <Link to="/signup">Click here to pop us your info and we'll get you signed up for our newsletter!</Link>
      </div>
      <div style={bannerTextStyles}>
        Free Shipping on Canadian orders over $100.
      </div>
    </div>
  );
};

export default Banners;
