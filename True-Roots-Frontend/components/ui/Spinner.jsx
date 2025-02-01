import React from 'react';
import styles from '@/styles/components/ui/spinner.module.scss';

const Spinner = ({ size = 'medium', color = 'primary' }) => {
  return (
    <div
      className={`${styles.spinnerWrapper} ${styles[size]} ${styles[color]}`}
    >
      <div className={styles.spinner}>
        <div></div>
        <div></div>
        <div></div>
        <div></div>
      </div>
    </div>
  );
};

export default Spinner;
