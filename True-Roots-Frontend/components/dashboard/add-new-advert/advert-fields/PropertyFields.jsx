'use client';

import styles from '@/styles/components/dashboards/add-new-advert/advert-fields/property-fields.module.scss';
import { useState, useEffect, use } from 'react';
import { getCategoriesAsList } from '@/services/category-service';
import YupErrorMessaga from '@/components/common/YupErrorMessaga';

export default function PropertyFields({ onPropertyChange, state }) {
  const [categories, setCategories] = useState([]);
  const [error, setError] = useState(null);
  const [selectedCategory, setSelectedCategory] = useState(null);
  const [categoryPropertyKeys, setCategoryPropertyKeys] = useState([]);
  const [properties, setProperties] = useState([]);

  useEffect(() => {
    onPropertyChange(properties);
  }, [properties, onPropertyChange]);

  useEffect(() => {
    const fetchCategories = async () => {
      try {
        const res = await getCategoriesAsList();
        setCategories(res);
      } catch (error) {
        setError(error.message);
      }
    };

    fetchCategories();
  }, []);

  useEffect(() => {
    if (selectedCategory) {
      const category = categories.find(
        (category) => category.id === Number(selectedCategory)
      );
      setCategoryPropertyKeys(category?.categoryPropertyKeys || []);

      // Initialize properties with empty values for each key
      const initialProperties =
        category?.categoryPropertyKeys.map((key) => ({
          id: key.id,
          value: ''
        })) || [];
      setProperties(initialProperties);
    }else{
      setCategoryPropertyKeys([]);
    }
  }, [selectedCategory, categories]);

  const handlePropertyChange = (id, newValue) => {
    const updatedProperties = properties.map((prop) =>
      prop.id === id ? { ...prop, value: newValue } : prop
    );
    setProperties(updatedProperties);
    // Convert to the format expected by the schema
    const formattedProperties = updatedProperties.map((prop) => ({
      id: prop.id,
      value: prop.value
    }));
    onPropertyChange(formattedProperties);
  };

  return (
    <div className={styles.container}>
      <p>Properties</p>

      <div className={styles.category}>
        <label htmlFor="category">Category</label>
        <select
          typeof="number"
          id="category_id"
          name="category_id"
          value={selectedCategory || ''}
          onChange={(e) => setSelectedCategory(e.target.value)}
        >
          <option value="">Choose</option>
          {Array.isArray(categories) &&
            categories.map((category, index) => (
              <option key={index} value={category.id}>
                {category.title}
              </option>
            ))}
        </select>
      </div>
      {state.errors?.category_id && (
        <YupErrorMessaga error={state.errors.category_id} />
      )}
      <div
        className={
          categoryPropertyKeys.length === 1
            ? styles.line1
            : categoryPropertyKeys.length < 3
            ? styles.line2
            : styles.line3
        }
      >
        {Array.isArray(categoryPropertyKeys) &&
          categoryPropertyKeys.map((property, index) => (
            <div className={styles.floor} key={index}>
              <label>{property.name}</label>
              <input
                type="text"
                id="properties"
                name="properties"
                value={
                  properties.find((p) => p.id === property.id)?.value || ''
                }
                onChange={(e) =>
                  handlePropertyChange(property.id, e.target.value)
                }
              />
              {state?.errors?.[`properties[${index}].value`] && 

              <div className={styles.error}>
                <YupErrorMessaga
                  error={state.errors[`properties[${index}].value`]}
                />
              </div>
                
              }
            </div>
          ))}
      </div>
    </div>
  );
}
