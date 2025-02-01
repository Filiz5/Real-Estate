'use client';

import styles from '@/styles/components/dashboards/update-advert/advert-fields/property-fields.module.scss';
import { useState, useEffect, use } from 'react';
import { getCategoriesAsList } from '@/services/category-service';
import YupErrorMessaga from '@/components/common/YupErrorMessaga';

export default function PropertyFields({onPropertyChange, state, advert }) {
  const [categories, setCategories] = useState([]);
  const [error, setError] = useState(null);
  const [selectedCategory, setSelectedCategory] = useState(null);
  const [ categoryPropertyKeys, setCategoryPropertyKeys ] = useState([]);
  const [properties, setProperties] = useState([]);
  const [category, setCategory] = useState(null);
  const [categoryPropertyValues, setCategoryPropertyValues] = useState([]);

  useEffect(() => {
    onPropertyChange(properties);
  }, [properties, onPropertyChange]);

  useEffect(() => {
    const fetchCategories = async () => {
      try {
        const res = await getCategoriesAsList();
        setCategories(res);
        if (advert) {
          setCategory(advert.category);
          setSelectedCategory(advert?.category?.id);
          setCategoryPropertyValues(advert.categoryPropertyValues);
          setCategoryPropertyKeys(advert.category.categoryPropertyKeys);
          setProperties(
            advert.categoryPropertyKeys.map((key) => {
              const matchingValue = advert.categoryPropertyValues.find(
                (value) => value.categoryPropertyKey.id === key.id
              );
              return {
                id: key.id,
                value: matchingValue ? matchingValue.value : '',
              };
            })
          );
        }
      } catch (error) {
        setError(error.message);
      }
    };

    fetchCategories();
  }, [advert]);


  useEffect(() => {
    if (selectedCategory!== category?.id) {
      setCategoryPropertyKeys([]);
      setProperties([]);
      
      const newCategory = categories.find(
        (category) => category.id === Number(selectedCategory)
      );

      if (newCategory) {
        setCategoryPropertyKeys(newCategory.categoryPropertyKeys);

        // Initialize properties with empty values
        const newProperties = newCategory.categoryPropertyKeys.map((key) => ({
          id: key.id,
          value: '',
        }));
        setProperties(newProperties);

      }
    
    }else{
      const category = categories.find(
        (category) => category.id === Number(selectedCategory)
      );
      setCategoryPropertyKeys(category?.categoryPropertyKeys || []);

      // Initialize properties with values from categoryPropertyValues
      const initialProperties =
        category?.categoryPropertyKeys.map((key) => {
          const matchingValue = categoryPropertyValues.find(
            (value) => value.categoryPropertyKey.id === key.id
          );
          return {
            id: key.id,
            value: matchingValue ? matchingValue.value : ''
          };
        }) || [];
      setProperties(initialProperties);
    }
  }, [selectedCategory, categories, category, categoryPropertyValues]);

  const handlePropertyChange = (id, newValue) => {
    setProperties((prev) =>
      prev.map((prop) => (prop.id === id ? { ...prop, value: newValue } : prop))
    );
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
        {state.errors?.category_id && (
                <YupErrorMessaga error={state.errors.category_id} />
              )}
      </div>
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
              <label htmlFor={property.name}>{property.name}</label>
              <input
                type="text"
                id="properties"
                name="properties"
                defaultValue={
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
