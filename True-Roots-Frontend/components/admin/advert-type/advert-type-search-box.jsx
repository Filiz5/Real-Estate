
import React from 'react';
import styles from '@/styles/components/admin/advert-type/advert-type-search-box.module.scss';

export default function SearchBoxAdvertType({ searchTerm, setSearchTerm, handleAddNew }) {
  return (
    <div className={styles.searchContainer}>
      <input
        type="text"
        placeholder="Search advert types"
        value={searchTerm}
        onChange={(e) => setSearchTerm(e.target.value)}
        className={styles.searchInput}
      />
      <span role="img" aria-label="search" className={styles.searchIcon}>🔍</span>
      <button onClick={handleAddNew} className={styles.addButton}>
        Add New
      </button>
    </div>
  );
}
