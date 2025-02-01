'use client';

import styles from '@/styles/components/admin/category/properites.module.scss';
import { useEffect, useState } from 'react';
import { v4 as uuidv4 } from 'uuid'; // Import UUID for unique IDs

export default function Properties({ handleProperties, state }) {
  const [propertyKeyRequests, setProperties] = useState([
    { id: uuidv4(), name: '' }
  ]);

  useEffect(() => {
    handleProperties(propertyKeyRequests);
  }, [propertyKeyRequests, handleProperties]);

  // Add a new property with a unique ID
  const addProperty = (e) => {
    e.preventDefault();
    setProperties([...propertyKeyRequests, { id: uuidv4(), name: '' }]);
  };

  // Handle input changes for a specific property
  const handlePropertyChange = (id, value) => {
    const updatedProperties = propertyKeyRequests.map((property) =>
      property.id === id ? { ...property, name: value } : property
    );
    setProperties(updatedProperties);
  };

  // Delete a property by its unique ID
  const deleteProperty = (id) => {
    const updatedProperties = propertyKeyRequests.filter(
      (property) => property.id !== id
    );
    setProperties(updatedProperties);
  };

  return (
    <div className={styles.container}>
      <h3>Properties</h3>
      <div className={styles.outer_container}>
        <div className={styles.add_button}>
          <button
            title="Add property field"
            onClick={addProperty}
          >
            +
          </button>
        </div>
       
        <div className={styles.inner_container}>
          {propertyKeyRequests.map((property) => (
            <div key={property.id} className={styles.property}>
              <input
                type="text"
                value={property.name}
                onChange={(e) =>
                  handlePropertyChange(property.id, e.target.value)
                }
              />
              {propertyKeyRequests.length > 1 && (
                <button
                  title="Delete property field"
                  className={styles.delete_button}
                  onClick={() => deleteProperty(property.id)}
                >
                  -
                </button>
              )}
            </div>
          ))}
        </div>
      </div>
      {state?.errors?.properties && (
        <span className={styles.error}>{state.errors.properties}</span>
      )}
    </div>
  );
}
