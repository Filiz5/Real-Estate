"use client";

import styles from '@/styles/components/dashboards/add-new-advert/advert-fields/image-fields.module.scss';
import Image from 'next/image';
import { useEffect, useState } from 'react';
import { FaCheck } from 'react-icons/fa';

export default function ImageFields({ onImageChange, state }) {
  const [images, setImages] = useState([]);
  const [selectedImageIndex, setSelectedImageIndex] = useState(null);

   useEffect(() => {
     onImageChange(images); // Notify parent when images change
   }, [images, onImageChange]);

  const handleImageUpload = (event) => {
    const files = Array.from(event.target.files); // Support multiple files
    files.forEach((file) => {
      const reader = new FileReader();
      reader.onload = () => {
        setImages((prevImages) => [
          ...prevImages,
          { data: reader.result, featured: false, name: file.name, type: file.type }
        ]);
      };
      reader.readAsDataURL(file);
    });
  };

  const handleSelectImage = (index) => {
    setSelectedImageIndex(index);
  };

  const setAsFeatured = () => {
    if (selectedImageIndex !== null) {
      setImages((prevImages) =>
        prevImages.map((img, i) => ({
          ...img,
          featured: i === selectedImageIndex
        }))
      );
      setSelectedImageIndex(null); // Clear selection after setting as featured
    }
  };

  const deleteImage = (index) => {
    setImages((prevImages) => prevImages.filter((_, i) => i !== index));
    if (selectedImageIndex === index) {
      setSelectedImageIndex(null); // Clear selection if deleted
    }
  };
  return (
    <div className={styles.container}>
      <p className={styles.head_line}>Images</p>
      <div className={styles.upload_container}>
        <label htmlFor="images" className={styles.upload_button}>
          +
        </label>
        <input
          type="file"
          accept="image/*"
          onChange={handleImageUpload}
          id="images"
          name="images"
          multiple
        />
      </div>
      <div className={styles.image_container} title='Jpg, jpeg, png, gif formats are supported'>
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
            />
            {state?.errors?.[`images[${index}].type`] && (
              <div className={styles.image_error}>
                <p>{state.errors[`images[${index}].type`]}</p>
              </div>
            )}
          </div>
        ))}
        <div className={styles.button_group}>
          <button
            className={styles.action_button}
            onClick={setAsFeatured}
            disabled={selectedImageIndex === null}
          >
            Set as Featured
          </button>
          <button
            className={`${styles.action_button} ${styles.delete}`}
            onClick={() => deleteImage(selectedImageIndex)}
            disabled={selectedImageIndex === null}
          >
            Delete
          </button>
        </div>
      </div>
      {state?.errors?.images && (
        <div className={styles.error}>
          <p>{state.errors.images}</p>
        </div>
      )}
    </div>
  );
}
