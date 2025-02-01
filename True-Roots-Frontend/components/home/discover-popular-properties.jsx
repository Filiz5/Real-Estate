'use client';
import { useState, useEffect } from 'react';
import styles from '@/styles/components/home/DiscoverPopularProperties.module.scss';
import { ProgressSpinner } from 'primereact/progressspinner';
import AdvertCard from '@/components/cards/AdvertCard'; // AdvertCard bileşenini içe aktar
import { getPopularAdverts } from '@/services/advert-service';

const DiscoverPopularProperties = ({ session }) => {
  const [properties, setProperties] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    const fetchPopularProperties = async () => {
      try {
        const data = await getPopularAdverts();

        if (data && data.object) {
          setProperties(data.object); // Gelen object array'ini kaydet
        }
      } catch (error) {
        setError(error);
      } finally {
        setLoading(false);
      }
    };

    fetchPopularProperties();
  }, []);

  return (
    <div className={styles.discoverSection}>
      <h2>Discover Popular Properties</h2>
      {loading ? (
        <div className={styles.spinnerContainer}>
          <ProgressSpinner />
        </div>
      ) : properties.length > 0 ? (
        <div className={styles.propertiesGrid}>
          {properties.map((property) => (
            <AdvertCard
              key={property.id}
              id={property.id}
              advertData={property}
              session={session} // Kullanıcı oturum bilgisi
            />
          ))}
        </div>
      ) : (
        error ? (
          <p className={styles.notFoundProperty}>{error.message}</p>
        ) : (
          <p className={styles.notFoundProperty}>No data available.</p>
        )
      )}
    </div>
  );
};

export default DiscoverPopularProperties;
