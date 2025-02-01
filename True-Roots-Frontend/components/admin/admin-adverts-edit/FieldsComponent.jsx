"use client";

import styles from '@/styles/components/admin/admin-adverts-edit/fields-component.module.scss';
import statuses from '@/data/status';
import { useEffect, useState } from 'react';

export default function FieldsComponent({ fields, handleStasuChange, error }) {
    const [selectedStatus, setSelectedStatus] = useState(fields?.status || '');

    useEffect(() => {
        handleStasuChange(selectedStatus);
    }, [selectedStatus, handleStasuChange]);
    
  return (
    <div className={styles.container}>
      <div className={styles.title}>
        <label>Title</label>
        <input type="text" name="title" readOnly defaultValue={fields.title} />
      </div>
      <div className={styles.description}>
        <label>Description</label>
        <textarea name="description" readOnly defaultValue={fields.desc} />
      </div>
      <div className={styles.cat_type}>
        <div className={styles.category}>
          <label>Category</label>
          <input
            type="text"
            name="category"
            readOnly
            defaultValue={fields.category}
          />
        </div>
        <div className={styles.advert_type}>
          <label>Advert Type</label>
          <input type="text" name="type" readOnly defaultValue={fields.type} />
        </div>
      </div>
      <div className={styles.adress_container}>
        <div className={styles.country}>
          <label>Country</label>
          <input
            type="text"
            name="country"
            readOnly
            defaultValue={fields.country}
          />
        </div>
        <div className={styles.city}>
          <label>City</label>
          <input type="text" name="city" readOnly defaultValue={fields.city} />
        </div>
        <div className={styles.district}>
          <label>District</label>
          <input
            type="text"
            name="district"
            readOnly
            defaultValue={fields.district.name}
          />
        </div>
      </div>
      <div className={styles.location}>
        <label>Location</label>
        <input
          type="text"
          name="location"
          readOnly
          defaultValue={fields.location}
        />
      </div>
      <div className={styles.status_price}>
        <div className={styles.status}>
          <label htmlFor="advertStatus">Status</label>
          <select
            type="text"
            name="advertStatus"
            id="advertStatus"
            value={selectedStatus || ''}
            onChange={(e) => setSelectedStatus(e.target.value)}
          >
            <option value="">Choose</option>
            {statuses.map((status, index) => (
              <option key={index} value={status.name}>
                {status.name}
              </option>
            ))}
          </select>
          {error && <div className={styles.error}>{error}</div>}
        </div>
        <div className={styles.price}>
          <label>Price</label>
          <input
            type="text"
            name="price"
            readOnly
            defaultValue={fields.price}
          />
        </div>
      </div>
    </div>
  );
}
