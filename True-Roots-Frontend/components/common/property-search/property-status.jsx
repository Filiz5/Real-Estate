import styles from '@/styles/components/common/property-search/property-status.module.scss';

export const PropertyStatus = ({ status, onChange, options = [] }) => (
  <div className={styles.PropertyStatusContainer}>
    <p>Property Status</p>
    <div className={styles.PropertyStatusInputs}>
      <label key={0} className={styles.label}>
        <input
          type="radio"
          name="status"
          value=""
          checked={status === ''}
          onChange={(e) => onChange(e.target.name, e.target.value)}
          className={styles.input}
        />
        All
      </label>
      {options.map((option) => (
        <label key={option.value} className={styles.label}>
          <input
            type="radio"
            name="status"
            value={option.value}
            checked={String(status) === String(option.value)}
            onChange={(e) => onChange(e.target.name, e.target.value)}
            className={styles.input}
          />
          {option.name}
        </label>
      ))}
    </div>
  </div>
);