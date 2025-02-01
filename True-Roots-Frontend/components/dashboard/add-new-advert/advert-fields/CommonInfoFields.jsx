"use client";

import styles from '@/styles/components/dashboards/add-new-advert/advert-fields/common-info-fields.module.scss';
import { useEffect, useState } from 'react';
import { getAdvertTypes } from '@/services/advert-type-service';
import YupErrorMessaga from '@/components/common/YupErrorMessaga';

export default function CommonInfoFields({ state }) {
  const [advertTypes, setAdvertTypes] = useState([]);
  const [price, setPrice] = useState('');

  useEffect(() => {
    // Fetch common info fields from the server
    const advertTypes = async () => {
    const advertTypes = await getAdvertTypes();
    setAdvertTypes(advertTypes?.object || []);
  }
  advertTypes();
  }, []);

  const handleInput = (e) => {
    let value = e.target.value.replace(/\D/g, ''); // Remove non-numeric characters

    // Format the number: Add dots every 3 digits
    value = value.replace(/\B(?=(\d{3})+(?!\d))/g, '.');

    e.target.value = value;
  };
  return (
    <div className={styles.common_info}>
      <div className={styles.head_line}>
        <p>Common Information</p>
      </div>
      <div className={styles.title}>
        <label htmlFor="title">Title</label>
        <input type="text" id="title" name="title" />
        {state.errors?.title && <YupErrorMessaga error={state.errors.title} />}
      </div>
      <div className={styles.description}>
        <label htmlFor="desc">Description</label>
        <textarea id="desc" name="desc" />
        {state.errors?.desc && <YupErrorMessaga error={state.errors.desc} />}
      </div>
      <div className={styles.price_advert}>
        <div className={styles.price}>
          <label htmlFor="price">Price</label>
          <input
            type="text"
            id="price"
            name="price"
            min="1"
            max='999999999'
            onChange={(e) => setPrice(e.target.value)}
            value={price}
            onInput={handleInput}
          />
          {state.errors?.price && (
            <YupErrorMessaga error={state.errors.price} />
          )}
        </div>
        <div className={styles.advert_type}>
          <label htmlFor="advert_type_id">Advert Type</label>
          <select id="advert_type_id" name="advert_type_id">
            <option value="">Choose</option>
            {advertTypes.map((type) => (
              <option key={type.id} value={type.id}>
                {type.title}
              </option>
            ))}
          </select>
          {state.errors?.advert_type_id && (
            <YupErrorMessaga error={state.errors.advert_type_id} />
          )}
        </div>
      </div>
    </div>
  );
}
