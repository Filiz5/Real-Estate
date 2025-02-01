'use client';

import React from 'react';
import { Button } from 'primereact/button';
import { MdOutlineSearch } from 'react-icons/md';
import styles from '@/styles/components/home/SearchBar.module.scss';

const SearchBar = ({ searchQuery, onSearchQueryChange, onSearch }) => {
  return (
    <div className={styles.searchBar}>
      <input
        type="text"
        placeholder="Search for properties..."
        value={searchQuery}
        onChange={(e) => onSearchQueryChange(e.target.value)}
        className={styles.searchInput}
      />
      <Button
        icon={<MdOutlineSearch size={40} />}
        className={styles.searchButton}
        onClick={onSearch} // Arama butonuna basıldığında çağrılır
      />
    </div>
  );
};

export default SearchBar;
