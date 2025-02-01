'use client';

import { useState, useEffect } from 'react';
import Image from 'next/image';
import styles from '@/styles/components/common/image-galery.module.scss';
import { GrPrevious, GrNext } from 'react-icons/gr';
import { IoClose } from 'react-icons/io5';

const ImageGallery = ({ images }) => {
  const [mainImage, setMainImage] = useState('');
  const [isModalOpen, setIsModalOpen] = useState(false);

  useEffect(() => {
    if (images.length > 0) {
      setMainImage(images[0].data);
    }
  }, [images]);

  const handleSwipe = (direction) => {
    const currentIndex = images.findIndex((img) => img.data === mainImage);
    let newIndex = currentIndex;

    if (direction === 'left') {
      newIndex = (currentIndex + 1) % images.length;
    } else if (direction === 'right') {
      newIndex = (currentIndex - 1 + images.length) % images.length;
    }

    setMainImage(images[newIndex].data);
  };

  const currentIndex = images.findIndex((img) => img.data === mainImage) + 1;

  return (
    <>
      {/* Main Image */}
      <div className={styles.main_image_container}>
        {mainImage && (
          <div className={styles.image_wrapper}>
            <button
              className={`${styles.arrow_button} ${styles.left}`}
              onClick={() => handleSwipe('right')}
            >
              <GrPrevious className={styles.previous} />
            </button>
            <Image
              src={mainImage}
              alt="Main Property"
              className={styles.main_image}
              width={750}
              height={350}
              quality={100}
              priority
              onClick={() => setIsModalOpen(true)} // Open modal
            />
            <button
              className={`${styles.arrow_button} ${styles.right}`}
              onClick={() => handleSwipe('left')}
            >
              <GrNext className={styles.next} />
            </button>
            <div className={styles.image_info}>
              <span>
                {currentIndex} / {images.length}
              </span>
            </div>
          </div>
        )}
      </div>

      {/* Thumbnails */}
      <div className={styles.thumbnail_container}>
        {images.map((image, index) => (
          <div key={index} className={styles.thumbnail_wrapper}>
            <Image
              src={image.data}
              alt={`Thumbnail ${index + 1}`}
              className={styles.thumbnail}
              width={80}
              height={60}
              onClick={() => setMainImage(image.data)}
            />
          </div>
        ))}
      </div>

      {/* Full-Screen Modal */}
      {isModalOpen && (
        <div className={styles.modal}>
          <button
            className={styles.close_button}
            onClick={() => setIsModalOpen(false)}
          >
            <IoClose />
          </button>
          <div className={styles.modal_content}>
            <button
              className={`${styles.modal_arrow} ${styles.left}`}
              onClick={() => handleSwipe('right')}
            >
              <GrPrevious />
            </button>
            <Image
              src={mainImage}
              alt="Full-Screen Image"
              className={styles.full_screen_image}
              fill
              objectFit="contain"
            />
            <button
              className={`${styles.modal_arrow} ${styles.right}`}
              onClick={() => handleSwipe('left')}
            >
              <GrNext />
            </button>
            <div className={styles.modal_info}>
              <span>
                {currentIndex} / {images.length}
              </span>
            </div>
          </div>
        </div>
      )}
    </>
  );
};

export default ImageGallery;
