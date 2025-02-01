
import { IoSearchOutline } from 'react-icons/io5';
import styles from '@/styles/components/common/property-search/search-box.module.scss';

export const SearchBox = ({ search, onChange }) => (
  <div className={styles.searchBoxContainer}>
    <p className={styles.searchBoxTitle}>Find Your Home</p>
    <div className={styles.searchBoxWrapper}>
      <IoSearchOutline className={styles.searchIcon} />

      <input
        type="text"
        name="search"
        value={search}
        onChange={(e) => onChange(e.target.name, e.target.value)}
        placeholder="What are you looking for?"
        className={styles.searchBoxInput}
      />
    </div>
  </div>
);
