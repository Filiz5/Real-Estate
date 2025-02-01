import React from 'react';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import {
  faHome,
  faBuilding,
  faBriefcase,
  faHouseDamage
} from '@fortawesome/free-solid-svg-icons';
import styles from '@styles/components/home/categoryCard.module.scss';

const icons = {
  House: faHome,
  Apartments: faBuilding,
  Offices: faBriefcase,
  Villas: faHome,
  Bungalows: faHouseDamage
};

const CategoryCard = ({ category, amount }) => {
  const icon = icons[category] || faHome; // Varsayılan ikon

  return (
    <div className={styles.categoryCard}>
      <div className={styles.iconContainer}>
        <FontAwesomeIcon icon={icon} />
      </div>
      <div className={styles.textContainer}>
        <h3>{category || 'Unknown'}</h3>
        <p>{amount || 0} Properties</p>
      </div>
    </div>
  );
};

export default CategoryCard;
