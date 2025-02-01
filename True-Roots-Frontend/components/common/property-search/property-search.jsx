'use client';
import { useEffect, useState } from 'react';
import { useRouter } from 'next/navigation'; // For navigating to a new URL

import { adverTypeGetAll } from '@/services/advertTypeGetAll-service';
import { adverStatusGetAll } from '@/services/advertStatusGetAll-service';
import { getAllCountries } from '@/services/getAllCountries-service';
import { getAllCities } from '@/services/getAllCities-service';
import { SearchBox } from '@/components/common/property-search/search-box';
import { PropertyStatus } from '@/components/common/property-search/property-status';
import { PropertyType } from '@/components/common/property-search/property-type';
import { PriceRange } from '@/components/common/property-search/price-range';
import { LocationDropdown } from '@/components/common/property-search/Locations-dropDown';
import { IoSearchOutline } from 'react-icons/io5';
import styles from '@/styles/components/common/property-search/property-search.module.scss';

export const PropertySearch = ({ closeSidebar }) => {
  const router = useRouter();
  const [formData, setFormData] = useState({
    search: '',
    status: '',
    type: '',
    location: '',
    minPrice: '',
    maxPrice: ''
  });

  const [advertStatus, setAdvertStatus] = useState([]);
  const [advertTypes, setAdvertTypes] = useState([]);
  const [countries, setCountries] = useState([]);
  const [cities, setCities] = useState([]);
  const [error, setError] = useState(null);

  useEffect(() => {
    const fetchAdvertStatus = async () => {
      const result = await adverStatusGetAll();

      if (result?.object) {
        setAdvertStatus(
          result?.object.map((advert) => ({
            value: advert.id,
            name: advert.title
          }))
        );
      } else {
        setError(result.message || 'Error fetching advert status.');
      }
    };

    fetchAdvertStatus();

    const fetchAdvertTypes = async () => {
      const result = await adverTypeGetAll();

      if (result?.object?.content) {
        setAdvertTypes(
          result?.object?.content.map((advert) => ({
            value: advert.id,
            name: advert.title
          }))
        );
      } else {
        setError(result.message || 'Error fetching advert types.');
      }
    };

    fetchAdvertTypes();

    const fetchAllCountries = async () => {
      const result = await getAllCountries();

      if (result.object) {
        setCountries(
          result.object.map((country) => ({
            value: country.id,
            name: country.name
          }))
        );
      } else {
        setError(result.message || 'Error fetching all countries.');
      }
    };

    fetchAllCountries();

    const fetchAllCities = async () => {
      const result = await getAllCities();

      if (result.object) {
        setCities(
          result.object.map((city) => ({
            value: city.id,
            name: city.name,
            countryName: city.countryName
          }))
        );
      } else {
        setError(result.message || 'Error fetching all cities.');
      }
    };

    fetchAllCities();
  }, []);

  const handleChange = (name, value) => {
    setFormData((prev) => ({ ...prev, [name]: value }));
  };

  const handleSubmit = (e) => {
    e.preventDefault();

    const url = new URL(window.location.href);
    url.searchParams.set('q', formData.search || '');
    url.searchParams.set('categoryId', formData.type || '');
    url.searchParams.set('status', formData.status || '');
    url.searchParams.set('location', formData.location || '');
    url.searchParams.set('priceStart', formData.minPrice || '');
    url.searchParams.set('priceEnd', formData.maxPrice || '');

    // Remove empty values
    url.searchParams.forEach((value, key) => {
      if (!value) url.searchParams.delete(key);
    });

    // Navigate to the updated URL
    router.push(url.toString());

    // Close the sidebar
    if (closeSidebar) {
      closeSidebar();
    }
  };

  return (
    <div className={styles.container}>
      <form onSubmit={handleSubmit} className={styles.form}>
        <SearchBox search={formData.search} onChange={handleChange} />
        <PropertyStatus
          status={formData.status}
          onChange={handleChange}
          options={advertStatus}
        />
        <PropertyType
          type={formData.type}
          onChange={handleChange}
          options={advertTypes}
        />
        <PriceRange
          minPrice={formData.minPrice}
          maxPrice={formData.maxPrice}
          onChange={handleChange}
        />
        <LocationDropdown
          location={formData.location}
          onChange={handleChange}
          cityOptions={cities}
          countryOptions={countries}
        />
        <button type="submit" className={styles.button}>
          <IoSearchOutline className={styles.searchIcon} />
          Search
        </button>
      </form>
    </div>
  );
};
