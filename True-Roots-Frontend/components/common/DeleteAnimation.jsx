import React from 'react';
import { FaHome } from 'react-icons/fa';
import styles from '@/styles/components/common/delete-animation.module.scss';

const DeleteAnimation = ({ isVisible }) => {
  if (!isVisible) return null;

  return (
    <div className={styles.overlay}>
      <div className={styles.animationContainer}>
        <FaHome className={styles.icon} />
        <div className={styles.progressBar} />
      </div>
    </div>
  );
};

export default DeleteAnimation;
