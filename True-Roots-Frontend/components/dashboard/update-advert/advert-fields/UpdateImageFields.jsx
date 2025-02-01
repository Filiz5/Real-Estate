"use client";

import { swAlert } from '@/helpers/swal';
import { deleteImage, updateFesturedImage } from '@/services/advert-service';
import styles from '@/styles/components/dashboards/update-advert/advert-fields/update-image-fields.module.scss';
import Image from 'next/image';
import { useRouter } from 'next/navigation';
import { useEffect, useState } from 'react';
import { FaCheck } from 'react-icons/fa';

export default function ImageFields({ advert }) {
  const router = useRouter();
  const [images, setImages] = useState([]);
  const [selectedImageIndex, setSelectedImageIndex] = useState(null);

  useEffect(() => {
    if (advert) {
      setImages(advert.images);
    }
  }, [advert]);


  const handleSelectImage = (index) => {
    setSelectedImageIndex(index);
  };

  const setAsFeatured = async (index) => {
    const image = images[index];
    if (!image) {
      swAlert('No image selected', 'error');
      return;
    }

    const res = await updateFesturedImage(image?.id);
    if (res.success){
      swAlert(res.message, 'success');
      router.refresh();
      setSelectedImageIndex(null);
    }else{
      swAlert(res.message, 'error');
      setSelectedImageIndex(null);
    }

  };

  const handleDelete = async (index) => {
    const image = images[index];
    if (!image) {
      swAlert('No image selected', 'error');
      return;
    }
    const res = await deleteImage(image?.id);
    if (res.success){
      swAlert(res.message, 'success');
      setSelectedImageIndex(null);
      router.refresh();
    }else{
      swAlert(res.message, 'error');
      selectedImageIndex(null);
    }
  };
  return (
    <div className={styles.container}>
      <p className={styles.head_line}>Update Images</p>
      
      <div className={styles.image_container}>
        {images.map((image, index) => (
          <div
            key={index}
            className={`${styles.image_card} ${
              index === selectedImageIndex ? styles.selected : ''
            } ${image.featured ? styles.featured : ''}`}
            onClick={() => handleSelectImage(index)}
          >
            <div className={styles.image_overlay}>
              {image.featured ? (
                <span className={styles.checkmark}>
                  <FaCheck />
                </span>
              ) : (
                <span className={styles.circle}></span>
              )}
            </div>
            <Image
              src={image.data}
              alt={`Uploaded ${index}`}
              width={157}
              height={100}
              title={image.name}
            />
            {/* {image.featured && (
            <div className={styles.featured_label}>Featured</div>
          )} */}
          </div>
        ))}
        <div className={styles.button_group}>
          <button
            className={styles.action_button}
            onClick={() => setAsFeatured(selectedImageIndex)}
            disabled={selectedImageIndex === null}
          >
            Set as Featured
          </button>
          <button
            className={`${styles.action_button} ${styles.delete}`}
            onClick={() => handleDelete(selectedImageIndex)}
            disabled={selectedImageIndex === null}
          >
            Delete
          </button>
        </div>
      </div>
    </div>
  );
}
