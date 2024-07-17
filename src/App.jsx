import React from 'react';
import { BrowserRouter as Router, Route, Routes } from 'react-router-dom';
import PictureSwiper from '/src/components/PictureSwiper';

import Header from '/src/components/Header';

import Banners from '/src/components/Banners';

import './styles.css';



function App() {

  const images = [
    '/images/product1.png',
    '/images/product2.png',  
    '/images/product3.png',
    
  ]
  return (
    <Router>
      <div className="App">
        <Banners />
        <Header />
        <PictureSwiper images={images }/>
        <Footer />
      </div> 
    </Router>
  );
}

function Footer() {
  return (
    <footer style={{ padding: '10px', textAlign: 'center', backgroundColor: '#f8f8f8', borderTop: '1px solid #ddd' }}>
      <p>© 2023 Sewjo fabrics Company. All rights reserved.</p>
    </footer>
  );
}

export default App;
