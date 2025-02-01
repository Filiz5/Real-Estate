'use client';

import React, { useState, useEffect } from 'react';
import { RadioButton } from 'primereact/radiobutton';
import styles from '@styles/components/home/RadioButtons.module.scss';
import { config } from '@/helpers/config';
import { IoIosRemoveCircleOutline } from 'react-icons/io';

const API_URL = config.api.baseUrl;

const RadioButtons = ({ onCategoryChange }) => {
  const [categories, setCategories] = useState([]); // Backend'den gelen kategoriler
  const [selectedCategory, setSelectedCategory] = useState(null); // Seçili kategori

  // Backend'den kategorileri çek
  useEffect(() => {
    const fetchCategories = async () => {
      try {
        const response = await fetch(`${API_URL}/categories`, {
          method: 'GET',
          headers: {
            'Content-Type': 'application/json'
          }
        });

        if (!response.ok) {
          throw new Error(`HTTP error! status: ${response.status}`);
        }

        const data = await response.json();

        if (data?.object?.content && Array.isArray(data.object.content)) {
          setCategories(data.object.content); // `object.content` içindeki kategorileri al
        } else {
          console.error('Invalid data format:', data);
        }
      } catch (error) {
        console.error('Error fetching categories:', error.message);
      }
    };

    fetchCategories();
  }, []);

  // Kategori seçimi
  const handleCategorySelect = (categoryId) => {
    setSelectedCategory(categoryId);
    onCategoryChange(categoryId); // Üst bileşene seçimi ilet
  };

  return (
    <div className={styles.container}>
      <div className={styles.radioContainer}>
        <div className={styles.radioLabel}>
          <label>Category</label>
          <select
            name=""
            id=""
            onChange={(e) => handleCategorySelect(e.target.value)}
            className={styles.radioInput}
          >
            <option value="">Seciniz</option>
            {categories.map((category) => (
              <option key={category.id} value={category.id}>
                {category.title}
              </option>
            ))}
          </select>
        </div>
      </div>
    </div>
  );
};

export default RadioButtons;
