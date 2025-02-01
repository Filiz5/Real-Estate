import { useState } from 'react';
import Spacer from '../Spacer';
import styles from '@/styles/components/common/property-search/location-dropDown.module.scss';

export const LocationDropdown = ({
  location, // This will now only hold the city ID
  onChange, // Function to update formData in the parent
  cityOptions,
  countryOptions
}) => {
  const [selectedCountry, setSelectedCountry] = useState(''); 
  const [filteredCities, setFilteredCities] = useState([]); 

 
  const handleCountryChange = (e) => {
    const selectedCountry = e.target.value;
    setSelectedCountry(selectedCountry);

   
    const updatedCities = cityOptions.filter(
      (city) => city.countryName === selectedCountry
    );
    setFilteredCities(updatedCities);

    
    onChange('location', ''); 
  };

  
  const handleCityChange = (e) => {
    const selectedCityId = e.target.value;
    onChange('location', selectedCityId); 
  };

  return (
    <div className={styles.locationContainer}>
      <p>Location</p>

      <div className={styles.inputs}>
        <div className={styles.locationInputs}>
          <select
            name="country"
            value={selectedCountry}
            onChange={handleCountryChange}
            className={styles.locationSelect}
          >
            <option value="" disabled>
              Select Country
            </option>
            {countryOptions.map((country) => (
              <option
                key={country.value}
                value={country.name}
                className={styles.selectOption}
              >
                {country.name}
              </option>
            ))}
          </select>
        </div>
        <Spacer height={10} />

        <div className={styles.locationInputs}>
          <select
            name="city"
            value={location || ''}
            onChange={handleCityChange}
            className={styles.locationSelect}
            disabled={filteredCities.length === 0}
          >
            <option value="" disabled>
              Select City
            </option>
            {filteredCities.map((city) => (
              <option
                key={city.value}
                value={city.value}
                className={styles.selectOption}
              >
                {city.name}
              </option>
            ))}
          </select>
        </div>
      </div>
    </div>
  );
};
