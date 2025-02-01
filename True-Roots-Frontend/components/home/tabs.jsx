'use client';

import { useState, useEffect } from 'react';
import { Button } from 'primereact/button';
import styles from '@/styles/components/home/Tabs.module.scss';
import Spinner from '../ui/Spinner';



const Tabs = ({ onTypeChange, types }) => {
  const [advertTypes, setAdvertTypes] = useState([]);
  const [selected, setSelected] = useState(null);
  const [isLoading, setIsLoading] = useState(true);

  console.log('selected', selected);

  useEffect(() => {
    if (types) {
      setAdvertTypes(types || []);
      setIsLoading(false);
    }
  }, [types]);

  const handleCategoryClick = (typeId) => {
    if (selected === typeId) {
      setSelected(null);
      onTypeChange(null);
    } else {
      setSelected(typeId);
      onTypeChange(typeId);
    }
  };

  if (isLoading) return <Spinner />;

  return (
  <div className={styles.tabs}>
    {advertTypes.map((type) => (
      <Button
        key={type.id}
        label={type.title}
        onClick={() => handleCategoryClick(type.id)}
        severity={selected === type.id ? 'primary' : 'secondary'}
        className={`${styles.tab} ${
          selected === type.id ? styles.active : ''
        } outlined`}
      />
    ))}
  </div>
  );
};

export default Tabs;
