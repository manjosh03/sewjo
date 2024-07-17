import React, { useState } from 'react';

const PictureSwiper = ({ images }) => {
  const [currentIndex, setCurrentIndex] = useState(0);

  const nextImage = () => {
    setCurrentIndex((prevIndex) => (prevIndex + 1) % images.length);
  };

  const prevImage = () => {
    setCurrentIndex((prevIndex) => (prevIndex - 1 + images.length) % images.length);
  };

  return (
    <div style={styles.container}>
      <div style={styles.wrapper}>
        <button onClick={prevImage} style={styles.button}>
          &#10094;
        </button>
        <img src={images[currentIndex]} alt={`Slide ${currentIndex}`} style={styles.image} />
        <button onClick={nextImage} style={styles.button}>
          &#10095;
        </button>
      </div>
    </div>
  );
};

const styles = {
  container: {
    position: 'relative',
    maxWidth: '600px',
    margin: 'auto',
  },
  wrapper: {
    position: 'relative',
    display: 'flex',
    alignItems: 'center',
  },
  image: {
    width: '100%',
    borderRadius: '10px',
  },
  button: {
    backgroundColor: 'rgba(0, 0, 0, 0.5)',
    border: 'none',
    color: 'white',
    padding: '10px',
    cursor: 'pointer',
    borderRadius: '50%',
    userSelect: 'none',
  },
};

export default PictureSwiper;
