'use client';

import { useState, useEffect, Suspense } from 'react';
import styles from '@/styles/components/home/ExploreProperties.module.scss';
import Loader from '../common/Loader';
import CityCards from './home-components/CityCards';

const ExploreProperties = ({ cities }) => {
  const [cityAdverts, setCityAdverts] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [currentIndex, setCurrentIndex] = useState(0);
  const cardsToShow = 5;

  useEffect(() => {
    if (cities) {
      setCityAdverts(cities || []);
      setLoading(false);
    }
  }, [cities]);

  const handleNext = () => {
    if (currentIndex + cardsToShow < cityAdverts.length) {
      setCurrentIndex(currentIndex + cardsToShow);
    }
  };

  const handlePrev = () => {
    if (currentIndex > 0) {
      setCurrentIndex(currentIndex - cardsToShow);
    }
  };

  if (loading) return <Loader />;
  if (error) return <p>{error}</p>;

  return (
    <div className={styles.container}>
      <CityCards
        cityAdverts={cityAdverts}
        currentIndex={currentIndex}
        cardsToShow={cardsToShow}
        handleNext={handleNext}
        handlePrev={handlePrev}
      />
    </div>
  );
};

export default ExploreProperties;
