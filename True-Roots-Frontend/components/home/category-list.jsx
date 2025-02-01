'use client';

import React, { useState, useEffect } from 'react';
import styles from '@styles/components/home/CategoryList.module.scss';
import CategoryCard from './category-card';
import { getCategoriesForHomePage } from '@/services/category-service';
import { GrFormNext, GrFormPrevious } from 'react-icons/gr';
import { FaHome } from 'react-icons/fa';

const CategoryList = ({category}) => {
  const [categories, setCategories] = useState([]);
  const [error, setError] = useState(null);
  const [currentIndex, setCurrentIndex] = useState(0);
  const cardsToShow = 5;

  useEffect(() => {
      if(category){
        setCategories(category || []);
      }
  }, [category]);

  const handleNext = () => {
    if (currentIndex + cardsToShow < categories.length) {
      setCurrentIndex(currentIndex + cardsToShow);
    }
  };

  // Move to the previous set of items
  const handlePrev = () => {
    if (currentIndex > 0) {
      setCurrentIndex(currentIndex - cardsToShow);
    }
  };

  return (
    <div className={styles.container}>
      {error ? (
        <p>{error}</p>
      ) : (
        <div className={styles.outer_container}>
        <div className={styles.title}>
          <p>Explore Properites</p>
        </div>
          
          <div className={styles.inner_container}>
            <div className={styles.previous_button}>
              <button
                className={styles.button}
                onClick={handlePrev}
                disabled={currentIndex === 0}
              >
                <GrFormPrevious size={30} />
              </button>
            </div>
            <div className={styles.card_container}>
              {categories
                .slice(currentIndex, currentIndex + cardsToShow)
                .map((item, index) => (
                  <div key={index} className={styles.card}>
                    <div className={styles.card_icon}>
                      <FaHome size={40} className={styles.home_icon} />
                    </div>
                    <div className={styles.card_content}>
                      <p>{item.category}</p>
                      <p>{item.amount}</p>
                    </div>
                  </div>
                ))}
            </div>

            <div className={styles.next_button}>
              <button
                className={styles.button}
                onClick={handleNext}
                disabled={currentIndex + cardsToShow >= categories.length}
              >
                <GrFormNext size={30} />
              </button>
            </div>
          </div>
        </div>
      )}
    </div>
  );
};

export default CategoryList;