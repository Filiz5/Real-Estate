'use client';

import styles from '@/styles/components/common/property-search/property-type.module.scss';

export const PropertyType = ({ type, onChange, options =[] }) => {
  return (
    <div className={styles.PropertyTypeContainer}>
      <p>Property Type</p>
      <div className={styles.PropertyTypeInputs}>
        <label key={0} className={styles.label}>
          <input
            type="radio"
            name="type"
            value=""
            checked={type === ''} 
            onChange={(e) => onChange(e.target.name, e.target.value)}
            className={styles.input}
          />
          All
        </label>
        {options.map((option) => (
          <label key={option.value} className={styles.label}>
            <input
              type="radio"
              name="type"
              value={option.value}
              checked={String(type) === String(option.value)} 
              onChange={(e) => onChange(e.target.name, e.target.value)}
              className={styles.input}
            />
            {option.name}
          </label>
        ))}
      </div>
    </div>
  );
};
