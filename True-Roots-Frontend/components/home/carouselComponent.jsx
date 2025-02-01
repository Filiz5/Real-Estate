'use client';

import React from 'react';
import { Carousel } from 'primereact/carousel';
import Image from 'next/image';
import styles from '@styles/components/home/CarouselComponent.module.scss';

const CarouselComponent = ({ images }) => {
  const itemTemplate = (item) => (
    <div className={styles.carouselItem}>
      <div className={styles.imageContainer}>
        <Image
          src={item.src}
          alt={item.alt || 'Carousel Image'}
          className={styles.carouselImage}
          width={600}
          height={400}
          style={{ objectFit: 'cover' }}
        />
      </div>
    </div>
  );

  return (
    <div className={styles.carouselContainer}>
      <Carousel
        value={images}
        numVisible={3}
        numScroll={1}
        circular
        autoplayInterval={3000}
        responsiveOptions={[
          { breakpoint: '1400px', numVisible: 3, numScroll: 1 },
          { breakpoint: '1199px', numVisible: 2, numScroll: 1 },
          { breakpoint: '767px', numVisible: 2, numScroll: 1 },
          { breakpoint: '575px', numVisible: 1, numScroll: 1 },
        ]}
        itemTemplate={itemTemplate}
        className="custom-carousel"
      />
    </div>
  );
};

export default CarouselComponent;
