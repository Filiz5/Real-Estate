'use client';

import React, { useEffect, useState } from 'react';
import Title from './title';
import Tabs from './tabs';
import SearchBar from './search-bar';
import Categories from './categories';
import { Carousel } from 'primereact/carousel'; // PrimeReact Carousel import edildi
import Image from 'next/image'; // next/image import edildi
import styles from '@styles/components/home/HomePage.module.scss';
import RadioButtons from '@/components/home/radio-buttons';
import { useRouter } from 'next/navigation';

const HomePage = ({types}) => {
  const [advertType, setAdvertType] = useState(null);
  const [selectedCategoryId, setSelectedCategoryId] = useState(null); // RadioButton seçimleri
  const [searchQuery, setSearchQuery] = useState(''); // Arama kutusu girdisi
  const [advertTypes, setAdvertTypes] = useState([]); // Rent/Sale seçenekleri
  const router = useRouter();

  console.log('advertType', advertType);


  useEffect(() => {
      setAdvertTypes(types || []);
  }, [types]);

  // Carousel resim veri seti
  const images = [
    {
      src: '/assets/images/banner 1.png',
      alt: 'Elegant Office'
    },
  ];

  const handleTabChange = (typeId) => {
    setAdvertType(typeId); // Rent veya Sale seçimi
  };

  const handleSearch = () => {
    const url = new URL(`/properties`, window.location.href);

    if (searchQuery) url.searchParams.set('q', searchQuery);
    else url.searchParams.delete('q');
    if(advertType) url.searchParams.set('status', advertType);
    else url.searchParams.delete('status');
    
    router.push(url.toString());
  };

  

  return (
    <div className={styles.container}>
      {/* Carousel Alanı */}
      <div className={styles.text_container}>
        <h1>Easy Way to Find a Perfect Property</h1>
        {/* Rent/Sale Tabs */}
        <Tabs onTypeChange={handleTabChange} types={advertTypes} />
        {/* Search Bar */}
        <SearchBar
          searchQuery={searchQuery}
          onSearchQueryChange={setSearchQuery} // Arama kutusu değerini güncelle
          onSearch={handleSearch} // Arama butonuna basıldığında
        />
        {/* Radio Buttons for Categories */}
        {/* <RadioButtons onCategoryChange={handleCategoryChange} /> */}
      </div>
      <div className={styles.carousel}>
        <Image
          src={'/assets/images/banner 1.png'}
          alt={'Carousel Image'}
          className={styles.carouselImage}
          width={800}
          height={490}
        />
      </div>
    </div>
  );
};

export default HomePage;
///adverts?q=beyoğlu&category_id=12&advert_type_id=3&price_start=500&price_end=1500&status=1;page=1&size
//=10&sort=date&type=asc