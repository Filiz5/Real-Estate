'use client';
import { Suspense, useState, useEffect } from 'react';
import Loader from '@/components/common/Loader';
import AdvertCard from '@/components/cards/AdvertCard';
import NoDataAvailable from '@/components/common/no-data-available/noDataAvailable';
import { Pagination } from '@/components/common/Pagination';
import { PropertySearch } from '@/components/common/property-search/property-search';
import Spacer from '@/components/common/Spacer';
import styles from '@/styles/components/adverts/adverts.module.scss';

const Adverts = ({ res, session, page, size }) => {
  const { content } = res?.object || [];
  const [filteredAdverts, setFilteredAdverts] = useState(content || []);
  const [currentURL, setCurrentURL] = useState('');
  const [isSidebarOpen, setIsSidebarOpen] = useState(false);

  useEffect(() => {
    setFilteredAdverts(content || []);

    if (typeof window !== 'undefined') {
      setCurrentURL(window.location.href); // Full URL
    }

    // Listen to window resize events
    const handleResize = () => {
      if (window.innerWidth >= 768) {
        setIsSidebarOpen(false); // Close sidebar when transitioning to large screen
      }
    };

    window.addEventListener('resize', handleResize);

    // Clean up event listener
    return () => {
      window.removeEventListener('resize', handleResize);
    };
  }, [content]);

  const isDataAvailable =
    Array.isArray(filteredAdverts) && filteredAdverts.length > 0;

  const toggleSidebar = () => {
    setIsSidebarOpen((prevState) => !prevState);
  };

  return (
    <div className={styles.layoutContainer}>
      {/* Sidebar */}
      <div className={`${styles.sidebar} ${isSidebarOpen ? styles.open : ''}`}>
        {isSidebarOpen && (
          <div className={styles.sidebarHeader}>
            <span className={styles.filterTitle}>Filter</span>
            <button className={styles.closeButton} onClick={toggleSidebar}>
              X
            </button>
          </div>
        )}
        <PropertySearch closeSidebar={() => setIsSidebarOpen(false)}/>
      </div>

      {/* Main content */}
      <div className={styles.container}>
        {/* Filter button (only on small screens) */}
        {!isSidebarOpen && (
          <button className={styles.filterButton} onClick={toggleSidebar}>
            Filter
          </button>
        )}

        <Suspense fallback={<Loader />}>
          <div className={styles.advertImageLoader}>
            {isDataAvailable ? (
              filteredAdverts.map((advert, index) => (
                <AdvertCard
                  id={advert.id}
                  advertData={advert}
                  key={advert.id || index}
                  session={session}
                />
              ))
            ) : (
              <NoDataAvailable />
            )}
          </div>

          <Spacer height={230} />

          {res?.object?.totalPages > 0 && (
            <Pagination
              baseUrl={currentURL}
              currentPage={+page}
              size={size}
              totalPages={res?.object?.totalPages || 1}
            />
          )}
        </Suspense>
      </div>
    </div>
  );
};

export default Adverts;
