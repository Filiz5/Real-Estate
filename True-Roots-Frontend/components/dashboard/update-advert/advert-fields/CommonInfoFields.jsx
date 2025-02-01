"use client";

import styles from '@/styles/components/dashboards/update-advert/advert-fields/common-info-fields.module.scss';
import { useEffect, useState } from 'react';
import { getAdvertTypes } from '@/services/advert-type-service';
import YupErrorMessaga from '@/components/common/YupErrorMessaga';

export default function CommonInfoFields({ state, advert }) {
   const [advertTypes, setAdvertTypes] = useState([]);
   const [price, setPrice] = useState('');
   const [selectedAdvertTypeId, setSelectedAdvertTypeId] = useState(
     advert?.advertType?.id || ''
   );

   console.log('price', price);

  useEffect(() => {
    setPrice(advert?.price);
    const fetchAdvertTypes = async () => {
      const fetchedAdvertTypes = await getAdvertTypes();

      setAdvertTypes(fetchedAdvertTypes.object);

      // Set initial value if `advert.advertTypeId` exists
      if (advert?.advertType?.id) {
        setSelectedAdvertTypeId(advert.advertType.id);
      }
    };

    fetchAdvertTypes();
  }, [advert]);

  const handleInput = (e) => {
    e.target.value = e.target.value.replace(/\D/g, '');
  };

  return (
    <div className={styles.common_info}>
      <div className={styles.head_line}>
        <p>Common Information</p>
      </div>
      <div className={styles.title}>
        <label htmlFor="title">Title</label>
        <input
          type="text"
          id="title"
          name="title"
          defaultValue={advert?.title}
        />
        {state.errors?.title && <YupErrorMessaga error={state.errors.title} />}
      </div>
      <div className={styles.description}>
        <label htmlFor="desc">Description</label>
        <textarea id="desc" name="desc" defaultValue={advert?.desc} />
        {state.errors?.desc && <YupErrorMessaga error={state.errors.desc} />}
      </div>
      <div className={styles.price_advert}>
        <div className={styles.price}>
          <label htmlFor="price">Price</label>
          <input
            type="number"
            id="price"
            name="price"
            defaultValue={price}
            onInput={handleInput}
          />
          {state.errors?.price && (
            <YupErrorMessaga error={state.errors.price} />
          )}
        </div>
        <div className={styles.advert_type}>
          <label htmlFor="advert_type_id">Advert Type</label>
          <select
            id="advert_type_id"
            name="advert_type_id"
            value={selectedAdvertTypeId}
            onChange={(e) => setSelectedAdvertTypeId(e.target.value)}
          >
            <option value="">Choose Advert Type</option>
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
