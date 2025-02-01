'use client';

import React, { useState, useEffect } from 'react';
import styles from '@/styles/components/admin/advert-type/advert-type-search-box.module.scss';

export default function TourRequestSearchBox({ handleSearchTerm }) {
  const [searchTerm, setSearchTerm] = useState('');

  useEffect(() => {
      
      handleSearchTerm(searchTerm)
    }, [searchTerm, handleSearchTerm]);

  return (
    <div className={styles.searchContainer}>
      <input
        type="text"
        placeholder="Type something"
        value={searchTerm}
        onChange={(e) => setSearchTerm(e.target.value)}
        className={styles.searchInput}
      />
    </div>
  );
}
