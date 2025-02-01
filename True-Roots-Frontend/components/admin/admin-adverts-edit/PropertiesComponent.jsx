'use client';

import styles from '@/styles/components/admin/admin-adverts-edit/properties-component.module.scss';
import { useState, useEffect, use } from 'react';

export default function PropertyFields({properties}) {
  const [propertyFields, setPropertyFields] = useState([]);

  useEffect(() => {
    setPropertyFields(properties || []);
  }, [properties]);
  


  

  return (
    <div className={styles.container}>
      <p>Properties</p>
      <div className={styles.inner_container}>
        {propertyFields && propertyFields.length > 0 && propertyFields.map((property, index) => (
          <div key={index} className={styles.property}>
            <label>{property.categoryPropertyKey.name}</label>
            <input readOnly defaultValue={property.value}/>
          </div>
        ))}
      </div> 
    </div>
  );
}
/*
object: {
    id: 15,
    title: 'Big Family Apartment',
    desc: 'in central Location',
    slug: 'big-family-apartment',
    price: 5000,
    advertStatus: 'PENDING',
    builtIn: false,
    viewCount: 5,
    location: 'sisli sokak no:65',
    createdAt: '2024-12-30T12:36:46.783603',
    updatedAt: '2025-01-06T19:53:02.111959',
    userId: 3,
    advertType: { id: 2, title: 'For Rent' },
    country: { id: 1, name: 'Turkey' },
    city: { id: 40, name: 'İstanbul' },
    district: { id: 328, name: 'Şişli' },
    category: {
      id: 2,
      title: 'Apartment',
      icon: null,
      builtIn: false,
      seq: 0,
      slug: null,
      categoryPropertyKeys: [Array],
      createdAt: null,
      updatedAt: null,
      active: false
    },
    categoryPropertyValues: [ [Object], [Object], [Object] ],
    images: [ [Object], [Object], [Object], [Object], [Object], [Object] ],
    favorites: [],
    logs: [ [Object], [Object], [Object] ],
    tourRequests: [ [Object], [Object] ],
    active: false,
    deleted: false,
    favorited: false
  },
*/
