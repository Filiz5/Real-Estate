"use client";

import { getAdvertTypes } from '@/services/advert-type-service';
import { getCategoriesAsList } from '@/services/category-service';
import styles from '@/styles/components/admin/admin-adverts/searchbox.module.scss';
import { useRouter } from 'next/navigation';
import { useEffect, useState } from 'react';

const advertStatus = [
  { id: 0, label: 'Pending' },
  { id: 1, label: 'Activated' },
  { id: 2, label: 'Rejected' }
];

export default function SearchBox() {
    const [categories, setCategories] = useState([]);
    const [advertTypes, setAdvertTypes] = useState([]);
    const [searchTerm, setSearchTerm] = useState('');
    const [categoryId, setCategoryId] = useState('');
    const [advertTypeId, setAdvertTypeId] = useState('');
    const [priceStart, setPriceStart] = useState('');
    const [priceEnd, setPriceEnd] = useState('');
    const [statusValue, setStatusValue] = useState('');
    const router = useRouter();

    useEffect(() => {
        const fetchCategories = async () => {
            const data = await getCategoriesAsList();
            const types = await getAdvertTypes();
            setAdvertTypes(types.object);
            setCategories(data);
        }
        fetchCategories();
    }, []);

    useEffect(() => {
        const url = new URL(window.location.href);
        if(searchTerm) url.searchParams.set('q', searchTerm);
        else url.searchParams.delete('q');
        router.push(url.toString());
    }, [searchTerm, router]);

    const handleFilter = () => {
        const url = new URL(window.location.href);
        if(categoryId) url.searchParams.set('categoryId', categoryId);
        else url.searchParams.delete('categoryId');
        if(advertTypeId) url.searchParams.set('advertType', advertTypeId);
        else url.searchParams.delete('advertType');
        if(priceStart) url.searchParams.set('priceStart', priceStart);
        else url.searchParams.delete('priceStart');
        if(priceEnd) url.searchParams.set('priceEnd', priceEnd);
        else url.searchParams.delete('priceEnd');
        if(statusValue) url.searchParams.set('status', statusValue);
        else url.searchParams.delete('status');
        router.push(url.toString());
    };

    const handleInput = (e) => {
     e.target.value = e.target.value.replace(/\D/g, ''); 
    };

    
        
  return (
    <div className={styles.main_container}>
      <div className={styles.search_container}>
        <input
          type="text"
          className={styles.search_input}
          placeholder="Search"
          value={searchTerm}
          onChange={(e) => setSearchTerm(e.target.value)}
        />
      </div>
      <div className={styles.filter_container}>
        <div className={styles.category_filter}>
          <select
            className={styles.category_select}
            value={categoryId}
            onChange={(e) => setCategoryId(e.target.value)}
          >
            <option value="">Category</option>
            {categories.length === 0 ? (
              <option value="">No categories</option>
            ) : (
              categories.map((category) => (
                <option key={category.id} value={category.id}>
                  {category.title}
                </option>
              ))
            )}
          </select>
        </div>
        <div className={styles.advert_type_filter}>
          <select
            className={styles.advert_type_select}
            value={advertTypeId}
            onChange={(e) => setAdvertTypeId(e.target.value)}
          >
            <option value="">Advert Type</option>
            {advertTypes.length === 0 ? (
              <option value="">No advert types</option>
            ) : (
              advertTypes.map((type) => (
                <option key={type.id} value={type.id}>
                  {type.title}
                </option>
              ))
            )}
          </select>
        </div>
        <div className={styles.status_filter}>
          <select
            className={styles.status_select}
            value={statusValue}
            onChange={(e) => setStatusValue(e.target.value)}
          >
            <option value="">Status</option>
            {advertStatus.map((status) => (
              <option key={status.id} value={status.id}>
                {status.label}
              </option>
            ))}
          </select>
        </div>
        <div className={styles.price_start}>
          <input
            type="number"
            className={styles.price_input}
            placeholder="Price Start"
            value={priceStart}
            onChange={(e) => setPriceStart(e.target.value)}
            min={1}
            onInput={handleInput}
          />
        </div>
        <div className={styles.price_end}>
          <input
            type="number"
            className={styles.price_input}
            placeholder="Price End"
            value={priceEnd}
            onChange={(e) => setPriceEnd(e.target.value)}
            min={1}
            onInput={handleInput}
          />
        </div>
      </div>
      <div className={styles.button_container}>
        <button
          className={styles.search_button}
          onClick={() => {
            handleFilter();
          }}
        >
          Search
        </button>
      </div>
    </div>
  );
}
