import styles from '@/styles/components/common/property-search/price-range.module.scss';

export const PriceRange = ({ minPrice, maxPrice, onChange }) => (
  <div className={styles.priceRangeContainer}>
    <p>Price Range</p>
    <div className={styles.PriceRangeInputs}>
      <input
        type="number"
        name="minPrice"
        value={minPrice}
        onChange={(e) => onChange(e.target.name, e.target.value)} // Handle status change
        placeholder="Min"
        className={styles.input}
      />

      <input
        type="number"
        name="maxPrice"
        value={maxPrice}
        onChange={(e) => onChange(e.target.name, e.target.value)} // Handle status change
        placeholder="Max"
        className={styles.input}
      />
    </div>
  </div>
);
