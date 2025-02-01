"use client";
import styles from '@/styles/components/dashboards/add-new-advert/advert-fields/address-fields.module.scss';
import { useEffect, useState } from 'react';
import { getCountries, getCities, getDistricts } from '@/services/address-service';
import { set } from 'react-hook-form';
import YupErrorMessaga from '@/components/common/YupErrorMessaga';

export default function AddressFields({ state }) {
  const [countries, setCountries] = useState([]);
  const [cities, setCities] = useState([]);
  const [districts, setDistricts] = useState([]);
  const [filteredCities, setFilteredCities] = useState([]);
  const [filteredDistricts, setFilteredDistricts] = useState([]);
  const [selectedCountry, setSelectedCountry] = useState(null);
  const [selectedCity, setSelectedCity] = useState(null);
  const [selectedDistrict, setSelectedDistrict] = useState(null);
  const [countryName, setCountryName] = useState(null);
  const [cityName, setCityName] = useState(null);

  useEffect(() => {
    // Fetch address fields from the server
    const fetchData = async () => {
      const fetchedCountries = await getCountries();
      setCountries(fetchedCountries); 
      const fetchedCities = await getCities();
      setCities(fetchedCities);
      const fetchedDistricts = await getDistricts();
      setDistricts(fetchedDistricts);
    };
    fetchData();
  }, []);

  // Update cities when selectedCountry changes
  useEffect(() => {
    if (selectedCountry) {
      const country = countries.find(
        (country) => country.id === Number(selectedCountry)
      );

      if (country) {
        setCountryName(country.name);
      }
      
    } else {
      setCountryName(null);
    }
  }, [selectedCountry, countries, countryName]);

  useEffect(() => {
    if (selectedCountry && countryName) {
      const filtered = cities.filter(
        (city) => city.countryName === countryName
      );
      setFilteredCities(filtered);
    } else {
      setFilteredCities([]); // Clear filteredCities if no country is selected
    }
  }, [selectedCountry, cities, countryName]);
      

  useEffect(() => {
    if (selectedCity) {
      const city = cities.find(
        (city) => city.id === Number(selectedCity)
      );
      if (city) {
        setCityName(city.name);
      }
    } else {
      setCityName(null);
    }
  }, [selectedCity, cities, cityName]);

  // Update districts when selectedCity changes
  useEffect(() => {
    if (selectedCity && cityName) {
      const filtered = districts.filter(
        (district) => district.cityName === cityName
      );
      setFilteredDistricts(filtered);
    } else {
      setFilteredDistricts([]); // Clear filteredDistricts if no city is selected
    }
  }, [selectedCity, districts, cityName]);
  return (
    <div className={styles.address_container}>
      <div className={styles.head_line}>
        <p>Address Information</p>
      </div>
      <div className={styles.selects_container}>
        <div className={styles.country}>
          <label htmlFor="country_id">Country</label>
          <select
            typeof="number"
            name="country_id"
            id="country_id"
            value={selectedCountry || ''}
            onChange={(e) => setSelectedCountry(e.target.value)}
          >
            <option value="">Choose</option>
            {countries.map((country) => (
              <option key={country.id} value={country.id}>
                {country.name}
              </option>
            ))}
          </select>
          {state.errors?.country_id && (
            <YupErrorMessaga error={state.errors.country_id} />
          )}
        </div>

        <div className={styles.city}>
          <label htmlFor="city_id">City</label>
          <select
            typeof="number"
            name="city_id"
            id="city_id"
            value={selectedCity || ''}
            onChange={(e) => setSelectedCity(e.target.value)}
          >
            <option value="">Choose</option>
            {filteredCities.map((city, index) => (
              <option key={index} value={city.id}>
                {city.name}
              </option>
            ))}
          </select>
          {state.errors?.city_id && (
            <YupErrorMessaga error={state.errors.city_id} />
          )}
        </div>

        <div className={styles.neigbourhood}>
          <label htmlFor="district_id">Neigbourhood</label>
          <select
            name="district_id"
            id="district_id"
            value={selectedDistrict || ''}
            onChange={(e) => setSelectedDistrict(e.target.value)}
          >
            <option value="">Choose</option>
            {filteredDistricts.map((district, index) => (
              <option key={index} value={district.id}>
                {district.name}
              </option>
            ))}
          </select>
          {state.errors?.district_id && (
            <YupErrorMessaga error={state.errors.district_id} />
          )}
        </div>
      </div>
      <div className={styles.location}>
        <label htmlFor="location">Address Line</label>
        <input type="text" id="location" name="location" />
        {state.errors?.location && (
          <YupErrorMessaga error={state.errors.location} />
        )}
      </div>
    </div>
  );
}
