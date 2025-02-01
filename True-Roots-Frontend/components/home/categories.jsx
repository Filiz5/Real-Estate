'use client';

import React, { useState } from 'react';
import { Form } from 'react-bootstrap'; 
import 'bootstrap/dist/css/bootstrap.min.css';
import styles from '@/styles/components/home/Category.module.scss';

const Categories = ({ onFilterChange }) => {
  const [selectedFilters, setSelectedFilters] = useState([]); 

  const handleFilterChange = (e) => {
    const value = e.target.value;
    const checked = e.target.checked;

    let updatedFilters = [...selectedFilters];

    if (checked) {
      // Seçili değilse ekle
      updatedFilters.push(value);
    } else {
      // Seçiliyse kaldır
      updatedFilters = updatedFilters.filter((item) => item !== value);
    }

    setSelectedFilters(updatedFilters); 
    onFilterChange(updatedFilters); 
  };

  return (
    <div className={styles.categories}>
      
      <div className={styles.filterList}>
        {/* House */}
        <Form.Check
          type="checkbox"
          id="house"
          label="House"
          value="House"
          onChange={handleFilterChange}
          checked={selectedFilters.includes('House')}
          className={styles.customCheckbox}
        />

        {/* Apartment */}
        <Form.Check
          type="checkbox"
          id="apartment"
          label="Apartment"
          value="Apartment"
          onChange={handleFilterChange}
          checked={selectedFilters.includes('Apartment')}
          className={styles.customCheckbox}
        />

        {/* Villa */}
        <Form.Check
          type="checkbox"
          id="villa"
          label="Villa"
          value="Villa"
          onChange={handleFilterChange}
          checked={selectedFilters.includes('Villa')}
          className={styles.customCheckbox}
        />

        {/* Office */}
        <Form.Check
          type="checkbox"
          id="office"
          label="Office"
          value="Office"
          onChange={handleFilterChange}
          checked={selectedFilters.includes('Office')}
          className={styles.customCheckbox}
        />
      </div>

      {/* Seçili Filtreler */}
      <div className={styles.selectedFilters}>
        {selectedFilters.length > 0
          ? `Selected Filters: ${selectedFilters.join(', ')}`
          : 'No filter selected'}
      </div>
    </div>
  );
};

export default Categories;
